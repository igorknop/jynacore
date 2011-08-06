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
package br.ufjf.mmc.jynacore.systemdynamics.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import br.ufjf.mmc.jynacore.JynaValued;
import br.ufjf.mmc.jynacore.systemdynamics.Auxiliary;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.InfiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.Information;
import br.ufjf.mmc.jynacore.systemdynamics.Rate;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsItem;

public class DefaultSystemDynamicsModel implements SystemDynamicsModel {

	private Map<String, SystemDynamicsItem> objects;
	private Set<FiniteStock> finiteLevels;
	private Set<Rate> rates;
	private Set<InfiniteStock> infiniteLevels;
	private Set<Auxiliary> auxiliars;
	private Set<Information> informations;
	private String modelName;

	public DefaultSystemDynamicsModel() {
		objects = new HashMap<String, SystemDynamicsItem>();
		finiteLevels = new HashSet<FiniteStock>();
		infiniteLevels = new HashSet<InfiniteStock>();
		rates = new HashSet<Rate>();
		auxiliars = new HashSet<Auxiliary>();
		informations = new HashSet<Information>();
		modelName = "Simple Dynamic Model";
	}

	@Override
	public void put(SystemDynamicsItem sdObject) {
		put(sdObject.getName(), sdObject);
	}

	@Override
	public void put(String key, SystemDynamicsItem sdObject) {
		if (getObjectByName(sdObject).isEmpty()) {
			sdObject.setName(key);
			objects.put(key, sdObject);
			if (sdObject instanceof FiniteStock) {
				finiteLevels.add((FiniteStock) sdObject);
			}
			if (sdObject instanceof Rate) {
				rates.add((Rate) sdObject);
			}
			if (sdObject instanceof InfiniteStock) {
				infiniteLevels.add((InfiniteStock) sdObject);
			}
			if (sdObject instanceof Auxiliary) {
				auxiliars.add((Auxiliary) sdObject);
			}
			if (sdObject instanceof Information) {
				informations.add((Information) sdObject);
			}
		}
	}

	@Override
	public SystemDynamicsItem get(String key) {
		return objects.get(key);
	}

	@Override
	public String getObjectByName(SystemDynamicsItem sdObject) {
		String tempKey = "";
		if (objects.containsValue(sdObject)) {
			for (String key : objects.keySet()) {
				if (objects.get(key).equals(sdObject))
					tempKey = key;
			}
		}
		return tempKey;
	}

	@Override
	public Map<String, SystemDynamicsItem> getItems() {
		return objects;
	}

	@Override
	public void setItems(Map<String, SystemDynamicsItem> newObjects) {
		this.objects = newObjects;
	}

	@Override
	public Set<FiniteStock> getFiniteLevels() {
		return finiteLevels;
	}

	@Override
	public Set<InfiniteStock> getInfiniteLevels() {
		return infiniteLevels;
	}

	@Override
	public Set<Rate> getRates() {
		return rates;
	}

	@Override
	public Set<Auxiliary> getAuxiliaries() {
		return auxiliars;
	}

	@Override
	public Set<Information> getInformations() {
		return informations;
	}

	@Override
	public String toString() {
		return getName();

		/*
		 * StringBuilder sb = new StringBuilder(super.toString());
		 * sb.append("Model:" + "\n"); sb.append("Finite Levels:" +
		 * finiteLevels.size() + "\n\t"); for (FiniteStock item : finiteLevels)
		 * { sb.append(item.getName() + "\n\t\t");
		 * sb.append(item.getInitialValue() + "\n\t\t");
		 * sb.append(item.getValue() + "\n\t"); } sb.append("Rates:" +
		 * rates.size() + "\n\t"); for (Rate item : rates) {
		 * sb.append(item.getName() + "\n\t\t"); sb.append("Source:
		 * "+(item.getFromLevel()==null?"
		 * empty?":item.getFromLevel().getName())+"\n\t\t"); sb.append("Target:
		 * "+(item.getToLevel()==null?"
		 * empty?":item.getToLevel().getName())+"\n\t\t"); sb.append("exp: " +
		 * item.getExpression() + "\n\t\t"); sb.append("informations: "); for
		 * (String k : item.getInformations().keySet()) { sb.append(k + " "); }
		 * sb.append("\n\t\t"); sb.append(item.getValue() + "\n\t"); }
		 * sb.append("Auxiliars:" + auxiliars.size() + "\n\t"); for (Auxiliary
		 * item : auxiliars) { sb.append(item.getName() + "\n\t\t");
		 * sb.append("exp: " + ((Variable)item).getExpression() + "\n\t\t");
		 * sb.append("informations: "); if (item instanceof Variable) { for
		 * (String k : ((Variable)item).getInformations().keySet()) {
		 * sb.append(k + " "); } sb.append("\n\t\t"); }
		 * sb.append(item.getValue() + "\n\t"); } return sb.toString();
		 */}

	@Override
	public String getName() {
		return modelName;
	}

	@Override
	public void setName(String newName) {
		modelName = newName;
	}

	@Override
	public Collection<JynaValued> getAllJynaValued() {
		List<JynaValued> l = new ArrayList<JynaValued>();
		for (Entry<String, SystemDynamicsItem> entry : getItems().entrySet()) {
			if (entry.getValue() instanceof JynaValued) {
				l.add((JynaValued) entry.getValue());
			}
		}
		return l;
	}

}
