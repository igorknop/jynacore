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

import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;

public class TextModelViewer {
	private SystemDynamicsModel model;
	public void setModel(SystemDynamicsModel model){
		this.model = model;
	}
	public SystemDynamicsModel getModel(){
		return model;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Model\n");
		return sb.toString();
	}

}
