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
/**
 * 
 */
package br.ufjf.mmc.jynacore.expression.impl;

import java.io.StringWriter;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import br.ufjf.mmc.jynacore.expression.BooleanOperator;
import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.ExpressionValueType;
import br.ufjf.mmc.jynacore.expression.NameOperator;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.Operator;
import br.ufjf.mmc.jynacore.expression.OperatorEvaluator;

/**
 * @author Knop
 * 
 */
public class DefaultExpression implements Expression {

	private Expression leftOperand;
	private Expression middleOperand;
	private Operator operator;
	private OperatorEvaluator operatorEvaluator;
	private Expression rightOperand;
	private Object value;
	private ExpressionValueType valueType;
	private Expression extraOperand;

	public DefaultExpression() {

	}

	public DefaultExpression(Object newValue, Operator tOperator,
			ExpressionValueType type, OperatorEvaluator operatorEval) {
		setOperatorEvaluator(operatorEval);
		setValueType(type);
		setValue(newValue);
		setOperator(tOperator);
	}

	public DefaultExpression(Object newValue, Operator tOperator,
			ExpressionValueType type) {
		setValueType(type);
		setValue(newValue);
		setOperator(tOperator);
	}

	public DefaultExpression(Object newValue, Operator tOperator) {
		setValue(newValue);
		setOperator(tOperator);
	}

	public DefaultExpression(Operator tOperator) {
		setOperator(tOperator);
	}

	public DefaultExpression(Expression oldExpression) {
		setOperator(oldExpression.getOperator());

		setLeftOperand(oldExpression.getLeftOperand() != null ? oldExpression
				.getLeftOperand().getDeepCopy() : null);
		setMiddleOperand(oldExpression.getMiddleOperand() != null ? oldExpression
				.getMiddleOperand().getDeepCopy()
				: null);
		setRightOperand(oldExpression.getRightOperand() != null ? oldExpression
				.getRightOperand().getDeepCopy() : null);
		setExtraOperand(oldExpression.getExtraOperand() != null ? oldExpression
				.getRightOperand().getDeepCopy() : null);

		setValue(oldExpression.getValue());
		setValueType(oldExpression.getValueType());

		// Mantive a referência aqui
		setOperatorEvaluator(getOperatorEvaluator());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#getLeftOperand()
	 */

	@Override
	public Expression getLeftOperand() {
		return leftOperand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#getMiddleOperand()
	 */
	@Override
	public Expression getMiddleOperand() {
		return middleOperand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#getOperator()
	 */
	@Override
	public Operator getOperator() {
		return operator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#getOperatorEvaluator()
	 */
	@Override
	public OperatorEvaluator getOperatorEvaluator() {
		if (operatorEvaluator == null)
			operatorEvaluator = new DefaultOperatorEvaluator();
		return operatorEvaluator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#getRightOperand()
	 */
	@Override
	public Expression getRightOperand() {
		return rightOperand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#getValue()
	 */
	@Override
	public Object getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#getValueType()
	 */
	@Override
	public ExpressionValueType getValueType() {
		if (valueType == null) {
			if (getValue() instanceof Boolean) {
				valueType = ExpressionValueType.BOOLEAN;
			}
			if (getValue() instanceof Number) {
				valueType = ExpressionValueType.NUMBER;
			}
			if (getValue() instanceof String) {
				valueType = ExpressionValueType.NAME;
			}
		}
		return valueType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#isLeaf()
	 */
	@Override
	public boolean isLeaf() {
		if (getOperator() instanceof NameOperator)
			return true;
		return (getLeftOperand() == null && getMiddleOperand() == null
				&& getRightOperand() == null && getExtraOperand() == null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#setLeftOperand(br.ufjf.mmc.jynacore.expression.Expression)
	 */
	@Override
	public void setLeftOperand(Expression newOperand) {
		leftOperand = newOperand;
		// leftOperand.setOperatorEvaluator(getOperatorEvaluator());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#setMiddleOperand(br.ufjf.mmc.jynacore.expression.Expression)
	 */
	@Override
	public void setMiddleOperand(Expression newOperand) {
		middleOperand = newOperand;
		// middleOperand.setOperatorEvaluator(getOperatorEvaluator());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#setOperator(java.lang.String)
	 */
	@Override
	public void setOperator(Operator newOperator) {
		operator = newOperator;
		if (operator instanceof NameOperator) {
			setValueType(ExpressionValueType.NAME);
		} else if (operator instanceof NumberOperator) {
			setValueType(ExpressionValueType.NUMBER);
		} else if (operator instanceof BooleanOperator) {
			setValueType(ExpressionValueType.BOOLEAN);
		} else
			setValueType(ExpressionValueType.REFERENCE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#setOperatorEvaluator(br.ufjf.mmc.jynacore.expression.OperatorEvaluator)
	 */
	@Override
	public void setOperatorEvaluator(OperatorEvaluator newOperatorEvaluator) {
		operatorEvaluator = newOperatorEvaluator;
		if (leftOperand != null) {
			leftOperand.setOperatorEvaluator(getOperatorEvaluator());
		}
		if (middleOperand != null) {
			middleOperand.setOperatorEvaluator(getOperatorEvaluator());
		}
		if (rightOperand != null) {
			rightOperand.setOperatorEvaluator(getOperatorEvaluator());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#setRightOperand(br.ufjf.mmc.jynacore.expression.Expression)
	 */
	@Override
	public void setRightOperand(Expression newOperand) {
		rightOperand = newOperand;
		// rightOperand.setOperatorEvaluator(getOperatorEvaluator());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object newValue) {
		value = newValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.expression.Expression#setValueType(br.ufjf.mmc.jynacore.expression.ExpressionValueType)
	 */
	@Override
	public void setValueType(ExpressionValueType newValueType) {
		valueType = newValueType;
	}

	@Override
	public Object evaluate() throws Exception {
		switch (getValueType()) {
		case BOOLEAN:
			return getOperatorEvaluator().evaluateBoolean(this);
		case NUMBER:
			return getOperatorEvaluator().evaluateNumber(this);
		case NAME:
			return getOperatorEvaluator().evaluateName(this);
		case REFERENCE:
			return getOperatorEvaluator().evaluateReference(this);
		default:
			throw new Exception("Unsuported Operator");
		}
	}

	@Override
	public String toMathML() throws Exception {
		XMLOutputter outp = new XMLOutputter();
		StringWriter sw = new StringWriter();

		Format f = Format.getPrettyFormat();
		f.setIndent("  ");
		f.setLineSeparator("\n");

		outp.setFormat(f);
		Document doc = new Document();
		doc.setRootElement(ExpressionToMathMLConverter.convertToMathml(this));
		outp.output(doc, sw);
		return sw.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(ExpressionToJavaConverter.convertToJavaString(this));
		return sb.toString();
	}

	@Override
	public Expression getDeepCopy() {
		return new DefaultExpression(this);
	}

	@Override
	public Expression getExtraOperand() {
		return extraOperand;
	}

	@Override
	public void setExtraOperand(Expression newOperand) {
		extraOperand = newOperand;
	}
}
