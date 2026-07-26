import { describe, it, expect } from "vitest";
import { OscilatorySystem } from "../src/examples/SystemDynamicsExamples.js";
import { DefaultFiniteStock } from "../src/systemdynamics/SystemDynamicsItem.js";
import { DefaultSystemDynamicsEulerMethod } from "../src/systemdynamics/simulator/SystemDynamicsSimulationMethod.js";

describe("OscilatorySystemTest", () => {
  it("testOscilatorySystem", () => {
    const model = new OscilatorySystem();
    const method = new DefaultSystemDynamicsEulerMethod();
    method.setModel(model);
    method.setInitialTime(0.0);
    method.setStepSize(1.0);
    method.reset();

    // 10 steps (t = 10)
    for (let i = 0; i < 10; i++) method.step();
    expect(method.getTime()).toBe(10.0);
    let xVal = (model.get("X") as DefaultFiniteStock).getValue();
    expect(Math.abs(0.57079 - xVal)).toBeLessThan(0.001);

    // 10 additional steps (t = 20)
    for (let i = 0; i < 10; i++) method.step();
    expect(method.getTime()).toBe(20.0);
    xVal = (model.get("X") as DefaultFiniteStock).getValue();
    expect(Math.abs(-0.453019 - xVal)).toBeLessThan(0.001);

    // 30 additional steps (t = 50)
    for (let i = 0; i < 30; i++) method.step();
    expect(method.getTime()).toBe(50.0);
    xVal = (model.get("X") as DefaultFiniteStock).getValue();
    expect(Math.abs(0.343355 - xVal)).toBeLessThan(0.001);

    // 50 additional steps (t = 100)
    for (let i = 0; i < 50; i++) method.step();
    expect(method.getTime()).toBe(100.0);
    xVal = (model.get("X") as DefaultFiniteStock).getValue();
    expect(Math.abs(-1.40885 - xVal)).toBeLessThan(0.001);
  });
});
