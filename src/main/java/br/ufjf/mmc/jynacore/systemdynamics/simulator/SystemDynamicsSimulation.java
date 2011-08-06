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
package br.ufjf.mmc.jynacore.systemdynamics.simulator;

import br.ufjf.mmc.jynacore.JynaSimulation;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
/**
 * A single System Dynamics Simulation
 * @author Knop
 *
 */
public interface SystemDynamicsSimulation extends JynaSimulation{
	
	/**
	 * Sets model to be simulated
	 * @param model
	 * @throws Exception 
	 */
	void setModel(SystemDynamicsModel model) throws Exception;
	
	/**
	 * Get model to be simulated
	 * @retunr model
	 */
	SystemDynamicsModel getModel();
	
	/**
	 * Set simulation method
	 * @param newMethod
	 */
	void setMethod(SystemDynamicsSimulationMethod newMethod);

	/**
	 * Get simulation method
	 * @return
	 */
	SystemDynamicsSimulationMethod  getMethod();
}
