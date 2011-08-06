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

import java.util.HashMap;

import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenario;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection;

/**
 * @author Knop
 *
 */
public class DefaultMetaModelScenario extends HashMap<String, MetaModelScenarioConnection> implements MetaModelScenario {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4710565146802143205L;
	private String metaModelName;
	private String name;
	private MetaModel metaModel;
	private String fileName;

	/**
	 * 
	 */
	public DefaultMetaModelScenario() {
		super();
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelScenario#getMetaModelName()
	 */
	@Override
	public String getMetaModelName() {
		return metaModelName;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelScenario#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelScenario#setMetaModelName(java.lang.String)
	 */
	@Override
	public void setMetaModelName(String metaModelName) {
		this.metaModelName =  metaModelName;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelScenario#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public MetaModel getMetaModel() {
		return metaModel;
	}

	@Override
	public void setMetaModel(MetaModel newMetaModel) {
		setMetaModelName(newMetaModel.getName());
		metaModel = newMetaModel;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public void setFileName(String newFileName) {
		fileName = newFileName;
	}
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public void put(MetaModelScenarioConnection connection) {
		put(connection.getName(),connection);
		
	}
}
