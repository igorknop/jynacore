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
package br.ufjf.mmc.jynacore;

/**
 * Define main simulation paramenters
 * 
 * @author Knop
 * 
 */
/**
 * @author Knop
 * 
 */
public interface JynaSimulationProfile {
	/**
	 * Profile name
	 * 
	 * @param newName
	 */
	void setName(String newName);

	/**
	 * Profile name
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Set time limits. Calculate the lowest iteractions number to reach the
	 * time limit.
	 * 
	 * @param initialTime
	 *            the initial time of simulation. Time unit.
	 * @param finalTime
	 *            the final time of simulation. Time unit.
	 * @param timeIterval
	 *            the initial time of simulation. Time unit.
	 */
	void setTimeLimits(Double initialTime, Double finalTime, Double timeIterval);

	/**
	 * Set time limits. Calculate the time interval number to reach the time
	 * limit in given number of iteractions.
	 * 
	 * @param initialTime
	 *            the initial time of simulation. Time unit.
	 * @param finalTime
	 *            the final time of simulation. Time unit.
	 * @param timeSteps
	 *            the total number of iterations. No unit.
	 */
	void setTimeLimits(Double initialTime, Double finalTime, Integer timeSteps);

	/**
	 * Set time limits. Calculate the final time basead on given iteractions and
	 * time interval. A time 0 is assumed as initial time.
	 * 
	 * @param timeIterval
	 *            the initial time of simulation. Time unit.
	 * @param timeSteps
	 *            the total number of iterations. No unit.
	 */
	void setTimeLimits(Double timeInterval, Integer timeSteps);

	/**
	 * Set time limits. Calculates the time interval to reach final time in a
	 * given number of iterations. A time 0 is assumed as initial time.
	 * 
	 * @param timeIterval
	 *            the initial time of simulation. Time unit.
	 * @param timeSteps
	 *            the total number of iterations. No unit.
	 */
	void setTimeLimits(Integer timeSteps, Double finalTime);

	/**
	 * Get simulation initial time
	 * 
	 * @return initial time
	 */
	Double getInitialTime();

	/**
	 * Set simulation initial time
	 * 
	 * @param newInitialTime
	 */
	void setInitialTime(Double newInitialTime);

	/**
	 * Get simulation final time
	 * 
	 * @return final time
	 */
	Double getFinalTime();

	/**
	 * Set simulation final time
	 * 
	 * @param newFinalTime
	 */
	void setFinalTime(Double newFinalTime);

	/**
	 * Get number of time steps in simulation
	 * 
	 * @return number of time steps
	 */
	Integer getTimeSteps();

	/**
	 * Set number of time steps in simulation
	 * 
	 * @param newTimeSteps
	 */
	void setTimeSteps(Integer newTimeSteps);

	/**
	 * Get time interval between intervals
	 * 
	 * @return time interval
	 */
	Double getTimeInterval();

	/**
	 * Set time interval between intervals
	 * 
	 * @param newTimeInterval
	 */
	void setTimeInterval(Double newTimeInterval);
}
