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
import br.ufjf.mmc.jynacore.expression.OperatorEvaluator;
import br.ufjf.mmc.jynacore.expression.ReferenceOperator;

/**
 * @author Knop
 * 
 */
public class DefaultReferenceExpression extends DefaultExpression {
	public DefaultReferenceExpression() {
		super();
		setValueType(ExpressionValueType.REFERENCE);
		setOperator(ReferenceOperator.REFERENCE);
	}

	public DefaultReferenceExpression(Object newReference) {
		this();
		setValue(newReference);
	}

	public DefaultReferenceExpression(String newReference, OperatorEvaluator operatorEval) {
		this();
		setValue(newReference);
		setOperatorEvaluator(operatorEval);
	}

}
