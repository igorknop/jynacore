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
package br.ufjf.mmc.jynacore.tests.metamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationData;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceProperty;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceStorer;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstanceStorerJDOM;
import br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulation;
import br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceEulerMethod;
import br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceSimulation;

/**
 * @author Knop
 * 
 */
public class DefaultMetaModelInstanceSimulatorTest {

	private MetaModelInstance metaModelInstance;
	private final File testdir = new File("src\\test\\resources");

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceSimulation#reset()}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testReset() throws Exception {
		MetaModelInstanceStorer storer = new DefaultMetaModelInstanceStorerJDOM();
		metaModelInstance = storer.loadFromFile(new File(testdir,
				"simpleSoftwareProjectInstance.jymmi"));
		MetaModelInstanceSimulation simulator = new DefaultMetaModelInstanceSimulation();
		JynaSimulationData data = new DefaultSimulationData();
		simulator.setSimulationData(data);
		simulator.setMethod(new DefaultMetaModelInstanceEulerMethod());
		simulator.setModel(metaModelInstance);

		simulator.reset();

		ClassInstance coding = (ClassInstance) metaModelInstance
				.getClassInstances().get("Coding");
		ClassInstanceProperty duration = (ClassInstanceProperty) coding
				.get("duration");

		ClassInstanceStock level = (ClassInstanceStock) coding
				.get("TimeToConclude");
		//ClassInstanceRate work = (ClassInstanceRate) coding.get("Work");
		assertNotNull(level);
		assertEquals((Double)25.0, duration.getValue());
		assertEquals((Double)25.0, level.getValue());
	}

	/**
	 * Test method for
	 * {@link br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceSimulation#run()}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testRunSimple() throws Exception {
		MetaModelInstanceStorer storer = new DefaultMetaModelInstanceStorerJDOM();
		metaModelInstance = storer.loadFromFile(new File(testdir,
				"simpleSoftwareProjectInstance.jymmi"));
		// metaModelInstance = new SimpleSoftwareProjectInstance();
		MetaModelInstanceSimulation simulator = new DefaultMetaModelInstanceSimulation();
		JynaSimulationData data = new DefaultSimulationData();
		simulator.setSimulationData(data);
		simulator.setMethod(new DefaultMetaModelInstanceEulerMethod());
		simulator.setModel(metaModelInstance);
		metaModelInstance.updateReferences();
		simulator.reset();
		ClassInstance coding = (ClassInstance) metaModelInstance
				.getClassInstances().get("Coding");
		ClassInstanceProperty duration = (ClassInstanceProperty) coding
				.get("duration");

		ClassInstanceStock level = (ClassInstanceStock) coding
				.get("TimeToConclude");
		ClassInstanceRate work = (ClassInstanceRate) coding.get("Work");
		assertNotNull(level);
		assertEquals((Double) 0.0, simulator.getTime());
		assertEquals((Double) 25.0, duration.getValue());
		assertEquals((Double) 25.0, level.getValue());
		// assertEquals(0.0, work.getValue());
		simulator.getProfile().setTimeSteps(1);
		simulator.getProfile().setTimeInterval(1.0);
		simulator.getProfile().setInitialTime(0.0);
		simulator.run();
		assertEquals((Double) 1.0, simulator.getTime());
		assertEquals((Double) 25.0, duration.getValue());
		assertEquals((Double) 23.2, level.getValue());
		assertEquals((Double) (-1.8), work.getValue());
	}

	@Test
	public final void testRunMedium() throws Exception {
		MetaModelInstanceStorer storer = new DefaultMetaModelInstanceStorerJDOM();
		metaModelInstance = storer.loadFromFile(new File(testdir,
				"mediumSoftwareProjectInstance.jymmi"));
		// metaModelInstance = new SimpleSoftwareProjectInstance();
		MetaModelInstanceSimulation simulator = new DefaultMetaModelInstanceSimulation();
		JynaSimulationData data = new DefaultSimulationData();
		simulator.setSimulationData(data);
		simulator.setMethod(new DefaultMetaModelInstanceEulerMethod());
		simulator.setModel(metaModelInstance);
		metaModelInstance.updateReferences();
		simulator.reset();
		ClassInstance coding = (ClassInstance) metaModelInstance
				.getClassInstances().get("Coding");
		ClassInstanceProperty duration = (ClassInstanceProperty) coding
				.get("duration");

		ClassInstanceStock level = (ClassInstanceStock) coding
				.get("TimeToConclude");
		ClassInstanceRate work = (ClassInstanceRate) coding.get("Work");
		assertNotNull(level);
		assertEquals((Double) 0.0, simulator.getTime());
		assertEquals((Double) 5.0, duration.getValue());
		assertEquals((Double) 5.0, level.getValue());
		// assertEquals(0.0, work.getValue());
		simulator.getProfile().setTimeSteps(1);
		simulator.getProfile().setTimeInterval(1.0);
		simulator.getProfile().setInitialTime(0.0);
		simulator.run();
		assertEquals((Double) 1.0, simulator.getTime());
		assertEquals((Double) 25.0, duration.getValue());
		assertEquals((Double) 26.8, level.getValue());
		assertEquals((Double) 1.0, work.getValue());
	}

}
