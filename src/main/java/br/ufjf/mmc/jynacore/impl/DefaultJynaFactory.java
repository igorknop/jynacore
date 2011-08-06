package br.ufjf.mmc.jynacore.impl;

import java.net.URI;

import br.ufjf.mmc.jynacore.JynaFactory;
import br.ufjf.mmc.jynacore.JynaModelStorer;
import br.ufjf.mmc.jynacore.JynaSimulation;
import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaSimulationMethod;
import br.ufjf.mmc.jynacore.JynaSimulationProfile;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceStorer;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstanceStorerJDOM;
import br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceEulerMethod;
import br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceSimulation;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModelStorer;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultSystemDynamicsModelStorerJDOM;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsEulerMethod;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsSimulation;

public class DefaultJynaFactory implements JynaFactory {
	public final static String SYSTEM_DYNAMICS = "jyna";
	public final static String METAMODEL = "jymmi";
	private String type = SYSTEM_DYNAMICS;

	@Override
	public JynaSimulationMethod newModelMethod() {
		if (type.equals(SYSTEM_DYNAMICS)) {
			return new DefaultSystemDynamicsEulerMethod();
		} else if (type.equals(METAMODEL)) {
			return new DefaultMetaModelInstanceEulerMethod();
		}
		return null;
	}

	@Override
	public JynaSimulation newModelSimulation() {
		if (type.equals(SYSTEM_DYNAMICS)) {
			return new DefaultSystemDynamicsSimulation();
		} else if (type.equals(METAMODEL)) {
			return new DefaultMetaModelInstanceSimulation();
		}
		return null;
	}

	@Override
	public JynaModelStorer newModelStorer() {
		if (type.equals(SYSTEM_DYNAMICS)) {
			return new DefaultSystemDynamicsModelStorerJDOM();
		} else if (type.equals(METAMODEL)) {
			return new DefaultMetaModelInstanceStorerJDOM();
		}
		return null;
	}

	@Override
	public JynaSimulationData newSimulationData() {
		if (type.equals(SYSTEM_DYNAMICS)) {
			return new DefaultSimulationData();
		} else if (type.equals(METAMODEL)) {
			return new DefaultSimulationData();
		}
		return null;
	}

	@Override
	public JynaSimulationProfile newSimulationProfile() {
		if (type.equals(SYSTEM_DYNAMICS)) {
			return new DefaultSimulationProfile();
		} else if (type.equals(METAMODEL)) {
			return new DefaultSimulationProfile();
		}
		return null;
	}

	@Override
	public void setTypeByURI(URI uri) throws Exception {
		if (uri.getPath().contains(
				MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION)) {
			type = METAMODEL;
		} else if (uri.getPath().contains(
				SystemDynamicsModelStorer.SYSTEM_DYNAMICS_MODEL_EXTENSION)) {
			type = SYSTEM_DYNAMICS;
		} else
			throw new Exception("Unknow file type");
	}

}
