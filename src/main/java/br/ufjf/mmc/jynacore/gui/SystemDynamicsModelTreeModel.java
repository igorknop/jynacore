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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;

/**
 * @author Knop
 * 
 */
public class SystemDynamicsModelTreeModel implements TreeModel {
	
	private static final String INFO = "Information";
	private static final String INFINITE = "Infinite";
	private static final String AUX = "Auxiliary";
	private static final String RATE = "Rate";
	private static final String LEVEL = "Stock";
	private static final Set<String> modelParts = new HashSet<String>();
	private SystemDynamicsModel model;
	private List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	public SystemDynamicsModelTreeModel(SystemDynamicsModel model) {
		this.model = model;
		String[] s = {LEVEL,RATE,AUX,INFINITE,INFO};
		modelParts.addAll(Arrays.asList(s));
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
		if (parent == model){
			switch (index) {
			case 0:
				return LEVEL;
			case 1:
				return RATE;
			case 2:
				return AUX;
			case 3:
				return INFINITE;
			case 4:
				return INFO;
			default:
				return null;
			}
			
		}
		if(parent == LEVEL){
			 List<Object> l = Arrays.asList(model.getFiniteLevels().toArray());
			return l.get(index);
		}
		if(parent == INFINITE){
			 List<Object> l = Arrays.asList(model.getInfiniteLevels().toArray());
				return l.get(index);
		}
		if(parent == RATE){
			 List<Object> l = Arrays.asList(model.getRates().toArray());
				return l.get(index);
		}
		if(parent == AUX){
			 List<Object> l = Arrays.asList(model.getAuxiliaries().toArray());
				return l.get(index);
		}
		if(parent == INFO){
			 List<Object> l = Arrays.asList(model.getInformations().toArray());
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
			return 5;
		}
		if(parent == LEVEL){
			return model.getFiniteLevels().size();
		}
		if(parent == INFINITE){
			return model.getInfiniteLevels().size();
		}
		if(parent == RATE){
			return model.getRates().size();
		}
		if(parent == AUX){
			return model.getAuxiliaries().size();
		}
		if(parent == INFO){
			return model.getInformations().size();
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
		if(parent==model){
			if(child==LEVEL) return 0;
			if(child==RATE) return 1;
			if(child==AUX) return 2;
			if(child==INFINITE) return 3;
			if(child==INFO) return 4;
		}
		if(parent == LEVEL){
			List<Object> l = Arrays.asList(model.getFiniteLevels().toArray());
			return l.indexOf(child);
		}
		if(parent == INFINITE){
			return model.getInfiniteLevels().size();
		}
		if(parent == RATE){
			return model.getRates().size();
		}
		if(parent == AUX){
			return model.getAuxiliaries().size();
		}
		if(parent == INFO){
			return model.getInformations().size();
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
//		if (modelParts.contains(node)) {
//			// SystemDynamicsItem new_name = (//SystemDynamicsItem) arg0;
//			return true;
//		}
		if (node ==model) return false;
		if(node == LEVEL){
			return model.getFiniteLevels().size()==0;
		}
		if(node == INFINITE){
			return model.getInfiniteLevels().size()==0;
		}
		if(node == RATE){
			return model.getRates().size()==0;
		}
		if(node == AUX){
			return model.getAuxiliaries().size()==0;
		}
		if(node == INFO){
			return model.getInformations().size()==0;
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
