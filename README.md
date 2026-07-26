# JynaCore

**JynaCore** is a System Dynamics modeling engine and AST mathematical expression evaluator. It allows developers to build differential model graphs (Stocks, Rates, Auxiliaries, Variables, Information flow), execute numerical simulations (Euler and 4th-Order Runge-Kutta / RK4 solvers), and persist models to XML (`.jyna` files).

This repository contains both the original **Java** implementation (Maven-based) and the modern **TypeScript** library port (Vitest-based).

---

## TypeScript Library

The TypeScript version (`src/` and `tests/`) is a full port of the JynaCore engine designed for Node.js and browser environments.

### Installation & Setup

Ensure Node.js (v18+) is installed.

```bash
# Install dependencies
npm install

# Type-check and compile TypeScript code
npx tsc

# Run Vitest test suite
npm test
```

### TypeScript Quick Start

```typescript
import {
  DefaultSystemDynamicsModel,
  DefaultFiniteStock,
  DefaultInfiniteStock,
  DefaultRate,
  DefaultVariable,
  DefaultExpression,
  DefaultNameExpression,
  DefaultNumberConstantExpression,
  NumberOperator,
  DefaultSystemDynamicsEulerMethod,
} from "jynacore";

// 1. Instantiate model
const model = new DefaultSystemDynamicsModel();

// 2. Define Stocks
const source = new DefaultInfiniteStock();
source.setName("PopulationSource");

const population = new DefaultFiniteStock();
population.setName("Population");
population.setInitialValue(new DefaultNumberConstantExpression(1.0)); // P(0) = 1.0

// 3. Define Growth Factor Variable
const growthFactor = new DefaultVariable();
growthFactor.setName("GrowthFactor");
growthFactor.setExpression(new DefaultNumberConstantExpression(0.5)); // rate = 0.5

// 4. Define Rate (dP/dt = GrowthFactor * Population)
const growthRate = new DefaultRate();
growthRate.setName("GrowthRate");
growthRate.setSourceAndTarget(source, population);

const rateExpr = new DefaultExpression(NumberOperator.TIMES);
rateExpr.setLeftOperand(new DefaultNameExpression("GrowthFactor"));
rateExpr.setRightOperand(new DefaultNameExpression("Population"));
growthRate.setExpression(rateExpr);

// 5. Add elements to model
model.put("PopulationSource", source);
model.put("Population", population);
model.put("GrowthFactor", growthFactor);
model.put("GrowthRate", growthRate);

// 6. Run Euler Solver
const solver = new DefaultSystemDynamicsEulerMethod();
solver.setModel(model);
solver.setInitialTime(0.0);
solver.setStepSize(1.0);
solver.reset();

for (let step = 0; step < 10; step++) {
  solver.step();
}

console.log("Population at step 10:", population.getValue());
```

### TypeScript XML Persistence (`.jyna`)

```typescript
import { SystemDynamicsModelStorerXML, OscilatorySystem } from "jynacore";

const storer = new SystemDynamicsModelStorerXML();

// Save model to XML
const model = new OscilatorySystem();
storer.saveToFile(model, "./oscillatory.jyna");

// Load model from XML
const loadedModel = storer.loadFromFile("./oscillatory.jyna");
```

---

## Java Implementation (Maven)

The original Java source code is located under `src/main/java/` and `src/test/java/`.

### Java Prerequisites

- **Java JDK**: 1.6 or higher (JDK 8/11/17+ recommended)
- **Build Tool**: Apache Maven 3.x

### Key Dependencies (`pom.xml`)

- `jdom:jdom` (v1.0): XML DOM parsing & serialization (`DefaultSystemDynamicsModelStorerJDOM`).
- `jfree:jfreechart` (v1.0.5): Simulation result visualization.
- `jgraph:jgraph` (v5.13.0.0): Visual model graphing.
- `junit:junit` (v4.8.2): Unit testing framework.

### Building & Running Java Tests

```bash
# Clean and compile Java source files
mvn clean compile

# Run JUnit test suite
mvn test

# Package Java JAR artifact
mvn package
```

### Java Package Structure (`br.ufjf.mmc.jynacore`)

- `br.ufjf.mmc.jynacore.expression`: Expression AST nodes, evaluators, and MathML converters (`ExpressionToMathMLConverter`).
- `br.ufjf.mmc.jynacore.systemdynamics`: System Dynamics core items (`Stock`, `Rate`, `Auxiliary`, `Variable`, `Information`) and `DefaultSystemDynamicsModel`.
- `br.ufjf.mmc.jynacore.systemdynamics.simulator`: Simulation runners and integration algorithms (`DefaultSystemDynamicsEulerMethod`, `DefaultSystemDynamicsRK4Method`).
- `br.ufjf.mmc.jynacore.metamodel`: Meta-modeling capabilities, class definitions, and scenario compilers (`UserModelCompiler`, `ExpertModelCompiler`).

---

## Feature Comparison

| Feature | Java Implementation | TypeScript Port |
| :--- | :--- | :--- |
| **Language & Version** | Java 1.6+ | TypeScript 7 / ES2022 |
| **Build Tool** | Maven (`pom.xml`) | npm (`package.json`) |
| **Testing Framework** | JUnit 4 | Vitest 2 |
| **XML Parser** | JDOM | `fast-xml-parser` |
| **MathML Math Parser** | Yes | Yes (`ExpressionConverters`) |
| **Solvers (Euler, RK4)** | Yes | Yes |
| **Model Persistence (.jyna)** | `SystemDynamicsModelStorerJDOM` | `SystemDynamicsModelStorerXML` |

---

## Project Structure

```text
jynacore/
├── pom.xml                           # Java Maven configuration
├── package.json                      # TypeScript npm configuration
├── tsconfig.json                     # TypeScript compiler configuration
├── vitest.config.ts                  # Vitest test configuration
├── src/
│   ├── index.ts                      # TypeScript entrypoint
│   ├── expression/                   # TypeScript expression engine & MathML converters
│   ├── systemdynamics/               # TypeScript system dynamics & solvers (Euler/RK4)
│   ├── examples/                     # TypeScript example models
│   └── main/java/br/ufjf/mmc/jynacore/  # Original Java source code
└── tests/                            # TypeScript Vitest test suites
```

---

## License

GNU Lesser General Public License v3.0 (LGPL-3.0). See original JynaCore license terms for details.
