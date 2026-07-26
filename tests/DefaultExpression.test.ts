import { describe, it, expect, beforeEach } from "vitest";
import {
  BooleanOperator,
  Expression,
  ExpressionValueType,
  NumberOperator,
  OperatorEvaluator,
} from "../src/expression/ExpressionTypes.js";
import { DefaultExpression } from "../src/expression/DefaultExpression.js";
import { DefaultOperatorEvaluator } from "../src/expression/OperatorEvaluator.js";

describe("DefaultExpressionTest", () => {
  let operatorEvaluator: OperatorEvaluator;
  let exprConstant: Expression;
  let exprConstant2: Expression;
  let exprDoubleSimpleOperator: Expression;
  let exprBooleanSimpleOperator: Expression;

  beforeEach(() => {
    operatorEvaluator = new DefaultOperatorEvaluator();
    exprBooleanSimpleOperator = new DefaultExpression();
    exprConstant = new DefaultExpression();
    exprConstant2 = new DefaultExpression();
    exprDoubleSimpleOperator = new DefaultExpression();
  });

  it("testEvaluateDoubleConstant", () => {
    exprConstant.setOperatorEvaluator(operatorEvaluator);
    exprConstant.setValue(10.0);
    exprConstant.setOperator(NumberOperator.CONSTANT);
    expect(exprConstant.evaluate()).toBe(10.0);
  });

  it("testEvaluateBooleanConstant", () => {
    exprConstant2.setOperatorEvaluator(operatorEvaluator);
    exprConstant2.setValue(true);
    exprConstant2.setOperator(BooleanOperator.CONSTANT);
    expect(exprConstant2.evaluate()).toBe(true);
  });

  it("testEvaluateDoubleSimpleOperators", () => {
    // (2+3)*(100/20)
    exprDoubleSimpleOperator.setOperatorEvaluator(operatorEvaluator);
    exprDoubleSimpleOperator.setOperator(NumberOperator.TIMES);

    const exprSum = new DefaultExpression();
    exprSum.setOperator(NumberOperator.ADD);
    exprSum.setValueType(ExpressionValueType.NUMBER);
    exprSum.setLeftOperand(
      new DefaultExpression(2.0, NumberOperator.CONSTANT, ExpressionValueType.NUMBER, operatorEvaluator)
    );
    exprSum.setRightOperand(
      new DefaultExpression(3.0, NumberOperator.CONSTANT, ExpressionValueType.NUMBER, operatorEvaluator)
    );
    exprDoubleSimpleOperator.setLeftOperand(exprSum);

    const exprDiv = new DefaultExpression();
    exprDiv.setOperator(NumberOperator.DIVIDE);
    exprDiv.setValueType(ExpressionValueType.NUMBER);
    exprDiv.setLeftOperand(
      new DefaultExpression(100.0, NumberOperator.CONSTANT, ExpressionValueType.NUMBER, operatorEvaluator)
    );
    exprDiv.setRightOperand(
      new DefaultExpression(20.0, NumberOperator.CONSTANT, ExpressionValueType.NUMBER, operatorEvaluator)
    );
    exprDoubleSimpleOperator.setRightOperand(exprDiv);

    expect(exprDoubleSimpleOperator.evaluate()).toBe(25.0);
  });

  it("testEvaluateDoubleModOperator", () => {
    const exprMod = new DefaultExpression();
    exprMod.setOperator(NumberOperator.MOD);
    exprMod.setValueType(ExpressionValueType.NUMBER);
    exprMod.setLeftOperand(
      new DefaultExpression(100.0, NumberOperator.CONSTANT, ExpressionValueType.NUMBER, operatorEvaluator)
    );
    exprMod.setRightOperand(
      new DefaultExpression(20.0, NumberOperator.CONSTANT, ExpressionValueType.NUMBER, operatorEvaluator)
    );

    expect(exprMod.evaluate()).toBe(0.0);

    const exprMod2 = new DefaultExpression();
    exprMod2.setOperator(NumberOperator.MOD);
    exprMod2.setValueType(ExpressionValueType.NUMBER);
    exprMod2.setLeftOperand(
      new DefaultExpression(105.0, NumberOperator.CONSTANT, ExpressionValueType.NUMBER, operatorEvaluator)
    );
    exprMod2.setRightOperand(
      new DefaultExpression(20.0, NumberOperator.CONSTANT, ExpressionValueType.NUMBER, operatorEvaluator)
    );

    expect(exprMod2.evaluate()).toBe(5.0);
  });

  it("testEvaluateBooleanSimpleOperators", () => {
    // !(2>3)&&(1<=1)
    exprBooleanSimpleOperator.setOperatorEvaluator(operatorEvaluator);
    exprBooleanSimpleOperator.setOperator(BooleanOperator.AND);
    exprBooleanSimpleOperator.setValueType(ExpressionValueType.BOOLEAN);

    const exprGT = new DefaultExpression();
    exprGT.setOperator(BooleanOperator.GREATERTHAN);
    exprGT.setValueType(ExpressionValueType.BOOLEAN);
    exprGT.setLeftOperand(
      new DefaultExpression(2.0, NumberOperator.CONSTANT, ExpressionValueType.NUMBER, operatorEvaluator)
    );
    exprGT.setRightOperand(
      new DefaultExpression(3.0, NumberOperator.CONSTANT, ExpressionValueType.NUMBER, operatorEvaluator)
    );

    const exprNot = new DefaultExpression(null, BooleanOperator.NOT, ExpressionValueType.BOOLEAN, operatorEvaluator);
    exprNot.setMiddleOperand(exprGT);

    exprBooleanSimpleOperator.setLeftOperand(exprNot);

    const exprLET = new DefaultExpression();
    exprLET.setOperator(BooleanOperator.LOWEROREQUALTHAN);
    exprLET.setValueType(ExpressionValueType.BOOLEAN);
    exprLET.setLeftOperand(
      new DefaultExpression(1.0, NumberOperator.CONSTANT, ExpressionValueType.NUMBER, operatorEvaluator)
    );
    exprLET.setRightOperand(
      new DefaultExpression(1.0, NumberOperator.CONSTANT, ExpressionValueType.NUMBER, operatorEvaluator)
    );
    exprBooleanSimpleOperator.setRightOperand(exprLET);

    expect(exprBooleanSimpleOperator.evaluate()).toBe(true);
  });
});
