import {
  BooleanOperator,
  Expression,
  ExpressionValueType,
  NameOperator,
  NumberOperator,
  Operator,
  OperatorEvaluator,
  ReferenceOperator,
} from "./ExpressionTypes.js";
import { DefaultOperatorEvaluator } from "./OperatorEvaluator.js";

export class DefaultExpression implements Expression {
  private leftOperand: Expression | null = null;
  private middleOperand: Expression | null = null;
  private rightOperand: Expression | null = null;
  private extraOperand: Expression | null = null;
  private operator: Operator | null = null;
  private operatorEvaluator: OperatorEvaluator | null = null;
  private value: any = null;
  private valueType: ExpressionValueType | null = null;

  constructor(
    newValueOrOldExpr?: any,
    tOperator?: Operator | null,
    type?: ExpressionValueType | null,
    operatorEval?: OperatorEvaluator | null
  ) {
    if (newValueOrOldExpr && typeof newValueOrOldExpr === "object" && "evaluate" in newValueOrOldExpr) {
      const oldExpr = newValueOrOldExpr as Expression;
      this.setOperator(oldExpr.getOperator());
      this.setLeftOperand(oldExpr.getLeftOperand() ? oldExpr.getLeftOperand()!.getDeepCopy() : null);
      this.setMiddleOperand(oldExpr.getMiddleOperand() ? oldExpr.getMiddleOperand()!.getDeepCopy() : null);
      this.setRightOperand(oldExpr.getRightOperand() ? oldExpr.getRightOperand()!.getDeepCopy() : null);
      this.setExtraOperand(oldExpr.getExtraOperand() ? oldExpr.getExtraOperand()!.getDeepCopy() : null);
      this.setValue(oldExpr.getValue());
      this.setValueType(oldExpr.getValueType());
      this.setOperatorEvaluator(oldExpr.getOperatorEvaluator());
    } else if (tOperator !== undefined) {
      if (operatorEval !== undefined && operatorEval !== null) {
        this.setOperatorEvaluator(operatorEval);
      }
      if (type !== undefined && type !== null) {
        this.setValueType(type);
      }
      this.setValue(newValueOrOldExpr);
      this.setOperator(tOperator);
    } else if (newValueOrOldExpr instanceof NumberOperator || newValueOrOldExpr instanceof BooleanOperator || newValueOrOldExpr instanceof ReferenceOperator || newValueOrOldExpr instanceof NameOperator) {
      this.setOperator(newValueOrOldExpr);
    }
  }

  getLeftOperand(): Expression | null {
    return this.leftOperand;
  }

  setLeftOperand(newOperand: Expression | null): void {
    this.leftOperand = newOperand;
    if (this.leftOperand && this.operatorEvaluator) {
      this.leftOperand.setOperatorEvaluator(this.operatorEvaluator);
    }
  }

  getMiddleOperand(): Expression | null {
    return this.middleOperand;
  }

  setMiddleOperand(newOperand: Expression | null): void {
    this.middleOperand = newOperand;
    if (this.middleOperand && this.operatorEvaluator) {
      this.middleOperand.setOperatorEvaluator(this.operatorEvaluator);
    }
  }

  getRightOperand(): Expression | null {
    return this.rightOperand;
  }

  setRightOperand(newOperand: Expression | null): void {
    this.rightOperand = newOperand;
    if (this.rightOperand && this.operatorEvaluator) {
      this.rightOperand.setOperatorEvaluator(this.operatorEvaluator);
    }
  }

  getExtraOperand(): Expression | null {
    return this.extraOperand;
  }

  setExtraOperand(newOperand: Expression | null): void {
    this.extraOperand = newOperand;
    if (this.extraOperand && this.operatorEvaluator) {
      this.extraOperand.setOperatorEvaluator(this.operatorEvaluator);
    }
  }

  getOperator(): Operator | null {
    return this.operator;
  }

  setOperator(newOperator: Operator | null): void {
    this.operator = newOperator;
    if (this.operator instanceof NameOperator) {
      this.setValueType(ExpressionValueType.NAME);
    } else if (this.operator instanceof NumberOperator) {
      this.setValueType(ExpressionValueType.NUMBER);
    } else if (this.operator instanceof BooleanOperator) {
      this.setValueType(ExpressionValueType.BOOLEAN);
    } else if (this.operator instanceof ReferenceOperator) {
      this.setValueType(ExpressionValueType.REFERENCE);
    }
  }

  getOperatorEvaluator(): OperatorEvaluator {
    if (!this.operatorEvaluator) {
      this.operatorEvaluator = new DefaultOperatorEvaluator();
    }
    return this.operatorEvaluator;
  }

  setOperatorEvaluator(newOperatorEvaluator: OperatorEvaluator | null): void {
    this.operatorEvaluator = newOperatorEvaluator;
    if (this.operatorEvaluator) {
      if (this.leftOperand) this.leftOperand.setOperatorEvaluator(this.operatorEvaluator);
      if (this.middleOperand) this.middleOperand.setOperatorEvaluator(this.operatorEvaluator);
      if (this.rightOperand) this.rightOperand.setOperatorEvaluator(this.operatorEvaluator);
      if (this.extraOperand) this.extraOperand.setOperatorEvaluator(this.operatorEvaluator);
    }
  }

  getValue(): any {
    return this.value;
  }

  setValue(newValue: any): void {
    this.value = newValue;
  }

  getValueType(): ExpressionValueType | null {
    if (this.valueType === null) {
      if (typeof this.getValue() === "boolean") {
        this.valueType = ExpressionValueType.BOOLEAN;
      } else if (typeof this.getValue() === "number") {
        this.valueType = ExpressionValueType.NUMBER;
      } else if (typeof this.getValue() === "string") {
        this.valueType = ExpressionValueType.NAME;
      }
    }
    return this.valueType;
  }

  setValueType(newValueType: ExpressionValueType | null): void {
    this.valueType = newValueType;
  }

  isLeaf(): boolean {
    if (this.getOperator() instanceof NameOperator) return true;
    return (
      this.getLeftOperand() === null &&
      this.getMiddleOperand() === null &&
      this.getRightOperand() === null &&
      this.getExtraOperand() === null
    );
  }

  evaluate(): any {
    const type = this.getValueType();
    const evalEngine = this.getOperatorEvaluator();
    switch (type) {
      case ExpressionValueType.BOOLEAN:
        return evalEngine.evaluateBoolean(this);
      case ExpressionValueType.NUMBER:
        return evalEngine.evaluateNumber(this);
      case ExpressionValueType.NAME:
        return evalEngine.evaluateName(this);
      case ExpressionValueType.REFERENCE:
        return evalEngine.evaluateReference(this);
      default:
        throw new Error("Unsupported Operator or Expression Value Type");
    }
  }

  getDeepCopy(): Expression {
    return new DefaultExpression(this);
  }

  toMathML(): string {
    // Basic MathML XML output representation
    return `<math><mtext>${this.toString()}</mtext></math>`;
  }

  toString(): string {
    const op = this.getOperator();
    if (!op) return String(this.getValue() ?? "");
    const sym = op.getSymbol();

    if (op instanceof NameOperator) {
      return String(this.getValue());
    }
    if (op === NumberOperator.CONSTANT || op === BooleanOperator.CONSTANT) {
      return String(this.getValue());
    }
    if (this.leftOperand && this.rightOperand) {
      return `(${this.leftOperand.toString()} ${sym} ${this.rightOperand.toString()})`;
    }
    if (this.middleOperand) {
      return `${sym}(${this.middleOperand.toString()})`;
    }
    return String(this.getValue() ?? "");
  }
}

export class DefaultNameExpression extends DefaultExpression {
  constructor(newName?: string, operatorEval?: OperatorEvaluator) {
    super();
    this.setValueType(ExpressionValueType.NAME);
    this.setOperator(NameOperator.NAME);
    if (newName !== undefined) this.setValue(newName);
    if (operatorEval !== undefined) this.setOperatorEvaluator(operatorEval);
  }
}

export class DefaultNumberConstantExpression extends DefaultExpression {
  constructor(newValue?: number, operatorEval?: OperatorEvaluator) {
    super();
    this.setValueType(ExpressionValueType.NUMBER);
    this.setOperator(NumberOperator.CONSTANT);
    if (newValue !== undefined) this.setValue(newValue);
    if (operatorEval !== undefined) this.setOperatorEvaluator(operatorEval);
  }
}

export class DefaultReferenceExpression extends DefaultExpression {
  constructor(newReference?: any, operatorEval?: OperatorEvaluator) {
    super();
    this.setValueType(ExpressionValueType.REFERENCE);
    this.setOperator(ReferenceOperator.REFERENCE);
    if (newReference !== undefined) this.setValue(newReference);
    if (operatorEval !== undefined) this.setOperatorEvaluator(operatorEval);
  }
}

export class DefaultMultipleReferenceExpression extends DefaultExpression {
  constructor(newReferences?: any[], operatorEval?: OperatorEvaluator) {
    super();
    this.setValueType(ExpressionValueType.REFERENCE);
    this.setOperator(ReferenceOperator.MULTIPLE_REFERENCE);
    if (newReferences !== undefined) this.setValue(newReferences);
    if (operatorEval !== undefined) this.setOperatorEvaluator(operatorEval);
  }
}
