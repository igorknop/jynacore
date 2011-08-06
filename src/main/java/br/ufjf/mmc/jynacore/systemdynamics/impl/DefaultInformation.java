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
package br.ufjf.mmc.jynacore.systemdynamics.impl;

import br.ufjf.mmc.jynacore.systemdynamics.Information;
import br.ufjf.mmc.jynacore.systemdynamics.InformationConsumer;
import br.ufjf.mmc.jynacore.systemdynamics.InformationSource;

/**
 * @author Knop
 * 
 */
public class DefaultInformation implements Information {

	private InformationConsumer consumer;
	private InformationSource source;
	private String name;

	/**
	 * Default Constructor
	 */
	public DefaultInformation(InformationSource source,
			InformationConsumer consumer) {
		name = "";
		this.source = source;
		this.consumer = consumer;
		consumer.addInformation(source.getName(), source);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.Information#getConsumer()
	 */
	@Override
	public InformationConsumer getConsumer() {
		return consumer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.Information#getSource()
	 */
	@Override
	public InformationSource getSource() {
		return source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.Information#setConsumer(br.ufjf.mmc.jynacore.InformationConsumer)
	 */
	@Override
	public void setConsumer(InformationConsumer newConsumer) {
		consumer.removeInformation(this.source);
		newConsumer.addInformation(this.source.getName(), this.source);
		consumer = newConsumer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.Information#setSource(br.ufjf.mmc.jynacore.InformationSource)
	 */
	@Override
	public void setSource(InformationSource newSource) {
		consumer.removeInformation(this.source);
		consumer.addInformation(newSource.getName(), newSource);
		source = newSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.SystemDynamicsObject#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.SystemDynamicsObject#setName(java.lang.String)
	 */
	@Override
	public void setName(String newName) {
		name = newName;
	}

	@Override
	public void setLink(InformationSource source, InformationConsumer consumer) {
		setSource(source);
		setConsumer(consumer);
	}

	@Override
	public String toString() {

		return getName() == null ? getSource().toString() + " to "
				+ getConsumer().toString() : getName();
	}
}
