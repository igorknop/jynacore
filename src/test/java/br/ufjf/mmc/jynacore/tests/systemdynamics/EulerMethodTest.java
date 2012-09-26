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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.JynaEvaluated;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNameExpression;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.InfiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.Information;
import br.ufjf.mmc.jynacore.systemdynamics.Rate;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultFiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultInfiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultInformation;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultRate;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultSystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsEulerMethod;

public class EulerMethodTest {

	private SystemDynamicsModel model1;
	private FiniteStock finLevel;
	private InfiniteStock infLevel;
	private Rate cteRate;
	private FiniteStock finLevel2;
	private SystemDynamicsModel model2;
	private Rate cteRate2;
	private FiniteStock finLevel3;
	private DefaultSystemDynamicsModel model3;
	private FiniteStock finLevel4;
	private FiniteStock finLevel5;
	private Rate rate3;

	@Before
	public void setUp() throws Exception {
		setUpModel1();
		setUpModel2();
		setUpModel3();
	}



	public void setUpModel1(){
		// [infLvl] ===Rate==> [finLevel]
		model1 = new DefaultSystemDynamicsModel();

		finLevel = new DefaultFiniteStock();
		finLevel.setInitialValue(0.0);

		infLevel = new DefaultInfiniteStock();

		cteRate = new DefaultRate();
		cteRate.setValue(3.0);
		cteRate.setSourceAndTarget(infLevel, finLevel);

		model1.put("Stock", finLevel);
		model1.put("Source", infLevel);
		model1.put("Rate", cteRate);		
	}
	private void setUpModel2() {
		// [finLevel2] ===Rate2==> [finLevel3]
		model2 = new DefaultSystemDynamicsModel();

		finLevel2 = new DefaultFiniteStock();
		finLevel2.setInitialValue(10.0);

		finLevel3 = new DefaultFiniteStock();
		finLevel3.setInitialValue(0.0);

		cteRate2 = new DefaultRate();
		cteRate2.setValue(3.0);
		cteRate2.setSourceAndTarget(finLevel2, finLevel3);

		model2.put("Level2", finLevel2);
		model2.put("Level3", finLevel3);
		model2.put("Rate2", cteRate2);

		
	}
	
	public void setUpModel3(){
		// [finLevel4] ===Rate3==> [finLevel5]
		//                  ^--(Var)---|
		// Exponencial growth
		model3 = new DefaultSystemDynamicsModel();

		finLevel4 = new DefaultFiniteStock();
		finLevel4.setInitialValue(100.0);

		finLevel5 = new DefaultFiniteStock();
		finLevel5.setInitialValue(1.0);

		
		rate3 = new DefaultRate();
		rate3.setSourceAndTarget(finLevel4, finLevel5);
		((JynaEvaluated)rate3).setExpression(new DefaultNameExpression("Level5"));

		
		model3.put("Level4", finLevel4);
		model3.put("Level5", finLevel5);
		model3.put("Rate3", rate3);

		Information inf1 = new DefaultInformation(finLevel5,rate3);
		inf1.setName("inf1");
		model3.put("Inf1", inf1);
		
	}
	@Test
	public final void testInfLvlRateCteLvl() throws Exception {
		SystemDynamicsSimulationMethod method = new DefaultSystemDynamicsEulerMethod();
		method.setModel(model1);
		method.setInitialTime(0.0);
		method.setStepSize(1.0);
		method.reset();
		method.step();
		assertEquals((Integer) 3, ((FiniteStock) method.getModel().get("Stock")).getValue());
		method.step();
		method.step();
		assertEquals((Integer) 9,((FiniteStock) method.getModel().get("Stock")).getValue());

	
	}
	@Test
	public final void testLvlRateCteLvl() throws Exception{
		SystemDynamicsSimulationMethod method2 = new DefaultSystemDynamicsEulerMethod();
		method2.setModel(model2);
		method2.setInitialTime(0.0);
		method2.setStepSize(1.0);
		method2.reset();
		method2.step();
		assertEquals(
				(Double) 3.0,( (FiniteStock) method2.getModel().get("Level3")).getValue());	
		assertEquals(
				(Double) 7.0,( (FiniteStock) method2.getModel().get("Level2")).getValue());
	}
	
	@Test
	public final void testInfRateLvl() throws Exception{
		SystemDynamicsSimulationMethod method3 = new DefaultSystemDynamicsEulerMethod();
		method3.setModel(model3);
		method3.setInitialTime(0.0);
		method3.setStepSize(1.0);
		method3.reset();
		method3.step();
		assertEquals(
				(Integer) 99,( (FiniteStock) method3.getModel().get("Level4")).getValue());	
		assertEquals(
				(Integer) 2,( (FiniteStock) method3.getModel().get("Level5")).getValue());
		method3.step();
		assertEquals(
				(Integer) 97,( (FiniteStock) method3.getModel().get("Level4")).getValue());	
		assertEquals(
				(Integer) 4,( (FiniteStock) method3.getModel().get("Level5")).getValue());
		method3.step();
		assertEquals(
				(Integer) 93,( (FiniteStock) method3.getModel().get("Level4")).getValue());	
		assertEquals(
				(Integer) 8,( (FiniteStock) method3.getModel().get("Level5")).getValue());
	}
}
