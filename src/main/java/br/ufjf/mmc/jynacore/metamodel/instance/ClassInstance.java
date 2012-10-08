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
package br.ufjf.mmc.jynacore.metamodel.instance;

import java.util.List;
import java.util.Map;

import br.ufjf.mmc.jynacore.JynaItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenario;
import br.ufjf.mmc.jynacore.metamodel.exceptions.instance.MetaModelInstanceException;
import br.ufjf.mmc.jynacore.metamodel.exceptions.instance.MetaModelInstanceInvalidLinkException;
import br.ufjf.mmc.jynacore.metamodel.exceptions.instance.MetaModelInstanceInvalidPropertyException;

/**
 * @author Knop
 *
 */
public interface ClassInstance extends Map<String, ClassInstanceItem>, JynaItem {
	void setMetaModelClass(MetaModelClass newClass);
	MetaModelClass  getMetaModelClass();

	void setMetaModelInstance(MetaModelInstance newMetaModelInstance);
	MetaModelInstance  getMetaModelInstance();
	
	void createFrom(MetaModelClass metaModelClass);
	void setProperty(String propertyName, Double newValue) throws MetaModelInstanceInvalidPropertyException;
	Double getProperty(String propertyName) throws MetaModelInstanceInvalidPropertyException, Exception;
	
	void setLink(String relationName, String targetName) throws MetaModelInstanceInvalidLinkException;
	void setLink(String relationName, String targetName, String targetRole) throws MetaModelInstanceInvalidLinkException;
	
	void setScenarioConnection(String scenarioName, String connectionName) throws MetaModelInstanceException, Exception;
	List<MetaModelScenario> getConnectedScenarios();

}
