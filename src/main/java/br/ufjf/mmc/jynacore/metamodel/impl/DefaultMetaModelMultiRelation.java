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

import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation;

/**
 * @author Knop
 * 
 */
public class DefaultMetaModelMultiRelation implements MetaModelMultiRelation {

	private String name;
	private String targetRole;
	private MetaModelClass source;
	private MetaModelClass target;
	//private String relationName;
	private MetaModel metaModel;

	/**
	 * 
	 */
	public DefaultMetaModelMultiRelation() {
	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation#getSource()
	 */
	@Override
	public MetaModelClass getSource() {
		return source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation#getTarget()
	 */
	@Override
	public MetaModelClass getTarget() {
		return target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation#getTargetRole()
	 */
	@Override
	public String getTargetRole() {
		return targetRole;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation#setSource(br.ufjf.mmc.jynacore.metamodel.MetaModelClass)
	 */
	@Override
	public void setSource(MetaModelClass newSource) {
		source = newSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation#setTarget(br.ufjf.mmc.jynacore.metamodel.MetaModelClass)
	 */
	@Override
	public void setTarget(MetaModelClass newTarget) {
		target = newTarget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation#setTargetRole(java.lang.String)
	 */
	@Override
	public void setTargetRole(String newTargetRole) {
		targetRole = newTargetRole;
	}

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
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelItem#setName(java.lang.String)
	 */
	@Override
	public void setName(String newName) {
		name = newName;

	}

	@Override
	public String getRelationName() {
		return name;
	}

	@Override
	public void setRelationName(String newName) {
		name = newName;
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
	public String toString() {
		return getName();
	}

}
