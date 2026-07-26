import { describe, it, expect, beforeEach } from "vitest";
import { DefaultNameExpression, DefaultNumberConstantExpression } from "../src/expression/DefaultExpression.js";
import { DefaultFiniteStock, DefaultInfiniteStock, DefaultInformation, DefaultRate, FiniteStock, InfiniteStock, Rate } from "../src/systemdynamics/SystemDynamicsItem.js";
import { DefaultSystemDynamicsModel, SystemDynamicsModel } from "../src/systemdynamics/SystemDynamicsModel.js";
import { DefaultSystemDynamicsEulerMethod } from "../src/systemdynamics/simulator/SystemDynamicsSimulationMethod.js";

describe("EulerMethodTest", () => {
  let model1: SystemDynamicsModel;
  let finLevel: FiniteStock;
  let infLevel: InfiniteStock;
  let cteRate: Rate;

  let model2: SystemDynamicsModel;
  let finLevel2: FiniteStock;
  let finLevel3: FiniteStock;
  let cteRate2: Rate;

  let model3: SystemDynamicsModel;
  let finLevel4: FiniteStock;
  let finLevel5: FiniteStock;
  let rate3: Rate;

  function setUpModel1() {
    model1 = new DefaultSystemDynamicsModel();
    finLevel = new DefaultFiniteStock();
    finLevel.setInitialValue(0.0);
    infLevel = new DefaultInfiniteStock();
    cteRate = new DefaultRate();
    cteRate.setExpression(new DefaultNumberConstantExpression(3.0));
    cteRate.setSourceAndTarget(infLevel, finLevel);

    model1.put("Stock", finLevel);
    model1.put("Source", infLevel);
    model1.put("Rate", cteRate);
  }

  function setUpModel2() {
    model2 = new DefaultSystemDynamicsModel();
    finLevel2 = new DefaultFiniteStock();
    finLevel2.setInitialValue(10.0);
    finLevel3 = new DefaultFiniteStock();
    finLevel3.setInitialValue(0.0);

    cteRate2 = new DefaultRate();
    cteRate2.setExpression(new DefaultNumberConstantExpression(3.0));
    cteRate2.setSourceAndTarget(finLevel2, finLevel3);

    model2.put("Level2", finLevel2);
    model2.put("Level3", finLevel3);
    model2.put("Rate2", cteRate2);
  }

  function setUpModel3() {
    model3 = new DefaultSystemDynamicsModel();
    finLevel4 = new DefaultFiniteStock();
    finLevel4.setInitialValue(100.0);

    finLevel5 = new DefaultFiniteStock();
    finLevel5.setInitialValue(1.0);

    rate3 = new DefaultRate();
    rate3.setSourceAndTarget(finLevel4, finLevel5);
    rate3.setExpression(new DefaultNameExpression("Level5"));

    model3.put("Level4", finLevel4);
    model3.put("Level5", finLevel5);
    model3.put("Rate3", rate3);

    const inf1 = new DefaultInformation(finLevel5, rate3);
    inf1.setName("inf1");
    model3.put("Inf1", inf1);
  }

  beforeEach(() => {
    setUpModel1();
    setUpModel2();
    setUpModel3();
  });

  it("testInfLvlRateCteLvl", () => {
    const method = new DefaultSystemDynamicsEulerMethod();
    method.setModel(model1);
    method.setInitialTime(0.0);
    method.setStepSize(1.0);
    method.reset();
    method.step();
    expect((method.getModel().get("Stock") as FiniteStock).getValue()).toBe(3.0);
    method.step();
    method.step();
    expect((method.getModel().get("Stock") as FiniteStock).getValue()).toBe(9.0);
  });

  it("testLvlRateCteLvl", () => {
    const method2 = new DefaultSystemDynamicsEulerMethod();
    method2.setModel(model2);
    method2.setInitialTime(0.0);
    method2.setStepSize(1.0);
    method2.reset();
    method2.step();
    expect((method2.getModel().get("Level3") as FiniteStock).getValue()).toBe(3.0);
    expect((method2.getModel().get("Level2") as FiniteStock).getValue()).toBe(7.0);
  });

  it("testInfRateLvl", () => {
    const method3 = new DefaultSystemDynamicsEulerMethod();
    method3.setModel(model3);
    method3.setInitialTime(0.0);
    method3.setStepSize(1.0);
    method3.reset();
    method3.step();
    expect((method3.getModel().get("Level4") as FiniteStock).getValue()).toBe(99.0);
    expect((method3.getModel().get("Level5") as FiniteStock).getValue()).toBe(2.0);
    method3.step();
    expect((method3.getModel().get("Level4") as FiniteStock).getValue()).toBe(97.0);
    expect((method3.getModel().get("Level5") as FiniteStock).getValue()).toBe(4.0);
    method3.step();
    expect((method3.getModel().get("Level4") as FiniteStock).getValue()).toBe(93.0);
    expect((method3.getModel().get("Level5") as FiniteStock).getValue()).toBe(8.0);
  });
});
