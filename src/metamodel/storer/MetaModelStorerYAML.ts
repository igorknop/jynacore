import * as fs from "fs";
import YAML from "yaml";

export interface MetaModelClassItemData {
  name: string;
  type: "finiteStock" | "auxiliary" | "property" | "rate" | "table";
  initialValue?: string;
  expression?: string;
  value?: number;
  source?: string;
  target?: string;
  values?: number[];
}

export interface MetaModelClassData {
  name: string;
  items: MetaModelClassItemData[];
}

export interface MetaModelRelationData {
  name: string;
  type: "singleRelation" | "multiRelation";
  source: string;
  target: string;
  targetRole?: string;
}

export interface MetaModelData {
  name: string;
  classes: MetaModelClassData[];
  relations: MetaModelRelationData[];
}

export class MetaModelStorerYAML {
  static readonly EXTENSION = "jym";

  loadFromFile(filePath: string): MetaModelData {
    const yamlText = fs.readFileSync(filePath, "utf-8");
    return this.loadFromYamlString(yamlText);
  }

  loadFromYamlString(yamlText: string): MetaModelData {
    const parsed = YAML.parse(yamlText);
    return {
      name: parsed.name ?? "MetaModel",
      classes: parsed.classes ?? [],
      relations: parsed.relations ?? [],
    };
  }

  saveToFile(model: MetaModelData, filePath: string): void {
    const yamlStr = this.saveToYamlString(model);
    fs.writeFileSync(filePath, yamlStr, "utf-8");
  }

  saveToYamlString(model: MetaModelData): string {
    return YAML.stringify(model);
  }
}
