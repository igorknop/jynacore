package br.ufjf.mmc.jynacore.metamodel.instance.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import br.ufjf.mmc.jynacore.JynaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenario;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioStorer;
import br.ufjf.mmc.jynacore.metamodel.MetaModelStorer;
import br.ufjf.mmc.jynacore.metamodel.exceptions.instance.MetaModelInstanceException;
import br.ufjf.mmc.jynacore.metamodel.impl.JDOMMetaModelScenarioStorer;
import br.ufjf.mmc.jynacore.metamodel.impl.JDOMMetaModelStorer;
import br.ufjf.mmc.jynacore.metamodel.impl.YamlMetaModelStorer;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceProperty;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceSingleRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceTable;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceScenarioConnect;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceStorer;

public class DefaultMetaModelInstanceStorerYAML implements MetaModelInstanceStorer {

	@Override
	public MetaModelInstance loadFromFile(File fileName) throws Exception {
		MetaModelInstance modelInstance = new DefaultMetaModelInstance();
		Yaml yaml = new Yaml();
		InputStream input = new FileInputStream(fileName);
		Map<String, Object> root = yaml.load(input);
		input.close();

		if (root == null) {
			return modelInstance;
		}

		String metaModelFileName = (String) root.get("metaModelFile");
		File metaModelFile = new File(fileName.getParent(), metaModelFileName);

		MetaModelStorer metaModelStorer;
		if (metaModelFileName != null && metaModelFileName.endsWith(".jym")) {
			metaModelStorer = new YamlMetaModelStorer();
		} else {
			metaModelStorer = new JDOMMetaModelStorer();
		}
		MetaModel metaModel = metaModelStorer.loadFromFile(metaModelFile);
		modelInstance.setMetaModel(metaModel);

		modelInstance.setName((String) root.get("name"));
		modelInstance.setMetaModelFileName(metaModelFileName);

		List<Map<String, Object>> classInstancesList = (List<Map<String, Object>>) root.get("classInstances");
		if (classInstancesList != null) {
			for (Map<String, Object> ciMap : classInstancesList) {
				String classInstanceName = (String) ciMap.get("name");
				String classInstanceClass = (String) ciMap.get("class");
				ClassInstance classInstance = modelInstance.addNewClassInstance(classInstanceName, classInstanceClass);

				List<Map<String, Object>> itemsList = (List<Map<String, Object>>) ciMap.get("items");
				if (itemsList != null) {
					for (Map<String, Object> itemMap : itemsList) {
						String type = (String) itemMap.get("type");
						if ("property".equals(type)) {
							String propName = (String) itemMap.get("name");
							Object val = itemMap.get("value");
							if (val != null) {
								classInstance.setProperty(propName, Double.valueOf(val.toString()));
							}
						} else if ("stock".equals(type)) {
							String stockName = (String) itemMap.get("name");
							Object val = itemMap.get("value");
							ClassInstanceStock stock = (ClassInstanceStock) classInstance.get(stockName);
							if (stock != null && val != null) {
								stock.setValue(Double.valueOf(val.toString()));
							}
						} else if ("table".equals(type)) {
							String tableName = (String) itemMap.get("name");
							ClassInstanceTable table = (ClassInstanceTable) classInstance.get(tableName);
							if (table != null) {
								table.getValues().clear();
								List<Object> vals = (List<Object>) itemMap.get("values");
								if (vals != null) {
									for (Object v : vals) {
										table.getValues().add(Double.valueOf(v.toString()));
									}
								}
							}
						}
					}
				}
			}

			// Set relations
			for (Map<String, Object> ciMap : classInstancesList) {
				String classInstanceName = (String) ciMap.get("name");
				ClassInstance classInstance = modelInstance.getClassInstances().get(classInstanceName);
				List<Map<String, Object>> itemsList = (List<Map<String, Object>>) ciMap.get("items");
				if (itemsList != null) {
					for (Map<String, Object> itemMap : itemsList) {
						String type = (String) itemMap.get("type");
						if ("singleRelation".equals(type)) {
							String relName = (String) itemMap.get("name");
							String relTarget = (String) itemMap.get("target");
							classInstance.setLink(relName, relTarget);
						} else if ("multiRelation".equals(type)) {
							String relName = (String) itemMap.get("name");
							List<String> targets = (List<String>) itemMap.get("targets");
							if (targets != null) {
								for (String target : targets) {
									classInstance.setLink(relName, target);
								}
							}
						}
					}
				}
			}
		}

		// Scenarios
		Map<String, Object> scenariosMap = (Map<String, Object>) root.get("scenarios");
		if (scenariosMap != null) {
			List<String> filesList = (List<String>) scenariosMap.get("files");
			if (filesList != null) {
				for (String scenarioFileName : filesList) {
					File scenarioFile = new File(fileName.getParent(), scenarioFileName);
					MetaModelScenarioStorer metaModelScenarioStorer = new JDOMMetaModelScenarioStorer();
					MetaModelScenario scenario = metaModelScenarioStorer.loadFromFile(scenarioFile, metaModel);
					metaModel.putScenario(scenario);
				}
			}

			List<Map<String, Object>> connectsList = (List<Map<String, Object>>) scenariosMap.get("connects");
			if (connectsList != null) {
				for (Map<String, Object> connMap : connectsList) {
					String instanceName = (String) connMap.get("instance");
					String scenarioName = (String) connMap.get("scenario");
					String connectName = (String) connMap.get("name");
					ClassInstance instance = modelInstance.getClassInstances().get(instanceName);
					if (instance != null) {
						instance.setScenarioConnection(scenarioName, connectName);
					}
				}
			}
		}

		return modelInstance;
	}

	@Override
	public void saveToFile(MetaModelInstance modelInstance, File fileName) throws Exception {
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("name", modelInstance.getName());
		root.put("metaModelFile", modelInstance.getMetaModelFileName());

		List<Map<String, Object>> classInstancesList = new ArrayList<Map<String, Object>>();
		for (Entry<String, ClassInstance> entry : modelInstance.getClassInstances().entrySet()) {
			classInstancesList.add(getClassInstanceMap(entry.getKey(), entry.getValue()));
		}
		root.put("classInstances", classInstancesList);

		Map<String, Object> scenariosMap = new HashMap<String, Object>();
		Set<String> scenarioFiles = new HashSet<String>();
		if (modelInstance.getMetaModel() != null && modelInstance.getMetaModel().getScenarios() != null) {
			for (Entry<String, MetaModelScenario> e : modelInstance.getMetaModel().getScenarios().entrySet()) {
				scenarioFiles.add(e.getValue().getFileName());
			}
		}
		scenariosMap.put("files", new ArrayList<String>(scenarioFiles));

		List<Map<String, Object>> connectsList = new ArrayList<Map<String, Object>>();
		if (modelInstance.getScenariosConnects() != null) {
			for (MetaModelInstanceScenarioConnect c : modelInstance.getScenariosConnects()) {
				Map<String, Object> connMap = new HashMap<String, Object>();
				connMap.put("scenario", c.getScenario().getName());
				connMap.put("name", c.getConnect().getName());
				connMap.put("instance", c.getInstance().getName());
				connectsList.add(connMap);
			}
		}
		scenariosMap.put("connects", connectsList);
		root.put("scenarios", scenariosMap);

		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setPrettyFlow(true);
		Yaml yaml = new Yaml(options);

		Writer writer = new FileWriter(fileName);
		yaml.dump(root, writer);
		writer.close();
	}

	private Map<String, Object> getClassInstanceMap(String classInstanceName, ClassInstance classInstance) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", classInstance.getName());
		map.put("class", classInstance.getMetaModelClass().getName());

		List<Map<String, Object>> itemsList = new ArrayList<Map<String, Object>>();

		for (Entry<String, ClassInstanceItem> entry : classInstance.entrySet()) {
			ClassInstanceItem item = entry.getValue();
			Map<String, Object> itemMap = new HashMap<String, Object>();

			if (item instanceof ClassInstanceProperty) {
				ClassInstanceProperty prop = (ClassInstanceProperty) item;
				itemMap.put("type", "property");
				itemMap.put("name", prop.getName());
				itemMap.put("value", prop.getValue());
				itemsList.add(itemMap);
			} else if (item instanceof ClassInstanceStock) {
				ClassInstanceStock stock = (ClassInstanceStock) item;
				itemMap.put("type", "stock");
				itemMap.put("name", stock.getName());
				itemMap.put("value", stock.getValue());
				itemsList.add(itemMap);
			} else if (item instanceof ClassInstanceSingleRelation) {
				ClassInstanceSingleRelation rel = (ClassInstanceSingleRelation) item;
				itemMap.put("type", "singleRelation");
				itemMap.put("name", rel.getName());
				if (rel.getTarget() != null) {
					itemMap.put("target", rel.getTarget().getName());
				}
				itemsList.add(itemMap);
			} else if (item instanceof ClassInstanceMultiRelation) {
				ClassInstanceMultiRelation rel = (ClassInstanceMultiRelation) item;
				itemMap.put("type", "multiRelation");
				itemMap.put("name", rel.getName());
				List<String> targetNames = new ArrayList<String>();
				if (rel.getTargets() != null) {
					for (Entry<String, ClassInstance> tEntry : rel.getTargets().entrySet()) {
						targetNames.add(tEntry.getValue().getName());
					}
				}
				itemMap.put("targets", targetNames);
				itemsList.add(itemMap);
			} else if (item instanceof ClassInstanceTable) {
				ClassInstanceTable table = (ClassInstanceTable) item;
				itemMap.put("type", "table");
				itemMap.put("name", table.getName());
				itemMap.put("values", new ArrayList<Double>(table.getValues()));
				itemsList.add(itemMap);
			}
		}

		map.put("items", itemsList);
		return map;
	}

	public void saveToFile(JynaModel model, File fileName) throws Exception {
		saveToFile((MetaModelInstance) model, fileName);
	}

	@Override
	public JynaModel load(URI modelURI) throws Exception {
		return loadFromFile(new File(modelURI));
	}

	@Override
	public void save(JynaModel model, URI modelURI) throws Exception {
		saveToFile(model, new File(modelURI));
	}
}
