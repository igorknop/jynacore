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

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.JynaModel;
import br.ufjf.mmc.jynacore.JynaModelStorer;
import br.ufjf.mmc.jynacore.JynaSimulableModel;
import br.ufjf.mmc.jynacore.JynaSimulation;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceStorer;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstanceStorerJDOM;
import br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceSimulation;

/**
 * @author Knop
 *
 */
public class DefaultMetaModelInstanceEulerMethodTest {

	private final File testdir = new File("src/test/resources");

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceEulerMethod#reset()}.
	 * @throws Exception 
	 */
	@Test
	public final void testResetSimple() throws Exception {
		JynaModelStorer storer = new DefaultMetaModelInstanceStorerJDOM();
		File fileName = new File(testdir, "oldModels/simpleSoftwareProjectInstance."+MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION);
		JynaModel  modelInstance = storer.load(fileName.toURI());
		JynaSimulation simulator = new DefaultMetaModelInstanceSimulation();
		simulator.setModel((JynaSimulableModel) modelInstance);
		simulator.getProfile().setTimeLimits(0.0, 100.0, 200);
		simulator.reset();
		simulator.register();
	}
	/**
	 * Test method for {@link br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceEulerMethod#reset()}.
	 * @throws Exception 
	 */
	@Test
	public final void testResetMedium() throws Exception {
		JynaModelStorer storer = new DefaultMetaModelInstanceStorerJDOM();
		File fileName = new File(testdir, "oldModels/mediumSoftwareProjectInstance."+MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION);
		JynaModel  modelInstance = storer.load(fileName.toURI());
		JynaSimulation simulator = new DefaultMetaModelInstanceSimulation();
		simulator.setModel((JynaSimulableModel) modelInstance);
		simulator.getProfile().setTimeLimits(0.0, 100.0, 200);
		simulator.reset();
		simulator.register();
	}

	/**
	 * Test method for {@link br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceEulerMethod#step()}.
	 * @throws Exception 
	 */
	@Test
	public final void testStepSimple() throws Exception {
		JynaModelStorer storer = new DefaultMetaModelInstanceStorerJDOM();
		File fileName = new File(testdir, "oldModels/simpleSoftwareProjectInstance."+MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION);
		JynaModel  modelInstance = storer.load(fileName.toURI());
		JynaSimulation simulator = new DefaultMetaModelInstanceSimulation();
		simulator.setModel((JynaSimulableModel) modelInstance);
		simulator.getProfile().setTimeLimits(0.0, 100.0, 200);
		simulator.step();
		simulator.register();
	}
	/**
	 * Test method for {@link br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceEulerMethod#step()}.
	 * @throws Exception 
	 */
	@Test
	public final void testStepMedium() throws Exception {
		JynaModelStorer storer = new DefaultMetaModelInstanceStorerJDOM();
		File fileName = new File(testdir, "oldModels/mediumSoftwareProjectInstance."+MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION);
		JynaModel  modelInstance = storer.load(fileName.toURI());
		JynaSimulation simulator = new DefaultMetaModelInstanceSimulation();
		simulator.setModel((JynaSimulableModel) modelInstance);
		simulator.getProfile().setTimeLimits(0.0, 100.0, 200);
		simulator.step();
		simulator.register();
	}

}
