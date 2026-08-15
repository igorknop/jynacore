package br.ufjf.mmc.jynacore.tests.metamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.MetaModelSingleRelation;
import br.ufjf.mmc.jynacore.metamodel.MetaModelStorer;
import br.ufjf.mmc.jynacore.metamodel.impl.YamlMetaModelStorer;
import br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject.MediumSoftwareProject;

public class YamlMetaModelStorerTest {
	private File testdir;

	@Before
	public void setUp() throws Exception {
		testdir = new File("src/test/resources");
		if (!testdir.exists()) {
			testdir.mkdirs();
		}
	}

	@Test
	public void testSaveAndLoadYAML() throws Exception {
		MetaModel model = new MediumSoftwareProject();
		MetaModelStorer storer = new YamlMetaModelStorer();
		File fileName = new File(testdir, "mediumSoftwareProject-test." + MetaModelStorer.META_MODEL_YAML_EXTENSION);

		storer.saveToFile(model, fileName);
		assertTrue(fileName.exists());

		MetaModel loadedModel = storer.loadFromFile(fileName);
		assertNotNull(loadedModel);
		assertEquals(7, loadedModel.size());
		assertEquals("MyProject", loadedModel.getName());
		assertTrue(loadedModel.get("Activity") instanceof MetaModelClass);
		assertTrue(loadedModel.get("Artifact") instanceof MetaModelClass);
		assertTrue(loadedModel.get("Developer") instanceof MetaModelClass);
		assertTrue(loadedModel.get("Precedence") instanceof MetaModelMultiRelation);
		assertTrue(loadedModel.get("Team") instanceof MetaModelMultiRelation);
		assertTrue(loadedModel.get("Income") instanceof MetaModelMultiRelation);
		assertTrue(loadedModel.get("Outcome") instanceof MetaModelSingleRelation);

		if (fileName.exists()) {
			fileName.delete();
		}
	}
}
