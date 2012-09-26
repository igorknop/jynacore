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

import java.io.File;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject.MediumSoftwareProject;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.parse.impl.UserModelCompiler;

public class ExpertModelCompilerTest {

	private File userFile;
	private String userCode;
	private UserModelCompiler compiler;
	private MetaModelInstance instance;
	private MediumSoftwareProject model;

	@Before
	public void setUp() throws Exception {
		userFile = new File(
				"C:\\Users\\Knop\\workspace\\SPDS\\src\\test\\resources\\sintaxe_user_model.txt");
		Scanner reader = new Scanner(userFile);
		StringBuilder sb = new StringBuilder();
		while (reader.hasNextLine()) {
			sb.append(reader.nextLine());
		}
		userCode = sb.toString();
		instance = new DefaultMetaModelInstance();
		model = new MediumSoftwareProject();
		compiler = new UserModelCompiler(instance,model);

	}

	@Test
	public void testCompiler() {
		compiler.setCode(userCode);
		assertEquals(-1, compiler.princ());
	}

}
