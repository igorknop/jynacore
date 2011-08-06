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
import java.util.Map;

import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem;

/**
 * @author Knop
 * 
 */
@SuppressWarnings("serial")
public class DefaultMetaModelClass extends HashMap<String, MetaModelClassItem>
		implements MetaModelClass {

	// private List<MetaModelClassItem> publicItems;
	// private List<MetaModelClassItem> privateItems;
	// private Map<String, MetaModelClassItem> items;
	private String name;
	// private String className;
	private MetaModel metaModel;

	/**
	 * 
	 */
	public DefaultMetaModelClass() {
		// publicItems = new ArrayList<MetaModelClassItem>();
		// privateItems = new ArrayList<MetaModelClassItem>();
		// items = new HashMap<String, MetaModelClassItem>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClass#getPrivateItems()
	 */
	// @Override
	// public List<MetaModelClassItem> getPrivateItems() {
	// return privateItems;
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelClass#getPublicItems()
	 */
	// @Override
	// public List<MetaModelClassItem> getPublicItems() {
	// return publicItems;
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelClass#setPrivateItems(java.util
	 * .List)
	 */
	// @Override
	// public void setPrivateItems(List<MetaModelClassItem> newPrivateItems) {
	// privateItems = newPrivateItems;
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelClass#setPublicItems(java.util
	 * .List)
	 */
	// @Override
	// public void setPublicItems(List<MetaModelClassItem> newPublicItems) {
	// publicItems = newPublicItems;
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelItem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelItem#setName(java.lang.String)
	 */
	@Override
	public void setName(String newName) {
		name = newName;
	}

	@Override
	public String getClassName() {
		return name;
	}

	@Override
	public void setClassName(String newClassName) {
		name = newClassName;
	}

	@Override
	public MetaModel getMetaModel() {
		return metaModel;
	}

	@Override
	public void setMetaModel(MetaModel newMetaModel) {
		metaModel = newMetaModel;
	}

	@Override
	public void put(MetaModelClassItem item) {
		put(item.getName(), item);
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public MetaModelClassItem put(String key, MetaModelClassItem value) {
		value.setMetaModelClass(this);
		return super.put(key, value);
	}
	
	@Override
	public void putAll(Map<? extends String, ? extends MetaModelClassItem> m) {
		for(Entry<? extends String, ? extends MetaModelClassItem> e: m.entrySet()){
			e.getValue().setMetaModelClass(this);
		}
		super.putAll(m);
	}
}
