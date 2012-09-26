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

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModel;
import br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject.MediumSoftwareProject;

/**
 * @author Knop
 *
 */
public class MediumSoftwareProjectTest {

	private DefaultMetaModel project;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		project = new MediumSoftwareProject();
		
	}

	/**
	 * Test method for {@link br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModel#getItems()}.
	 */
	@Test
	public final void testGetItems() {
		assertNotNull(project.get("Developer"));
		assertNotNull(project.get("Activity"));
		assertNotNull(project.get("Artifact"));
		assertNotNull(project.get("Income"));
		assertNotNull(project.get("Outcome"));
		assertNotNull(project.get("Team"));
		assertNotNull(project.get("Precedence"));
	}

	/**
	 * Test method for {@link br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModel#getName()}.
	 */
	@Test
	public final void testGetModelName() {
		assertEquals("MyProject", project.getName());
	}

	/**
	 * Test method for {@link br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModel#setName(java.lang.String)}.
	 */
	@Test
	public final void testSetModelName() {
		project.setName("MyProject2");
		assertEquals("MyProject2", project.getName());
		project.setName("MyProject");
		assertEquals("MyProject", project.getName());
	}

}
