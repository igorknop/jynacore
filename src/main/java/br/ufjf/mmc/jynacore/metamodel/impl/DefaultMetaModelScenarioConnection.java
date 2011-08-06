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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioAffect;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConstraint;

/**
 * @author Knop
 * 
 */
public class DefaultMetaModelScenarioConnection implements
		MetaModelScenarioConnection {

	private List<MetaModelScenarioAffect> affectList;
	private Map<String, MetaModelClassItem> classItems;
	private String className;
	private String name;
	private List<MetaModelScenarioConstraint> constraints;

	public DefaultMetaModelScenarioConnection() {
		affectList = new ArrayList<MetaModelScenarioAffect>();
		classItems = new HashMap<String, MetaModelClassItem>();
		constraints = new ArrayList<MetaModelScenarioConstraint>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection#getAffectList
	 * ()
	 */
	@Override
	public List<MetaModelScenarioAffect> getAffectList() {
		return affectList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection#getClassItems
	 * ()
	 */
	@Override
	public Map<String, MetaModelClassItem> getClassItems() {
		return classItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection#getClassName()
	 */
	@Override
	public String getClassName() {
		return className;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection#setAffectList
	 * (java.util.List)
	 */
	@Override
	public void setAffectList(List<MetaModelScenarioAffect> newAffectList) {
		affectList = newAffectList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection#setClassItems
	 * (java.util.Map)
	 */
	@Override
	public void setClassItems(Map<String, MetaModelClassItem> newClassItems) {
		classItems = newClassItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection#setClassName
	 * (java.lang.String)
	 */
	@Override
	public void setClassName(String newClassName) {
		className = newClassName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection#setName(java
	 * .lang.String)
	 */
	@Override
	public void setName(String newName) {
		name = newName;
	}

	@Override
	public List<MetaModelScenarioConstraint> getConstraints() {
		return constraints;
	}

	@Override
	public void setConstraints(List<MetaModelScenarioConstraint> newConstraints) {
		constraints = newConstraints;
	}

	@Override
	public String toString() {
		return getName();
	}
}
