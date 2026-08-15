import { describe, it, expect } from "vitest";
import * as path from "path";
import * as fs from "fs";
import { MetaModelStorerYAML, MetaModelData } from "../src/metamodel/storer/MetaModelStorerYAML.js";
import { MetaModelInstanceStorerYAML, MetaModelInstanceData } from "../src/metamodel/storer/MetaModelInstanceStorerYAML.js";

describe("MetaModelYAMLTest", () => {
  const mmStorer = new MetaModelStorerYAML();
  const mmiStorer = new MetaModelInstanceStorerYAML();

  it("testSaveAndLoadMetaModelJYM", () => {
    const metaModel: MetaModelData = {
      name: "MediumSoftwareProject",
      classes: [
        {
          name: "Developer",
          items: [
            { name: "WorkTime", type: "finiteStock", initialValue: "<math><cn>10</cn></math>" },
            { name: "Productivity", type: "property", value: 1.5 },
          ],
        },
        {
          name: "Activity",
          items: [
            { name: "RemainingWork", type: "finiteStock", initialValue: "<math><cn>100</cn></math>" },
          ],
        },
      ],
      relations: [
        {
          name: "Team",
          type: "multiRelation",
          source: "Activity",
          target: "Developer",
          targetRole: "members",
        },
      ],
    };

    const tmpJymPath = path.join(process.cwd(), "mediumSoftwareProject-test.jym");
    mmStorer.saveToFile(metaModel, tmpJymPath);
    expect(fs.existsSync(tmpJymPath)).toBe(true);

    const reloaded = mmStorer.loadFromFile(tmpJymPath);
    expect(reloaded.name).toBe("MediumSoftwareProject");
    expect(reloaded.classes.length).toBe(2);
    expect(reloaded.classes[0].name).toBe("Developer");
    expect(reloaded.relations.length).toBe(1);
    expect(reloaded.relations[0].name).toBe("Team");

    if (fs.existsSync(tmpJymPath)) fs.unlinkSync(tmpJymPath);
  });

  it("testSaveAndLoadMetaModelInstanceJYI", () => {
    const instance: MetaModelInstanceData = {
      name: "Medium Software Project Instance",
      metaModelFile: "mediumSoftwareProject-save.jym",
      classInstances: [
        {
          name: "D1",
          class: "Developer",
          items: [
            { name: "Productivity", type: "property", value: 2.0 },
            { name: "WorkTime", type: "stock", value: 15.0 },
          ],
        },
        {
          name: "Coding",
          class: "Activity",
          items: [
            { name: "Team", type: "multiRelation", targets: ["D1"] },
          ],
        },
      ],
      scenarios: {
        files: ["scenario1.jymms"],
        connects: [{ scenario: "Scenario1", name: "Connect1", instance: "D1" }],
      },
    };

    const tmpJyiPath = path.join(process.cwd(), "mediumSoftwareProjectInstance-test.jyi");
    mmiStorer.saveToFile(instance, tmpJyiPath);
    expect(fs.existsSync(tmpJyiPath)).toBe(true);

    const reloaded = mmiStorer.loadFromFile(tmpJyiPath);
    expect(reloaded.name).toBe("Medium Software Project Instance");
    expect(reloaded.metaModelFile).toBe("mediumSoftwareProject-save.jym");
    expect(reloaded.classInstances.length).toBe(2);
    expect(reloaded.classInstances[0].name).toBe("D1");
    expect(reloaded.classInstances[1].items[0].targets).toContain("D1");

    if (fs.existsSync(tmpJyiPath)) fs.unlinkSync(tmpJyiPath);
  });
});
