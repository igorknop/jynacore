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
package br.ufjf.mmc.jynacore.metamodel.simulator;

import br.ufjf.mmc.jynacore.JynaSimulationMethod;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;

/**
 * @author Knop
 * 
 */
public interface MetaModelInstanceSimulationMethod extends JynaSimulationMethod {
	/**
	 * Sets model to be simulated
	 * 
	 * @param modelInstance
	 * @throws Exception
	 */
	void setMetaModelInstance(MetaModelInstance modelInstance) throws Exception;

	/**
	 * Gets model instance to be simulated
	 * @return {@link MetaModelInstance}
	 */
	MetaModelInstance getModelInstance();

	/**
	 * Do all calculations to advance in time
	 * 
	 * @throws Exception
	 */
	void step() throws Exception;

	/**
	 * Set the time step size
	 * 
	 * @param newStepSize
	 */
	void setStepSize(Double newStepSize);

	/**
	 * Get the time step size
	 * 
	 * @return
	 */
	Double getStepSize();

	/**
	 * Set initial time shift of simulation
	 * 
	 * @param newInitialTime
	 */
	void setInitialTime(Double newInitialTime);

	/**
	 * Set initial time shift of simulation
	 * 
	 * @return
	 */
	Double getInitialTime();

	/**
	 * Get current time
	 * 
	 * @return
	 */
	Double getTime();

	/**
	 * Reset model simulated values
	 * 
	 * @throws Exception
	 */
	void reset() throws Exception;

}
