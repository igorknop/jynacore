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
import br.ufjf.mmc.jynacore.expression.impl.DefaultNumberConstantExpression;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassItemVisibility;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassStock;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultClassInstanceStock;

/**
 * @author Knop
 *
 */
public class DefaultMetaModelClassStock implements MetaModelClassStock {

	private Expression expression;
	private String name;
	private MetaModelClassItemVisibility visibility = MetaModelClassItemVisibility.PUBLIC;

	/**
	 * 
	 */
	public DefaultMetaModelClassStock() {
		expression = new DefaultNumberConstantExpression(0.0);
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClassStock#getExpression()
	 */
	@Override
	public Expression getExpression() {
		return expression;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClassStock#setExpression(java.lang.String)
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
		ClassInstanceStock newStockInstance = new DefaultClassInstanceStock();
		newStockInstance.setName(getName());
		newStockInstance.setValue(0.0);
		newStockInstance.setInitialValue(getExpression().getDeepCopy());
		return newStockInstance;
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
