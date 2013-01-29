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
package br.ufjf.mmc.jynacore.impl;

import br.ufjf.mmc.jynacore.JynaSimulationProfile;

/**
 * @author Knop
 * 
 */
public class DefaultSimulationProfile implements JynaSimulationProfile {

	private String name;
	private Double initialTime;
	//private Double finalTime;
	private Double timeInterval;
	private Integer stepCount;

	public DefaultSimulationProfile() {
		name = "Default profile";
		this.stepCount = 100;
		this.timeInterval = 100.0 / stepCount;
		this.initialTime = 0.0;
      
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.JynaSimulationProfile#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.JynaSimulationProfile#setName(java.lang.String)
	 */
	@Override
	public void setName(String newName) {
		name = newName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.JynaSimulationProfile#setTimeLimits(java.lang.Double
	 * , java.lang.Double, java.lang.Double)
	 */
	@Override
	public void setTimeLimits(Double initialTime, Double finalTime,
			Double timeInterval) {
		this.initialTime = initialTime;
		//this.finalTime = finalTime;
		this.timeInterval = timeInterval;
		this.stepCount = (int) ((finalTime - initialTime) / timeInterval);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.JynaSimulationProfile#setTimeLimits(java.lang.Double
	 * , java.lang.Double, java.lang.Integer)
	 */
	@Override
	public void setTimeLimits(Double initialTime, Double finalTime,
			Integer timeSteps) {
		this.initialTime = initialTime;
		//this.finalTime = finalTime;
		this.timeInterval = ((finalTime - initialTime) / timeSteps);
		this.stepCount = timeSteps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.JynaSimulationProfile#setTimeLimits(java.lang.Double
	 * , java.lang.Integer)
	 */
	@Override
	public void setTimeLimits(Double timeInterval, Integer timeSteps) {
		this.timeInterval = timeInterval;
		this.stepCount = timeSteps;
		this.initialTime = 0.0;
		//this.finalTime = timeInterval * timeSteps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.JynaSimulationProfile#setTimeLimits(java.lang.Integer
	 * , java.lang.Double)
	 */
	@Override
	public void setTimeLimits(Integer timeSteps, Double finalTime) {
		this.stepCount = timeSteps;
		//this.finalTime = finalTime;
		this.timeInterval = finalTime / timeSteps;
		this.initialTime = 0.0;
	}

	@Override
	public Double getFinalTime() {
		return initialTime+timeInterval*stepCount;
	}

	@Override
	public Double getInitialTime() {
		return initialTime;
	}

	@Override
	public Double getTimeInterval() {
		return timeInterval;
	}

	@Override
	public Integer getTimeSteps() {
		return stepCount;
	}

	@Override
	public void setFinalTime(Double newFinalTime) {
		stepCount = (int)((newFinalTime-initialTime)/timeInterval);
		
	}

	@Override
	public void setInitialTime(Double newInitialTime) {
		initialTime = newInitialTime;
	}

	@Override
	public void setTimeInterval(Double newTimeInterval) {
		timeInterval = newTimeInterval;
	}

	@Override
	public void setTimeSteps(Integer newTimeSteps) {
		stepCount = newTimeSteps;
	}

}
