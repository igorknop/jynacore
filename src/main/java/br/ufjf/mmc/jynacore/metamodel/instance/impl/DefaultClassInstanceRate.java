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
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock;

/**
 * @author Knop
 * 
 */
public class DefaultClassInstanceRate implements ClassInstanceRate {

	private Expression expression;
	private ClassInstanceStock source;
	private ClassInstanceStock target;
	private Double value;
	private String name;
	private ClassInstance classInstance;

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate#getExpression()
	 */
	@Override
	public Expression getExpression() {
		return expression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate#getSource()
	 */
	@Override
	public ClassInstanceStock getSource() {
		return source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate#getTarget()
	 */
	@Override
	public ClassInstanceStock getTarget() {
		return target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate#getValue()
	 */
	@Override
	public Double getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate#setExpression(br.ufjf.mmc.jynacore.expression.Expression)
	 */
	@Override
	public void setExpression(Expression newExpression) {
		expression = newExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate#setSource(br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock)
	 */
	@Override
	public void setSource(ClassInstanceStock targetStock) {
		source = targetStock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate#setTarget(br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock)
	 */
	@Override
	public void setTarget(ClassInstanceStock targetStock) {
		target = targetStock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
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
	public String toString() {
		return getName();
	}

}
