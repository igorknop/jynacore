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
package br.ufjf.mmc.jynacore.tests.metamodel;


import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject.MediumSoftwareProjectInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;

public class MediumSoftwareProjectInstanceTest {

	private MediumSoftwareProjectInstance projectInstance;
	//private MetaModel project;

	@Before
	public void setUp() throws Exception {
		projectInstance = new MediumSoftwareProjectInstance();
		//project = projectInstance.getMetaModel();
		
	}
	
	@Test
	public void testGetClassInstances(){
		assertNotNull(projectInstance.getClassInstances());
		ClassInstance D1 = projectInstance.getClassInstances().get("D1");
		ClassInstance D2 = projectInstance.getClassInstances().get("D2");
		ClassInstance AM = projectInstance.getClassInstances().get("AnalisysModel");
		ClassInstance DM = projectInstance.getClassInstances().get("DesignModel");
		ClassInstance SC = projectInstance.getClassInstances().get("SourceCode");
		ClassInstance DS = projectInstance.getClassInstances().get("Designing");
		ClassInstance CD = projectInstance.getClassInstances().get("Coding");
		
		assertNotNull(D2);
		assertNotNull(AM);
		assertNotNull(DM);
		assertNotNull(SC);
		assertNotNull(DS);
		assertNotNull(CD);
		assertNotNull(D1);
		System.out.println(projectInstance);
	}
	

}
