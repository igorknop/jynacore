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

import br.ufjf.mmc.jynacore.expression.ExpressionValueType;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.OperatorEvaluator;

/**
 * @author Knop
 * 
 */
public class DefaultNumberConstantExpression extends DefaultExpression {
	public DefaultNumberConstantExpression() {
		setValueType(ExpressionValueType.NUMBER);
		setOperator(NumberOperator.CONSTANT);
	}
	
	public DefaultNumberConstantExpression(Number newValue){
		this();
		setValue(newValue);
	}

	public DefaultNumberConstantExpression(double newValue,
			OperatorEvaluator operatorEval) {
		this();
		setValue(newValue);
		setOperatorEvaluator(operatorEval);
	}
}
