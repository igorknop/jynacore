/*******************************************************************************
 * Copyright (c) 2009 Igor Knop.
 *     This file is part of JynaCore.
 * 
 *     JynaCore is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     JynaCore is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with JynaCore.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.ufjf.mmc.jynacore.tests.systemdynamics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.impl.DefaultExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNumberConstantExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultReferenceExpression;
import br.ufjf.mmc.jynacore.systemdynamics.Constant;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.Information;
import br.ufjf.mmc.jynacore.systemdynamics.Variable;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultConstant;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultFiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultVariable;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultInformation;

public class DoubleVariableTest {

	private FiniteStock level1;
	private FiniteStock level2;
	private Constant const1;

	@Before
	public void setUp() throws Exception {
		level1 = new DefaultFiniteStock();
		// level1.setInitialValue(10.0);
		level1.setInitialValue(new DefaultNumberConstantExpression(10.0));
		level1.setName("Level1");
		level1.setInitialValue((Double) level1.getInitialValue().evaluate());

		level2 = new DefaultFiniteStock();
		// level2.setInitialValue(14.0);
		level2.setInitialValue(new DefaultNumberConstantExpression(14.0));
		level2.setName("Level2");
		level2.setInitialValue((Double) level2.getInitialValue().evaluate());

		const1 = new DefaultConstant();
		const1.setName("Const1");
		const1.setValue(0.25);
		// const1.setNValue(new DefaultNumberConstantExpression(10.0));

	}

	@Test
	public final void test1info() throws Exception {

		// Single information
		// [Level1 = 10] --------> (Var1 = 0.5*Level1)
		// expected: 0.5*10 = 5.0
		Variable var1 = new DefaultVariable();
		var1.setName("Var1");
		// var1.setExpression("0.5*Level1");
		Expression expr = new DefaultExpression(NumberOperator.TIMES);
		expr.setLeftOperand(new DefaultNumberConstantExpression(0.5));
		expr.setRightOperand(new DefaultReferenceExpression(level1));
		var1.setExpression(expr);
		Information inf1 = new DefaultInformation(level1, var1);
		inf1.setName("inf1");
		Double r = (Double) var1.getExpression().evaluate();
//		assertEquals(5.0, var1.getValue());
		assertEquals((Double) 5.0, r);

	}

	public final void test2info() throws Exception {
		// Two informations
		// [Level1 = 10] ---> (Var3 = Const1*Level1) <--- <Const1=0.25>
		// expected: 0.25*10 = 2.5
		Variable var2 = new DefaultVariable();
		var2.setName("Var2");
		Expression expr2 = new DefaultExpression(NumberOperator.TIMES);
		expr2.setLeftOperand(new DefaultReferenceExpression(const1));
		expr2.setRightOperand(new DefaultReferenceExpression(level2));

		// var2.setExpression("Const1*Level1");
		var2.setExpression(expr2);
		Information inf2 = new DefaultInformation(level1, var2);
		inf2.setName("Inf2");
		Information inf3 = new DefaultInformation(const1, var2);
		inf3.setName("Inf3");
		var2.getExpression().evaluate();
		assertEquals((Double) 2.5, var2.getValue());

	}

	public final void test3info1() throws Exception {
		// Three informations
		// [Level1 = 10] ---> (Var3 = Const1*(Level2-Level1) <--- <Const1=0.25>
		// [Level1 = 14] --->
		// expected: 0.25*(14-10) = 1
		Variable var3 = new DefaultVariable();
		var3.setName("Var3");

		Expression expr3 = new DefaultExpression(NumberOperator.TIMES);
		expr3.setLeftOperand(new DefaultReferenceExpression(const1));
		Expression expr3_1 = new DefaultExpression(NumberOperator.ADD);
		expr3_1.setLeftOperand(new DefaultReferenceExpression(level1));
		expr3_1.setRightOperand(new DefaultReferenceExpression(level2));
		expr3.setRightOperand(expr3_1);
		// var3.setExpression("Const1*(Level2-Level1)");
		var3.setExpression(expr3);
		Information inf4 = new DefaultInformation(level1, var3);
		inf4.setName("Inf4");
		Information inf5 = new DefaultInformation(const1, var3);
		inf5.setName("Inf5");
		Information inf6 = new DefaultInformation(level2, var3);
		inf6.setName("Inf6");
		var3.getExpression().evaluate();
		assertEquals((Double) 1.0, var3.getValue());

	}

	public final void test3info() throws Exception {
		// Three informations
		// [Level1 = 10] ---> (Var4 = 2*(Level1) ---> (Var5 = 3*Var4)
		Variable var4 = new DefaultVariable();
		var4.setName("Var4");
		Expression expr4 = new DefaultExpression(NumberOperator.TIMES);
		expr4.setLeftOperand(new DefaultNumberConstantExpression(2.0));
		expr4.setRightOperand(new DefaultReferenceExpression(level1));

		//var4.setExpression("2*Level1");
		var4.setExpression(expr4);
		Variable var5 = new DefaultVariable();
		var5.setName("Var5");
		Expression expr5 = new DefaultExpression(NumberOperator.TIMES);
		expr5.setLeftOperand(new DefaultNumberConstantExpression(3.0));
		expr5.setRightOperand(new DefaultReferenceExpression(var4));		
//		var5.setNExpression("3*Var4");
		var5.setExpression(expr5);
		var5.getExpression().evaluate();
		//var4.evaluate();
		assertEquals((Double) 60.0, var5.getValue());
	}

}
