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
package br.ufjf.mmc.jynacore;

/**
 * Basic model simulation behavior. Usages:
 * <ul>
 * <li>Restores the model's state to the one defined in design time.</li>
 * <li>Restores the model's state to the one stored in file model.</li>
 * <li>Create a full, time-finite length {@link JynaSimulationData} with fixed
 * model and no chance for user interaction based on the current
 * {@link JynaSimulationProfile}</li>
 * <li>Create a single step {@link JynaSimulationData} due a time advance to
 * current model's state and composition until reach the
 * {@link JynaSimulationProfile} limits.</li>
 * <li>Create a single step {@link JynaSimulationData} due a time advance to
 * current model's state and composition without stoping on reach the
 * {@link JynaSimulationProfile} limits.</li>
 * </ul>
 * 
 * @author Knop
 * 
 */
public interface JynaSimulation {
	/**
	 * Sets model to be simulated
	 * 
	 * @param model
	 * @throws Exception
	 */
	void setModel(JynaSimulableModel newModel) throws Exception;

	/**
	 * Get model to be simulated
	 * 
	 * @return Model
	 */
	JynaSimulableModel getModel();

	/**
	 * Set simulation method
	 * 
	 * @param newMethod
	 * @throws Exception
	 */
	void setMethod(JynaSimulationMethod newMethod) throws Exception;

	/**
	 * Get simulation method
	 * 
	 * @return
	 */
	JynaSimulationMethod getMethod();


	/**
	 * Run one step of simulation simulation
	 * 
	 * @throws Exception
	 */
	void step() throws Exception;

	/**
	 * Run the full simulation
	 * 
	 * @throws Exception
	 */
	void run() throws Exception;

	/**
	 * Load all default values defined in design time. Clear the current model
	 * state.
	 * 
	 * @throws Exception
	 */
	void reset() throws Exception;


	/**
	 * Sets SimulationData to store data
	 * 
	 * @param sdata
	 */
	void setSimulationData(JynaSimulationData sdata);

	/**
	 * Get SimulationData
	 * 
	 * @return
	 */
	JynaSimulationData getSimulatonData();


	/**
	 * Register current state in SimulationData
	 * @throws Exception 
	 */
	void register() throws Exception;


	Double getTime();

	void setProfile(JynaSimulationProfile profile);
	
	JynaSimulationProfile getProfile();

}
