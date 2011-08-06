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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;

import br.ufjf.mmc.jynacore.JynaModel;
import br.ufjf.mmc.jynacore.JynaModelStorer;
import br.ufjf.mmc.jynacore.JynaSimulableModel;
import br.ufjf.mmc.jynacore.JynaSimulation;
import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaSimulationMethod;
import br.ufjf.mmc.jynacore.JynaSimulationProfile;
import br.ufjf.mmc.jynacore.JynaValued;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationData;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationProfile;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceStorer;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstanceStorerJDOM;
import br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceEulerMethod;
import br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceSimulation;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsItem;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModelStorer;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultSystemDynamicsModelStorerJDOM;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsEulerMethod;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsSimulation;

/**
 * @author Knop
 * 
 */
public class SysoutSimulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			URI uri = new URI(args[0]);
//			URI uri = new URI("file:///C:/Users/Knop/workspace/SPDS_jynacore/src/test/resources/sd/fullOscilatory.jyna");
//			 URI uri = new URI(
//			 "file:///C:/Users/Knop/workspace/SPDS_jynacore/src/test/resources/simpleSoftwareProjectInstance2.jymmi");
			JynaModelStorer storer;
			JynaModel model;
			JynaSimulation simulation;
			JynaSimulationMethod method;
			JynaSimulationProfile profile = new DefaultSimulationProfile();
			JynaSimulationData data = new DefaultSimulationData();

			if (uri.getPath().contains(
					MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION)) {
				simulation = new DefaultMetaModelInstanceSimulation();
				storer = new DefaultMetaModelInstanceStorerJDOM();
				method = new DefaultMetaModelInstanceEulerMethod();
				model = storer.load(uri);
				MetaModelInstance modelMMI = (MetaModelInstance) model;
				for (Entry<String, ClassInstance> e : modelMMI
						.getClassInstances().entrySet()) {
					for (Entry<String, ClassInstanceItem> ei : e.getValue()
							.entrySet()) {
						if (ei.getValue() instanceof JynaValued) {
							data.add(e.getKey() + "." + ei.getKey(),
									(JynaValued) ei.getValue());
						}
					}

				}
			} else if (uri.getPath().contains(
					SystemDynamicsModelStorer.SYSTEM_DYNAMICS_MODEL_EXTENSION)) {
				simulation = new DefaultSystemDynamicsSimulation();
				storer = new DefaultSystemDynamicsModelStorerJDOM();
				method = new DefaultSystemDynamicsEulerMethod();
				model = storer.load(uri);
				SystemDynamicsModel modelSD = (SystemDynamicsModel) model;
				for (SystemDynamicsItem item : modelSD.getItems().values()) {
					if (item instanceof JynaValued) {
						JynaValued valued = (JynaValued) item;
						data.add(valued);
					}
				}
			} else {
				throw new Exception("Unknow file type");
			}
			profile.setTimeLimits(1.0, 100);
			simulation.setProfile(profile);
			simulation.setMethod(method);
			simulation.setModel((JynaSimulableModel) model);
			simulation.setSimulationData(data);
			simulation.reset();
			simulation.run();
			System.out.println(data);
		} catch (URISyntaxException e) {
			System.err.println("Invalid URI passed as param! " + args[1]);
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println(e.getCause());
			e.printStackTrace();
		}

	}

}
