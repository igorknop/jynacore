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

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;

import br.ufjf.mmc.jynacore.systemdynamics.Information;
import br.ufjf.mmc.jynacore.systemdynamics.Rate;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsItem;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultSystemDynamicsModel;

/**
 * @author Knop
 * 
 */
@SuppressWarnings("serial")
public class SystemDynamicsGraphModel extends DefaultGraphModel {
	private static int col = 0;
	private SystemDynamicsModel model;
	private HashMap<Object, GraphCell> rootMap;
	//private HashMap<ClassInstanceRelation, GraphCell> edgeMap;

	public SystemDynamicsGraphModel(SystemDynamicsModel model) {
		this.model = model != null ? model : new DefaultSystemDynamicsModel();
		rootMap = new HashMap<Object, GraphCell>();
		updateMap();
	}

	private void updateMap() {
		col = 0;
		rootMap.clear();
		for (SystemDynamicsItem v : model.getFiniteLevels()) {
			DefaultGraphCell c = new DefaultGraphCell(v);
			GraphConstants.setBounds(c.getAttributes(), new Rectangle2D.Double(
					20 + (col % 5) * 100, 50 + (col++ / 5) * 50, 80, 40));
			GraphConstants.setOpaque(c.getAttributes(), true);
			GraphConstants.setGradientColor(c.getAttributes(), Color.GREEN);
			GraphConstants.setBorderColor(c.getAttributes(), Color.BLACK);
			GraphConstants.setBorder(c.getAttributes(), BorderFactory
					.createLineBorder(Color.BLACK));
			DefaultPort p = new DefaultPort();
			c.add(p);
			rootMap.put(v, c);
		}
		for (SystemDynamicsItem v : model.getAuxiliaries()) {
			DefaultGraphCell c = new DefaultGraphCell(v);
			GraphConstants.setBounds(c.getAttributes(), new Rectangle2D.Double(
					20 + (col % 5) * 100, 50 + (col++ / 5) * 50, 40, 40));
			GraphConstants.setOpaque(c.getAttributes(), true);
			GraphConstants.setGradientColor(c.getAttributes(), Color.BLUE);
			GraphConstants.setBorderColor(c.getAttributes(), Color.BLACK);
			GraphConstants.setBorder(c.getAttributes(), BorderFactory
					.createLineBorder(Color.BLACK));
			DefaultPort p = new DefaultPort();
			c.add(p);
			rootMap.put(v, c);
		}
		for (SystemDynamicsItem v : model.getInfiniteLevels()) {
			DefaultGraphCell c = new DefaultGraphCell(v);
			GraphConstants.setBounds(c.getAttributes(), new Rectangle2D.Double(
					20 + (col % 5) * 100, 50 + (col++ / 5) * 50, 50, 40));
			GraphConstants.setOpaque(c.getAttributes(), true);
			GraphConstants
					.setGradientColor(c.getAttributes(), Color.LIGHT_GRAY);
			GraphConstants.setBorderColor(c.getAttributes(), Color.BLACK);
			GraphConstants.setBorder(c.getAttributes(), BorderFactory
					.createLineBorder(Color.BLACK));
			DefaultPort p = new DefaultPort();
			c.add(p);
			rootMap.put(v, c);
		}
		for (Rate v : model.getRates()) {
			DefaultEdge c = new DefaultEdge(v);
			GraphConstants.setBounds(c.getAttributes(), new Rectangle2D.Double(
					20 + (col % 5) * 100, 50 + (col++ / 5) * 50, 30, 30));
			GraphConstants.setLineEnd(c.getAttributes(),
					GraphConstants.ARROW_CLASSIC);
			GraphConstants.setLineWidth(c.getAttributes(), 4);
			GraphConstants.setLineColor(c.getAttributes(), Color.GRAY);

			c.setSource(((DefaultGraphCell) rootMap.get(v.getFromLevel()))
					.getChildAt(0));
			c.setTarget(((DefaultGraphCell) rootMap.get(v.getToLevel()))
					.getChildAt(0));
			GraphConstants.setLineStyle(c.getAttributes(),
					GraphConstants.STYLE_ORTHOGONAL);
			 DefaultPort p = new DefaultPort();
//				DefaultGraphCell k = new DefaultGraphCell(v);
//				GraphConstants.setSize(c.getAttributes(), new Dimension( 20, 20));
//				GraphConstants.setOpaque(c.getAttributes(), true);
//				GraphConstants
//				.setGradientColor(c.getAttributes(), Color.RED);
				 c.add(p);
				 
				// c.add(new Shape)
//				 k.add(p);
//			 c.add(k);
			rootMap.put(v, c);
		}
		for (Information v : model.getInformations()) {
			DefaultEdge c = new DefaultEdge(v);
			GraphConstants.setBounds(c.getAttributes(), new Rectangle2D.Double(
					20 + (col % 5) * 100, 50 + (col++ / 5) * 50, 30, 30));
			GraphConstants.setLineEnd(c.getAttributes(),
					GraphConstants.ARROW_SIMPLE);
			c.setSource(((DefaultGraphCell) rootMap.get(v.getSource()))
					.getChildAt(0));
			c.setTarget(((DefaultGraphCell) rootMap.get(v.getConsumer()))
					.getChildAt(0));
			GraphConstants.setLineStyle(c.getAttributes(),
					GraphConstants.STYLE_BEZIER);
			rootMap.put(v, c);
		}
		// for (SystemDynamicsItem v : model.getObjects().values()) {
		// for (ClassInstanceItem i : v. values()) {
		// if (i instanceof ClassInstanceSingleRelation) {
		// ClassInstanceSingleRelation rel = (ClassInstanceSingleRelation) i;
		// DefaultEdge c = new DefaultEdge(rel);
		// GraphConstants.setLineEnd(c.getAttributes(),
		// GraphConstants.ARROW_SIMPLE);
		// c.setSource(((DefaultGraphCell) rootMap.get(v))
		// .getChildAt(0));
		// c.setTarget(((DefaultGraphCell) rootMap
		// .get(rel.getTarget())).getChildAt(0));
		// GraphConstants.setLineStyle(c.getAttributes(),
		// GraphConstants.STYLE_BEZIER);
		// rootMap.put(rel, c);
		// }
		// if (i instanceof ClassInstanceMultiRelation) {
		// ClassInstanceMultiRelation rel = (ClassInstanceMultiRelation) i;
		// for (ClassInstance ci : rel.getTargets().values()) {
		// MultiRelationMock mock = new MultiRelationMock(rel);
		// DefaultEdge c = new DefaultEdge(mock);
		// c.setSource(((DefaultGraphCell) rootMap.get(v))
		// .getChildAt(0));
		// c.setTarget(((DefaultGraphCell) rootMap.get(ci))
		// .getChildAt(0));
		// GraphConstants.setLineEnd(c.getAttributes(),
		// GraphConstants.ARROW_CLASSIC);
		// GraphConstants.setLineStyle(c.getAttributes(),
		// GraphConstants.STYLE_BEZIER);
		// // c.setSource(rootMap.get(v));
		// // c.setTarget(rootMap.get(ci));
		// rootMap.put(mock, c);
		// }
		// }
		// }
		// }

	}

	@Override
	public boolean isEdge(Object arg0) {
		if (arg0 instanceof Rate)
			return true;
		if (arg0 instanceof Information)
			return true;
		 if (arg0 instanceof GraphCell
				 && ((DefaultGraphCell) arg0).getUserObject() instanceof
				 Rate)
				 return true;
		 if (arg0 instanceof GraphCell
				 && ((DefaultGraphCell) arg0).getUserObject() instanceof
				 Information)
				 return true;

		return false;
	}

	@Override
	public int getRootCount() {
		return rootMap.size();
	}

	@Override
	public List<GraphCell> getRoots() {
		return new ArrayList<GraphCell>(rootMap.values());
	}

	@Override
	public Object getRootAt(int arg0) {
		return new ArrayList<GraphCell>(rootMap.values()).get(arg0);
	}

	// class MultiRelationMock implements ClassInstanceRelation{
	// private ClassInstanceMultiRelation multiRelation;
	// public MultiRelationMock(ClassInstanceMultiRelation multiRelation) {
	// this.multiRelation = multiRelation;
	// }
	// @Override
	// public String toString() {
	// return multiRelation.toString();
	// }
	// public List<MultiRelationMock> getAll(){
	// List<MultiRelationMock> l= new ArrayList<MultiRelationMock>();
	// l.add(new MultiRelationMock(multiRelation));
	// return l;
	// }
	// @Override
	// public ClassInstance getClassInstance() {
	// return multiRelation.getClassInstance();
	// }
	// @Override
	// public String getName() {
	// return multiRelation.getName();
	// }
	// @Override
	// public void setClassInstance(ClassInstance newClassInstance) {
	// multiRelation.setClassInstance(newClassInstance);
	// }
	// @Override
	// public void setName(String Name) {
	// multiRelation.setName(Name);
	// }
	// }
}
