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

import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModel;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.parse.MetaModelInstanceParser;

/**
 * @author Knop
 *
 */
@Deprecated
public class DefaultMetaModelInstanceParser implements MetaModelInstanceParser {

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.parse.MetaModelInstanceParser#loadFromFile(java.io.File)
	 */
	@Override
	public MetaModelInstance loadFromFile(File metaModelInstanceFile)
			throws Exception {
		MetaModelInstance modelInstance = new DefaultMetaModelInstance();
		MetaModel model = new DefaultMetaModel();
		
		Scanner reader = new Scanner(metaModelInstanceFile);
		StringBuilder sb = new StringBuilder();
		while (reader.hasNextLine()) {
			sb.append(reader.nextLine());
		}
		String code = sb.toString();
		//FIX ME: How UserModelCompiler can gen a MetaModel?
		UserModelCompiler compiler = new UserModelCompiler(modelInstance,model);
		compiler.setCode(code);
		return modelInstance;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.parse.MetaModelInstanceParser#loadFromXMLFile(java.io.File)
	 */
	@Override
	public MetaModelInstance loadFromXMLFile(File metaModelInstanceFile)
			throws Exception {
		throw new UnsupportedOperationException();
//		return null;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.parse.MetaModelInstanceParser#saveToFile(br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance, java.io.File)
	 */
	@Override
	public void saveToFile(MetaModelInstance metaModelInstance,
			File metaModelFile) throws Exception {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.metamodel.parse.MetaModelInstanceParser#saveToXMLFile(br.ufjf.mmc.jynacore.metamodel.MetaModel, java.io.File)
	 */
	@Override
	public void saveToXMLFile(MetaModel metaModelInstance,
			File metaModelInstanceFile) throws Exception {
		throw new UnsupportedOperationException();
	}

}
