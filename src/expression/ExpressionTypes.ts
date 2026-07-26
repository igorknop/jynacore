export enum ExpressionValueType {
  NUMBER = "NUMBER",
  BOOLEAN = "BOOLEAN",
  NAME = "NAME",
  REFERENCE = "REFERENCE",
  STRING = "STRING",
}

export interface Operator {
  getSymbol(): string;
  name?: string;
}

export enum NumberOperatorType {
  ADD = "+",
  MINUS = "-",
  TIMES = "*",
  DIVIDE = "/",
  POWER = "^",
  ABS = "ABS",
  LOG = "LOG",
  NEGATION = "-",
  SINE = "SIN",
  SQUARE = "SQR",
  SQUAREROOT = "SQRT",
  CONSTANT = "",
  MINIMUM = "MIN",
  MAXIMUM = "MAX",
  GROUPSUM = "GROUPSUM",
  IF = "IF",
  GROUPMAX = "GROUPMAX",
  COSINE = "COSINE",
  TAN = "TAN",
  EXP = "EXP",
  MOD = "%",
  RAND = "RAND",
  RANDINT = "RANDINT",
  GROUPMIN = "GROUPMIN",
  GROUPCOUNT = "GROUPCOUNT",
  LOOKUP = "LOOKUP",
}

export class NumberOperator implements Operator {
  constructor(public readonly name: keyof typeof NumberOperatorType, public readonly symbol: string) {}

  getSymbol(): string {
    return this.symbol;
  }

  static readonly ADD = new NumberOperator("ADD", "+");
  static readonly MINUS = new NumberOperator("MINUS", "-");
  static readonly TIMES = new NumberOperator("TIMES", "*");
  static readonly DIVIDE = new NumberOperator("DIVIDE", "/");
  static readonly POWER = new NumberOperator("POWER", "^");
  static readonly ABS = new NumberOperator("ABS", "ABS");
  static readonly LOG = new NumberOperator("LOG", "LOG");
  static readonly NEGATION = new NumberOperator("NEGATION", "-");
  static readonly SINE = new NumberOperator("SINE", "SIN");
  static readonly SQUARE = new NumberOperator("SQUARE", "SQR");
  static readonly SQUAREROOT = new NumberOperator("SQUAREROOT", "SQRT");
  static readonly CONSTANT = new NumberOperator("CONSTANT", "");
  static readonly MINIMUM = new NumberOperator("MINIMUM", "MIN");
  static readonly MAXIMUM = new NumberOperator("MAXIMUM", "MAX");
  static readonly GROUPSUM = new NumberOperator("GROUPSUM", "GROUPSUM");
  static readonly IF = new NumberOperator("IF", "IF");
  static readonly GROUPMAX = new NumberOperator("GROUPMAX", "GROUPMAX");
  static readonly COSINE = new NumberOperator("COSINE", "COSINE");
  static readonly TAN = new NumberOperator("TAN", "TAN");
  static readonly EXP = new NumberOperator("EXP", "EXP");
  static readonly MOD = new NumberOperator("MOD", "%");
  static readonly RAND = new NumberOperator("RAND", "RAND");
  static readonly RANDINT = new NumberOperator("RANDINT", "RANDINT");
  static readonly GROUPMIN = new NumberOperator("GROUPMIN", "GROUPMIN");
  static readonly GROUPCOUNT = new NumberOperator("GROUPCOUNT", "GROUPCOUNT");
  static readonly LOOKUP = new NumberOperator("LOOKUP", "LOOKUP");

  static readonly GROUP_OPERATORS = [
    NumberOperator.GROUPCOUNT,
    NumberOperator.GROUPSUM,
    NumberOperator.GROUPMIN,
    NumberOperator.GROUPMAX,
  ];
}

export enum BooleanOperatorType {
  OR = "||",
  AND = "&&",
  IMPLIES = "IMPL",
  IFONLYIF = "IOI",
  NOT = "!",
  CONSTANT = "",
  GREATERTHAN = ">",
  GREATEROREQUALTHAN = ">=",
  LOWERTHAN = "<",
  LOWEROREQUALTHAN = "<=",
  EQUALS = "==",
  NOTEQUALS = "!=",
}

export class BooleanOperator implements Operator {
  constructor(public readonly name: keyof typeof BooleanOperatorType, public readonly symbol: string) {}

  getSymbol(): string {
    return this.symbol;
  }

  static readonly OR = new BooleanOperator("OR", "||");
  static readonly AND = new BooleanOperator("AND", "&&");
  static readonly IMPLIES = new BooleanOperator("IMPLIES", "IMPL");
  static readonly IFONLYIF = new BooleanOperator("IFONLYIF", "IOI");
  static readonly NOT = new BooleanOperator("NOT", "!");
  static readonly CONSTANT = new BooleanOperator("CONSTANT", "");
  static readonly GREATERTHAN = new BooleanOperator("GREATERTHAN", ">");
  static readonly GREATEROREQUALTHAN = new BooleanOperator("GREATEROREQUALTHAN", ">=");
  static readonly LOWERTHAN = new BooleanOperator("LOWERTHAN", "<");
  static readonly LOWEROREQUALTHAN = new BooleanOperator("LOWEROREQUALTHAN", "<=");
  static readonly EQUALS = new BooleanOperator("EQUALS", "==");
  static readonly NOTEQUALS = new BooleanOperator("NOTEQUALS", "!=");
}

export class ReferenceOperator implements Operator {
  constructor(public readonly name: string) {}

  getSymbol(): string {
    return "";
  }

  static readonly REFERENCE = new ReferenceOperator("REFERENCE");
  static readonly MULTIPLE_REFERENCE = new ReferenceOperator("MULTIPLE_REFERENCE");
}

export class NameOperator implements Operator {
  constructor(public readonly name: string) {}

  getSymbol(): string {
    return "";
  }

  static readonly NAME = new NameOperator("NAME");
}

export interface OperatorEvaluator {
  evaluateBoolean(expression: Expression): boolean;
  evaluateName(expression: Expression): any;
  evaluateNumber(expression: Expression): number;
  evaluateReference(expression: Expression): any;
}

export interface Expression {
  getValue(): any;
  setValue(newValue: any): void;

  setLeftOperand(newOperand: Expression | null): void;
  getLeftOperand(): Expression | null;
  setMiddleOperand(newOperand: Expression | null): void;
  getMiddleOperand(): Expression | null;
  setRightOperand(newOperand: Expression | null): void;
  getRightOperand(): Expression | null;
  setExtraOperand(newOperand: Expression | null): void;
  getExtraOperand(): Expression | null;

  getValueType(): ExpressionValueType | null;
  setValueType(newValueType: ExpressionValueType | null): void;

  getOperator(): Operator | null;
  setOperator(newOperator: Operator | null): void;

  getOperatorEvaluator(): OperatorEvaluator | null;
  setOperatorEvaluator(newOperator: OperatorEvaluator | null): void;

  isLeaf(): boolean;
  evaluate(): any;
  getDeepCopy(): Expression;
  toMathML(): string;
}
