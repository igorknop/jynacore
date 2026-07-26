import { describe, it, expect } from "vitest";
import * as path from "path";
import * as fs from "fs";
import { OscilatorySystem } from "../src/examples/SystemDynamicsExamples.js";
import { SystemDynamicsModelStorerXML } from "../src/systemdynamics/storer/SystemDynamicsModelStorerXML.js";
import { DefaultSystemDynamicsEulerMethod } from "../src/systemdynamics/simulator/SystemDynamicsSimulationMethod.js";
import { DefaultFiniteStock } from "../src/systemdynamics/SystemDynamicsItem.js";

describe("SystemDynamicsXMLTest", () => {
  const storer = new SystemDynamicsModelStorerXML();
  const sampleXmlPath = path.join(process.cwd(), "src/test/resources/oscilatory-save.jyna");

  it("testLoadExistingOscilatorySave", () => {
    if (!fs.existsSync(sampleXmlPath)) return;

    const model = storer.loadFromFile(sampleXmlPath);
    expect(model).toBeDefined();

    const method = new DefaultSystemDynamicsEulerMethod();
    method.setModel(model);
    method.setInitialTime(0.0);
    method.setStepSize(1.0);
    method.reset();

    for (let i = 0; i < 10; i++) method.step();
    const xVal = (model.get("X") as DefaultFiniteStock).getValue();
    expect(Math.abs(0.57079 - xVal)).toBeLessThan(0.01);
  });

  it("testSaveAndReloadModel", () => {
    const model = new OscilatorySystem();
    const tmpPath = path.join(process.cwd(), "oscilatory-temp.jyna");

    storer.saveToFile(model, tmpPath);
    expect(fs.existsSync(tmpPath)).toBe(true);

    const reloadedModel = storer.loadFromFile(tmpPath);
    expect(reloadedModel.get("X")).toBeDefined();
    expect(reloadedModel.get("Y")).toBeDefined();

    if (fs.existsSync(tmpPath)) fs.unlinkSync(tmpPath);
  });
});
