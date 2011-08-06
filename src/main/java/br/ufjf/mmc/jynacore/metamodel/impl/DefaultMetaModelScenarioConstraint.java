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

import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConstraint;

/**
 * @author Knop
 * 
 */
public class DefaultMetaModelScenarioConstraint implements
		MetaModelScenarioConstraint {

	private String connectionName;
	private String relationName;
	private String scenarioName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConstraint#getConnection
	 * ()
	 */
	@Override
	public String getConnectionName() {
		return connectionName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConstraint#getRelation()
	 */
	@Override
	public String getRelationName() {
		return relationName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConstraint#getScenario()
	 */
	@Override
	public String getScenarioName() {
		return scenarioName;
	}

	@Override
	public void setConnectionName(String newConnectionName) {
		connectionName = newConnectionName;
	}

	@Override
	public void setRelationName(String newRelationName) {
		relationName = newRelationName;
	}

	@Override
	public void setScenarioName(String newScenarioName) {
		scenarioName = newScenarioName;
	}

	@Override
	public String toString() {
		return getScenarioName();
	}
}
