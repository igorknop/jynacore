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
package br.ufjf.mmc.jynacore.metamodel.instance.impl;

import java.util.Map.Entry;

import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassProperty;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassRate;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassStock;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassTable;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceProperty;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceTable;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceFactory;

/**
 * @author Knop
 * 
 */
public class DefaultMetaModelInstanceFactory implements
		MetaModelInstanceFactory {

	/*
	 * Para criar uma inst�ncia de uma classe: <ol> <li>Para cada instancia
	 * criar todos os stock,property,proc e rate</li> <li>Para cada rate setar o
	 * affected(target) local</li>
	 * 
	 * @seebr.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceFactory#
	 * createClassInstance()
	 */
	@Override
	public ClassInstance createClassInstance(String instanceName,
			MetaModelClass metaModelClass) {
		DefaultClassInstance newInstance = new DefaultClassInstance();

		newInstance.setName(instanceName);
		newInstance.setMetaModelClass(metaModelClass);

		for (Entry<String, MetaModelClassItem> entry : metaModelClass
				.entrySet()) {
			MetaModelClassItem mmcItem = entry.getValue();
			if (mmcItem instanceof MetaModelClassProperty) {
				MetaModelClassProperty mmcProp = (MetaModelClassProperty) mmcItem;
				ClassInstanceProperty ciPro = (ClassInstanceProperty) mmcProp
						.getNewInstance();
				newInstance.put(ciPro.getName(), ciPro);
			}
			if (mmcItem instanceof MetaModelClassStock) {
				MetaModelClassStock mmcStock = (MetaModelClassStock) mmcItem;
				ClassInstanceStock ciSto = (ClassInstanceStock) mmcStock
						.getNewInstance();
				newInstance.put(ciSto.getName(), ciSto);
			}
			if (mmcItem instanceof MetaModelClassAuxiliary) {
				MetaModelClassAuxiliary mmcProc = (MetaModelClassAuxiliary) mmcItem;
				ClassInstanceAuxiliary ciProc = (ClassInstanceAuxiliary) mmcProc
						.getNewInstance();
				newInstance.put(ciProc.getName(), ciProc);
			}
			if (mmcItem instanceof MetaModelClassRate) {
				MetaModelClassRate mmcRate = (MetaModelClassRate) mmcItem;
				ClassInstanceRate ciRat = (ClassInstanceRate) mmcRate
						.getNewInstance();
				newInstance.put(ciRat.getName(), ciRat);
			}
			if (mmcItem instanceof DefaultMetaModelClassTable) {
				DefaultMetaModelClassTable mmcTable = (DefaultMetaModelClassTable) mmcItem;
				ClassInstanceTable ciTable = (ClassInstanceTable) mmcTable
						.getNewInstance();
				newInstance.put(ciTable.getName(), ciTable);
			}
		}
		// Busca por referencias locais das taxas
		for (Entry<String, MetaModelClassItem> entry : metaModelClass
				.entrySet()) {
			MetaModelClassItem mmcItem = entry.getValue();
			if (mmcItem instanceof MetaModelClassRate) {
				MetaModelClassRate mmcRate = (MetaModelClassRate) mmcItem;
				MetaModelClassItem rTarget = mmcRate.getTarget();
				if (rTarget != null) {
					ClassInstanceItem ciItem = newInstance.get(rTarget
							.getName());
					ClassInstanceStock ciStock = (ClassInstanceStock) ciItem;

					if (ciStock != null) {
						ClassInstanceItem ciRate = newInstance.get(mmcRate
								.getName());
						((ClassInstanceRate) ciRate).setTarget(ciStock);
					}
				}
				MetaModelClassItem rSource = mmcRate.getSource();
				if (rSource != null) {
					ClassInstanceItem ciItem = newInstance.get(rSource
							.getName());
					ClassInstanceStock ciStock = (ClassInstanceStock) ciItem;

					if (ciStock != null) {
						ClassInstanceItem ciRate = newInstance.get(mmcRate
								.getName());
						((ClassInstanceRate) ciRate).setSource(ciStock);
					}
				}
			}
		}

		return newInstance;
	}

	/*
	 * Para criar uma inst�ncia de modelo os seguintes passos s�o tomados: <ol>
	 * <li>Criar todas instancias de classes</li> <li>Para cada instancia criar
	 * todos os stock,property,proc e rate</li> <li>Para cada rate setar o
	 * affected(target) local</li> <li>Fazer todas as liga��es das rela��es</li>
	 * <li>Para cada rate setar os affected(target) externos</li> <li>Para cada
	 * equa��o atribuir o Name a uma refer�ncia</li> <li>Para cada equa��o
	 * atribuir uma refer�ncia externa com base ans liga��es</li> <li>Para cade
	 * SET definir o valor inicial para as property</li> </ol>
	 * 
	 * @seebr.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceFactory#
	 * createMetaModelInstance()
	 */
	@Override
	public MetaModelInstance createMetaModelInstance() {
		MetaModelInstance newInstance = new DefaultMetaModelInstance();
		return newInstance;
	}

}
