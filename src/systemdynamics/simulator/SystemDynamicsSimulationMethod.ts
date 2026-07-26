import { Expression, NameOperator } from "../../expression/ExpressionTypes.js";
import { DefaultNumberConstantExpression, DefaultReferenceExpression } from "../../expression/DefaultExpression.js";
import { DefaultOperatorEvaluatorWithParameters } from "../../expression/OperatorEvaluator.js";
import { Auxiliary, FiniteStock, Rate, Variable, DefaultVariable } from "../SystemDynamicsItem.js";
import { SystemDynamicsModel } from "../SystemDynamicsModel.js";

export interface SystemDynamicsSimulationMethod {
  getStepSize(): number;
  setStepSize(stepSize: number): void;
  getInitialTime(): number;
  setInitialTime(initialTime: number): void;
  getTime(): number;
  getModel(): SystemDynamicsModel;
  setModel(model: SystemDynamicsModel): void;
  reset(): void;
  step(): void;
}

export class DefaultSystemDynamicsEulerMethod implements SystemDynamicsSimulationMethod {
  private interval: number = 1.0;
  private rates = new Map<string, Rate>();
  private finiteLevels = new Map<string, FiniteStock>();
  private auxiliaries = new Map<string, Auxiliary>();
  private model!: SystemDynamicsModel;
  private initialTime: number = 0.0;
  private currentStep: number = 0;
  private currentTime: number = 0.0;
  private _TIME_: Variable;
  private _TIME_STEP_: Variable;

  constructor() {
    this._TIME_ = new DefaultVariable();
    this._TIME_.setName("_TIME_");
    this._TIME_.setExpression(new DefaultNumberConstantExpression(this.currentTime));
    this._TIME_STEP_ = new DefaultVariable();
    this._TIME_STEP_.setName("_TIME_STEP_");
    this._TIME_STEP_.setExpression(new DefaultNumberConstantExpression(this.interval));
    this.setInterval(1.0);
  }

  getStepSize(): number {
    return this.interval;
  }

  setStepSize(newInterval: number): void {
    this.setInterval(newInterval);
  }

  private setInterval(newInterval: number): void {
    this.interval = newInterval;
    this._TIME_STEP_.getExpression()?.setValue(this.interval);
  }

  getInitialTime(): number {
    return this.initialTime;
  }

  setInitialTime(newInitialTime: number): void {
    this.initialTime = newInitialTime;
  }

  getTime(): number {
    return this.currentTime;
  }

  getModel(): SystemDynamicsModel {
    return this.model;
  }

  setModel(model: SystemDynamicsModel): void {
    this.model = model;
    this.rates.clear();
    this.finiteLevels.clear();
    this.auxiliaries.clear();

    for (const [key, item] of model.getItems().entries()) {
      if ("setSourceAndTarget" in item) {
        this.rates.set(key, item as Rate);
      }
      if ("getInitialValue" in item) {
        this.finiteLevels.set(key, item as FiniteStock);
      }
      if ("getExpression" in item && !("getInitialValue" in item) && !("setSourceAndTarget" in item)) {
        this.auxiliaries.set(key, item as unknown as Auxiliary);
      }
    }
    this.setupReferences();
  }

  private setupReferences(): void {
    if (!this.model) return;
    for (const o of this.model.getFiniteLevels()) {
      this.name2ref(o.getInitialValue());
    }
    for (const o of this.model.getRates()) {
      this.name2ref(o.getExpression());
    }
    for (const o of this.model.getAuxiliaries()) {
      if ("getExpression" in o) {
        this.name2ref((o as Variable).getExpression());
      }
    }
  }

  private name2ref(expr: Expression | null): void {
    if (!expr) return;
    if (expr.getOperator() instanceof NameOperator) {
      const val = expr.getValue();
      if (val === "_TIME_") {
        expr.setMiddleOperand(new DefaultReferenceExpression(this._TIME_));
      } else if (val === "_TIME_STEP_") {
        expr.setMiddleOperand(new DefaultReferenceExpression(this._TIME_STEP_));
      } else {
        expr.setMiddleOperand(new DefaultReferenceExpression(this.model.get(String(val))));
      }
    } else {
      this.name2ref(expr.getLeftOperand());
      this.name2ref(expr.getMiddleOperand());
      this.name2ref(expr.getRightOperand());
    }
  }

  reset(): void {
    this.currentStep = 0;
    this.currentTime = this.initialTime;
    this._TIME_.getExpression()?.setValue(this.currentTime);

    for (const [, stock] of this.finiteLevels.entries()) {
      const initExpr = stock.getInitialValue();
      stock.setValue(Number(initExpr.evaluate()));
    }
    for (const [, rate] of this.rates.entries()) {
      const expr = rate.getExpression();
      if (expr) {
        rate.setValue(Number(expr.evaluate()));
      }
    }
    for (const [, aux] of this.auxiliaries.entries()) {
      if ("getExpression" in aux) {
        const v = aux as Variable;
        const expr = v.getExpression();
        if (expr) {
          v.setValue(Number(expr.evaluate()));
        }
      }
    }
  }

  step(): void {
    // 1. Calculate variables
    for (const [, aux] of this.auxiliaries.entries()) {
      if ("getExpression" in aux) {
        const v = aux as Variable;
        const expr = v.getExpression();
        if (expr) {
          v.setValue(Number(expr.evaluate()));
        }
      }
    }

    // 2. Calculate rates values
    for (const [, rate] of this.rates.entries()) {
      const expr = rate.getExpression();
      if (expr) {
        rate.setValue(Number(expr.evaluate()));
      }
    }

    // 3. Calculate rate effects on finite stocks
    for (const [, rate] of this.rates.entries()) {
      const fromLevel = rate.getFromLevel();
      const toLevel = rate.getToLevel();
      const dt = this.getStepSize();

      if (fromLevel && "getInitialValue" in fromLevel) {
        const flevel = fromLevel as FiniteStock;
        flevel.setValue(flevel.getValue() - rate.getValue() * dt);
      }
      if (toLevel && "getInitialValue" in toLevel) {
        const flevel = toLevel as FiniteStock;
        flevel.setValue(flevel.getValue() + rate.getValue() * dt);
      }
    }

    this.currentTime += this.interval;
    this._TIME_.getExpression()?.setValue(this.currentTime);
    this.currentStep++;
  }
}

export class DefaultSystemDynamicsRK4Method implements SystemDynamicsSimulationMethod {
  private initialTime: number = 0.0;
  private model!: SystemDynamicsModel;
  private stepSize: number = 0.1;
  private currentTime: number = 0.0;
  private rates = new Map<string, Rate>();
  private finiteLevels = new Map<string, FiniteStock>();
  private auxiliaries = new Map<string, Auxiliary>();
  private currentStep: number = 0;
  private _TIME_: Variable;
  private _TIME_STEP_: Variable;

  constructor() {
    this._TIME_ = new DefaultVariable();
    this._TIME_.setName("_TIME_");
    this._TIME_.setExpression(new DefaultNumberConstantExpression(this.currentTime));
    this._TIME_STEP_ = new DefaultVariable();
    this._TIME_STEP_.setName("_TIME_STEP_");
    this._TIME_STEP_.setExpression(new DefaultNumberConstantExpression(this.stepSize));
  }

  getInitialTime(): number {
    return this.initialTime;
  }

  setInitialTime(newInitialTime: number): void {
    this.initialTime = newInitialTime;
  }

  getModel(): SystemDynamicsModel {
    return this.model;
  }

  setModel(model: SystemDynamicsModel): void {
    this.model = model;
    this.rates.clear();
    this.finiteLevels.clear();
    this.auxiliaries.clear();

    for (const [key, item] of model.getItems().entries()) {
      if ("setSourceAndTarget" in item) {
        this.rates.set(key, item as Rate);
      }
      if ("getInitialValue" in item) {
        this.finiteLevels.set(key, item as FiniteStock);
      }
      if ("getExpression" in item && !("getInitialValue" in item) && !("setSourceAndTarget" in item)) {
        this.auxiliaries.set(key, item as unknown as Auxiliary);
      }
    }
    this.setupReferences();
  }

  private setupReferences(): void {
    if (!this.model) return;
    for (const o of this.model.getFiniteLevels()) {
      this.name2ref(o.getInitialValue());
    }
    for (const o of this.model.getRates()) {
      this.name2ref(o.getExpression());
    }
    for (const o of this.model.getAuxiliaries()) {
      if ("getExpression" in o) {
        this.name2ref((o as Variable).getExpression());
      }
    }
  }

  private name2ref(expr: Expression | null): void {
    if (!expr) return;
    if (expr.getOperator() instanceof NameOperator) {
      const val = expr.getValue();
      if (val === "_TIME_") {
        expr.setMiddleOperand(new DefaultReferenceExpression(this._TIME_));
      } else if (val === "_TIME_STEP_") {
        expr.setMiddleOperand(new DefaultReferenceExpression(this._TIME_STEP_));
      } else {
        expr.setMiddleOperand(new DefaultReferenceExpression(this.model.get(String(val))));
      }
    } else {
      this.name2ref(expr.getLeftOperand());
      this.name2ref(expr.getMiddleOperand());
      this.name2ref(expr.getRightOperand());
    }
  }

  getStepSize(): number {
    return this.stepSize;
  }

  setStepSize(newStepSize: number): void {
    this.stepSize = newStepSize;
    this._TIME_STEP_.getExpression()?.setValue(this.stepSize);
  }

  getTime(): number {
    return this.currentTime;
  }

  reset(): void {
    for (const [, stock] of this.finiteLevels.entries()) {
      stock.setValue(Number(stock.getInitialValue().evaluate()));
    }
    for (const [, rate] of this.rates.entries()) {
      rate.setValue(0.0);
    }
    for (const [, aux] of this.auxiliaries.entries()) {
      if ("getExpression" in aux) {
        const v = aux as Variable;
        const expr = v.getExpression();
        if (expr) {
          v.setValue(Number(expr.evaluate()));
        }
      }
    }
    this.currentStep = 0;
    this.currentTime = this.initialTime;
    this._TIME_.getExpression()?.setValue(this.currentTime);
  }

  step(): void {
    let x0: number, x1: number, x2: number, x3: number, x4: number, x5: number;
    let k1: number, k2: number, k3: number, k4: number;

    // 1. Calculate variables
    for (const [, aux] of this.auxiliaries.entries()) {
      if ("getExpression" in aux) {
        const v = aux as Variable;
        const expr = v.getExpression();
        if (expr) {
          v.setValue(Number(expr.evaluate()));
        }
      }
    }

    // 2. RK4 step for each rate
    for (const [, rate] of this.rates.entries()) {
      const origExpr = rate.getExpression();
      if (!origExpr) continue;

      const nf = origExpr.getDeepCopy();
      const oev = new DefaultOperatorEvaluatorWithParameters();
      nf.setOperatorEvaluator(oev);

      const fromLevel = rate.getFromLevel();
      const toLevel = rate.getToLevel();
      const dt = this.getStepSize();

      if (fromLevel && "getInitialValue" in fromLevel) {
        const flevel = fromLevel as FiniteStock;
        x0 = flevel.getValue();
        x1 = x0;
        oev.getParameters().set(flevel.getName(), x1);
        k1 = Number(nf.evaluate());

        x2 = x0 + (k1 * dt) / 2;
        oev.getParameters().set(flevel.getName(), x2);
        k2 = Number(nf.evaluate());

        x3 = x0 + (k2 * dt) / 2;
        oev.getParameters().set(flevel.getName(), x3);
        k3 = Number(nf.evaluate());

        x4 = x0 + k3 * dt;
        oev.getParameters().set(flevel.getName(), x4);
        k4 = Number(nf.evaluate());

        const effRate = (k1 + 2 * k2 + 2 * k3 + k4) / 6;
        x5 = x0 - dt * effRate;
        flevel.setValue(x5);
        rate.setValue(effRate);

        if (toLevel && "getInitialValue" in toLevel) {
          const toFLevel = toLevel as FiniteStock;
          toFLevel.setValue(toFLevel.getValue() + effRate * dt);
        }
      } else if (toLevel && "getInitialValue" in toLevel) {
        const flevel = toLevel as FiniteStock;
        x0 = flevel.getValue();
        x1 = x0;
        oev.getParameters().set(flevel.getName(), x1);
        k1 = Number(nf.evaluate());

        x2 = x0 + (k1 * dt) / 2;
        oev.getParameters().set(flevel.getName(), x2);
        k2 = Number(nf.evaluate());

        x3 = x0 + (k2 * dt) / 2;
        oev.getParameters().set(flevel.getName(), x3);
        k3 = Number(nf.evaluate());

        x4 = x0 + k3 * dt;
        oev.getParameters().set(flevel.getName(), x4);
        k4 = Number(nf.evaluate());

        const effRate = (k1 + 2 * k2 + 2 * k3 + k4) / 6;
        x5 = x0 + dt * effRate;
        flevel.setValue(x5);
        rate.setValue(effRate);
      }
    }

    this.currentTime += this.getStepSize();
    this._TIME_.getExpression()?.setValue(this.currentTime);
    this.currentStep++;
  }
}

export class DefaultSystemDynamicsSimulation {
  private method: SystemDynamicsSimulationMethod;
  private finalTime: number = 10.0;

  constructor(method?: SystemDynamicsSimulationMethod) {
    this.method = method ?? new DefaultSystemDynamicsEulerMethod();
  }

  getMethod(): SystemDynamicsSimulationMethod {
    return this.method;
  }

  setMethod(method: SystemDynamicsSimulationMethod): void {
    this.method = method;
  }

  getFinalTime(): number {
    return this.finalTime;
  }

  setFinalTime(finalTime: number): void {
    this.finalTime = finalTime;
  }

  run(): void {
    this.method.reset();
    while (this.method.getTime() < this.finalTime) {
      this.method.step();
    }
  }
}
