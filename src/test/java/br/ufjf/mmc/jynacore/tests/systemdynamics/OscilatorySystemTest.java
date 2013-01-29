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

import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationData;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationProfile;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.InformationSource;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModelStorer;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultSystemDynamicsModelStorerJDOM;
import br.ufjf.mmc.jynacore.systemdynamics.impl.examples.OscilatorySystem;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulation;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsEulerMethod;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsSimulation;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.JFreeChartDataConverter;

public class OscilatorySystemTest {


	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testOscilatorySystem() throws Exception {
		SystemDynamicsModel model;
		model = new OscilatorySystem();
		SystemDynamicsSimulation simulator = new DefaultSystemDynamicsSimulation();
		SystemDynamicsSimulationMethod method = new DefaultSystemDynamicsEulerMethod();
		simulator.setProfile(new DefaultSimulationProfile());
		simulator.getProfile().setInitialTime(0.0);
		simulator.getProfile().setTimeInterval(1.0);
		simulator.setMethod(method);
		simulator.setModel(model);
		simulator.reset();
		
		JynaSimulationData data = new DefaultSimulationData();
		for (FiniteStock flvl : model.getFiniteLevels()) {
			data.add(flvl.getName(), flvl);
		}
		data.register(0.0);
		simulator.setSimulationData(data);

		simulator.getProfile().setTimeSteps(10);
		simulator.run();
		assertEquals((Double) 10.0, simulator.getTime());
		assert(Math.abs(0.57079 - ((InformationSource) model.get("X"))
				.getValue())<0.0001);
		simulator.getProfile().setTimeSteps(10);
		simulator.run();
		assertEquals((Double) 20.0, simulator.getTime());
		assert(Math.abs(-0.453019-((InformationSource) model.get("X"))
				.getValue())<0.001);
		System.out.println(((InformationSource) model.get("X"))
				.getValue());
		
		simulator.getProfile().setTimeSteps(30);
		simulator.run();
		assertEquals((Double) 50.0, simulator.getTime());
		assert(Math.abs(0.343355- ((InformationSource) model.get("X"))
				.getValue())<0.001);
		System.out.println(((InformationSource) model.get("X"))
				.getValue());
		simulator.getProfile().setTimeSteps(50);
		simulator.run();
		assertEquals((Double) 100.0, simulator.getTime());
		assert(Math.abs(-1.40885- ((InformationSource) model.get("X"))
				.getValue())<0.001);
		System.out.println(((InformationSource) model.get("X"))
				.getValue());
		
//		JFreeChart chart = ChartFactory.createXYLineChart(
//				"Modelo Oscilat�rio", // chart title
//				"time", // x axis label
//				"value", // y axis label
//				JFreeChartDataConverter.toXYDataSet(data), // data
//				PlotOrientation.VERTICAL,
//				true, // include legend
//				true, // tooltips
//				false // urls
//		);
//		JFrame frame = new JFrame();
//		frame.add(new ChartPanel( chart));
//		frame.setPreferredSize(new Dimension(640,480));
//		frame.pack();
//		frame.setLocationRelativeTo(null);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//		System.out.println();
	}
	@Test
	public final void testOscilatorySystemFromLoad() throws Exception {

		SystemDynamicsModelStorer storer = new DefaultSystemDynamicsModelStorerJDOM();
		SystemDynamicsModel model2;
		File fileName;
		File testdir = new File("src/test/resources");
		fileName = new File(testdir, "oscilatory-save.jyna");
		model2 = storer.loadFromFile(fileName);


		SystemDynamicsSimulation simulator = new DefaultSystemDynamicsSimulation();
		SystemDynamicsSimulationMethod method = new DefaultSystemDynamicsEulerMethod();
      DefaultSimulationProfile profile = new DefaultSimulationProfile();
      simulator.setProfile(profile);
		simulator.getProfile().setInitialTime(0.0);
		simulator.getProfile().setTimeInterval(1.0);
		simulator.setMethod(method);
		simulator.setModel(model2);
		simulator.reset();
		
		JynaSimulationData data = new DefaultSimulationData();
		for (FiniteStock flvl : model2.getFiniteLevels()) {
			data.add(flvl.getName(), flvl);
		}
		data.register(0.0);
		simulator.setSimulationData(data);

		simulator.getProfile().setTimeSteps(10);
		simulator.run();
		assertEquals((Double) 10.0, 		simulator.getTime());
		assert(Math.abs(0.57079 - ((InformationSource) model2.get("X"))
				.getValue())<0.0001);
		simulator.getProfile().setTimeSteps(10);
		simulator.run();
		assertEquals((Double) 20.0, simulator.getTime());
		assert(Math.abs(-0.453019-((InformationSource) model2.get("X"))
				.getValue())<0.001);
		System.out.println(((InformationSource) model2.get("X"))
				.getValue());
		
		simulator.getProfile().setTimeSteps(30);
		simulator.run();
		assertEquals((Double) 50.0, simulator.getTime());
		assert(Math.abs(0.343355- ((InformationSource) model2.get("X"))
				.getValue())<0.001);
		System.out.println(((InformationSource) model2.get("X"))
				.getValue());
		simulator.getProfile().setTimeSteps(50);
		simulator.run();
		assertEquals((Double) 100.0, simulator.getTime());
		assert(Math.abs(-1.40885- ((InformationSource) model2.get("X"))
				.getValue())<0.001);
		System.out.println(((InformationSource) model2.get("X"))
				.getValue());
		
//		JFreeChart chart = ChartFactory.createXYLineChart(
//				"Modelo Oscilat�rio", // chart title
//				"time", // x axis label
//				"value", // y axis label
//				JFreeChartDataConverter.toXYDataSet(data), // data
//				PlotOrientation.VERTICAL,
//				true, // include legend
//				true, // tooltips
//				false // urls
//		);
//		JFrame frame = new JFrame();
//		frame.add(new ChartPanel( chart));
//		frame.setPreferredSize(new Dimension(640,480));
//		frame.pack();
//		frame.setLocationRelativeTo(null);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//		System.out.println();
	}

}
