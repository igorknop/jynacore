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

import br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject.MediumSoftwareProjectInstance;
import br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject.simple.SimpleSoftwareProjectInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceSingleRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceStorer;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstanceStorerJDOM;

/**
 * @author Knop
 *
 */
public class JDOMMetaModelInstanceStorerTest {
	private File testdir;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testdir = new File("src\\test\\resources");
	}

	/**
	 * Test method for {@link br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstanceStorerJDOM#loadFromFile(java.io.File)}.
	 * @throws Exception 
	 */
	@Test
	public final void testLoadFromFile() throws Exception {
		MetaModelInstanceStorer storer = new DefaultMetaModelInstanceStorerJDOM();
		File fileName = new File(testdir, "mediumSoftwareProjectInstance."+MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION);
		MetaModelInstance  modelInstance = storer.loadFromFile(fileName);
		assertNotNull(modelInstance);
		assertEquals(7,modelInstance.getClassInstances().size());
		assertEquals("Medium Software Project Instance", modelInstance.getName());
		
		assertNotNull(modelInstance.getClassInstances().get("D1"));
		assertEquals("Developer", modelInstance.getClassInstances().get("D1").getMetaModelClass().getName());
		
		assertNotNull(modelInstance.getClassInstances().get("D2"));
		assertEquals("Developer", modelInstance.getClassInstances().get("D2").getMetaModelClass().getName());
		
		assertNotNull(modelInstance.getClassInstances().get("Coding"));
		assertEquals("Activity", modelInstance.getClassInstances().get("Coding").getMetaModelClass().getName());
		assertEquals(true, modelInstance.getClassInstances().get("Coding").get("Outcome") instanceof ClassInstanceSingleRelation);
		assertEquals(true, modelInstance.getClassInstances().get("Coding").get("Income") instanceof ClassInstanceMultiRelation);
		assertEquals(true, modelInstance.getClassInstances().get("Coding").get("Team") instanceof ClassInstanceMultiRelation);
		assertEquals(true, modelInstance.getClassInstances().get("Coding").get("Precedence") instanceof ClassInstanceMultiRelation);

		assertNotNull(modelInstance.getClassInstances().get("Designing"));
		assertEquals("Activity", modelInstance.getClassInstances().get("Designing").getMetaModelClass().getName());
		assertEquals(true, modelInstance.getClassInstances().get("Designing").get("Outcome") instanceof ClassInstanceSingleRelation);
		assertEquals(true, modelInstance.getClassInstances().get("Designing").get("Income") instanceof ClassInstanceMultiRelation);
		assertEquals(true, modelInstance.getClassInstances().get("Designing").get("Team") instanceof ClassInstanceMultiRelation);

		assertNotNull(modelInstance.getClassInstances().get("DesignModel"));
		assertEquals("Artifact", modelInstance.getClassInstances().get("DesignModel").getMetaModelClass().getName());
		assertNotNull(modelInstance.getClassInstances().get("AnalisysModel"));
		assertEquals("Artifact", modelInstance.getClassInstances().get("AnalisysModel").getMetaModelClass().getName());
		assertNotNull(modelInstance.getClassInstances().get("SourceCode"));
		assertEquals("Artifact", modelInstance.getClassInstances().get("SourceCode").getMetaModelClass().getName());

		storer = new DefaultMetaModelInstanceStorerJDOM();
		fileName = new File(testdir, "mediumSoftwareProjectInstance-save2."+MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION);
		storer.saveToFile(modelInstance, fileName);

		storer = new DefaultMetaModelInstanceStorerJDOM();
		fileName = new File(testdir, "simpleSoftwareProjectInstance."+MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION);
		modelInstance = storer.loadFromFile(fileName);
		modelInstance = new SimpleSoftwareProjectInstance();
		assertNotNull(modelInstance);
		assertEquals(3,modelInstance.getClassInstances().size());
		assertEquals("Simple Software Project Instance", modelInstance.getName());
		
		assertNotNull(modelInstance.getClassInstances().get("D1"));
		assertEquals("Developer", modelInstance.getClassInstances().get("D1").getMetaModelClass().getName());
		
		assertNotNull(modelInstance.getClassInstances().get("D2"));
		assertEquals("Developer", modelInstance.getClassInstances().get("D2").getMetaModelClass().getName());
		assertNotNull(modelInstance.getClassInstances().get("Coding"));
		assertEquals("Activity", modelInstance.getClassInstances().get("Coding").getMetaModelClass().getName());
		assertNotNull(modelInstance.getClassInstances().get("Coding").get("Team"));
		assertEquals(true, modelInstance.getClassInstances().get("Coding").get("Team") instanceof ClassInstanceMultiRelation);
	}
	@Test
	public final void testLoadFromFileSimple() throws Exception {
		MetaModelInstanceStorer storer = new DefaultMetaModelInstanceStorerJDOM();
		File fileName = new File(testdir, "simpleSoftwareProjectInstance."+MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION);
		MetaModelInstance modelInstance = storer.loadFromFile(fileName);
		modelInstance = new SimpleSoftwareProjectInstance();
		assertNotNull(modelInstance);
		assertEquals(3,modelInstance.getClassInstances().size());
		assertEquals("Simple Software Project Instance", modelInstance.getName());
		
		assertNotNull(modelInstance.getClassInstances().get("D1"));
		assertEquals("Developer", modelInstance.getClassInstances().get("D1").getMetaModelClass().getName());
		
		assertNotNull(modelInstance.getClassInstances().get("D2"));
		assertEquals("Developer", modelInstance.getClassInstances().get("D2").getMetaModelClass().getName());
		assertNotNull(modelInstance.getClassInstances().get("Coding"));
		assertEquals("Activity", modelInstance.getClassInstances().get("Coding").getMetaModelClass().getName());
		assertNotNull(modelInstance.getClassInstances().get("Coding").get("Team"));
		assertEquals(true, modelInstance.getClassInstances().get("Coding").get("Team") instanceof ClassInstanceMultiRelation);
	}

	/**
	 * Test method for {@link br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstanceStorerJDOM#saveToFile(br.ufjf.mmc.jynacore.metamodel.MetaModel, java.io.File)}.
	 * @throws Exception 
	 */
	@Test
	public final void testSaveToFile() throws Exception {
		MetaModelInstance  modelInstance = new MediumSoftwareProjectInstance();
		MetaModelInstanceStorer storer = new DefaultMetaModelInstanceStorerJDOM();
		File fileName = new File(testdir, "mediumSoftwareProjectInstance-save."+MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION);
		storer.saveToFile(modelInstance, fileName);
		assertEquals(true, fileName.exists());
		
		modelInstance = new SimpleSoftwareProjectInstance();
		storer = new DefaultMetaModelInstanceStorerJDOM();
		fileName = new File(testdir, "simpleSoftwareProjectInstance-save."+MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION);
		storer.saveToFile(modelInstance, fileName);
		assertEquals(true, fileName.exists());
		
	}
	@Test
	public final void testSaveToFileSimple() throws Exception {
		MetaModelInstance modelInstance = new SimpleSoftwareProjectInstance();
		MetaModelInstanceStorer storer = new DefaultMetaModelInstanceStorerJDOM();
		File fileName = new File(testdir, "simpleSoftwareProjectInstance-save."+MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION);
		storer.saveToFile(modelInstance, fileName);
		assertEquals(true, fileName.exists());
		
	}

}
