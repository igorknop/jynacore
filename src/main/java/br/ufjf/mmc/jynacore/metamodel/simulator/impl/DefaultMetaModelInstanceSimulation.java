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
package br.ufjf.mmc.jynacore.metamodel.simulator.impl;

import br.ufjf.mmc.jynacore.JynaSimulableModel;
import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaSimulationMethod;
import br.ufjf.mmc.jynacore.JynaSimulationProfile;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulationMethod;
import br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulation;

/**
 * @author Knop
 * 
 */
public class DefaultMetaModelInstanceSimulation implements
		MetaModelInstanceSimulation {

	private MetaModelInstance metaModelInstance;
	private MetaModelInstanceSimulationMethod method;
	private JynaSimulationData data;
	private int steps;
	private JynaSimulationProfile profile;

	public DefaultMetaModelInstanceSimulation() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulation#
	 * getMetaModelIntance()
	 */
	@Override
	public MetaModelInstance getModel() {
		return metaModelInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulation#
	 * getMethod()
	 */
	@Override
	public MetaModelInstanceSimulationMethod getMethod() {
		return method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulation#
	 * getSimulatonData()
	 */
	@Override
	public JynaSimulationData getSimulatonData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulation#
	 * getTime()
	 */
	@Override
	public Double getTime() {
		return getMethod().getTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulation#
	 * register()
	 */
	@Override
	public void register() throws Exception {
		if (data != null) {
			data.register(getMethod().getTime());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulation#
	 * reset()
	 */
	@Override
	public void reset() throws Exception {
		method.setMetaModelInstance(getModel());
		method.setInitialTime(getProfile().getInitialTime());
		method.setStepSize(getProfile().getTimeInterval());
		method.reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulation#
	 * run()
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
	 * br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulation#
	 * setMetaModelInstance
	 * (br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance)
	 */
	@Override
	public void setModel(MetaModelInstance metaModelInstance) throws Exception {
		this.metaModelInstance = metaModelInstance;
		getMethod().setMetaModelInstance(this.metaModelInstance);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulation#
	 * setMethod
	 * (br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod
	 * )
	 */
	@Override
	public void setMethod(MetaModelInstanceSimulationMethod newMethod) {
		method = newMethod;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulation#
	 * setSimulatonData
	 * (br.ufjf.mmc.jynacore.systemdynamics.simulator.SimulationData)
	 */
	@Override
	public void setSimulationData(JynaSimulationData sdata) {
		data = sdata;
	}

	@Override
	public void setMethod(JynaSimulationMethod newMethod) {
		setMethod((MetaModelInstanceSimulationMethod) newMethod);
	}

	@Override
	public void setModel(JynaSimulableModel model) throws Exception {
		setModel((MetaModelInstance) model);
	}

	@Override
	public void step() throws Exception {
		method.setInitialTime(getProfile().getInitialTime());
		method.setStepSize(getProfile().getTimeInterval());
		method.step();
	}

	@Override
	public JynaSimulationProfile getProfile() {
		return profile;
	}

	@Override
	public void setProfile(JynaSimulationProfile profile) {
		this.profile = profile;
	}

}
