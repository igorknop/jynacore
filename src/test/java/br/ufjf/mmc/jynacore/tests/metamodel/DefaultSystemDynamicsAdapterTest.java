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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject.MediumSoftwareProjectInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.SystemDynamicsAdapter;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultSystemDynamicsAdapter;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.Variable;

@Deprecated
public class DefaultSystemDynamicsAdapterTest {

	private MediumSoftwareProjectInstance modelInstance;

	@Before
	public void setUp() throws Exception {
		modelInstance = new MediumSoftwareProjectInstance();
		
	}

	@Test
	public final void testConvertToSystemDynamics() throws Exception {
		SystemDynamicsAdapter adapter = new DefaultSystemDynamicsAdapter();
		SystemDynamicsModel sdModel;
		sdModel = adapter.convertToSystemDynamics(modelInstance);
		//D1 Developer
		//PROC
		assertNotNull(sdModel.get("D1_experience"));
		assertEquals((Double) 1.0, ((Variable) sdModel.get("D1_experience")).getValue());
		assertNotNull(sdModel.get("D1_Productivity"));
		assertEquals("D1_experience", ((Variable) sdModel.get("D1_Productivity")).getExpression());
		//assertEquals(1.0, ((Variable) sdModel.get("D1_Productivity")).getValue());

		//D2 Developer
		//PROC
		assertNotNull(sdModel.get("D2_experience"));
		assertEquals((Double)0.8, ((Variable) sdModel.get("D2_experience")).getValue());
		assertNotNull(sdModel.get("D2_Productivity"));
		assertEquals("D2_experience", ((Variable) sdModel.get("D2_Productivity")).getExpression());
		//assertEquals(0.8, ((Variable) sdModel.get("D2_Productivity")).getValue());

		//Analisys Artifact
		//PROC
		assertNotNull(sdModel.get("AnalisysModel_latent_errors"));
		assertEquals((Double)10.0, ((Variable) sdModel.get("AnalisysModel_latent_errors")).getValue());
		//STOCK
		assertNotNull(sdModel.get("AnalisysModel_Errors"));

		//DesignModel
		//PROC
		assertNotNull(sdModel.get("DesignModel_latent_errors"));
		//Errors
		assertNotNull(sdModel.get("DesignModel_Errors"));

		//SourceCode
		//PROC
		assertNotNull(sdModel.get("SourceCode_latent_errors"));
		//Errors
		assertNotNull(sdModel.get("SourceCode_Errors"));

		//Designing Activity
		//STOCK
		assertNotNull(sdModel.get("Designing_TimeToConclude"));
		assertNotNull(sdModel.get("Designing_ExecutingOrDone"));
		//RATE
		assertNotNull(sdModel.get("Designing_Work"));
		assertNotNull(sdModel.get("Designing_RTExecuting"));

		//assertNotNull(sdModel.get("Designing_ErrorsTransmit1"));
		//assertNotNull(sdModel.get("Designing_ErrorsCommited1"));
		//PROC
		assertNotNull(sdModel.get("Designing_duration"));
		assertNotNull(sdModel.get("Designing_DependOk"));
		assertNotNull(sdModel.get("Designing_Prod"));
		assertNotNull(sdModel.get("Designing_ErrorsPerDay"));

		//Designing Coding
		//STOCK
		assertNotNull(sdModel.get("Coding_TimeToConclude"));
		assertNotNull(sdModel.get("Coding_ExecutingOrDone"));
		//RATE
		assertNotNull(sdModel.get("Coding_Work"));
		assertNotNull(sdModel.get("Coding_RTExecuting"));
		
		//assertNotNull(sdModel.get("Coding_ErrorsTransmit1"));
		//assertNotNull(sdModel.get("Coding_ErrorsCommited1"));
		//PROC
		assertNotNull(sdModel.get("Coding_duration"));
		assertNotNull(sdModel.get("Coding_DependOk"));
		assertNotNull(sdModel.get("Coding_Prod"));
		assertNotNull(sdModel.get("Coding_ErrorsPerDay"));
				
		System.out.println(sdModel);
	}

}
