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
package br.ufjf.mmc.jynacore.example;

import br.ufjf.mmc.jynacore.JynaSimulableModel;
import br.ufjf.mmc.jynacore.JynaSimulation;
import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaSimulationMethod;
import br.ufjf.mmc.jynacore.JynaSimulationProfile;
import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.impl.DefaultExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNumberConstantExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultReferenceExpression;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationData;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationProfile;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.InfiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.Information;
import br.ufjf.mmc.jynacore.systemdynamics.Rate;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.Variable;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultFiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultInfiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultInformation;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultRate;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultSystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultVariable;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsEulerMethod;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsSimulation;

/**
 * @author Knop
 * 
 */
public class SDModelSimulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			JynaSimulationProfile profile = new DefaultSimulationProfile();
			JynaSimulationData data = new DefaultSimulationData();
			JynaSimulation simulation = new DefaultSystemDynamicsSimulation();
			JynaSimulationMethod method = new DefaultSystemDynamicsEulerMethod();

			SystemDynamicsModel modelSD = new DefaultSystemDynamicsModel();
			modelSD.setName("Crescimento de Usuários");

			FiniteStock users = new DefaultFiniteStock();
			users.setName("Usuários");
			users.setInitialValue(10.0);
			modelSD.put(users);

			InfiniteStock userPool = new DefaultInfiniteStock();
			userPool.setName("Fonte de Usuários");
			modelSD.put(userPool);

			Variable incFactor = new DefaultVariable();
			incFactor.setName("Taxa de Adesão");
			incFactor.setExpression(new DefaultNumberConstantExpression(0.05));
			modelSD.put(incFactor);

			Rate userInc = new DefaultRate();
			userInc.setName("Adesão");
			userInc.setSource(userPool);
			userInc.setTarget(users);
			userInc.setSourceAndTarget(userPool, users);
			
			Expression exp = new DefaultExpression();
			exp.setOperator(NumberOperator.TIMES);
			exp.setLeftOperand(new DefaultReferenceExpression(incFactor));
			exp.setRightOperand(new DefaultReferenceExpression(users));
			userInc.setExpression(exp);
			modelSD.put(userInc);
			
			Information info1 = new DefaultInformation(users,userInc);
			modelSD.put(info1);
			Information info2 = new DefaultInformation(incFactor,userInc);
			modelSD.put(info2);
			
			
			profile.setTimeLimits(360, 36.0);

			simulation.setProfile(profile);
			simulation.setMethod(method);
			data.addAll(((JynaSimulableModel) modelSD).getAllJynaValued());
			simulation.setSimulationData(data);
			simulation.setModel((JynaSimulableModel) modelSD);
			simulation.reset();
			data.register(0.0);
			simulation.run();
			//data.register(36.0);
			System.out.println(data);
		} catch (Exception e) {
			System.err.println(e.getCause());
			e.printStackTrace();
		}

	}

}
