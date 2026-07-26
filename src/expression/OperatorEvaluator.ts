import {
  BooleanOperator,
  Expression,
  NumberOperator,
  OperatorEvaluator,
  ReferenceOperator,
} from "./ExpressionTypes.js";
import { JynaEvaluated, JynaValued } from "../JynaCoreInterfaces.js";

export class DefaultOperatorEvaluator implements OperatorEvaluator {
  evaluateBoolean(expression: Expression): boolean {
    const op = expression.getOperator();
    if (!(op instanceof BooleanOperator)) {
      return Boolean(expression.getValue());
    }

    const left = expression.getLeftOperand();
    const right = expression.getRightOperand();
    const middle = expression.getMiddleOperand();

    switch (op.name) {
      case "AND":
        return Boolean(left?.evaluate()) && Boolean(right?.evaluate());
      case "OR":
        return Boolean(left?.evaluate()) || Boolean(right?.evaluate());
      case "IMPLIES":
        return !(Boolean(left?.evaluate()) && !Boolean(right?.evaluate()));
      case "IFONLYIF":
        return Boolean(left?.evaluate()) === Boolean(right?.evaluate());
      case "NOT":
        return !Boolean(middle?.evaluate());
      case "EQUALS": {
        const lVal = left?.evaluate();
        const rVal = right?.evaluate();
        return lVal === rVal;
      }
      case "NOTEQUALS": {
        const lVal = left?.evaluate();
        const rVal = right?.evaluate();
        return lVal !== rVal;
      }
      case "GREATERTHAN":
        return Number(left?.evaluate()) > Number(right?.evaluate());
      case "GREATEROREQUALTHAN":
        return Number(left?.evaluate()) >= Number(right?.evaluate());
      case "LOWERTHAN":
        return Number(left?.evaluate()) < Number(right?.evaluate());
      case "LOWEROREQUALTHAN":
        return Number(left?.evaluate()) <= Number(right?.evaluate());
      default:
        return Boolean(expression.getValue());
    }
  }

  evaluateName(expression: Expression): any {
    const target = expression.getMiddleOperand();
    if (target !== null && target !== undefined) {
      if (target.getOperator() instanceof ReferenceOperator) {
        const refObj = target.getValue();
        if (refObj && typeof refObj === "object") {
          if ("getValue" in refObj && typeof refObj.getValue === "function") {
            return refObj.getValue();
          }
        }
        return target.evaluate();
      } else {
        return target.getValue();
      }
    } else {
      return expression.getValue();
    }
  }

  evaluateNumber(expression: Expression): number {
    const op = expression.getOperator();
    if (!(op instanceof NumberOperator)) {
      return Number(expression.getValue());
    }

    const left = expression.getLeftOperand();
    const right = expression.getRightOperand();
    const middle = expression.getMiddleOperand();
    const extra = expression.getExtraOperand();

    switch (op.name) {
      case "ABS":
        return Math.abs(Number(middle?.evaluate()));
      case "ADD":
        return Number(left?.evaluate()) + Number(right?.evaluate());
      case "MINUS":
        return Number(left?.evaluate()) - Number(right?.evaluate());
      case "TIMES":
        return Number(left?.evaluate()) * Number(right?.evaluate());
      case "DIVIDE":
        return Number(left?.evaluate()) / Number(right?.evaluate());
      case "NEGATION":
        return -Number(middle?.evaluate());
      case "MINIMUM":
        return Math.min(Number(left?.evaluate()), Number(right?.evaluate()));
      case "MAXIMUM":
        return Math.max(Number(left?.evaluate()), Number(right?.evaluate()));
      case "CONSTANT":
        return Number(expression.getValue());
      case "LOG":
        return Math.log(Number(middle?.evaluate()));
      case "EXP":
        return Math.exp(Number(middle?.evaluate()));
      case "SINE":
        return Math.sin(Number(middle?.evaluate()));
      case "COSINE":
        return Math.cos(Number(middle?.evaluate()));
      case "TAN":
        return Math.tan(Number(middle?.evaluate()));
      case "SQUARE": {
        const v = Number(middle?.evaluate());
        return v * v;
      }
      case "SQUAREROOT":
        return Math.sqrt(Number(middle?.evaluate()));
      case "MOD": {
        const l = Math.floor(Number(left?.evaluate()));
        const r = Math.floor(Number(right?.evaluate()));
        return l % r;
      }
      case "RAND":
        return Math.random();
      case "RANDINT": {
        const max = Math.round(Number(middle?.evaluate()));
        return Math.floor(Math.random() * max);
      }
      case "POWER":
        return Math.pow(Number(left?.evaluate()), Number(right?.evaluate()));
      case "IF":
        return Boolean(left?.evaluate()) ? Number(middle?.evaluate()) : Number(right?.evaluate());
      case "GROUPSUM": {
        let sum = 0;
        const set = new Set<any>();
        const val = middle?.getValue();
        if (Array.isArray(val) || val instanceof Set) {
          for (const item of val) set.add(item);
        }
        for (const item of set) {
          if (item && typeof item === "object" && "getValue" in item) {
            if (item.getValue() == null && "getExpression" in item && item.getExpression()) {
              item.setValue(item.getExpression().evaluate());
            }
            sum += Number(item.getValue());
          }
        }
        return sum;
      }
      case "GROUPMAX": {
        let max: number | null = null;
        const set = new Set<any>();
        const val = middle?.getValue();
        if (Array.isArray(val) || val instanceof Set) {
          for (const item of val) set.add(item);
        }
        for (const item of set) {
          if (item && typeof item === "object" && "getValue" in item) {
            if (item.getValue() == null && "getExpression" in item && item.getExpression()) {
              item.setValue(item.getExpression().evaluate());
            }
            const v = Number(item.getValue());
            if (max === null || v > max) max = v;
          }
        }
        return max ?? 0;
      }
      case "GROUPMIN": {
        let min: number | null = null;
        const set = new Set<any>();
        const val = middle?.getValue();
        if (Array.isArray(val) || val instanceof Set) {
          for (const item of val) set.add(item);
        }
        for (const item of set) {
          if (item && typeof item === "object" && "getValue" in item) {
            if (item.getValue() == null && "getExpression" in item && item.getExpression()) {
              item.setValue(item.getExpression().evaluate());
            }
            const v = Number(item.getValue());
            if (min === null || v < min) min = v;
          }
        }
        return min ?? 0;
      }
      case "GROUPCOUNT": {
        const set = new Set<any>();
        const val = middle?.getValue();
        if (Array.isArray(val) || val instanceof Set) {
          for (const item of val) set.add(item);
        }
        return set.size;
      }
      case "LOOKUP": {
        const tableObj = left?.getMiddleOperand()?.getValue();
        const val = Number(middle?.evaluate());
        const minVal = Number(right?.evaluate());
        const maxVal = Number(extra?.evaluate());
        const values: number[] = tableObj?.getValues?.() ?? tableObj?.values ?? [];
        const size = values.length;
        const index = Math.round(Math.floor((val / (maxVal - minVal)) * size));
        const clampedIndex = Math.max(0, Math.min(size - 1, index));
        return values[clampedIndex] ?? 0;
      }
      default:
        return Number(expression.getValue());
    }
  }

  evaluateReference(expression: Expression): any {
    const val = expression.getValue();
    if (val && typeof val === "object") {
      if ("getExpression" in val && typeof val.getExpression === "function") {
        const expr = val.getExpression();
        if (expr) {
          const evaluated = expr.evaluate();
          if ("setValue" in val && typeof val.setValue === "function" && typeof evaluated === "number") {
            val.setValue(evaluated);
          }
          return evaluated;
        }
      }
      if ("getValue" in val && typeof val.getValue === "function") {
        return val.getValue();
      }
    }
    return val;
  }
}

export class DefaultOperatorEvaluatorWithParameters extends DefaultOperatorEvaluator {
  private overrideParams = new Map<string, number>();

  getParameters(): Map<string, number> {
    return this.overrideParams;
  }

  setParameters(newParams: Map<string, number>): void {
    this.overrideParams = newParams;
  }

  override evaluateName(expression: Expression): any {
    const valKey = String(expression.getValue());
    if (this.overrideParams.has(valKey)) {
      return this.overrideParams.get(valKey)!;
    }
    return super.evaluateName(expression);
  }
}

