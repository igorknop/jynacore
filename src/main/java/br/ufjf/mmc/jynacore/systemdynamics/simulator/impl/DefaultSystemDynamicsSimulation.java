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

import br.ufjf.mmc.jynacore.JynaSimulableModel;
import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaSimulationMethod;
import br.ufjf.mmc.jynacore.JynaSimulationProfile;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulation;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod;

/**
 * @author Knop
 * 
 */
public class DefaultSystemDynamicsSimulation implements
		SystemDynamicsSimulation {

	private SystemDynamicsSimulationMethod method;
	private SystemDynamicsModel model;
	private int steps;
	private JynaSimulationData data;
	private JynaSimulationProfile profile;

	public DefaultSystemDynamicsSimulation() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulation
	 * #getTime()
	 */
	@Override
	public Double getTime() {
		return method.getTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulation
	 * #getMethod()
	 */
	@Override
	public SystemDynamicsSimulationMethod getMethod() {
		return method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulation
	 * #getModel()
	 */
	@Override
	public SystemDynamicsModel getModel() {
		return model;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulation
	 * #run()
	 */
	@Override
	public void run() throws Exception {

		steps = getProfile().getTimeSteps();

		for (int i = 0; i < steps; i++) {
			step();
			register();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulation
	 * #setMethod
	 * (br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod
	 * )
	 */
	@Override
	public void setMethod(SystemDynamicsSimulationMethod newMethod) {
		method = newMethod;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulation
	 * #setModel(br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel)
	 */
	@Override
	public void setModel(SystemDynamicsModel model) throws Exception {
		this.model = model;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulation
	 * #reset()
	 */
	@Override
	public void reset() throws Exception {
		method.setModel(model);
		method.setInitialTime(getProfile().getInitialTime());
		method.setStepSize(getProfile().getTimeInterval());
		method.reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulation
	 * #getSimulatonData()
	 */
	@Override
	public JynaSimulationData getSimulatonData() {
		return data;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulation
	 * #register()
	 */
	@Override
	public void register() throws Exception {
		if (data != null) {
			data.register(method.getTime());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seebr.ufjf.mmc.jynacore.JynaSimulation#setMethod(br.ufjf.mmc.jynacore.
	 * JynaSimulationMethod)
	 */
	@Override
	public void setMethod(JynaSimulationMethod newMethod) throws Exception {
		setMethod((SystemDynamicsSimulationMethod) newMethod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seebr.ufjf.mmc.jynacore.JynaSimulation#setModel(br.ufjf.mmc.jynacore.
	 * JynaSimulableModel)
	 */
	@Override
	public void setModel(JynaSimulableModel model) throws Exception {
		setModel((SystemDynamicsModel) model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.JynaSimulation#setSimulationData(br.ufjf.mmc.jynacore
	 * .JynaSimulationData)
	 */
	@Override
	public void setSimulationData(JynaSimulationData sdata) {
		data = sdata;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.JynaSimulation#step()
	 */
	@Override
	public void step() throws Exception {
		method.setInitialTime(getProfile().getInitialTime());
		method.setStepSize(getProfile().getTimeInterval());
		method.step();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.JynaSimulation#getProfile()
	 */
	@Override
	public JynaSimulationProfile getProfile() {
		return profile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seebr.ufjf.mmc.jynacore.JynaSimulation#setProfile(br.ufjf.mmc.jynacore.
	 * JynaSimulationProfile)
	 */
	@Override
	public void setProfile(JynaSimulationProfile profile) {
		this.profile = profile;
	}

}
