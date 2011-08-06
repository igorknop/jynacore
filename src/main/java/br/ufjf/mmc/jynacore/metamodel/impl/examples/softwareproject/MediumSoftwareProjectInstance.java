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
package br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject;

import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstance;

/**
 * @author Knop
 *
 */
public class MediumSoftwareProjectInstance extends DefaultMetaModelInstance {
	private ClassInstance D1;
	private ClassInstance D2;
	private ClassInstance Designing;
	private ClassInstance AnalisysModel;
	private ClassInstance DesignModel;
	private ClassInstance SourceCode;
	private ClassInstance Coding;

	public MediumSoftwareProjectInstance() {
		super();
		setMetaModel(new MediumSoftwareProject());
		setName("Medium Software Project Instance");
		setMetaModelFileName("mediumSoftwareProject-save.jymm");
		try {
			D1 = addNewClassInstance("D1", "Developer");
			//getClassInstances().get("D1");
			D1.setProperty("experience",1.0);
			
			addNewClassInstance("D2", "Developer");
			D2 = getClassInstances().get("D2");
			D2.setProperty("experience",0.8);
			
			addNewClassInstance("AnalisysModel", "Artifact");
			AnalisysModel = getClassInstances().get("AnalisysModel");
			AnalisysModel.setProperty("latent_errors", 10.0);
			
			addNewClassInstance("DesignModel", "Artifact");
			DesignModel = getClassInstances().get("DesignModel");
			DesignModel.setProperty("latent_errors", 0.0);

			addNewClassInstance("SourceCode", "Artifact");
			SourceCode = getClassInstances().get("SourceCode");
			SourceCode.setProperty("latent_errors", 0.0);
			

			addNewClassInstance("Designing", "Activity");
			Designing = getClassInstances().get("Designing");
			Designing.setProperty("duration", 10.0);
			Designing.setLink("Team","D1");
			Designing.setLink("Income","AnalisysModel");
			Designing.setLink("Outcome","DesignModel");
			
			addNewClassInstance("Coding", "Activity");
			Coding = getClassInstances().get("Coding");
			Coding.setProperty("duration", 5.0);
			Coding.setLink("Team","D2");
			Coding.setLink("Precedence","Designing");
			Coding.setLink("Income","DesignModel");
			Coding.setLink("Outcome","SourceCode");
			

			updateReferences();
			System.out.println();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
