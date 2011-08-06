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

import java.util.ArrayList;
import java.util.List;

import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceTable;

/**
 * @author Knop
 *
 */
public class DefaultClassInstanceTable implements ClassInstanceTable {

	private List<Double> values;
	private ClassInstance classInstance;
	private String name;
	public DefaultClassInstanceTable() {
		values = new ArrayList<Double>();
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.inst.ClassInstanceTable#getvalues()
	 */
	@Override
	public List<Double> getValues() {
		return values;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.inst.ClassInstanceTable#setValues(java.util.List)
	 */
	@Override
	public void setValues(List<Double> newValues) {
		values = newValues;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem#getClassInstance()
	 */
	@Override
	public ClassInstance getClassInstance() {
		return classInstance;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem#setClassInstance(br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance)
	 */
	@Override
	public void setClassInstance(ClassInstance newClassInstance) {
		classInstance = newClassInstance;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem#setName(java.lang.String)
	 */
	@Override
	public void setName(String newName) {
		name = newName;
	}
	@Override
	public String toString() {
		return getName();
	}

}
