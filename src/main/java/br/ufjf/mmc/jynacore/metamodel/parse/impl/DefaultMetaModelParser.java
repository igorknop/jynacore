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
package br.ufjf.mmc.jynacore.metamodel.parse.impl;

import java.io.File;
import java.util.Scanner;

import com.sun.org.apache.xml.internal.utils.UnImplNode;

import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModel;
import br.ufjf.mmc.jynacore.metamodel.parse.MetaModelParser;

/**
 * @author Knop
 *
 */
@Deprecated
public class DefaultMetaModelParser implements MetaModelParser {

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.parse.MetaModelParser#loadFromFile(java.io.File)
	 */
	@Override
	public MetaModel loadFromFile(File metaModelFile) throws Exception {
		MetaModel model = new DefaultMetaModel();
		
		Scanner reader = new Scanner(metaModelFile);
		StringBuilder sb = new StringBuilder();
		while (reader.hasNextLine()) {
			sb.append(reader.nextLine());
		}
		String code = sb.toString();
		//FIX ME: How ExpertModelCompiler can gen a MetaModel?
		ExpertModelCompiler compiler = new ExpertModelCompiler();
		compiler.setCode(code);
		return model;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.parse.MetaModelParser#loadFromXMLFile(java.io.File)
	 */
	@Override
	public MetaModel loadFromXMLFile(File metaModelFile) throws Exception {
		throw new UnsupportedOperationException();
		
//		return null;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.parse.MetaModelParser#saveToFile(br.ufjf.mmc.jynacore.metamodel.MetaModel, java.io.File)
	 */
	@Override
	public void saveToFile(MetaModel metaModel, File metaModelFile)
			throws Exception {
		
		throw new UnsupportedOperationException();

	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.parse.MetaModelParser#saveToXMLFile(br.ufjf.mmc.jynacore.metamodel.MetaModel, java.io.File)
	 */
	@Override
	public void saveToXMLFile(MetaModel metaModel, File metaModelFile)
			throws Exception {
		throw new UnsupportedOperationException();

	}

}
