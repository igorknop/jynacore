/**
 * *****************************************************************************
 * Copyright (c) 2009 Igor Knop. This file is part of JynaCore.
 *
 * JynaCore is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * JynaCore is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JynaCore. If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************
 */
package br.ufjf.mmc.jynacore.tests.systemdynamics;

import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaSimulationProfile;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNumberConstantExpression;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationData;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationProfile;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.InfiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.Rate;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultFiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultInfiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultRate;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultSystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulation;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsEulerMethod;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsSimulation;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SimpleSimulatorTest {

   private SystemDynamicsModel model;
   private FiniteStock finLevel;
   private InfiniteStock infLevel;
   private Rate cteRate;

   @Before
   public void setUp() throws Exception {
      model = new DefaultSystemDynamicsModel();

      finLevel = new DefaultFiniteStock();
      finLevel.setInitialValue(0.0);

      infLevel = new DefaultInfiniteStock();

      cteRate = new DefaultRate();
      cteRate.setExpression(new DefaultNumberConstantExpression(1.0));

      cteRate.setSourceAndTarget(infLevel, finLevel);

      model.put("Stock", finLevel);
      model.put("Source", infLevel);
      model.put("Rate", cteRate);

      //method = new DefaultSystemDynamicsEulerMethod();
      //method.setInitialTime(0.0);
      //method.setStepSize(1.0);

   }

   @Test
   public final void testRun() throws Exception {
      SystemDynamicsSimulation simulator = new DefaultSystemDynamicsSimulation();
      JynaSimulationData data = new DefaultSimulationData();
      data.add("Stock", finLevel);

      JynaSimulationProfile profile = new DefaultSimulationProfile();
      profile.setTimeSteps(10);
      profile.setTimeInterval(1.0);
      profile.setInitialTime(0.0);
      simulator.setProfile(profile);
      simulator.setMethod(new DefaultSystemDynamicsEulerMethod());
      simulator.setModel(model);
      simulator.setSimulationData(data);
      simulator.run();
      assertEquals(10.0, data.getValue(0, data.getWatchedSize()-1));

      simulator.getProfile().setTimeSteps(9);
      simulator.run();
      assertEquals(19.0, data.getValue(0, 18));
   }
}
