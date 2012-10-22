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

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.metamodel.MetaModelScenario;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioStorer;
import br.ufjf.mmc.jynacore.metamodel.impl.JDOMMetaModelScenarioStorer;
import br.ufjf.mmc.jynacore.metamodel.impl.examples.generic.pipeline.LackOfCoffe;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceStorer;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstanceStorerJDOM;

/**
 * @author Knop
 * 
 */
public class JDOMMetaModelScenarioStorerTest {
	private File testdir;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testdir = new File("src/test/resources");
	}

	/**
	 * Test method for
	 * {@link br.ufjf.mmc.jynacore.metamodel.impl.JDOMMetaModelScenarioStorer#loadFromFile(java.io.File)}.
	 * 
	 * @throws Exception
	 */
   /*
	@Test
	public final void testLoadFromFile() throws Exception {
		//MetaModelScenarioStorer scenarioStorer = new JDOMMetaModelScenarioStorer();
		MetaModelInstanceStorer modelStorer = new DefaultMetaModelInstanceStorerJDOM();
		File modelFileName = new File(testdir, "oldModels/pipeLineInstance."
				+ MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION);
		MetaModelInstance model = modelStorer.loadFromFile(modelFileName);
		//scenarioStorer = new JDOMMetaModelScenarioStorer();
		//		MetaModelScenario scenario = scenarioStorer.loadFromFile(scenarioFileName, model.getMetaModel());
//		model.getMetaModel().putScenario(scenario);
//		model.getClassInstances().get("D2").setScenarioConnection("LackOfCoffe", "TheDeveloper");
		model.updateReferences();
//		MetaModelScenario scenario = new LackOfCoffe();
//		assertNotNull(model);
//		assertEquals(7, model.size());
//		assertEquals("MyProject", model.getName());
//		assertEquals(true, model.get("Activity") instanceof MetaModelClass);
//		assertEquals(true, model.get("Artifact") instanceof MetaModelClass);
//		assertEquals(true, model.get("Developer") instanceof MetaModelClass);
//		assertEquals(true,
//				model.get("Precedence") instanceof MetaModelMultiRelation);
//		assertEquals(true, model.get("Team") instanceof MetaModelMultiRelation);
//		assertEquals(true,
//				model.get("Income") instanceof MetaModelMultiRelation);
//		assertEquals(true,
//				model.get("Outcome") instanceof MetaModelSingleRelation);

	}
*/
	/**
	 * Test method for
	 * {@link br.ufjf.mmc.jynacore.metamodel.impl.JDOMMetaModelStorer#saveToFile(br.ufjf.mmc.jynacore.metamodel.MetaModel, java.io.File)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testSaveToFile() throws Exception {

//		MetaModelStorer modelStorer = new br.ufjf.mmc.jynacore.metamodel.impl.JDOMMetaModelStorer();
//		File modelFileName = new File(testdir, "pipeLine."
//				+ MetaModelStorer.META_MODEL_EXTENSION);
//		MetaModel model = modelStorer.loadFromFile(modelFileName);
		MetaModelScenario scenario = new LackOfCoffe();

		//MetaModelScenarioStorer storer = new JDOMMetaModelScenarioStorer();
		File scenarioFileName = new File(testdir, "oldModels/lackOfCoffe-save."
				+ MetaModelScenarioStorer.META_MODEL_SCENARIO_EXTENSION);
		MetaModelScenarioStorer scenarioStorer = new JDOMMetaModelScenarioStorer();
		scenarioStorer.saveToFile(scenario, scenarioFileName);
		assertEquals(true, scenarioFileName.exists());
//
//		model = new SimpleSoftwareProject();
//		storer = new JDOMMetaModelStorer();
//		fileName = new File(testdir, "simpleSoftwareProject-save."
//				+ MetaModelStorer.META_MODEL_EXTENSION);
//		storer.saveToFile(model, fileName);
//		assertEquals(true, fileName.exists());
	}

}
