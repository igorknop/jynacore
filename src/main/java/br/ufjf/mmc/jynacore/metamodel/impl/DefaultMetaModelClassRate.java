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
package br.ufjf.mmc.jynacore.metamodel.impl;

import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultExpression;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassItemVisibility;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassRate;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultClassInstanceRate;

/**
 * @author Knop
 *
 */
public class DefaultMetaModelClassRate implements MetaModelClassRate {

	private MetaModelClassItem target;
	private Expression expression;
	private String name;
	private MetaModelClassItemVisibility visibility = MetaModelClassItemVisibility.PUBLIC;
	private MetaModelClassItem source;

	/**
	 * 
	 */
	public DefaultMetaModelClassRate() {
		name = "NEW_RATE";
		expression = new DefaultExpression();
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClassRate#getTarget()
	 */
	@Override
	public MetaModelClassItem getTarget() {
		return target;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClassRate#getExpression()
	 */
	@Override
	public Expression getExpression() {
		return expression;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClassRate#setTarget(br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem)
	 */
	@Override
	public void setTarget(MetaModelClassItem target) {
		this.target =  target;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClassRate#setExpression(java.lang.String)
	 */
	@Override
	public void setExpression(Expression newExpression) {
		expression = newExpression;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem#setName(java.lang.String)
	 */
	@Override
	public void setName(String newName) {
		name = newName;
	}

	@Override
	public MetaModelClassItemVisibility getVisibility() {
		return visibility ;
	}

	@Override
	public void setVisibility(MetaModelClassItemVisibility newVisibility) {
		visibility = newVisibility;
	}
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public ClassInstanceItem getNewInstance() {
		ClassInstanceRate newRateInstance = new DefaultClassInstanceRate();
		newRateInstance.setName(getName());
		newRateInstance.setExpression(getExpression().getDeepCopy());
		return newRateInstance;
	}

	@Override
	public MetaModelClassItem getSource() {
		return source;
	}

	@Override
	public void setSource(MetaModelClassItem source) {
		this.source = source;
	}
	private MetaModelClass metaModelClass;

	@Override
	public MetaModelClass getMetaModelClass() {
		return metaModelClass;
	}

	@Override
	public void setMetaModelClass(MetaModelClass newClass) {
		metaModelClass = newClass;
	}


}
