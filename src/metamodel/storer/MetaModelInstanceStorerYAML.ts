import * as fs from "fs";
import YAML from "yaml";

export interface ClassInstanceItemData {
  name: string;
  type: "property" | "stock" | "singleRelation" | "multiRelation" | "table";
  value?: number;
  target?: string;
  targets?: string[];
  values?: number[];
}

export interface ClassInstanceData {
  name: string;
  class: string;
  items: ClassInstanceItemData[];
}

export interface ScenarioConnectData {
  scenario: string;
  name: string;
  instance: string;
}

export interface ScenariosData {
  files: string[];
  connects: ScenarioConnectData[];
}

export interface MetaModelInstanceData {
  name: string;
  metaModelFile: string;
  classInstances: ClassInstanceData[];
  scenarios?: ScenariosData;
}

export class MetaModelInstanceStorerYAML {
  static readonly EXTENSION = "jyi";

  loadFromFile(filePath: string): MetaModelInstanceData {
    const yamlText = fs.readFileSync(filePath, "utf-8");
    return this.loadFromYamlString(yamlText);
  }

  loadFromYamlString(yamlText: string): MetaModelInstanceData {
    const parsed = YAML.parse(yamlText);
    return {
      name: parsed.name ?? "MetaModelInstance",
      metaModelFile: parsed.metaModelFile ?? "",
      classInstances: parsed.classInstances ?? [],
      scenarios: parsed.scenarios ?? { files: [], connects: [] },
    };
  }

  saveToFile(instance: MetaModelInstanceData, filePath: string): void {
    const yamlStr = this.saveToYamlString(instance);
    fs.writeFileSync(filePath, yamlStr, "utf-8");
  }

  saveToYamlString(instance: MetaModelInstanceData): string {
    return YAML.stringify(instance);
  }
}
