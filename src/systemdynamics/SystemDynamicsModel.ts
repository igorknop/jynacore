import { JynaNamed, JynaValued } from "../JynaCoreInterfaces.js";
import {
  Auxiliary,
  FiniteStock,
  InfiniteStock,
  Information,
  Rate,
  SystemDynamicsItem,
} from "./SystemDynamicsItem.js";

export interface SystemDynamicsModel extends JynaNamed {
  put(item: SystemDynamicsItem): void;
  put(key: string, item: SystemDynamicsItem): void;
  get(key: string): SystemDynamicsItem | undefined;
  getObjectByName(sdObject: SystemDynamicsItem): string;
  getItems(): Map<string, SystemDynamicsItem>;
  setItems(newObjects: Map<string, SystemDynamicsItem>): void;
  getFiniteLevels(): Set<FiniteStock>;
  getInfiniteLevels(): Set<InfiniteStock>;
  getRates(): Set<Rate>;
  getAuxiliaries(): Set<Auxiliary>;
  getInformations(): Set<Information>;
  getAllJynaValued(): JynaValued[];
}

export class DefaultSystemDynamicsModel implements SystemDynamicsModel {
  private objects = new Map<string, SystemDynamicsItem>();
  private finiteLevels = new Set<FiniteStock>();
  private infiniteLevels = new Set<InfiniteStock>();
  private rates = new Set<Rate>();
  private auxiliaries = new Set<Auxiliary>();
  private informations = new Set<Information>();
  private modelName = "Simple Dynamic Model";

  getName(): string {
    return this.modelName;
  }

  setName(newName: string): void {
    this.modelName = newName;
  }

  put(keyOrItem: string | SystemDynamicsItem, sdObject?: SystemDynamicsItem): void {
    let key: string;
    let item: SystemDynamicsItem;

    if (typeof keyOrItem === "string") {
      key = keyOrItem;
      item = sdObject!;
    } else {
      item = keyOrItem;
      key = item.getName();
    }

    if (!this.getObjectByName(item)) {
      item.setName(key);
      this.objects.set(key, item);

      // Check item types
      if ("getInitialValue" in item) {
        this.finiteLevels.add(item as FiniteStock);
      } else if ("getValue" in item && !("setSourceAndTarget" in item) && !("getExpression" in item)) {
        this.infiniteLevels.add(item as InfiniteStock);
      }
      if ("setSourceAndTarget" in item) {
        this.rates.add(item as Rate);
      } else if ("getExpression" in item && !("getInitialValue" in item)) {
        this.auxiliaries.add(item as unknown as Auxiliary);
      }
      if ("getSource" in item && "getTarget" in item) {
        this.informations.add(item as Information);
      }
    }
  }

  get(key: string): SystemDynamicsItem | undefined {
    return this.objects.get(key);
  }

  getObjectByName(sdObject: SystemDynamicsItem): string {
    for (const [key, val] of this.objects.entries()) {
      if (val === sdObject) return key;
    }
    return "";
  }

  getItems(): Map<string, SystemDynamicsItem> {
    return this.objects;
  }

  setItems(newObjects: Map<string, SystemDynamicsItem>): void {
    this.objects = newObjects;
  }

  getFiniteLevels(): Set<FiniteStock> {
    return this.finiteLevels;
  }

  getInfiniteLevels(): Set<InfiniteStock> {
    return this.infiniteLevels;
  }

  getRates(): Set<Rate> {
    return this.rates;
  }

  getAuxiliaries(): Set<Auxiliary> {
    return this.auxiliaries;
  }

  getInformations(): Set<Information> {
    return this.informations;
  }

  getAllJynaValued(): JynaValued[] {
    const list: JynaValued[] = [];
    for (const item of this.objects.values()) {
      if ("getValue" in item && typeof (item as any).getValue === "function") {
        list.push(item as unknown as JynaValued);
      }
    }
    return list;
  }

  toString(): string {
    return this.getName();
  }
}
