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

package br.ufjf.mmc.jynacore.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaValued;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;

/**
 * @author Knop
 * 
 */
public class DefaultSimulationData extends XYSeriesCollection implements
		JynaSimulationData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2746754084234091554L;
	// NEW @Deprecated
	// NEW private HashMap<JynaValued, XYSeries> watchList;
	private List<String> namesList;
	private List<JynaValued> valuedList;
	private List<XYSeries> seriesList;
	private List<Number> timeList;

	/**
	 * 
	 */
	public DefaultSimulationData() {
		super();
		// NEW watchList = new HashMap<JynaValued, XYSeries>();
		namesList = new ArrayList<String>();
		valuedList = new ArrayList<JynaValued>();
		seriesList = new ArrayList<XYSeries>();
		timeList = new ArrayList<Number>();
	}

	@Override
	public void add(JynaValued itemToWatch) {
		// NEW if (watchList.containsKey(itemToWatch))
		// NEW return;
		if (valuedList.contains(itemToWatch))
			return;
		XYSeries newSeries = new XYSeries(itemToWatch.getName());
		// NEW watchList.put(itemToWatch, newSeries);
		valuedList.add(itemToWatch);
		seriesList.add(newSeries);
		namesList.add(itemToWatch.getName());
		addSeries(newSeries);
	}
	@Override
	public void add(String string, JynaValued itemToWatch) {
		if (namesList.contains(string))
			return;
//NEW		if (watchList.containsKey(itemToWatch))
//NEW			return;
		XYSeries newSeries = new XYSeries(string);
//NEW		watchList.put(itemToWatch, newSeries);
		valuedList.add(itemToWatch);
		seriesList.add(newSeries);
		namesList.add(string);
		addSeries(newSeries);
	}

	@Override
	public Integer getWatchedSize() {
		return timeList.size();
		// if(getSeriesCount()<1) return 0;
		// int count = 0;
		// for (Object os : getSeries()) {
		// XYSeries s = (XYSeries) os;
		// int ic = s.getItemCount();
		// count = (ic > count) ? ic : count;
		// }
		// return getSeries(0).getItemCount();
	}

	@Override
	public void register(Double time) throws Exception {
		// NEW for (Entry<JynaValued, XYSeries> e : watchList.entrySet()) {
		// NEW e.getValue().add(time, e.getKey().getValue());
		// NEW }
		timeList.add(time);
		for (int k = 0; k < valuedList.size(); k++) {
			seriesList.get(k).add(time, valuedList.get(k).getValue());
		}

	}

	@Override
	public void remove(JynaValued itemToWatch) {
		int k = valuedList.indexOf(itemToWatch);
		valuedList.remove(k);
		removeSeries(seriesList.get(k));
		seriesList.remove(k);
		namesList.remove(k);
		// NEW removeSeries(watchList.get(itemToWatch));
		// NEW watchList.remove(itemToWatch);
	}

	@Override
	public void removeAll() {
		removeAllSeries();
		valuedList.clear();
		seriesList.clear();
		namesList.clear();
		// NEW watchList.clear();
	}

	@Override
	public Integer getWatchedCount() {
		return getSeriesCount();
	}

	@Override
	public List<String> getWatchedNames() {
		return namesList;
		// NEW List<String> l = new ArrayList<String>();
		// NEW for(JynaValued j:values){
		// NEW l.add(j.getName());
		// NEW }
		// NEW return l;

		// for(XYSeries e:watchList.values()){
		// l.add(e.getKey().toString());
		// }
		// return l;
	}

	@Override
	public Number getValue(int series, int time) {
		return this.seriesList.get(series).getY(time);
		//NEW return getY(series, time);
	}

	@Override
	public Number getTime(int time) {
		if (getSeriesCount() < 1)
			return 0;
		return getX(0, time);
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Time");
		for (String n : namesList) {
			sb.append("\t");
			sb.append(n);
		}
		sb.append("\n");
		for (int i = 0; i < getWatchedSize(); i++) {
			sb.append(getTime(i));
			for (int j = 0; j < getWatchedCount(); j++) {
				sb.append("\t");
				sb.append(getValue(j, i));
			}
			sb.append("\n");
		}
//NEW		sb.append("Time\t");
//NEW		for (String n : getWatchedNames()) {
//NEW			sb.append(n + "\t");
//NEW		}
//NEW		sb.append("\n");
//NEW		for (int i = 0; i < getWatchedSize(); i++) {
//NEW			sb.append(getTime(i) + "\t");
//NEW			for (int j = 0; j < getWatchedCount(); j++) {
//NEW				sb.append(getValue(j, i) + "\t");
//NEW			}
//NEW			sb.append("\n");
//NEW		}
		return sb.toString();
	}

	@Override
	public void clearAll() {
		for (Object oSeries : getSeries()) {
			XYSeries series = (XYSeries) oSeries;
			series.clear();
		}
		timeList.clear();

	}

	@Override
	public void addAll(Collection<JynaValued> itemsToWatch) {
		for (JynaValued jv : itemsToWatch) {
			if (jv instanceof ClassInstanceItem) {
				ClassInstanceItem cii = (ClassInstanceItem) jv;
				add(cii.getClassInstance().getName() + "." + cii.getName(), jv);
			} else
				add(jv);
		}
	}

}
