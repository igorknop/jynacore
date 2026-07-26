import { describe, it, expect } from "vitest";
import { ExponencialGrowth, OscilatorySystem } from "../src/examples/SystemDynamicsExamples.js";
import { DefaultFiniteStock } from "../src/systemdynamics/SystemDynamicsItem.js";
import { DefaultSystemDynamicsRK4Method } from "../src/systemdynamics/simulator/SystemDynamicsSimulationMethod.js";

describe("RK4MethodTest", () => {
  it("testExponentialGrowthRK4", () => {
    const model = new ExponencialGrowth();
    const rk4 = new DefaultSystemDynamicsRK4Method();
    rk4.setModel(model);
    rk4.setInitialTime(0.0);
    rk4.setStepSize(0.1);
    rk4.reset();

    // 10 steps of dt=0.1 (t = 1.0)
    for (let i = 0; i < 10; i++) rk4.step();

    const popVal = (model.get("Population") as DefaultFiniteStock).getValue();
    // Theoretical solution for dP/dt = 0.5*P, P(0)=1 => P(1) = e^0.5 ≈ 1.64872
    expect(Math.abs(Math.exp(0.5) - popVal)).toBeLessThan(0.0001);
  });

  it("testOscilatorySystemRK4", () => {
    const model = new OscilatorySystem();
    const rk4 = new DefaultSystemDynamicsRK4Method();
    rk4.setModel(model);
    rk4.setInitialTime(0.0);
    rk4.setStepSize(0.1);
    rk4.reset();

    // 10 steps of dt=0.1 (t = 1.0)
    for (let i = 0; i < 10; i++) rk4.step();

    const xVal = (model.get("X") as DefaultFiniteStock).getValue();
    expect(xVal).toBeGreaterThan(0.0);
  });
});
