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

import java.util.HashMap;
import java.util.Map;

import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.OperatorEvaluatorParametrized;

/**
 * @author Knop
 * 
 */
public class DefaultOperatorEvaluatorWithParameters extends
		DefaultOperatorEvaluator implements OperatorEvaluatorParametrized {
	private Map<String, Double> overrideParams;

	public DefaultOperatorEvaluatorWithParameters() {
		super();
		overrideParams = new HashMap<String, Double>();
	}

	public Map<String, Double> getParameters() {
		return overrideParams;
	}

	public void setParameters(Map<String, Double> newParams) {
		overrideParams = newParams;
	}

	@Override
	public Object evaluateName(Expression expression) throws Exception {
		if (overrideParams.keySet().contains(expression.getValue())) {
			return overrideParams.get(expression.getValue());
		} else
			return super.evaluateName(expression);
	}
}
