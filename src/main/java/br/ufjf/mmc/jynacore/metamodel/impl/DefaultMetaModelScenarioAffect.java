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
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioAffect;

/**
 * @author Knop
 * 
 */
public class DefaultMetaModelScenarioAffect implements MetaModelScenarioAffect {

	private Expression oldExpression;
	private Expression expression;
	private String name;

	/**
	 * 
	 */
	public DefaultMetaModelScenarioAffect() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioAffect#getExpression()
	 */
	@Override
	public Expression getExpression() {
		return expression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioAffect#getOldExpression()
	 */
	@Override
	public Expression getOldExpression() {
		return oldExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioAffect#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioAffect#setExpression(
	 * br.ufjf.mmc.jynacore.expression.Expression)
	 */
	@Override
	public void setExpression(Expression newExpression) {
		expression = newExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioAffect#setName(java.lang
	 * .String)
	 */
	@Override
	public void setName(String newName) {
		name = newName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioAffect#setExpression(
	 * br.ufjf.mmc.jynacore.expression.Expression)
	 */
	@Override
	public void setOldExpression(Expression newOldExpression) {
		oldExpression = newOldExpression;
	}

	@Override
	public String toString() {
		return getName();
	}
}
