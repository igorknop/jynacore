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
import br.ufjf.mmc.jynacore.metamodel.MetaModelSingleRelation;

/**
 * @author Knop
 *
 */
public class DefaultMetaModelSingleRelation implements MetaModelSingleRelation {

	private MetaModelClass source;
	private MetaModelClass target;
	private String targetRole;
	private String name;
	//private String relationName;
	private MetaModel metaModel;

	/**
	 * 
	 */
	public DefaultMetaModelSingleRelation() {
	
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelSingleRelation#getSource()
	 */
	@Override
	public MetaModelClass getSource() {
		return source;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelSingleRelation#getTarget()
	 */
	@Override
	public MetaModelClass getTarget() {
		return target;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelSingleRelation#getTargetRole()
	 */
	@Override
	public String getTargetRole() {
		return targetRole;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelSingleRelation#setSource(br.ufjf.mmc.jynacore.metamodel.MetaModelClass)
	 */
	@Override
	public void setSource(MetaModelClass newSource) {
		source = newSource;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelSingleRelation#setTarget(br.ufjf.mmc.jynacore.metamodel.MetaModelClass)
	 */
	@Override
	public void setTarget(MetaModelClass newTarget) {
		target = newTarget;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelSingleRelation#setTargetRole(java.lang.String)
	 */
	@Override
	public void setTargetRole(String newTargetRole) {
		targetRole = newTargetRole;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.MetaModelItem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
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
