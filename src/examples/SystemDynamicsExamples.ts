import { NumberOperator } from "../expression/ExpressionTypes.js";
import { DefaultExpression, DefaultNameExpression, DefaultNumberConstantExpression } from "../expression/DefaultExpression.js";
import { DefaultFiniteStock, DefaultInfiniteStock, DefaultInformation, DefaultRate, DefaultVariable, FiniteStock, InfiniteStock, Rate, Variable } from "../systemdynamics/SystemDynamicsItem.js";
import { DefaultSystemDynamicsModel } from "../systemdynamics/SystemDynamicsModel.js";

export class ExponencialGrowth extends DefaultSystemDynamicsModel {
  static readonly GROWTH_RATE = "GrowthRate";
  static readonly POPULATON_SOURCE = "PopulatonSource";
  static readonly CURRENT_FACTOR = "CurrentFactor";
  static readonly CURRENT_POPULATION = "CurrentPopulation";
  static readonly GROWTH_FACTOR_VALUE = 0.5;
  static readonly GROWTH_FACTOR = "GrowthFactor";
  static readonly POPULATION_INITIAL_VALUE = 1.0;
  static readonly POPULATION = "Population";

  constructor() {
    super();

    const popSource = new DefaultInfiniteStock();
    popSource.setName(ExponencialGrowth.POPULATON_SOURCE);

    const population = new DefaultFiniteStock();
    population.setName(ExponencialGrowth.POPULATION);
    population.setInitialValue(new DefaultNumberConstantExpression(ExponencialGrowth.POPULATION_INITIAL_VALUE));

    const growthFactor = new DefaultVariable();
    growthFactor.setName(ExponencialGrowth.GROWTH_FACTOR);
    growthFactor.setExpression(new DefaultNumberConstantExpression(ExponencialGrowth.GROWTH_FACTOR_VALUE));

    const growthRate = new DefaultRate();
    growthRate.setName(ExponencialGrowth.GROWTH_RATE);
    growthRate.setSourceAndTarget(popSource, population);

    const exprTimes = new DefaultExpression(NumberOperator.TIMES);
    exprTimes.setLeftOperand(new DefaultNameExpression(ExponencialGrowth.GROWTH_FACTOR));
    exprTimes.setRightOperand(new DefaultNameExpression(ExponencialGrowth.POPULATION));
    growthRate.setExpression(exprTimes);

    const currentPopulation = new DefaultInformation(population, growthRate);
    currentPopulation.setName(ExponencialGrowth.CURRENT_POPULATION);
    const currentFactor = new DefaultInformation(growthFactor, growthRate);
    currentFactor.setName(ExponencialGrowth.CURRENT_FACTOR);

    this.put(ExponencialGrowth.POPULATION, population);
    this.put(ExponencialGrowth.POPULATON_SOURCE, popSource);
    this.put(ExponencialGrowth.GROWTH_RATE, growthRate);
    this.put(ExponencialGrowth.GROWTH_FACTOR, growthFactor);
    this.put(ExponencialGrowth.CURRENT_POPULATION, currentPopulation);
    this.put(ExponencialGrowth.CURRENT_FACTOR, currentFactor);
  }
}

export class OscilatorySystem extends DefaultSystemDynamicsModel {
  public XSource: InfiniteStock;
  public X: FiniteStock;
  public YSource: InfiniteStock;
  public Y: FiniteStock;
  public factor: Variable;
  public desiredX: Variable;
  public difference: Variable;
  public dX: Rate;
  public dY: Rate;

  constructor() {
    super();

    this.XSource = new DefaultInfiniteStock();
    this.X = new DefaultFiniteStock();
    this.X.setName("X");
    this.X.setInitialValue(new DefaultNumberConstantExpression(1.0));

    this.YSource = new DefaultInfiniteStock();
    this.Y = new DefaultFiniteStock();
    this.Y.setName("Y");
    this.Y.setInitialValue(new DefaultNumberConstantExpression(0.0));

    this.factor = new DefaultVariable();
    this.factor.setName("factor");
    this.factor.setExpression(new DefaultNumberConstantExpression(0.01));

    this.desiredX = new DefaultVariable();
    this.desiredX.setName("desiredX");
    this.desiredX.setExpression(new DefaultNumberConstantExpression(0.0));

    this.difference = new DefaultVariable();
    this.difference.setName("difference");

    const exprDiff = new DefaultExpression(NumberOperator.MINUS);
    exprDiff.setLeftOperand(new DefaultNameExpression("X"));
    exprDiff.setRightOperand(new DefaultNameExpression("desiredX"));
    this.difference.setExpression(exprDiff);

    this.dX = new DefaultRate();
    this.dX.setSourceAndTarget(this.XSource, this.X);
    this.dX.setExpression(new DefaultNameExpression("Y"));

    this.dY = new DefaultRate();
    this.dY.setSourceAndTarget(this.YSource, this.Y);

    const exprTimes = new DefaultExpression(NumberOperator.TIMES);
    const exprNeg = new DefaultExpression(NumberOperator.NEGATION);
    exprNeg.setMiddleOperand(new DefaultNameExpression("factor"));
    exprTimes.setLeftOperand(exprNeg);
    exprTimes.setRightOperand(new DefaultNameExpression("difference"));
    this.dY.setExpression(exprTimes);

    const currentX = new DefaultInformation(this.X, this.difference);
    const currentDesiredX = new DefaultInformation(this.desiredX, this.difference);
    const currentY = new DefaultInformation(this.Y, this.dX);
    const currentFactor = new DefaultInformation(this.factor, this.dY);
    const currentDiff = new DefaultInformation(this.difference, this.dY);

    this.put("X", this.X);
    this.put("Y", this.Y);
    this.put("dX", this.dX);
    this.put("dY", this.dY);
    this.put("XSource", this.XSource);
    this.put("YSource", this.YSource);
    this.put("difference", this.difference);
    this.put("desiredX", this.desiredX);
    this.put("factor", this.factor);

    this.put("CurrentX", currentX);
    this.put("CurrentY", currentY);
    this.put("CurrentFactor", currentFactor);
    this.put("CurrentDesiredX", currentDesiredX);
    this.put("CurrentDifference", currentDiff);
  }
}
