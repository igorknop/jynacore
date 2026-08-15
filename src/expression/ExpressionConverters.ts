import { XMLBuilder, XMLParser } from "fast-xml-parser";
import {
  BooleanOperator,
  Expression,
  NameOperator,
  NumberOperator,
  Operator,
} from "./ExpressionTypes.js";
import {
  DefaultExpression,
  DefaultNameExpression,
  DefaultNumberConstantExpression,
} from "./DefaultExpression.js";

export class ExpressionToMathMLConverter {
  static convertToMathml(expr: Expression): any {
    const parsedObj = this.parseExpressionToObj(expr);
    return {
      "m:math": {
        "@_xmlns:m": "http://www.w3.org/1998/Math/MathML",
        ...parsedObj,
      },
    };
  }

  static convertToMathmlString(expr: Expression): string {
    const builder = new XMLBuilder({
      ignoreAttributes: false,
      format: true,
    });
    return builder.build(this.convertToMathml(expr));
  }

  private static parseExpressionToObj(expr: Expression): any {
    const op = expr.getOperator();
    if (op instanceof NumberOperator) {
      return this.parseNumber(expr);
    } else if (op instanceof NameOperator) {
      return { "m:ci": expr.getValue()?.toString() ?? "" };
    } else if (op instanceof BooleanOperator) {
      return this.parseBoolean(expr);
    }
    return { "m:cn": "0" };
  }

  private static parseBoolean(expr: Expression): any {
    const op = expr.getOperator() as BooleanOperator;
    switch (op.name) {
      case "EQUALS":
        return {
          "m:apply": [
            { "m:eq": "" },
            this.parseExpressionToObj(expr.getLeftOperand()!),
            this.parseExpressionToObj(expr.getRightOperand()!),
          ],
        };
      case "NOTEQUALS":
        return {
          "m:apply": [
            { "m:neq": "" },
            this.parseExpressionToObj(expr.getLeftOperand()!),
            this.parseExpressionToObj(expr.getRightOperand()!),
          ],
        };
      case "GREATERTHAN":
        return {
          "m:apply": [
            { "m:gt": "" },
            this.parseExpressionToObj(expr.getLeftOperand()!),
            this.parseExpressionToObj(expr.getRightOperand()!),
          ],
        };
      case "GREATEROREQUALTHAN":
        return {
          "m:apply": [
            { "m:geq": "" },
            this.parseExpressionToObj(expr.getLeftOperand()!),
            this.parseExpressionToObj(expr.getRightOperand()!),
          ],
        };
      case "LOWERTHAN":
        return {
          "m:apply": [
            { "m:lt": "" },
            this.parseExpressionToObj(expr.getLeftOperand()!),
            this.parseExpressionToObj(expr.getRightOperand()!),
          ],
        };
      case "LOWEROREQUALTHAN":
        return {
          "m:apply": [
            { "m:leq": "" },
            this.parseExpressionToObj(expr.getLeftOperand()!),
            this.parseExpressionToObj(expr.getRightOperand()!),
          ],
        };
      case "AND":
        return {
          "m:apply": [
            { "m:and": "" },
            this.parseExpressionToObj(expr.getLeftOperand()!),
            this.parseExpressionToObj(expr.getRightOperand()!),
          ],
        };
      case "OR":
        return {
          "m:apply": [
            { "m:or": "" },
            this.parseExpressionToObj(expr.getLeftOperand()!),
            this.parseExpressionToObj(expr.getRightOperand()!),
          ],
        };
      case "NOT":
        return {
          "m:apply": [
            { "m:not": "" },
            this.parseExpressionToObj(expr.getMiddleOperand()!),
          ],
        };
      case "CONSTANT":
        return expr.getValue() ? { "m:true": "" } : { "m:false": "" };
      default:
        return { "m:true": "" };
    }
  }

  private static parseNumber(expr: Expression): any {
    const op = expr.getOperator() as NumberOperator;
    switch (op.name) {
      case "ADD":
        return {
          "m:apply": [
            { "m:plus": "" },
            this.parseExpressionToObj(expr.getLeftOperand()!),
            this.parseExpressionToObj(expr.getRightOperand()!),
          ],
        };
      case "MINUS":
        return {
          "m:apply": [
            { "m:minus": "" },
            this.parseExpressionToObj(expr.getLeftOperand()!),
            this.parseExpressionToObj(expr.getRightOperand()!),
          ],
        };
      case "TIMES":
        return {
          "m:apply": [
            { "m:times": "" },
            this.parseExpressionToObj(expr.getLeftOperand()!),
            this.parseExpressionToObj(expr.getRightOperand()!),
          ],
        };
      case "DIVIDE":
        return {
          "m:apply": [
            { "m:divide": "" },
            this.parseExpressionToObj(expr.getLeftOperand()!),
            this.parseExpressionToObj(expr.getRightOperand()!),
          ],
        };
      case "MOD":
        return {
          "m:apply": [
            { "m:remainder": "" },
            this.parseExpressionToObj(expr.getLeftOperand()!),
            this.parseExpressionToObj(expr.getRightOperand()!),
          ],
        };
      case "NEGATION":
        return {
          "m:apply": [
            { "m:inverse": "" },
            this.parseExpressionToObj(expr.getMiddleOperand() ?? expr.getLeftOperand()!),
          ],
        };
      case "CONSTANT":
        return { "m:cn": String(expr.getValue()) };
      default:
        return { "m:cn": String(expr.getValue() ?? 0) };
    }
  }

  static readFromMathML(mathObjOrXmlStr: any): Expression {
    let mathNode: any = mathObjOrXmlStr;
    if (typeof mathObjOrXmlStr === "string") {
      const parser = new XMLParser({
        ignoreAttributes: false,
        removeNSPrefix: false,
        parseTagValue: false,
      });
      mathNode = parser.parse(mathObjOrXmlStr);
    }
    const root = mathNode.math || mathNode["m:math"] || mathNode;
    const firstKey = Object.keys(root).find((k) => !k.startsWith("@_"));
    if (!firstKey) return new DefaultExpression();

    return this.readNode(firstKey, root[firstKey]);
  }

  private static readNode(tagName: string, nodeVal: any): Expression {
    const cleanTag = tagName.replace(/^.*:/, "");

    if (cleanTag === "cn") {
      const numText = typeof nodeVal === "object" ? nodeVal["#text"] ?? "0" : String(nodeVal);
      return new DefaultNumberConstantExpression(parseFloat(numText));
    }
    if (cleanTag === "ci") {
      const strText = typeof nodeVal === "object" ? nodeVal["#text"] ?? "" : String(nodeVal);
      return new DefaultNameExpression(strText.trim());
    }
    if (cleanTag === "true") {
      const e = new DefaultExpression();
      e.setOperator(BooleanOperator.CONSTANT);
      e.setValue(true);
      return e;
    }
    if (cleanTag === "false") {
      const e = new DefaultExpression();
      e.setOperator(BooleanOperator.CONSTANT);
      e.setValue(false);
      return e;
    }

    if (cleanTag === "apply") {
      let childPairs: { tag: string; val: any }[] = [];

      if (Array.isArray(nodeVal)) {
        for (const item of nodeVal) {
          for (const k of Object.keys(item)) {
            if (!k.startsWith("@_")) {
              childPairs.push({ tag: k, val: item[k] });
            }
          }
        }
      } else if (typeof nodeVal === "object" && nodeVal !== null) {
        for (const k of Object.keys(nodeVal)) {
          if (!k.startsWith("@_")) {
            const v = nodeVal[k];
            if (Array.isArray(v)) {
              for (const elem of v) {
                childPairs.push({ tag: k, val: elem });
              }
            } else {
              childPairs.push({ tag: k, val: v });
            }
          }
        }
      }

      if (childPairs.length === 0) return new DefaultExpression();

      const opTag = childPairs[0].tag.replace(/^.*:/, "");
      const op = this.getOperatorByTag(opTag);
      const expr = new DefaultExpression();
      expr.setOperator(op);

      const operands = childPairs.slice(1);
      if (operands.length === 1) {
        expr.setMiddleOperand(this.readNode(operands[0].tag, operands[0].val));
      } else if (operands.length >= 2) {
        expr.setLeftOperand(this.readNode(operands[0].tag, operands[0].val));
        expr.setRightOperand(this.readNode(operands[1].tag, operands[1].val));
        if (operands.length >= 3) {
          expr.setMiddleOperand(this.readNode(operands[1].tag, operands[1].val));
          expr.setRightOperand(this.readNode(operands[2].tag, operands[2].val));
        }
      }
      return expr;
    }

    return new DefaultExpression();
  }

  private static getOperatorByTag(tag: string): Operator {
    switch (tag) {
      case "plus":
        return NumberOperator.ADD;
      case "minus":
        return NumberOperator.MINUS;
      case "times":
        return NumberOperator.TIMES;
      case "divide":
        return NumberOperator.DIVIDE;
      case "remainder":
        return NumberOperator.MOD;
      case "power":
        return NumberOperator.POWER;
      case "abs":
        return NumberOperator.ABS;
      case "inverse":
        return NumberOperator.NEGATION;
      case "sin":
        return NumberOperator.SINE;
      case "cos":
        return NumberOperator.COSINE;
      case "tan":
        return NumberOperator.TAN;
      case "exp":
        return NumberOperator.EXP;
      case "log":
        return NumberOperator.LOG;
      case "eq":
        return BooleanOperator.EQUALS;
      case "neq":
        return BooleanOperator.NOTEQUALS;
      case "gt":
        return BooleanOperator.GREATERTHAN;
      case "lt":
        return BooleanOperator.LOWERTHAN;
      case "geq":
        return BooleanOperator.GREATEROREQUALTHAN;
      case "leq":
        return BooleanOperator.LOWEROREQUALTHAN;
      case "and":
        return BooleanOperator.AND;
      case "or":
        return BooleanOperator.OR;
      case "not":
        return BooleanOperator.NOT;
      default:
        return NumberOperator.CONSTANT;
    }
  }

}
