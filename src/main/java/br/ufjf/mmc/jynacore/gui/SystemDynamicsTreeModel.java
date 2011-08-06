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
package br.ufjf.mmc.jynacore.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;

/**
 * @author Knop
 * 
 */
public class SystemDynamicsTreeModel implements TreeModel {

	private static final String META_MODEL = "Meta Model";
	private static final String MODEL_INSTANCE = "Model Instance";
	private MetaModelInstance model;
	private List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	public SystemDynamicsTreeModel(MetaModelInstance model) {
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	@Override
	public void addTreeModelListener(TreeModelListener arg0) {
		listeners.add(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	@Override
	public Object getChild(Object parent, int index) {
		if (parent == model) {
			switch (index) {
			case 0:
				return META_MODEL;
			case 1:
				return MODEL_INSTANCE;
			default:
				return null;
			}

		}
		if (parent == META_MODEL) {
			List<Object> l = Arrays.asList(model.getMetaModel().values()
					.toArray());
			return l.get(index);
		}
		if (parent == MODEL_INSTANCE) {
			List<Object> l = Arrays.asList(model.getClassInstances().values()
					.toArray());
			return l.get(index);
		}
		if(parent instanceof ClassInstance){
			ClassInstance ci = (ClassInstance) parent;
			List<Object> l = Arrays.asList(ci.values()
					.toArray());
			return l.get(index);
			
		}
		if(parent instanceof MetaModelClass){
			MetaModelClass mmc = (MetaModelClass) parent;
			List<Object> l = Arrays.asList(mmc.values()
					.toArray());
			return l.get(index);
			
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	@Override
	public int getChildCount(Object parent) {
		if (parent == model) {
			return 2;
		} else if (parent == META_MODEL) {
			return model.getMetaModel().size();
		} else if (parent == MODEL_INSTANCE) {
			return model.getClassInstances().size();
		}
		if(parent instanceof ClassInstance){
			ClassInstance ci = (ClassInstance) parent;
			return ci.size();
			
		}
		if(parent instanceof MetaModelClass){
			MetaModelClass mmc = (MetaModelClass) parent;
			return mmc.size();
			
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent == model) {
			if (child == META_MODEL)
				return 0;
			if (child == MODEL_INSTANCE)
				return 1;
		}

		else if (parent == META_MODEL) {
			List<Object> l = Arrays.asList((model.getMetaModel().values())
					.toArray());
			return l.indexOf(child);

		} else if (parent == MODEL_INSTANCE) {
			List<Object> l = Arrays.asList((model.getClassInstances().values())
					.toArray());
			return l.indexOf(child);
		}
		if(parent instanceof ClassInstance){
			ClassInstance ci = (ClassInstance) parent;
			List<Object> l = Arrays.asList(ci.values()
					.toArray());
			return l.indexOf(child);
		}
		if(parent instanceof MetaModelClass){
			MetaModelClass mmc = (MetaModelClass) parent;
			List<Object> l = Arrays.asList(mmc.values()
					.toArray());
			return l.indexOf(child);
		}

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	@Override
	public Object getRoot() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	@Override
	public boolean isLeaf(Object node) {
		if (node == model)
			return false;
		if (node == META_MODEL) {
			return model.getMetaModel().size() == 0;
		}
		if (node == MODEL_INSTANCE) {
			return model.getClassInstances().size() == 0;
		}
		if(node instanceof ClassInstance){
			ClassInstance ci = (ClassInstance) node;
			return ci.size()==0;
		}
		if(node instanceof MetaModelClass){
			MetaModelClass mmc = (MetaModelClass) node;
			return mmc.size()==0;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	@Override
	public void removeTreeModelListener(TreeModelListener arg0) {
		listeners.remove(arg0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath,
	 *      java.lang.Object)
	 */
	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		//super.valueForPathChanged(arg0,arg1);
	}

}
