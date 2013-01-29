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

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import br.ufjf.mmc.jynacore.JynaEvaluated;
import br.ufjf.mmc.jynacore.JynaValued;
import br.ufjf.mmc.jynacore.expression.BooleanOperator;
import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.OperatorEvaluator;
import br.ufjf.mmc.jynacore.expression.ReferenceOperator;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceProperty;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceTable;
import br.ufjf.mmc.jynacore.systemdynamics.InformationSource;

/**
 * @author Knop
 * 
 */
public class DefaultOperatorEvaluator implements OperatorEvaluator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.expression.OperatorEvaluator#evaluateBoolean(br.
	 * ufjf.mmc.jynacore.expression.impl.Expression)
	 */
	@Override
	public Boolean evaluateBoolean(Expression expression) throws Exception {
		switch ((BooleanOperator) expression.getOperator()) {
		case AND:
			return (Boolean) expression.getLeftOperand().evaluate()
					&& (Boolean) expression.getRightOperand().evaluate();
		case OR:
			return (Boolean) expression.getLeftOperand().evaluate()
					|| (Boolean) expression.getRightOperand().evaluate();
		case IMPLIES:
			return !((Boolean) expression.getLeftOperand().evaluate() && !(Boolean) expression
					.getRightOperand().evaluate());
		case IFONLYIF:
			return ((Boolean) expression.getLeftOperand().evaluate())
					.equals((Boolean) expression.getRightOperand().evaluate());

		case NOT:
			return !(Boolean) expression.getMiddleOperand().evaluate();
		case EQUALS:
			return expression.getLeftOperand().evaluate().equals(
					expression.getRightOperand().evaluate());
		case NOTEQUALS:
			return !(expression.getLeftOperand().evaluate().equals(expression
					.getRightOperand().evaluate()));
		case GREATERTHAN:
			return (Double) expression.getLeftOperand().evaluate() > (Double) expression
					.getRightOperand().evaluate();
		case GREATEROREQUALTHAN:
			return (Double) expression.getLeftOperand().evaluate() >= (Double) expression
					.getRightOperand().evaluate();
		case LOWERTHAN:
			return (Double) expression.getLeftOperand().evaluate() < (Double) expression
					.getRightOperand().evaluate();
		case LOWEROREQUALTHAN:
			return (Double) expression.getLeftOperand().evaluate() <= (Double) expression
					.getRightOperand().evaluate();

		default:
			return (Boolean) expression.getValue();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.expression.OperatorEvaluator#evaluateName(br.ufjf
	 * .mmc.jynacore.expression.impl.DefaultExpression)
	 */
	@Override
	public Object evaluateName(Expression expression) throws Exception {
		Expression target = expression.getMiddleOperand();
		if (target != null) {
			if (target.getOperator() instanceof ReferenceOperator) {
				Object refObj = target.getValue();

				if (refObj instanceof ClassInstanceItem) {
					if (refObj instanceof ClassInstanceProperty) {
						ClassInstanceProperty ciProp = (ClassInstanceProperty) refObj;
						return ciProp.getValue();
					} else if (refObj instanceof ClassInstanceStock) {
						ClassInstanceStock ciStock = (ClassInstanceStock) refObj;
						return ciStock.getValue();
					} else if (refObj instanceof ClassInstanceAuxiliary) {
						ClassInstanceAuxiliary ciProc = (ClassInstanceAuxiliary) refObj;
						return ciProc.getExpression().evaluate();

					} else if (refObj instanceof ClassInstanceRate) {
						ClassInstanceRate ciRate = (ClassInstanceRate) refObj;
						return ciRate.getExpression().evaluate();
					}
				}
				if (target.getValue() instanceof JynaEvaluated) {
					return ((JynaEvaluated) target.getValue()).getExpression()
							.evaluate();
				}
				return target.evaluate();
			} else
				return target.getValue();
		} else
			return (String) expression.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.expression.OperatorEvaluator#evaluateNumber(br.ufjf
	 * .mmc.jynacore.expression.impl.DefaultExpression)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Number evaluateNumber(Expression expression) throws Exception {
		switch ((NumberOperator) expression.getOperator()) {
		case ABS:
			return Math
					.abs(((Double) expression.getMiddleOperand().evaluate()));
		case ADD:
			return (Double) expression.getLeftOperand().evaluate()
					+ (Double) expression.getRightOperand().evaluate();
		case MINUS:
			return (Double) expression.getLeftOperand().evaluate()
					- (Double) expression.getRightOperand().evaluate();
		case TIMES:
			Double a = (Double) expression.getLeftOperand().evaluate();
			Double b = (Double) expression.getRightOperand().evaluate();

			return a * b;

		case DIVIDE:
			return (Double) expression.getLeftOperand().evaluate()
					/ (Double) expression.getRightOperand().evaluate();
		case NEGATION:
			return -(Double) expression.getMiddleOperand().evaluate();
		case MINIMUM:
			return Math.min((Double) expression.getLeftOperand().evaluate(),
					(Double) expression.getRightOperand().evaluate());
		case MAXIMUM:
			return Math.max((Double) expression.getLeftOperand().evaluate(),
					(Double) expression.getRightOperand().evaluate());
		case CONSTANT:
			return (Double) expression.getValue();
		case LOG:
			return Math.log((Double) expression.getMiddleOperand().evaluate());
		case EXP:
			return Math.exp((Double) expression.getMiddleOperand().evaluate());
		case SINE:
			return Math.sin((Double) expression.getMiddleOperand().evaluate());
		case COSINE:
			return Math.cos((Double) expression.getMiddleOperand().evaluate());
		case TAN:
			return Math.tan((Double) expression.getMiddleOperand().evaluate());
		case MOD:
			return (Math.floor((Double)expression.getLeftOperand().evaluate()) % (Math.floor((Double) expression.getRightOperand().evaluate())));
		case RAND:
			Random rnd = new Random();
			return new Double(rnd.nextFloat());
		case RANDINT:
			Random rnd2 = new Random();
			return new Double(rnd2.nextInt((int) Math.round((((Double) expression.getMiddleOperand()
					.evaluate())))));
		case POWER:
			return Math.pow((Double) expression.getLeftOperand().evaluate(),
					(Double) expression.getRightOperand().evaluate());
		case IF:
			return ((Boolean) expression.getLeftOperand().evaluate()) ? (Number) expression
					.getMiddleOperand().evaluate()
					: (Number) expression.getRightOperand().evaluate();
		case GROUPSUM:
			Double sum = 0.0;
			Set s = new HashSet();
			if (expression.getMiddleOperand() != null
					&& expression.getMiddleOperand().getValue() instanceof Collection) {
				s.addAll((Collection) expression.getMiddleOperand().getValue());
			}
			for (Object o : s) {
				ClassInstanceItem cii = (ClassInstanceItem) o;
				if (cii instanceof JynaValued) {
					JynaValued vi = (JynaValued) cii;
					if (vi.getValue() == null && vi instanceof JynaEvaluated) {
						vi.setValue((Double) ((JynaEvaluated) vi)
								.getExpression().evaluate());
					}
					sum += vi.getValue();
				}
			}
			return sum;

		case GROUPMAX:
			Double max = null;
			Set g = new HashSet();
			if (expression.getMiddleOperand() != null
					&& expression.getMiddleOperand().getValue() instanceof Collection) {
				g.addAll((Collection) expression.getMiddleOperand().getValue());
			}
			for (Object o : g) {
				ClassInstanceItem cii = (ClassInstanceItem) o;
				if (cii instanceof JynaValued) {
					JynaValued vi = (JynaValued) cii;
					if (vi.getValue() == null && vi instanceof JynaEvaluated) {
						vi.setValue((Double) ((JynaEvaluated) vi)
								.getExpression().evaluate());
					}
					if (max == null)
						max = vi.getValue();
					if (vi.getValue() > max)
						max = vi.getValue();
				}
			}
			return max;
		case GROUPMIN:
			Double min = null;
			Set gm = new HashSet();
			if (expression.getMiddleOperand() != null
					&& expression.getMiddleOperand().getValue() instanceof Collection) {
				gm
						.addAll((Collection) expression.getMiddleOperand()
								.getValue());
			}
			for (Object o : gm) {
				ClassInstanceItem cii = (ClassInstanceItem) o;
				if (cii instanceof JynaValued) {
					JynaValued vi = (JynaValued) cii;
					if (vi.getValue() == null && vi instanceof JynaEvaluated) {
						vi.setValue((Double) ((JynaEvaluated) vi)
								.getExpression().evaluate());
					}
					if (min == null)
						min = vi.getValue();
					if (vi.getValue() < min)
						min = vi.getValue();
				}
			}
			return min;
		case GROUPCOUNT:
			Set gc = new HashSet();
			if (expression.getMiddleOperand() != null
					&& expression.getMiddleOperand().getValue() instanceof Collection) {
				gc
						.addAll((Collection) expression.getMiddleOperand()
								.getValue());
			}
			return gc.size();
		case LOOKUP:
			ClassInstanceTable table = (ClassInstanceTable) expression
					.getLeftOperand().getMiddleOperand().getValue();
			Double value = (Double) expression.getMiddleOperand().evaluate();
			Double minValue = (Double) expression.getRightOperand().evaluate();
			Double maxValue = (Double) expression.getExtraOperand().evaluate();
			int size = table.getValues().size();
			int index = (int) Math.round(Math
					.floor(((value / (maxValue - minValue)) * size)));
			return table.getValues().get(index);
		default:
			return (Double) expression.getValue();
		}

	}

	@Override
	public Object evaluateReference(Expression expression) throws Exception {
		if (expression.getValue() instanceof JynaEvaluated) {
			JynaEvaluated ev = (JynaEvaluated) expression.getValue();
			Object value = ev.getExpression().evaluate(); 
			if(ev instanceof JynaValued && value instanceof Double){
				JynaValued v = (JynaValued)ev;
				v.setValue((Double) value);
			}
			return value;
		} else if (expression.getValue() instanceof InformationSource) {
			InformationSource is = (InformationSource) expression.getValue();
			return is.getValue();
		}

		return expression.getValue();
	}
}
