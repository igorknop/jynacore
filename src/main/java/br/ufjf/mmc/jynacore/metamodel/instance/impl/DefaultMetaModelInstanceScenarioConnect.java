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

import br.ufjf.mmc.jynacore.metamodel.MetaModelScenario;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceScenarioConnect;

/**
 * @author Knop
 * 
 */
public class DefaultMetaModelInstanceScenarioConnect implements
		MetaModelInstanceScenarioConnect {

	private MetaModelScenario scenario;
	private MetaModelScenarioConnection connect;
	private ClassInstance instance;

	public DefaultMetaModelInstanceScenarioConnect(MetaModelScenario scenario,
			MetaModelScenarioConnection connection, ClassInstance instance) {
		this.scenario=scenario;
		this.connect = connection;
		this.instance = instance;
	}

	@Override
	public MetaModelScenarioConnection getConnect() {
		return connect;
	}

	@Override
	public ClassInstance getInstance() {
		return instance;
	}

	@Override
	public MetaModelScenario getScenario() {
		return scenario;
	}
	@Override
	public String toString() {
		return instance.getName()+":"+scenario.getName()+"."+connect.getName();
	}

}
