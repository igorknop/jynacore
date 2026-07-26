import { describe, it, expect } from "vitest";
import { ExponencialGrowth } from "../src/examples/SystemDynamicsExamples.js";
import { DefaultFiniteStock } from "../src/systemdynamics/SystemDynamicsItem.js";
import { DefaultSystemDynamicsEulerMethod } from "../src/systemdynamics/simulator/SystemDynamicsSimulationMethod.js";

describe("ExponencialGrowthTest", () => {
  it("TestRun", () => {
    const model = new ExponencialGrowth();
    const method = new DefaultSystemDynamicsEulerMethod();
    method.setModel(model);
    method.setInitialTime(0.0);
    method.setStepSize(1.0);
    method.reset();

    // 2 steps (t = 2)
    for (let i = 0; i < 2; i++) method.step();
    let popVal = (model.get("Population") as DefaultFiniteStock).getValue();
    expect(Math.abs(2.25 - popVal)).toBeLessThan(0.001);

    // 8 additional steps (total 10 steps)
    for (let i = 0; i < 8; i++) method.step();
    popVal = (model.get("Population") as DefaultFiniteStock).getValue();
    expect(Math.abs(57.6650390625 - popVal)).toBeLessThan(0.001);

    // 10 additional steps (total 20 steps)
    for (let i = 0; i < 10; i++) method.step();
    popVal = (model.get("Population") as DefaultFiniteStock).getValue();
    expect(Math.abs(3325.2567300797 - popVal)).toBeLessThan(0.001);

    // 30 additional steps (total 50 steps)
    for (let i = 0; i < 30; i++) method.step();
    popVal = (model.get("Population") as DefaultFiniteStock).getValue();
    expect(Math.abs(637621500.21405 - popVal)).toBeLessThan(0.01);

    // 50 additional steps (total 100 steps)
    for (let i = 0; i < 50; i++) method.step();
    popVal = (model.get("Population") as DefaultFiniteStock).getValue();
    expect(Math.abs(4.0656117753521523e17 - popVal) / 4.0656117753521523e17).toBeLessThan(0.00001);
  });
});
