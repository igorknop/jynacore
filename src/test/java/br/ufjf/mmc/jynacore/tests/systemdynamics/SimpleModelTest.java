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
package br.ufjf.mmc.jynacore.tests.systemdynamics;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultFiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultSystemDynamicsModel;


public class SimpleModelTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testAddObject() {
		SystemDynamicsModel model = new DefaultSystemDynamicsModel();
		
		FiniteStock objLevel = new  DefaultFiniteStock();
		
		objLevel.setInitialValue(0.0);
		model.put("Population", objLevel);
		
		assertTrue(model.getItems().containsValue(objLevel));
		assertTrue(model.getItems().containsKey("Population"));
	}

	@Test
	public final void testGet() {
		SystemDynamicsModel model = new DefaultSystemDynamicsModel();
		
		FiniteStock objLevel = new  DefaultFiniteStock();
		
		objLevel.setInitialValue(0.0);
		model.put("Population", objLevel);
		
		assertTrue(model.getItems().containsValue(model.get("Population")));
	}

}
