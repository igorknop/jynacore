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
import java.util.List;

import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassItemVisibility;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceTable;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultClassInstanceTable;

/**
 * @author Knop
 * 
 */
public class DefaultMetaModelClassTable implements br.ufjf.mmc.jynacore.metamodel.MetaModelClassTable {

	private MetaModelClassItemVisibility visibility;
	private String name;
	private List<Double> values;

	public DefaultMetaModelClassTable() {
		values = new ArrayList<Double>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem#getNewInstance()
	 */
	@Override
	public ClassInstanceItem getNewInstance() {
		ClassInstanceTable newInstance = new DefaultClassInstanceTable();
		newInstance.setName(getName());
		return newInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem#getVisibility()
	 */
	@Override
	public MetaModelClassItemVisibility getVisibility() {
		return visibility;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem#setName(java.lang.String)
	 */
	@Override
	public void setName(String newName) {
		name = newName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem#setVisibility(br.ufjf.mmc.jynacore.metamodel.MetaModelClassItemVisibility)
	 */
	@Override
	public void setVisibility(MetaModelClassItemVisibility newVisibility) {
		visibility = newVisibility;
	}

	public void setValues(String text) {
		String[] s = text.split(" ");
		for (int i = 0; i < s.length; i++) {
			values.add(Double.valueOf(s[i]));
		}
	}
	public List<Double> getValues() {
		return values;
	}
	public void setValues(List<Double> newValues){
		values = newValues;
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
	@Override
	public String toString() {
		return getName();
	}

}
