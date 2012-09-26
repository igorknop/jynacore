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

import br.ufjf.mmc.jynacore.metamodel.parse.impl.ExpertModelCompiler;

public class UserModelCompilerTest {

	private File expertFile;
	private String expertCode;
	private ExpertModelCompiler compiler;

	@Before
	public void setUp() throws Exception {
		expertFile = new File(
				"C:\\Users\\Knop\\workspace\\SPDS\\src\\test\\resources\\sintaxe_expert_model.txt");
		Scanner reader = new Scanner(expertFile);
		StringBuilder sb = new StringBuilder();
		while (reader.hasNextLine()) {
			sb.append(reader.nextLine());
		}
		expertCode = sb.toString();
		compiler = new ExpertModelCompiler();

	}

	@Test
	public void testCompiler() {
		compiler.setCode(expertCode);
		assertEquals(-1, compiler.princ());
	}

}
