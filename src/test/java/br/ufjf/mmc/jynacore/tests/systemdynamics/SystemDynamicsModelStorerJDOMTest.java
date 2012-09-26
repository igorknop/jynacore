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
package br.ufjf.mmc.jynacore.tests.systemdynamics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.InformationConsumer;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModelStorer;
import br.ufjf.mmc.jynacore.systemdynamics.Rate;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.Variable;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultSystemDynamicsModelStorerJDOM;
import br.ufjf.mmc.jynacore.systemdynamics.impl.examples.ExponencialGrowth;
import br.ufjf.mmc.jynacore.systemdynamics.impl.examples.OscilatorySystem;

/**
 * @author Knop
 * 
 */
public class SystemDynamicsModelStorerJDOMTest {

	private File testdir;
	private File fileName;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testdir = new File("src\\test\\resources");
	}

	/**
	 * Test method for
	 * {@link br.ufjf.mmc.jynacore.impl.JDOMMetaModelStorer#loadFromFile(java.io.File)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testLoadFromFile() throws Exception {
		SystemDynamicsModelStorer storer = new DefaultSystemDynamicsModelStorerJDOM();
		SystemDynamicsModel model;
		File fileName;
		fileName = new File(testdir, "exponencial.jyna");
		model = storer.loadFromFile(fileName);
		
		assertEquals(1, model.getFiniteLevels().size());
		assertNotNull(model.get("Population"));
		assertEquals(1.0,((FiniteStock)model.get("Population")).getInitialValue());
		assertEquals(1.0,((FiniteStock)model.get("Population")).getInitialValue().getValue());
		
		assertEquals(1, model.getInfiniteLevels().size());
		assertNotNull(model.get("PopulatonSource"));

		assertEquals(1, model.getRates().size());
		assertNotNull(model.get("GrowthRate"));
		assertNotNull(((Rate)model.get("GrowthRate")).getExpression());
		assertNotNull(((Rate)model.get("GrowthRate")).getExpression());
		

		assertNotNull(model.get("GrowthFactor"));
		assertNotNull(((Variable)model.get("GrowthFactor")).getExpression());
		assertNotNull(((Variable)model.get("GrowthFactor")).getExpression());

		assertNotNull(model.get("CurrentFactor"));
		assertNotNull(model.get("CurrentPopulation"));
		assertEquals(6, model.getItems().size());
		assertEquals(2, ((InformationConsumer) model.get("GrowthRate"))
				.getInformations().size());
	}
	/**
	 * Test method for
	 * {@link br.ufjf.mmc.jynacore.impl.JDOMMetaModelStorer#loadFromFile(java.io.File)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testLoadFromFileOsc() throws Exception {
		SystemDynamicsModelStorer storer = new DefaultSystemDynamicsModelStorerJDOM();
		SystemDynamicsModel model;
		File fileName;

		fileName = new File(testdir, "oscilatory.jyna");
		model = storer.loadFromFile(fileName);
		assertEquals(2, model.getFiniteLevels().size());
		assertEquals(2, model.getInfiniteLevels().size());
		assertEquals(2, model.getRates().size());
		assertEquals(14, model.getItems().size());
		assertEquals(2, ((InformationConsumer) model.get("difference"))
				.getInformations().size());

	}

	/**
	 * Test method for
	 * {@link br.ufjf.mmc.jynacore.impl.JDOMMetaModelStorer#saveToFile(br.ufjf.mmc.jynacore.SystemDynamicsModel, java.io.File)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testSaveToFile() throws Exception {
		SystemDynamicsModel  model = new ExponencialGrowth();
		SystemDynamicsModelStorer storer = new DefaultSystemDynamicsModelStorerJDOM();
		File fileName = new File(testdir, "exponencial-save.jyna");
		storer.saveToFile(model, fileName);
	}

	/**
	 * Test method for
	 * {@link br.ufjf.mmc.jynacore.impl.JDOMMetaModelStorer#saveToFile(br.ufjf.mmc.jynacore.SystemDynamicsModel, java.io.File)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testSaveToFileOsc() throws Exception {
		OscilatorySystem model = new OscilatorySystem();
		SystemDynamicsModelStorer storer = new DefaultSystemDynamicsModelStorerJDOM();
		fileName = new File(testdir, "oscilatory-save.jyna");
		storer.saveToFile(model, fileName);

	}

	/**
	 * Test method for
	 * {@link br.ufjf.mmc.jynacore.impl.JDOMMetaModelStorer#loadFromFile(java.io.File)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testLoadFromFileFullOsc() throws Exception {
		SystemDynamicsModelStorer storer = new DefaultSystemDynamicsModelStorerJDOM();
		SystemDynamicsModel model;
		File fileName;

		fileName = new File(testdir, "fullOscilatory.jyna");
		model = storer.loadFromFile(fileName);
		assertEquals(2, model.getFiniteLevels().size());
		assertEquals(2, model.getInfiniteLevels().size());
		assertEquals(2, model.getRates().size());
		assertEquals(18, model.getItems().size());
		//assertEquals(2, ((InformationConsumer) model.get("difference"))
		//		.getInformations().size());

	}
}
