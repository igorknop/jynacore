import { Expression } from "./expression/ExpressionTypes.js";

export interface JynaValued {
  getValue(): any;
  setValue(value: any): void;
}

export interface JynaEvaluated {
  getExpression(): Expression | null;
  setExpression(expression: Expression | null): void;
}

export interface JynaNamed {
  getName(): string;
  setName(name: string): void;
}
