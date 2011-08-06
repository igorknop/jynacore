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
package br.ufjf.mmc.jynacore.gui;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import br.ufjf.mmc.jynacore.JynaSimulationData;

public class SimulationDataTableModel extends DefaultTableModel implements
		TableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1661729322995265296L;
	private JynaSimulationData data;

	public SimulationDataTableModel(JynaSimulationData data) {
		this.data = data;

	}

	public SimulationDataTableModel() {
	}

	@Override
	public int getColumnCount() {
		if(data == null) return 1;
//		System.out.println(data.getWatchedCount()+1);
		return data.getWatchedCount() + 1;
	}

	@Override
	public String getColumnName(int arg0) {
		if (arg0 == 0)
			return "Time";
		return data.getWatchedNames().get(arg0 - 1);
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		// List<String> l = new ArrayList<String>(data.getKeySet());
		// l.get(arg0);
		// return super.getColumnClass(arg0);
		return Double.class;
	}

	@Override
	public int getRowCount() {
		if (data == null)
			return 0;
		return data.getWatchedSize();
	}

	@Override
	public Object getValueAt(int time, int column) {
		if (column > 0)
			return data.getValue(column - 1, time);
		else
			return data.getTime(time);
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}
	
	public void setData(JynaSimulationData data) {
		this.data = data;
	}
}
