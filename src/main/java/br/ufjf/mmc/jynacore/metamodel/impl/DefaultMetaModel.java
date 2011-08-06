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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModelItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenario;
import br.ufjf.mmc.jynacore.metamodel.exceptions.MetaModelScenarioException;

/**
 * @author Knop
 * 
 */
public class DefaultMetaModel extends HashMap<String, MetaModelItem> implements
		MetaModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String modelName;
	private Map<String, MetaModelScenario> scenarios;

	public DefaultMetaModel() {
		super();
		scenarios = new HashMap<String, MetaModelScenario>();
	}

	@Override
	public String getName() {
		return modelName;
	}

	@Override
	public void setName(String newName) {
		modelName = newName;
	}

	@Override
	public MetaModelItem put(MetaModelItem metaModelItem) {
		metaModelItem.setMetaModel(this);
		return put(metaModelItem.getName(), metaModelItem);
	}

	@Override
	public MetaModelItem put(String arg0, MetaModelItem arg1) {
		arg1.setMetaModel(this);
		return super.put(arg0, arg1);
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public Map<String, MetaModelScenario> getScenarios() {
		return Collections.unmodifiableMap(scenarios);
	}

	@Override
	public MetaModelScenario getScenario(String scenarioName) {
		return scenarios.get(scenarioName);
	}

	@Override
	public void putScenario(MetaModelScenario scenario) throws Exception {
		if (scenario.containsKey(scenario.getName()))
			throw new MetaModelScenarioException("A Scenario named: "
					+ scenario.getName() + " already defined in scenarios.");
		if (scenario.containsValue(scenario))
			throw new MetaModelScenarioException("Scenario: "
					+ scenario.getName() + " already in scenarios.");
		if (!scenario.getMetaModelName().equals(getName()))
			throw new MetaModelScenarioException("Scenario: "
					+ scenario.getName() + " was designed to work with "
					+ scenario.getMetaModelName() + ", not " + getName());
		scenarios.put(scenario.getName(), scenario);
	}

	@Override
	public void removeScenario(String scenarioName) {
		scenarios.remove(scenarioName);
	}

}
