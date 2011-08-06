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
package br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject.simple;

import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstance;

/**
 * @author Knop
 *
 */
public class SimpleSoftwareProjectInstance extends DefaultMetaModelInstance {
	private ClassInstance D1;
	private ClassInstance D2;
	private ClassInstance Coding;

	public SimpleSoftwareProjectInstance() {
		super();
		setMetaModel(new SimpleSoftwareProject());
		setName("Simple Software Project Instance");
		setMetaModelFileName("simpleSoftwareProject.jymm");
		try {
			
			D2 = addNewClassInstance("D2", "Developer");
			D2.setProperty("experience",1.0);
			
			D1 = addNewClassInstance("D1", "Developer");
			D1.setProperty("experience",0.8);

			Coding = addNewClassInstance("Coding", "Activity");
			Coding.setProperty("duration", 25.0);
			Coding.setLink("Team","D1");
			Coding.setLink("Team","D2");
			
			updateReferences();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
