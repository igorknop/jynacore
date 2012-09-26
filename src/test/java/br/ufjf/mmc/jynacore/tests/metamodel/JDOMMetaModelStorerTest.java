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

import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.MetaModelSingleRelation;
import br.ufjf.mmc.jynacore.metamodel.MetaModelStorer;
import br.ufjf.mmc.jynacore.metamodel.impl.JDOMMetaModelStorer;
import br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject.MediumSoftwareProject;
import br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject.simple.SimpleSoftwareProject;

/**
 * @author Knop
 *
 */
public class JDOMMetaModelStorerTest {
	private File testdir;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testdir = new File("src\\test\\resources");
	}

	/**
	 * Test method for {@link br.ufjf.mmc.jynacore.metamodel.impl.JDOMMetaModelStorer#loadFromFile(java.io.File)}.
	 * @throws Exception 
	 */
	@Test
	public final void testLoadFromFile() throws Exception {
		MetaModelStorer storer = new JDOMMetaModelStorer();
		File fileName = new File(testdir, "mediumSoftwareProject-save."+MetaModelStorer.META_MODEL_EXTENSION);
		MetaModel  model = storer.loadFromFile(fileName);
		assertNotNull(model);
		assertEquals(7, model.size());
		assertEquals("MyProject", model.getName());
		assertEquals(true, model.get("Activity") instanceof MetaModelClass);
		assertEquals(true, model.get("Artifact") instanceof MetaModelClass);
		assertEquals(true, model.get("Developer") instanceof MetaModelClass);
		assertEquals(true, model.get("Precedence") instanceof MetaModelMultiRelation);
		assertEquals(true, model.get("Team") instanceof MetaModelMultiRelation);
		assertEquals(true, model.get("Income") instanceof MetaModelMultiRelation);
		assertEquals(true, model.get("Outcome") instanceof MetaModelSingleRelation);

		storer = new JDOMMetaModelStorer();
		fileName = new File(testdir, "mediumSoftwareProject-save2."+MetaModelStorer.META_MODEL_EXTENSION);
		storer.saveToFile(model, fileName);
	}

	/**
	 * Test method for {@link br.ufjf.mmc.jynacore.metamodel.impl.JDOMMetaModelStorer#saveToFile(br.ufjf.mmc.jynacore.metamodel.MetaModel, java.io.File)}.
	 * @throws Exception 
	 */
	@Test
	public final void testSaveToFile() throws Exception {
		MetaModel  model = new MediumSoftwareProject();
		MetaModelStorer storer = new JDOMMetaModelStorer();
		File fileName = new File(testdir, "mediumSoftwareProject-save."+MetaModelStorer.META_MODEL_EXTENSION);
		storer.saveToFile(model, fileName);
		assertEquals(true, fileName.exists());

		model = new SimpleSoftwareProject();
		storer = new JDOMMetaModelStorer();
		fileName = new File(testdir, "simpleSoftwareProject-save."+MetaModelStorer.META_MODEL_EXTENSION);
		storer.saveToFile(model, fileName);
		assertEquals(true, fileName.exists());
	}

}
