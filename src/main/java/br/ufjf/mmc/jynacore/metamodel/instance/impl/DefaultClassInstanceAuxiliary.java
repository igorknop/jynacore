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
package br.ufjf.mmc.jynacore.metamodel.instance.impl;

import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceAuxiliary;

/**
 * @author Knop
 *
 */
public class DefaultClassInstanceAuxiliary implements ClassInstanceAuxiliary {

	private Expression expression;
	private String name;
	private ClassInstance classInstance;
	private Double value;

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceAuxiliary#getExpression()
	 */
	@Override
	public Expression getExpression() {
		return expression;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceAuxiliary#setExpression(br.ufjf.mmc.jynacore.expression.Expression)
	 */
	@Override
	public void setExpression(Expression newExpression) {
		expression = newExpression;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem#setName(java.lang.String)
	 */
	@Override
	public void setName(String Name) {
		name = Name;
	}

	@Override
	public ClassInstance getClassInstance() {
		return classInstance;
	}

	@Override
	public void setClassInstance(ClassInstance newClassInstance) {
		classInstance = newClassInstance;
	}

	@Override
	public void setValue(Double newValue) {
		value = newValue;
	}
	@Override
	public Double getValue() {
//		if(value==null)
//				setValue((Double)expression.evaluate());
		return value;
	}
	@Override
	public String toString() {
		return getName();
	}

}
