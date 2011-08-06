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
package br.ufjf.mmc.jynacore.systemdynamics.simulator.impl;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import br.ufjf.mmc.jynacore.JynaSimulationData;

public class JFreeChartDataConverter {
	public static XYDataset toXYDataSet(JynaSimulationData data) {
		XYDataset xydata = new XYSeriesCollection();
		for (int i = 0; i < data.getWatchedCount(); i++) {
			XYSeries xyserie = new XYSeries(data.getWatchedNames().get(i));
			for (int j = 0; j < data.getWatchedSize(); j++) {
				xyserie.add(data.getTime(j), data.getValue(i, j));
			}
			((XYSeriesCollection) xydata).addSeries(xyserie);

		}
		return xydata;
	}

}
