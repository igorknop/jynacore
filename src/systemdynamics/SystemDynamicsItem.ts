import { Expression } from "../expression/ExpressionTypes.js";
import { DefaultNumberConstantExpression } from "../expression/DefaultExpression.js";
import { JynaEvaluated, JynaNamed, JynaValued } from "../JynaCoreInterfaces.js";

export interface SystemDynamicsItem extends JynaNamed {
  getName(): string;
  setName(name: string): void;
}

export abstract class AbstractSystemDynamicsItem implements SystemDynamicsItem {
  private name: string = "";

  getName(): string {
    return this.name;
  }

  setName(name: string): void {
    this.name = name;
  }
}

export interface Stock extends SystemDynamicsItem, JynaValued {}

export interface FiniteStock extends Stock {
  getInitialValue(): Expression;
  setInitialValue(initialValue: Expression | number): void;
}

export class DefaultFiniteStock extends AbstractSystemDynamicsItem implements FiniteStock {
  private initialValue: Expression | null = null;
  private value: number = 0.0;

  getInitialValue(): Expression {
    return this.initialValue!;
  }

  setInitialValue(initialValue: Expression | number): void {
    if (typeof initialValue === "number") {
      this.initialValue = new DefaultNumberConstantExpression(initialValue);
    } else {
      this.initialValue = initialValue;
    }
  }


  getValue(): number {
    return this.value;
  }

  setValue(value: number): void {
    this.value = value;
  }
}

export interface InfiniteStock extends Stock {}

export class DefaultInfiniteStock extends AbstractSystemDynamicsItem implements InfiniteStock {
  private value: number = Infinity;

  getValue(): number {
    return this.value;
  }

  setValue(value: number): void {
    this.value = value;
  }
}

export interface Rate extends SystemDynamicsItem, JynaValued, JynaEvaluated {
  getFromLevel(): Stock | null;
  setFromLevel(fromLevel: Stock | null): void;
  getToLevel(): Stock | null;
  setToLevel(toLevel: Stock | null): void;
  setSourceAndTarget(fromLevel: Stock | null, toLevel: Stock | null): void;
}

export class DefaultRate extends AbstractSystemDynamicsItem implements Rate {
  private fromLevel: Stock | null = null;
  private toLevel: Stock | null = null;
  private expression: Expression | null = null;
  private value: number = 0.0;

  getFromLevel(): Stock | null {
    return this.fromLevel;
  }

  setFromLevel(fromLevel: Stock | null): void {
    this.fromLevel = fromLevel;
  }

  getToLevel(): Stock | null {
    return this.toLevel;
  }

  setToLevel(toLevel: Stock | null): void {
    this.toLevel = toLevel;
  }

  setSourceAndTarget(fromLevel: Stock | null, toLevel: Stock | null): void {
    this.fromLevel = fromLevel;
    this.toLevel = toLevel;
  }

  getExpression(): Expression | null {
    return this.expression;
  }

  setExpression(expression: Expression | null): void {
    this.expression = expression;
  }

  getValue(): number {
    return this.value;
  }

  setValue(value: number): void {
    this.value = value;
  }
}

export interface Auxiliary extends SystemDynamicsItem, JynaValued {}

export interface Variable extends Auxiliary, JynaEvaluated {
  getInformations(): Map<string, Information>;
}

export class DefaultVariable extends AbstractSystemDynamicsItem implements Variable {
  private expression: Expression | null = null;
  private value: number = 0.0;
  private informations = new Map<string, Information>();

  getExpression(): Expression | null {
    return this.expression;
  }

  setExpression(expression: Expression | null): void {
    this.expression = expression;
  }

  getValue(): number {
    return this.value;
  }

  setValue(value: number): void {
    this.value = value;
  }

  getInformations(): Map<string, Information> {
    return this.informations;
  }
}

export interface Constant extends Auxiliary {
  setExpression(expression: Expression | null): void;
  getExpression(): Expression | null;
}

export class DefaultConstant extends AbstractSystemDynamicsItem implements Constant {
  private expression: Expression | null = null;
  private value: number = 0.0;

  getExpression(): Expression | null {
    return this.expression;
  }

  setExpression(expression: Expression | null): void {
    this.expression = expression;
  }

  getValue(): number {
    return this.value;
  }

  setValue(value: number): void {
    this.value = value;
  }
}

export interface InformationSource extends SystemDynamicsItem, JynaValued {}
export interface InformationConsumer extends SystemDynamicsItem {}

export interface Information extends SystemDynamicsItem {
  getSource(): SystemDynamicsItem | null;
  setSource(source: SystemDynamicsItem | null): void;
  getTarget(): SystemDynamicsItem | null;
  setTarget(target: SystemDynamicsItem | null): void;
}

export class DefaultInformation extends AbstractSystemDynamicsItem implements Information {
  private source: SystemDynamicsItem | null = null;
  private target: SystemDynamicsItem | null = null;

  constructor(source?: SystemDynamicsItem | null, target?: SystemDynamicsItem | null) {
    super();
    if (source) this.source = source;
    if (target) this.target = target;
  }

  getSource(): SystemDynamicsItem | null {
    return this.source;
  }

  setSource(source: SystemDynamicsItem | null): void {
    this.source = source;
  }

  getTarget(): SystemDynamicsItem | null {
    return this.target;
  }

  setTarget(target: SystemDynamicsItem | null): void {
    this.target = target;
  }
}
