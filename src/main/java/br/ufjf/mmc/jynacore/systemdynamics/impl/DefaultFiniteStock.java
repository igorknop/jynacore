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
package br.ufjf.mmc.jynacore.systemdynamics.impl;

import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNumberConstantExpression;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;

public class DefaultFiniteStock implements FiniteStock {

	// private Double initialValue;
	private Double value;
	private String name;
	private Expression initialValue;

	public DefaultFiniteStock() {
		value = 0.0;
		name = "";
	}


	@Override
	public Double getValue() {
		return value;
	}

	@Override
	public void setInitialValue(Expression newInitialValue) {
		initialValue = newInitialValue;
	}

	@Override
	public void setInitialValue(Double newValue) {
		setInitialValue(new DefaultNumberConstantExpression(newValue));
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String newName) {
		name = newName;
	}

	@Override
	public void setValue(Double newValue) {
		value = newValue;
	}

	@Override
	public Expression getInitialValue() {
		return initialValue;
	}

	@Override
	public String toString() {
		return getName();
	}
}
