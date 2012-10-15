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

import java.util.Iterator;
import java.util.Set;

import br.ufjf.mmc.jynacore.expression.BooleanOperator;
import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NameOperator;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.ReferenceOperator;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;

/**
 * @author Knop
 * 
 */
public class ExpressionToJavaConverter {
	private static final String SEPARATOR = "_";

	public static String convertToJavaString(Expression e) {
		StringBuilder sb = new StringBuilder();
		if (e.getOperator() instanceof NameOperator) {
			if (e.getMiddleOperand() == null){
				sb.append(e.getValue());
			}else
			if (e.getMiddleOperand() != null
					&& e.getMiddleOperand().getOperator() instanceof ReferenceOperator) {
				ClassInstanceItem cii = (ClassInstanceItem) e
						.getMiddleOperand().getValue();
				if (cii.getClassInstance() != null) {
					sb.append(cii.getClassInstance().getName() + SEPARATOR
							+ cii.getName());
				} else {
					sb.append(cii.getName());
				}
			}
		}
		if (e.getOperator() instanceof NumberOperator)
			sb.append(convertNumber(e));
		if (e.getOperator() instanceof BooleanOperator)
			sb.append(convertBoolean(e));

		return sb.toString();
	}

	private static String convertBoolean(Expression e) {
		StringBuilder sb = new StringBuilder();
		BooleanOperator op = (BooleanOperator) e.getOperator();
		switch (op) {
		case AND:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append(" && ");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case OR:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append(" || ");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case EQUALS:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append(" == ");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case GREATEROREQUALTHAN:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append(" >= ");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case GREATERTHAN:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append(" > ");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case LOWEROREQUALTHAN:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append(" <= ");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case LOWERTHAN:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append(" < ");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case NOTEQUALS:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append(" != ");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case NOT:
			sb.append("!");
			if (!e.getMiddleOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getMiddleOperand()));
			if (!e.getMiddleOperand().isLeaf())
				sb.append(")");
			break;
		case IFONLYIF:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append(" == ");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case IMPLIES:
			sb.append("!");
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append(" && !");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case CONSTANT:
			sb.append(e.getValue().toString());

		default:
			break;
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private static String convertNumber(Expression e) {
		StringBuilder sb = new StringBuilder();
		NumberOperator op = (NumberOperator) e.getOperator();
		switch (op) {
		case ABS:
			sb.append("abs(");
			sb.append(convertToJavaString(e.getMiddleOperand()));
			sb.append(")");
			break;
		case NEGATION:
			sb.append("-");
			if (!e.getMiddleOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getMiddleOperand()));
			if (!e.getMiddleOperand().isLeaf())
				sb.append(")");
			break;
		case SINE:
			sb.append("sine(");
			sb.append(convertToJavaString(e.getMiddleOperand()));
			sb.append(")");
			break;
		case ADD:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append("+");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case DIVIDE:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append("/");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case TIMES:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append("*");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case MINUS:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append("-");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case MOD:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append("%");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			break;
		case SQUARE:
			sb.append("pow(");
			if (!e.getMiddleOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getMiddleOperand()));
			if (!e.getMiddleOperand().isLeaf())
				sb.append(",2)");
			sb.append(")");
			break;
		case SQUAREROOT:
			sb.append("sqrt(");
			if (!e.getMiddleOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getMiddleOperand()));
			if (!e.getMiddleOperand().isLeaf())
				sb.append(")");
			sb.append(")");
			break;
		case POWER:
			sb.append("sqrt(");
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append(",");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			sb.append(")");
			break;
		case LOG:
			sb.append("log(");
			if (!e.getMiddleOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getMiddleOperand()));
			if (!e.getMiddleOperand().isLeaf())
				sb.append(")");
			sb.append(")");
		case IF:
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append("?");
			if (!e.getMiddleOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getMiddleOperand()));
			if (!e.getMiddleOperand().isLeaf())
				sb.append(")");
			sb.append(":");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			sb.append(")");
			break;
		case MAXIMUM:
			sb.append("max(");
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append(",");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			sb.append(")");
			break;
		case MINIMUM:
			sb.append("min(");
			if (!e.getLeftOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			if (!e.getLeftOperand().isLeaf())
				sb.append(")");
			sb.append(",");
			if (!e.getRightOperand().isLeaf())
				sb.append("(");
			sb.append(convertToJavaString(e.getRightOperand()));
			if (!e.getRightOperand().isLeaf())
				sb.append(")");
			sb.append(")");
			break;
		case GROUPSUM:
			//sb.append(convertToJavaString(e.getLeftOperand()));
			if(e.getMiddleOperand()==null) break;
			Set<Object> setGS = (Set<Object>) e.getMiddleOperand().getValue();
			
			if(setGS.size()>0) sb.append("(");
			for (Iterator iterator = setGS.iterator(); iterator.hasNext();) {
				ClassInstanceItem cii = (ClassInstanceItem) iterator.next();
				sb.append(cii.getClassInstance().getName()+SEPARATOR+cii.getName());
				if(iterator.hasNext()) sb.append("+");
			}			
			if(setGS.size()>0) sb.append(")");
			
			break;
		case GROUPMAX:
			sb.append("/*GROUPSUM(");
			sb.append(convertToJavaString(e.getLeftOperand()));
			sb.append(",");
			sb.append(convertToJavaString(e.getRightOperand()));
			sb.append(")*/");
			break;
		case CONSTANT:
			sb.append(e.getValue().toString());
		default:
			break;
		}
		return sb.toString();
	}
}
