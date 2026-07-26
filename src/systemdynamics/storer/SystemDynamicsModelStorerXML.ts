import * as fs from "fs";
import { XMLBuilder, XMLParser } from "fast-xml-parser";
import { ExpressionToMathMLConverter } from "../../expression/ExpressionConverters.js";
import { DefaultFiniteStock, DefaultInfiniteStock, DefaultInformation, DefaultRate, DefaultVariable, Stock } from "../SystemDynamicsItem.js";
import { DefaultSystemDynamicsModel, SystemDynamicsModel } from "../SystemDynamicsModel.js";

export class SystemDynamicsModelStorerXML {
  loadFromFile(filePath: string): SystemDynamicsModel {
    const xmlText = fs.readFileSync(filePath, "utf-8");
    return this.loadFromXmlString(xmlText);
  }

  loadFromXmlString(xmlText: string): SystemDynamicsModel {
    const parser = new XMLParser({
      ignoreAttributes: false,
      removeNSPrefix: false,
      parseTagValue: false,
    });
    const parsed = parser.parse(xmlText);
    const root = parsed.systemDynamicsModel || parsed["m:systemDynamicsModel"] || parsed;

    const model = new DefaultSystemDynamicsModel();
    model.setName(root.name ?? "SystemDynamicsModel");

    // Stocks
    const stocksNode = root.stocks?.finiteStock;
    if (stocksNode) {
      const list = Array.isArray(stocksNode) ? stocksNode : [stocksNode];
      for (const itemNode of list) {
        const stock = new DefaultFiniteStock();
        stock.setName(itemNode.name);
        if (itemNode.initialValue) {
          const expr = ExpressionToMathMLConverter.readFromMathML(itemNode.initialValue);
          stock.setInitialValue(expr);
        }
        model.put(stock.getName(), stock);
      }
    }

    // Infinite Stocks
    const infStocksNode = root.infiniteStocks?.infiniteStock;
    if (infStocksNode) {
      const list = Array.isArray(infStocksNode) ? infStocksNode : [infStocksNode];
      for (const itemNode of list) {
        const infStock = new DefaultInfiniteStock();
        infStock.setName(itemNode.name);
        model.put(infStock.getName(), infStock);
      }
    }

    // Auxiliaries / Variables
    const auxNode = root.auxiliaries?.auxiliary;
    if (auxNode) {
      const list = Array.isArray(auxNode) ? auxNode : [auxNode];
      for (const itemNode of list) {
        const v = new DefaultVariable();
        v.setName(itemNode.name);
        if (itemNode.expression) {
          const expr = ExpressionToMathMLConverter.readFromMathML(itemNode.expression);
          v.setExpression(expr);
        }
        model.put(v.getName(), v);
      }
    }

    // Rates
    const ratesNode = root.rates?.rate;
    if (ratesNode) {
      const list = Array.isArray(ratesNode) ? ratesNode : [ratesNode];
      for (const itemNode of list) {
        const rate = new DefaultRate();
        rate.setName(itemNode.name);
        if (itemNode.expression) {
          const expr = ExpressionToMathMLConverter.readFromMathML(itemNode.expression);
          rate.setExpression(expr);
        }
        model.put(rate.getName(), rate);

        const fromName = itemNode.source;
        const toName = itemNode.target;
        if (fromName) rate.setFromLevel(model.get(fromName) as Stock);
        if (toName) rate.setToLevel(model.get(toName) as Stock);
      }
    }

    // Informations
    const infoNode = root.informations?.information;
    if (infoNode) {
      const list = Array.isArray(infoNode) ? infoNode : [infoNode];
      for (const itemNode of list) {
        const sourceObj = model.get(itemNode.source);
        const consumerObj = model.get(itemNode.consumer || itemNode.target);
        const info = new DefaultInformation(sourceObj, consumerObj);
        info.setName(itemNode.name);
        model.put(info.getName(), info);
      }
    }

    return model;
  }

  saveToFile(model: SystemDynamicsModel, filePath: string): void {
    const xmlStr = this.saveToXmlString(model);
    fs.writeFileSync(filePath, xmlStr, "utf-8");
  }

  saveToXmlString(model: SystemDynamicsModel): string {
    const finiteStocks: any[] = [];
    const infiniteStocks: any[] = [];
    const rates: any[] = [];
    const auxiliaries: any[] = [];
    const informations: any[] = [];

    for (const item of model.getItems().values()) {
      if ("getInitialValue" in item) {
        const f = item as any;
        finiteStocks.push({
          name: f.getName(),
          initialValue: ExpressionToMathMLConverter.convertToMathml(f.getInitialValue()),
        });
      } else if ("getValue" in item && !("setSourceAndTarget" in item) && !("getExpression" in item)) {
        infiniteStocks.push({ name: item.getName() });
      } else if ("setSourceAndTarget" in item) {
        const r = item as any;
        rates.push({
          name: r.getName(),
          expression: ExpressionToMathMLConverter.convertToMathml(r.getExpression()),
          source: r.getFromLevel()?.getName(),
          target: r.getToLevel()?.getName(),
        });
      } else if ("getExpression" in item) {
        const v = item as any;
        auxiliaries.push({
          name: v.getName(),
          expression: ExpressionToMathMLConverter.convertToMathml(v.getExpression()),
        });
      } else if ("getSource" in item) {
        const inf = item as any;
        informations.push({
          name: inf.getName(),
          source: inf.getSource()?.getName(),
          consumer: inf.getTarget()?.getName(),
        });
      }
    }

    const docObj = {
      systemDynamicsModel: {
        name: model.getName(),
        stocks: { finiteStock: finiteStocks },
        infiniteStocks: { infiniteStock: infiniteStocks },
        rates: { rate: rates },
        auxiliaries: { auxiliary: auxiliaries },
        informations: { information: informations },
      },
    };

    const builder = new XMLBuilder({ ignoreAttributes: false, format: true });
    return builder.build(docObj);
  }
}
