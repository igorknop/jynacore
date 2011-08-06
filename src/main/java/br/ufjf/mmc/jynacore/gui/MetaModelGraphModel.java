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

import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceSingleRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstance;

/**
 * @author Knop
 * 
 */
@SuppressWarnings("serial")
public class MetaModelGraphModel extends DefaultGraphModel {
	private static int col = 0;
	private MetaModelInstance model;
	private HashMap<Object, GraphCell> rootMap;
	//private HashMap<ClassInstanceRelation, GraphCell> edgeMap;

	public MetaModelGraphModel(MetaModelInstance model) {
		this.model = model != null ? model : new DefaultMetaModelInstance();
		rootMap = new HashMap<Object, GraphCell>();
		updateMap();
	}

	private void updateMap() {
		col = 0;
		
		for (ClassInstance v : model.getClassInstances().values()) {
			DefaultGraphCell c = new DefaultGraphCell(v);
			GraphConstants.setBounds(c.getAttributes(), new Rectangle2D.Double(
					20+(col%5 )*100, 50+(col++/5)*50, 80, 30));
			GraphConstants.setOpaque(c.getAttributes(), true);
			GraphConstants.setGradientColor(c.getAttributes(), Color.GREEN);
			GraphConstants.setBorderColor(c.getAttributes(), Color.BLACK);
			GraphConstants.setBorder(c.getAttributes(), BorderFactory.createLineBorder(Color.BLACK));
			DefaultPort p = new DefaultPort();
			c.add(p);
			rootMap.put(v, c);
		}
		for (ClassInstance v : model.getClassInstances().values()) {
			for (ClassInstanceItem i : v.values()) {
				if (i instanceof ClassInstanceSingleRelation) {
					ClassInstanceSingleRelation rel = (ClassInstanceSingleRelation) i;
					DefaultEdge c = new DefaultEdge(rel);
					GraphConstants.setLineEnd(c.getAttributes(), GraphConstants.ARROW_SIMPLE);
					c.setSource(((DefaultGraphCell) rootMap.get(v))
							.getChildAt(0));
					c.setTarget(((DefaultGraphCell) rootMap
							.get(rel.getTarget())).getChildAt(0));
					GraphConstants.setLineStyle(c.getAttributes(), GraphConstants.STYLE_BEZIER);
					rootMap.put(rel, c);
				}
				if (i instanceof ClassInstanceMultiRelation) {
					ClassInstanceMultiRelation rel = (ClassInstanceMultiRelation) i;
					for (ClassInstance ci : rel.getTargets().values()) {
						MultiRelationMock mock = new MultiRelationMock(rel);
						DefaultEdge c = new DefaultEdge(mock);
						c.setSource(((DefaultGraphCell) rootMap.get(v))
								.getChildAt(0));
						c.setTarget(((DefaultGraphCell) rootMap.get(ci))
								.getChildAt(0));
						GraphConstants.setLineEnd(c.getAttributes(), GraphConstants.ARROW_CLASSIC);
						GraphConstants.setLineStyle(c.getAttributes(), GraphConstants.STYLE_BEZIER);
						// c.setSource(rootMap.get(v));
						// c.setTarget(rootMap.get(ci));
						rootMap.put(mock, c);
					}
				}
			}
		}

	}

	@Override
	public boolean isEdge(Object arg0) {
		if (arg0 instanceof ClassInstanceRelation)
			return true;
		if (arg0 instanceof GraphCell
				&& ((DefaultGraphCell) arg0).getUserObject() instanceof ClassInstanceRelation)
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

	class MultiRelationMock implements ClassInstanceRelation{
		private ClassInstanceMultiRelation multiRelation;
		public MultiRelationMock(ClassInstanceMultiRelation multiRelation) {
			this.multiRelation = multiRelation;
		}
		@Override
		public String toString() {
			return multiRelation.toString();
		}
		public  List<MultiRelationMock> getAll(){
			List<MultiRelationMock> l= new ArrayList<MultiRelationMock>();
			l.add(new MultiRelationMock(multiRelation));
			return l;
		}
		@Override
		public ClassInstance getClassInstance() {
			return multiRelation.getClassInstance();
		}
		@Override
		public String getName() {
			return multiRelation.getName();
		}
		@Override
		public void setClassInstance(ClassInstance newClassInstance) {
			multiRelation.setClassInstance(newClassInstance);
		}
		@Override
		public void setName(String Name) {
			multiRelation.setName(Name);			
		}
	}
}

