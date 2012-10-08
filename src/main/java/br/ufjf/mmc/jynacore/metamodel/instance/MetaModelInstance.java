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
package br.ufjf.mmc.jynacore.metamodel.instance;

import java.util.List;
import java.util.Map;

import br.ufjf.mmc.jynacore.JynaSimulableModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModel;

public interface MetaModelInstance extends JynaSimulableModel{
	public void setName(String newName);
	public String getName();
	public void setMetaModel(MetaModel newModel);
	public MetaModel getMetaModel();
	public Map<String,ClassInstance> getClassInstances();
	public  void setClassInstances(Map<String,ClassInstance> newInstances);
	public ClassInstance addNewClassInstance(String instanceName, String className) throws Exception;
	public void updateReferences() throws Exception;
	public String getMetaModelFileName();
	public void setMetaModelFileName(String newFileName);
	public List<MetaModelInstanceScenarioConnect> getScenariosConnects();

   public void setTimeStepConstant(ClassInstanceProperty _TIME_);

   public void setTimeConstant(ClassInstanceProperty _TIME_STEP_);
}
