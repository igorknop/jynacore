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
package br.ufjf.mmc.jynacore.tests.expression;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.expression.BooleanOperator;
import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.ExpressionValueType;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.OperatorEvaluator;
import br.ufjf.mmc.jynacore.expression.exception.InvalidExpressionException;
import br.ufjf.mmc.jynacore.expression.impl.DefaultExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultOperatorEvaluator;
import br.ufjf.mmc.jynacore.expression.impl.ExpressionToMathMLConverter;

public class DefaultExpressionTest {

	private OperatorEvaluator operatorEvaluator;
	private Expression exprConstant;
	private Expression exprConstant2;
	private Expression exprDoubleSimpleOperator;
	private Expression exprBooleanSimpleOperator;

	@Before
	public void setUp() throws Exception {
		operatorEvaluator = new DefaultOperatorEvaluator();
		exprBooleanSimpleOperator = new DefaultExpression();
		exprConstant = new DefaultExpression();
		exprConstant2 = new DefaultExpression();
		exprDoubleSimpleOperator = new DefaultExpression();

	}

	@Test
	public final void testEvaluateDoubleConstant() throws Exception {
		exprConstant.setOperatorEvaluator(operatorEvaluator);
		exprConstant.setValue(10.0);
		exprConstant.setOperator(NumberOperator.CONSTANT);
		assertEquals(10.0, exprConstant.evaluate());
		// System.out.println(exprConstant.toString());
		printExprs(exprConstant);

	}

	@Test
	public final void testEvaluateBooleanConstant() throws Exception {
		exprConstant2.setOperatorEvaluator(operatorEvaluator);
		exprConstant2.setValue(true);
		exprConstant2.setOperator(BooleanOperator.CONSTANT);
		assertEquals(true, exprConstant2.evaluate());
		// System.out.println(exprConstant2.toString());
		printExprs(exprConstant2);

	}

	@Test
	public final void testEvaluateDoubleSimpleOperators() throws Exception {
		// (2+3)*(100/20)
		exprDoubleSimpleOperator.setOperatorEvaluator(operatorEvaluator);
		exprDoubleSimpleOperator.setOperator(NumberOperator.TIMES);
		// exprDoubleSimpleOperator.setValueType(ExpressionValueType.NUMBER);
		Expression exprSum;
		exprSum = new DefaultExpression();
		exprSum.setOperator(NumberOperator.ADD);
		exprSum.setValueType(ExpressionValueType.NUMBER);
		exprSum.setLeftOperand(new DefaultExpression(2.0,
				NumberOperator.CONSTANT, ExpressionValueType.NUMBER,
				operatorEvaluator));
		exprSum.setRightOperand(new DefaultExpression(3.0,
				NumberOperator.CONSTANT, ExpressionValueType.NUMBER,
				operatorEvaluator));
		exprDoubleSimpleOperator.setLeftOperand(exprSum);

		Expression exprDiv;
		exprDiv = new DefaultExpression();
		exprDiv.setOperator(NumberOperator.DIVIDE);
		exprDiv.setValueType(ExpressionValueType.NUMBER);
		exprDiv.setLeftOperand(new DefaultExpression(100.0,
				NumberOperator.CONSTANT, ExpressionValueType.NUMBER,
				operatorEvaluator));
		exprDiv.setRightOperand(new DefaultExpression(20.0,
				NumberOperator.CONSTANT, ExpressionValueType.NUMBER,
				operatorEvaluator));
		exprDoubleSimpleOperator.setRightOperand(exprDiv);

		assertEquals(25.0, exprDoubleSimpleOperator.evaluate());
		// System.out.println(exprDoubleSimpleOperator.toString());
		printExprs(exprDoubleSimpleOperator);

	}

	@Test
	public final void testEvaluateBooleanSimpleOperators() throws Exception {
		// !(2>3)&&(1<=1)
		exprBooleanSimpleOperator.setOperatorEvaluator(operatorEvaluator);
		exprBooleanSimpleOperator.setOperator(BooleanOperator.AND);
		exprBooleanSimpleOperator.setValueType(ExpressionValueType.BOOLEAN);
		Expression exprGT;
		exprGT = new DefaultExpression();
		exprGT.setOperator(BooleanOperator.GREATERTHAN);
		exprGT.setValueType(ExpressionValueType.BOOLEAN);
		exprGT.setLeftOperand(new DefaultExpression(2.0,
				NumberOperator.CONSTANT, ExpressionValueType.NUMBER,
				operatorEvaluator));
		exprGT.setRightOperand(new DefaultExpression(3.0,
				NumberOperator.CONSTANT, ExpressionValueType.NUMBER,
				operatorEvaluator));
		Expression exprNot;
		exprNot = new DefaultExpression(null, BooleanOperator.NOT,
				ExpressionValueType.BOOLEAN, operatorEvaluator);
		exprNot.setMiddleOperand(exprGT);

		exprBooleanSimpleOperator.setLeftOperand(exprNot);

		Expression exprLET;
		exprLET = new DefaultExpression();
		exprLET.setOperator(BooleanOperator.LOWEROREQUALTHAN);
		exprLET.setValueType(ExpressionValueType.BOOLEAN);
		exprLET.setLeftOperand(new DefaultExpression(1.0,
				NumberOperator.CONSTANT, ExpressionValueType.NUMBER,
				operatorEvaluator));
		exprLET.setRightOperand(new DefaultExpression(1.0,
				NumberOperator.CONSTANT, ExpressionValueType.NUMBER,
				operatorEvaluator));
		exprBooleanSimpleOperator.setRightOperand(exprLET);

		assertEquals(true, exprBooleanSimpleOperator.evaluate());
		// System.out.println(exprBooleanSimpleOperator.toString());
		printExprs(exprBooleanSimpleOperator);

	}

	private void printExprs(Expression expr) throws Exception {
		System.out.println(expr.toString());
		XMLOutputter outp = new XMLOutputter();

		Format f = Format.getPrettyFormat();
		f.setIndent("  ");
		f.setLineSeparator("\n");

		outp.setFormat(f);
		Document doc = new Document();
		doc.setRootElement(ExpressionToMathMLConverter.convertToMathml(expr));
		outp.output(doc, System.out);
		Expression newExpression = ExpressionToMathMLConverter
				.readFromMathML(doc.getRootElement());
		System.out.println(newExpression.toString());
	}
}
