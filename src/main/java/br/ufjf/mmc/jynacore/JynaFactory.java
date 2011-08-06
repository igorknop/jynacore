package br.ufjf.mmc.jynacore;

import java.net.URI;

public interface JynaFactory {

	JynaModelStorer newModelStorer();

	JynaSimulation newModelSimulation();

	JynaSimulationMethod newModelMethod();

	JynaSimulationProfile newSimulationProfile();

	JynaSimulationData newSimulationData();

	void setTypeByURI(URI uri) throws Exception;

}
