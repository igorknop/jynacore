import { describe, it, expect } from "vitest";
import * as path from "path";
import * as fs from "fs";
import { OscilatorySystem } from "../src/examples/SystemDynamicsExamples.js";
import { SystemDynamicsModelStorerYAML } from "../src/systemdynamics/storer/SystemDynamicsModelStorerYAML.js";
import { DefaultSystemDynamicsEulerMethod } from "../src/systemdynamics/simulator/SystemDynamicsSimulationMethod.js";
import { DefaultFiniteStock } from "../src/systemdynamics/SystemDynamicsItem.js";

describe("SystemDynamicsYAMLTest", () => {
  const storer = new SystemDynamicsModelStorerYAML();

  it("testSaveAndReloadModelYAML", () => {
    const model = new OscilatorySystem();
    const tmpPath = path.join(process.cwd(), "oscilatory-temp.jynay");

    storer.saveToFile(model, tmpPath);
    expect(fs.existsSync(tmpPath)).toBe(true);

    const reloadedModel = storer.loadFromFile(tmpPath);
    expect(reloadedModel.get("X")).toBeDefined();
    expect(reloadedModel.get("Y")).toBeDefined();

    const method = new DefaultSystemDynamicsEulerMethod();
    method.setModel(reloadedModel);
    method.setInitialTime(0.0);
    method.setStepSize(1.0);
    method.reset();

    for (let i = 0; i < 10; i++) method.step();
    const xVal = (reloadedModel.get("X") as DefaultFiniteStock).getValue();
    expect(Math.abs(0.57079 - xVal)).toBeLessThan(0.01);

    if (fs.existsSync(tmpPath)) fs.unlinkSync(tmpPath);
  });
});
