package br.ufjf.mmc.jynacore.tests.metamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.metamodel.MetaModelStorer;
import br.ufjf.mmc.jynacore.metamodel.impl.YamlMetaModelStorer;
import br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject.MediumSoftwareProject;
import br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject.MediumSoftwareProjectInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceStorer;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstanceStorerYAML;

public class YamlMetaModelInstanceStorerTest {
	private File testdir;

	@Before
	public void setUp() throws Exception {
		testdir = new File("src/test/resources");
		if (!testdir.exists()) {
			testdir.mkdirs();
		}
	}

	@Test
	public void testSaveAndLoadInstanceYAML() throws Exception {
		// Save base metamodel in YAML first
		MetaModelStorer mmStorer = new YamlMetaModelStorer();
		File mmFile = new File(testdir, "mediumSoftwareProject-save." + MetaModelStorer.META_MODEL_YAML_EXTENSION);
		mmStorer.saveToFile(new MediumSoftwareProject(), mmFile);

		MetaModelInstance modelInstance = new MediumSoftwareProjectInstance();
		modelInstance.setMetaModelFileName("mediumSoftwareProject-save." + MetaModelStorer.META_MODEL_YAML_EXTENSION);

		MetaModelInstanceStorer storer = new DefaultMetaModelInstanceStorerYAML();
		File fileName = new File(testdir, "mediumSoftwareProjectInstance-save." + MetaModelInstanceStorer.META_MODEL_INSTANCE_YAML_EXTENSION);

		storer.saveToFile(modelInstance, fileName);
		assertTrue(fileName.exists());

		MetaModelInstance loadedInstance = storer.loadFromFile(fileName);
		assertNotNull(loadedInstance);
		assertEquals(7, loadedInstance.getClassInstances().size());
		assertEquals("Medium Software Project Instance", loadedInstance.getName());

		assertNotNull(loadedInstance.getClassInstances().get("D1"));
		assertEquals("Developer", loadedInstance.getClassInstances().get("D1").getMetaModelClass().getName());

		assertNotNull(loadedInstance.getClassInstances().get("D2"));
		assertEquals("Developer", loadedInstance.getClassInstances().get("D2").getMetaModelClass().getName());

		assertNotNull(loadedInstance.getClassInstances().get("Coding"));
		assertEquals("Activity", loadedInstance.getClassInstances().get("Coding").getMetaModelClass().getName());

		if (fileName.exists()) {
			fileName.delete();
		}
		if (mmFile.exists()) {
			mmFile.delete();
		}
	}
}
