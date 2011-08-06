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

import org.jdom.Element;
import org.jdom.Namespace;

import br.ufjf.mmc.jynacore.expression.BooleanOperator;
import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NameOperator;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.Operator;
import br.ufjf.mmc.jynacore.expression.ReferenceOperator;
import br.ufjf.mmc.jynacore.expression.exception.InvalidExpressionException;

/**
 * @author Knop
 * 
 */
public class ExpressionToMathMLConverter {
	public static final String MATHML_NS_URI = "http://www.w3.org/1998/Math/MathML";
	public static final String CMATHML_NS = "m";
	public static final String MATHML_ROOT = "math";
	private static final String APPLY = "apply";
	private static final String PLUS = "plus";
	private static final String CN = "cn";
	private static final String MINUS = "minus";
	private static final String TIMES = "times";
	private static final String DIVIDE = "divide";
	private static final String POWER = "power";
	private static final String CI = "ci";
	private static final String ABS = "abs";
	private static final String INVERSE = "inverse";
	private static final String SINE = "sin";
	private static final String COSINE = "cos";
	private static final String TAN = "tan";
	private static final String EXP = "exp";
	private static final String LOG = "log";
	private static final String NOT = "not";
	private static final String IMPLIES = "implies";
	private static final String EQUALS = "eq";
	private static final String NOTEQUALS = "neq";
	private static final String GREATERTHAN = "gt";
	private static final String LOWERTHAN = "lt";
	private static final String GREATEROREQUALTHAN = "geq";
	private static final String LOWEROREQUALTHAN = "leq";
	private static final String TRUE = "true";
	private static final String FALSE = "false";
	private static final String AND = "and";
	private static final String OR = "or";
	private static final String IF = "if";
	private static final String MINIMUM = "min";
	private static final String MAXIMUM = "max";
	private static final String GROUPMAX = "groupmax";
	private static final String GROUPSUM = "groupsum";
	private static final String RAND = "rand";
	private static final String RANDINT = "randint";
	private static final String GROUPMIN = "groupmin";
	private static final String GROUPCOUNT = "groupcount";
	private static final String LOOKUP = "lookup";
	public static Namespace mathmlNS = Namespace.getNamespace(CMATHML_NS,
			MATHML_NS_URI);

	public static Element convertToMathml(Expression expr) throws Exception {

		Element eRoot = new Element(MATHML_ROOT, mathmlNS);
		eRoot.addContent(parse(expr));

		return eRoot;
	}

	private static Element parse(Expression expr) throws Exception {
		Element e = new Element(CN, mathmlNS);
		e.setText("0");
		if (expr.getOperator() instanceof NumberOperator) {
			e = parseNumber(expr);
		} else if (expr.getOperator() instanceof NameOperator) {
			e = parseName(expr);
		} else if (expr.getOperator() instanceof BooleanOperator) {
			e = parseBoolean(expr);
		} else if (expr.getOperator() instanceof ReferenceOperator) {
			e = parseReference(expr);
		}
		return e;
	}

	private static Element parseReference(Expression expr) {
		return null;
	}

	private static Element parseBoolean(Expression expr) throws Exception {
		BooleanOperator operator = (BooleanOperator) expr.getOperator();
		Element eLeft;
		Element eMiddle;
		Element eRight;
		Element eApply;
		switch (operator) {
		case EQUALS:
			eApply = new Element(APPLY, mathmlNS);
			Element eEquals = new Element(EQUALS, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eEquals);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case NOTEQUALS:
			eApply = new Element(APPLY, mathmlNS);
			Element eNeq = new Element(NOTEQUALS, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eNeq);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case GREATERTHAN:
			eApply = new Element(APPLY, mathmlNS);
			Element eGt = new Element(GREATERTHAN, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eGt);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case LOWERTHAN:
			eApply = new Element(APPLY, mathmlNS);
			Element eLt = new Element(LOWERTHAN, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eLt);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case IMPLIES:
			eApply = new Element(APPLY, mathmlNS);
			Element eImplies = new Element(IMPLIES, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eImplies);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case GREATEROREQUALTHAN:
			eApply = new Element(APPLY, mathmlNS);
			Element eGeq = new Element(GREATEROREQUALTHAN, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eGeq);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case LOWEROREQUALTHAN:
			eApply = new Element(APPLY, mathmlNS);
			Element eLeq = new Element(LOWEROREQUALTHAN, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eLeq);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case AND:
			eApply = new Element(APPLY, mathmlNS);
			Element eAnd = new Element(AND, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eAnd);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case OR:
			eApply = new Element(APPLY, mathmlNS);
			Element eOR = new Element(OR, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eOR);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case NOT:
			eApply = new Element(APPLY, mathmlNS);
			Element eNot = new Element(NOT, mathmlNS);
			eMiddle = parse(expr.getMiddleOperand());
			eApply.addContent(eNot);
			eApply.addContent(eMiddle);
			return eApply;

		case CONSTANT:
			Element eBool;
			if ((Boolean) expr.getValue()) {
				eBool = new Element(TRUE, mathmlNS);
			} else {
				eBool = new Element(FALSE, mathmlNS);
			}
			return eBool;

		default:
			break;
		}

		return null;
	}

	private static Element parseName(Expression expr) {
		Element eName = new Element(CI, mathmlNS);
		eName.setText(expr.getValue().toString());
		return eName;
	}

	private static Element parseNumber(Expression expr) throws Exception {
		NumberOperator operator = (NumberOperator) expr.getOperator();
		Element eLeft;
		Element eMiddle;
		Element eRight;
		Element eApply;
		switch (operator) {
		case ADD:
			eApply = new Element(APPLY, mathmlNS);
			Element eSum = new Element(PLUS, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eSum);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case MINUS:
			eApply = new Element(APPLY, mathmlNS);
			Element eMinus = new Element(MINUS, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eMinus);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case TIMES:
			eApply = new Element(APPLY, mathmlNS);
			Element eTimes = new Element(TIMES, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eTimes);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case DIVIDE:
			eApply = new Element(APPLY, mathmlNS);
			Element eDivide = new Element(DIVIDE, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eDivide);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case POWER:
			eApply = new Element(APPLY, mathmlNS);
			Element ePower = new Element(POWER, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(ePower);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case ABS:
			eApply = new Element(APPLY, mathmlNS);
			Element eABS = new Element(ABS, mathmlNS);
			eMiddle = parse(expr.getMiddleOperand());
			eApply.addContent(eABS);
			eApply.addContent(eMiddle);
			return eApply;
		case NEGATION:
			eApply = new Element(APPLY, mathmlNS);
			Element eNeg = new Element(INVERSE, mathmlNS);
			eMiddle = parse(expr.getMiddleOperand());
			eApply.addContent(eNeg);
			eApply.addContent(eMiddle);
			return eApply;
		case SINE:
			eApply = new Element(APPLY, mathmlNS);
			Element eSin = new Element(SINE, mathmlNS);
			eMiddle = parse(expr.getMiddleOperand());
			eApply.addContent(eSin);
			eApply.addContent(eMiddle);
			return eApply;
		case COSINE:
			eApply = new Element(APPLY, mathmlNS);
			Element eCosine = new Element(COSINE, mathmlNS);
			eMiddle = parse(expr.getMiddleOperand());
			eApply.addContent(eCosine);
			eApply.addContent(eMiddle);
			return eApply;
		case TAN:
			eApply = new Element(APPLY, mathmlNS);
			Element eTg = new Element(TAN, mathmlNS);
			eMiddle = parse(expr.getMiddleOperand());
			eApply.addContent(eTg);
			eApply.addContent(eMiddle);
			return eApply;
		case LOG:
			eApply = new Element(APPLY, mathmlNS);
			Element eLog = new Element(LOG, mathmlNS);
			eMiddle = parse(expr.getMiddleOperand());
			eApply.addContent(eLog);
			eApply.addContent(eMiddle);
			return eApply;
		case EXP:
			eApply = new Element(APPLY, mathmlNS);
			Element eExp = new Element(EXP, mathmlNS);
			eMiddle = parse(expr.getMiddleOperand());
			eApply.addContent(eExp);
			eApply.addContent(eMiddle);
			return eApply;
		case RAND:
			eApply = new Element(APPLY, mathmlNS);
			Element eRand = new Element(RAND, mathmlNS);
//			eMiddle = parse(expr.getMiddleOperand());
			eApply.addContent(eRand);
//			eApply.addContent(eMiddle);
			return eApply;
		case RANDINT:
			eApply = new Element(APPLY, mathmlNS);
			Element eRandInt = new Element(RANDINT, mathmlNS);
			eMiddle = parse(expr.getMiddleOperand());
			eApply.addContent(eRandInt);
			eApply.addContent(eMiddle);
			return eApply;

		case CONSTANT:
			Element eCn = new Element(CN, mathmlNS);
			eCn.setText(expr.getValue().toString());
			return eCn;

		case IF:
			eApply = new Element(APPLY, mathmlNS);
			Element eIf = new Element(IF, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eMiddle = parse(expr.getMiddleOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eIf);
			eApply.addContent(eLeft);
			eApply.addContent(eMiddle);
			eApply.addContent(eRight);
			return eApply;
		case MINIMUM:
			eApply = new Element(APPLY, mathmlNS);
			Element eMinimum = new Element(MINIMUM, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eMinimum);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case MAXIMUM:
			eApply = new Element(APPLY, mathmlNS);
			Element eMaximum = new Element(MAXIMUM, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eMaximum);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case GROUPSUM:
			eApply = new Element(APPLY, mathmlNS);
			Element eGroupsum = new Element(GROUPSUM, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eGroupsum);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case GROUPMAX:
			eApply = new Element(APPLY, mathmlNS);
			Element eGroupmax = new Element(GROUPMAX, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eGroupmax);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case GROUPCOUNT:
			eApply = new Element(APPLY, mathmlNS);
			Element eGroupCount = new Element(GROUPCOUNT, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eRight = parse(expr.getRightOperand());
			eApply.addContent(eGroupCount);
			eApply.addContent(eLeft);
			eApply.addContent(eRight);
			return eApply;
		case LOOKUP:
			eApply = new Element(APPLY, mathmlNS);
			Element eLookup = new Element(LOOKUP, mathmlNS);
			eLeft = parse(expr.getLeftOperand());
			eMiddle = parse(expr.getRightOperand());
			eRight = parse(expr.getRightOperand());
			Element eExtra = parse(expr.getExtraOperand());
			eApply.addContent(eLookup);
			eApply.addContent(eLeft);
			eApply.addContent(eMiddle);
			eApply.addContent(eRight);
			eApply.addContent(eExtra);
			return eApply;

		default:
			break;
		}

		throw new Exception("Expression to mathml convert exception: "+ operator +" not implemented");
	}

	public static Expression readFromMathML(Element eRoot)
			throws Exception {
		if (eRoot==null || !eRoot.getName().equals(MATHML_ROOT))
			throw new InvalidExpressionException();

		Element eFirstChild = (Element) eRoot.getChildren().get(0);

		return readElement(eFirstChild);

	}

	private static Expression readElement(Element element) throws Exception {

		Expression expr = new DefaultExpression();

		if (element.getName().equals(APPLY)) {
			int operands = element.getChildren().size() - 1;
			Element eCommand, eLeft, eMiddle, eRight, eExtra;
			switch (operands) {
			case 1:
				eCommand = (Element) element.getChildren().get(0);
				expr.setOperator(getOperator(eCommand));
				eMiddle = (Element) element.getChildren().get(1);
				expr.setMiddleOperand(readElement(eMiddle));
				break;
			case 2:
				eCommand = (Element) element.getChildren().get(0);
				expr.setOperator(getOperator(eCommand));
				eLeft = (Element) element.getChildren().get(1);
				eRight = (Element) element.getChildren().get(2);
				expr.setLeftOperand(readElement(eLeft));
				expr.setRightOperand(readElement(eRight));
				break;
			case 3:
				eCommand = (Element) element.getChildren().get(0);
				expr.setOperator(getOperator(eCommand));
				eLeft = (Element) element.getChildren().get(1);
				eMiddle = (Element) element.getChildren().get(2);
				eRight = (Element) element.getChildren().get(3);
				expr.setLeftOperand(readElement(eLeft));
				expr.setMiddleOperand(readElement(eMiddle));
				expr.setRightOperand(readElement(eRight));
				break;
			case 4:
				eCommand = (Element) element.getChildren().get(0);
				expr.setOperator(getOperator(eCommand));
				eLeft = (Element) element.getChildren().get(1);
				eMiddle = (Element) element.getChildren().get(2);
				eRight = (Element) element.getChildren().get(3);
				eExtra = (Element) element.getChildren().get(4);
				expr.setLeftOperand(readElement(eLeft));
				expr.setMiddleOperand(readElement(eMiddle));
				expr.setRightOperand(readElement(eRight));
				expr.setExtraOperand(readElement(eExtra));
				break;

			default:
				eCommand = (Element) element.getChildren().get(0);
				expr.setOperator(getOperator(eCommand));
				break;
			}

		} else if (element.getName().equals(CN)) {
			expr = new DefaultNumberConstantExpression(Double.valueOf(element
					.getText()));

		} else if (element.getName().equals(CI)) {
			expr = new DefaultNameExpression(element.getText());
		} else if (element.getName().equals(TRUE)) {
			expr = new DefaultExpression();
			expr.setOperator(BooleanOperator.CONSTANT);
			expr.setValue(true);
		} else if (element.getName().equals(FALSE)) {
			expr = new DefaultExpression();
			expr.setOperator(BooleanOperator.CONSTANT);
			expr.setValue(false);
		}

		return expr;
	}

	private static Operator getOperator(Element command) throws Exception {
		if (command.getName().equals(CN))
			return NumberOperator.CONSTANT;
		else if (command.getName().equals(CI))
			return NameOperator.NAME;
		else if (command.getName().equals(TRUE))
			return BooleanOperator.CONSTANT;
		else if (command.getName().equals(FALSE))
			return BooleanOperator.CONSTANT;
		else if (command.getName().equals(INVERSE))
			return NumberOperator.NEGATION;
		else if (command.getName().equals(PLUS))
			return NumberOperator.ADD;
		else if (command.getName().equals(MINUS))
			return NumberOperator.MINUS;
		else if (command.getName().equals(TIMES))
			return NumberOperator.TIMES;
		else if (command.getName().equals(DIVIDE))
			return NumberOperator.DIVIDE;
		else if (command.getName().equals(POWER))
			return NumberOperator.POWER;
		else if (command.getName().equals(ABS))
			return NumberOperator.ABS;
		else if (command.getName().equals(SINE))
			return NumberOperator.SINE;
		else if (command.getName().equals(COSINE))
			return NumberOperator.COSINE;
		else if (command.getName().equals(TAN))
			return NumberOperator.TAN;
		else if (command.getName().equals(EXP))
			return NumberOperator.EXP;
		else if (command.getName().equals(LOG))
			return NumberOperator.LOG;
		else if (command.getName().equals(RAND))
			return NumberOperator.RAND;
		else if (command.getName().equals(RANDINT))
			return NumberOperator.RANDINT;
		else if (command.getName().equals(IF))
			return NumberOperator.IF;
		else if (command.getName().equals(GROUPMAX))
			return NumberOperator.GROUPMAX;
		else if (command.getName().equals(GROUPMIN))
			return NumberOperator.GROUPMIN;
		else if (command.getName().equals(GROUPCOUNT))
			return NumberOperator.GROUPCOUNT;
		else if (command.getName().equals(GROUPSUM))
			return NumberOperator.GROUPSUM;
		else if (command.getName().equals(MAXIMUM))
			return NumberOperator.MAXIMUM;
		else if (command.getName().equals(MINIMUM))
			return NumberOperator.MINIMUM;
		else if (command.getName().equals(LOOKUP))
			return NumberOperator.LOOKUP;
		//Boolean
		else if (command.getName().equals(EQUALS))
			return BooleanOperator.EQUALS;
		else if (command.getName().equals(AND))
			return BooleanOperator.AND;
		else if (command.getName().equals(OR))
			return BooleanOperator.OR;
		else if (command.getName().equals(NOT))
			return BooleanOperator.NOT;
		else if (command.getName().equals(GREATERTHAN))
			return BooleanOperator.GREATERTHAN;
		else if (command.getName().equals(GREATEROREQUALTHAN))
			return BooleanOperator.GREATERTHAN;
		else if (command.getName().equals(LOWERTHAN))
			return BooleanOperator.LOWERTHAN;
		else if (command.getName().equals(LOWEROREQUALTHAN))
			return BooleanOperator.LOWEROREQUALTHAN;
		else if (command.getName().equals(IMPLIES))
			return BooleanOperator.IMPLIES;
		else throw new Exception("Unsuported operador: "+command.getName());

	}
}
