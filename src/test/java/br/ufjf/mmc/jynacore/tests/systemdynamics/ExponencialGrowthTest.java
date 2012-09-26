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
package br.ufjf.mmc.jynacore.tests.systemdynamics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.JynaSimulation;
import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaSimulationMethod;
import br.ufjf.mmc.jynacore.JynaSimulationProfile;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationData;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationProfile;
import br.ufjf.mmc.jynacore.systemdynamics.InformationSource;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.impl.examples.ExponencialGrowth;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsEulerMethod;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsSimulation;

public class ExponencialGrowthTest {

	private SystemDynamicsModel model;

	@Before
	public void setUp() throws Exception {
		model = new ExponencialGrowth();
	}

	@Test
	public void TestRun() throws Exception {

		model = new ExponencialGrowth();
		JynaSimulation simulation = new DefaultSystemDynamicsSimulation();
		JynaSimulationMethod method = new DefaultSystemDynamicsEulerMethod();
		JynaSimulationProfile profile = new DefaultSimulationProfile();
		JynaSimulationData data = new DefaultSimulationData();  

		simulation.setProfile(profile);
		simulation.setSimulationData(data);
		simulation.setMethod(method);
		simulation.setModel(model);

		simulation.reset();
		simulation.run();

		assertEquals((Double) 2.25, ((InformationSource) model.get("Population"))
				.getValue());
		System.out.printf("%f %f\n", simulation.getTime(), ((InformationSource) model.get("Population"))
				.getValue());

		simulation.getProfile().setTimeSteps(8);
		simulation.run();
		assertEquals((Double) 57.665, ((InformationSource) model.get("Population"))
				.getValue());
		System.out.printf("%f %f\n", simulation.getTime(), ((InformationSource) model.get("Population"))
				.getValue());
		
		simulation.getProfile().setTimeSteps(10);
		simulation.run();
		assertEquals((Double) 3325.26, ((InformationSource) model.get("Population"))
				.getValue());
		System.out.printf("%f %f\n", simulation.getTime(), ((InformationSource) model.get("Population"))
				.getValue());

		simulation.getProfile().setTimeSteps(30);
		simulation.run();
		assertEquals((Double) 6.376215002140496e8, ((InformationSource) model.get("Population"))
				.getValue());
		System.out.printf("%f %f\n", simulation.getTime(), ((InformationSource) model.get("Population"))
				.getValue());
		
		simulation.getProfile().setTimeSteps(50);
		simulation.run();
		assertEquals((Double) 4.0656117753521523e17, ((InformationSource) model.get("Population"))
				.getValue());
		System.out.printf("%f %f\n", simulation.getTime(), ((InformationSource) model.get("Population"))
				.getValue());
		
	}

}
