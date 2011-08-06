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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import br.ufjf.mmc.jynacore.JynaItem;
import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaValued;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;

/**
 * @author Knop
 * 
 */
public class DefaultSimulationData2 implements JynaSimulationData {
	private List<Map<Integer, Number>> seriesList = new ArrayList<Map<Integer, Number>>();
	private List<String> watchedNames = new ArrayList<String>();
	private List<JynaValued> watchedList = new ArrayList<JynaValued>();
	private List<Number> timeList = new ArrayList<Number>();

	@Override
	public void add(JynaValued itemToWatch) {
		if (!seriesList.contains(itemToWatch)) {
			Map<Integer, Number> series = new HashMap<Integer, Number>();
			seriesList.add(series);
			watchedNames.add(itemToWatch.getName());
			watchedList.add(itemToWatch);
		}
	}

	@Override
	public void add(String name, JynaValued value) {
		if (!watchedNames.contains(name)) {
			Map<Integer, Number> series = new HashMap<Integer, Number>();
			seriesList.add(series);
			watchedNames.add(name);
			watchedList.add(value);
		}
	}

	@Override
	public void addAll(Collection<JynaValued> itemsToWatch) {
		for (JynaValued item : itemsToWatch) {
			add(item);
		}
	}

	@Override
	public void clearAll() {
		seriesList.clear();
		timeList.clear();
	}

	@Override
	public Number getTime(int time) {
		return timeList.get(time);
	}

	@Override
	public Number getValue(int series, int time) {
		return seriesList.get(series).get(timeList.get(time));
	}

	@Override
	public Integer getWatchedCount() {
		return seriesList.size();
	}

	@Override
	public List<String> getWatchedNames() {
		return watchedNames;
	}

	@Override
	public Integer getWatchedSize() {
		return timeList.size();
	}

	@Override
	public void register(Double time) throws Exception {
		timeList.add(time);
		Integer k = timeList.size() - 1;
		for (int w = 0; w < watchedList.size(); w++) {
			seriesList.get(w).put(k, watchedList.get(w).getValue());
		}

	}

	@Override
	public void remove(JynaValued itemToWatch) {
		int k = watchedList.indexOf(itemToWatch);
		watchedList.remove(k);
		seriesList.remove(k);
		watchedNames.remove(k);
	}

	@Override
	public void removeAll() {
		watchedList.clear();
		seriesList.clear();
		watchedNames.clear();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Time");
		for (String s : watchedNames) {
			sb.append("\t");
			sb.append(s);
		}
		sb.append("\n");
		for (int t = 0; t < timeList.size(); t++) {
			sb.append(timeList.get(t));
			for (int w = 0; w < watchedList.size(); w++) {
				sb.append("\t");
				sb.append(seriesList.get(w).get(t));
			}
			sb.append("\n");
		}
		return super.toString();
	}
}
