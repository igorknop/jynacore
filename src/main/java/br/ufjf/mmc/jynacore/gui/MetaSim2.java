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
package br.ufjf.mmc.jynacore.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.TreeSelectionModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import br.ufjf.mmc.jynacore.JynaSimulation;
import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaSimulationMethod;
import br.ufjf.mmc.jynacore.JynaSimulationProfile;
import br.ufjf.mmc.jynacore.JynaValued;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationData;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationProfile;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceStorer;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstanceStorerJDOM;
import br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceEulerMethod;
import br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceRK4Method;
import br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceSimulation;

/**
 * @author Knop
 * 
 */
@SuppressWarnings("serial")
public class MetaSim2 extends JFrame {
	private static final String METHOD_RK4 = "Runge-Kutta 4";
	private static final String METHOD_EULER = "Euler";
	private static final int STATUS_IDDLE = 1;
	private static final int STATUS_RUNNING = 2;
	private static final int STATUS_PAUSED = 3;
	private static final int STATUS_FINISHED = 4;
	private static final int STATUS_NOMODEL = 0;
	private int status = STATUS_IDDLE;
	private JTextField txtFile;
	private JButton btnLoad;
	private JTextField txtStepSize;
	private JTextField txtStepCount;
	private JTextField txtStepDelay;
	private JComboBox jcoMethod;
	private JButton btnRun;
	private ChartPanel chartpanel;
	protected JFreeChart chart;
	public MetaModelInstance model;
	private ExecutorService executor;
	private JynaSimulationData simulationData;
	private JTree tree;
	private JGraph grafo;
	private JTable table;
	private JScrollPane dataPanel;
	private JPanel pnlControls;
	private JButton btnReset;
	private JButton btnStop;
	private JButton btnPause;

	public MetaSim2() {

		super();
		GridBagConstraints gbc = new GridBagConstraints();
		JTabbedPane tabPane = new JTabbedPane();

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.8;
		gbc.weighty = 0.2;
		gbc.gridwidth = 4;
		txtFile = new JTextField();
		txtFile.setEditable(false);

		mainPanel.add(txtFile, gbc);

		gbc.gridwidth = 1;
		gbc.weightx = 0.2;
		gbc.gridx = 4;
		btnLoad = new JButton("Load model");
		btnLoad.addActionListener(new alBtnLoadModel());
		mainPanel.add(btnLoad, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		txtStepSize = new JTextField("1.0");
		mainPanel.add(txtStepSize, gbc);

		gbc.gridx = 1;
		txtStepCount = new JTextField("100");
		mainPanel.add(txtStepCount, gbc);

		gbc.gridx = 2;
		txtStepDelay = new JTextField("0");
		mainPanel.add(txtStepDelay, gbc);
		String[] methods = { METHOD_EULER, METHOD_RK4 };

		gbc.gridx = 3;
		jcoMethod = new JComboBox(methods);
		jcoMethod.setEditable(false);
		// jcoMethod.setModel(JComboBox.)
		mainPanel.add(jcoMethod, gbc);

		gbc.gridx = 4;
		pnlControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnRun = new JButton("Run");
		btnRun.addActionListener(new alBtnRun());
		pnlControls.add(btnRun);

		btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (status == STATUS_RUNNING)
					setStatus(STATUS_PAUSED);
				else if (status == STATUS_PAUSED)
					setStatus(STATUS_RUNNING);
			}

		});
		pnlControls.add(btnPause);

		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		pnlControls.add(btnStop);

		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		pnlControls.add(btnReset);
		mainPanel.add(pnlControls, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 5;
		gbc.gridheight = 1;
		gbc.weighty = 0.6;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.VERTICAL;
		simulationData = new DefaultSimulationData();

		chart = getEmptyChart();

		chartpanel = new ChartPanel(chart);
		chartpanel.setAutoscrolls(true);
		mainPanel.add(chartpanel, gbc);
		// chartpanel.setMinimumSize(new Dimension(800,800));

		// add(mainPanel,BorderLayout.CENTER);
		JScrollPane treePanel = new JScrollPane();
		tree = new JTree();
		tree.setRootVisible(true);
		tree.setShowsRootHandles(true);
		tree.setEditable(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
//				System.out.println(e.getPath().getLastPathComponent());
//				System.out.println(e.getPath().getLastPathComponent().getClass());

			}

		});
		// tree.setMinimumSize(new Dimension(300,800));
		// treePanel.setMinimumSize(new Dimension(300,800));
		treePanel.setViewportView(tree);

		tabPane.add("Model", mainPanel);

		GraphModel modelo = new MetaModelGraphModel(model);
		GraphLayoutCache view = new GraphLayoutCache(modelo,
				new DefaultCellViewFactory());
		grafo = new JGraph(modelo, view);
		JScrollPane pnlGraph = new JScrollPane(grafo);
		tabPane.add("Diagram", pnlGraph);
		grafo.setAntiAliased(true);
		grafo.setDisconnectable(false);
		grafo.setConnectable(false);
		grafo.setMoveBeyondGraphBounds(false);
		grafo.setMoveBelowZero(false);
		// grafo.getGraphLayoutCache().insert(
		// new DefaultGraphCell(new String("Teste")));

		JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel,
				tabPane);
		tree.setModel(new MetaModelInstanceTreeModel(model));
		grafo.setModel(new MetaModelGraphModel(model));
		jsp.setOneTouchExpandable(true);

		jsp.setDividerLocation(180);
		add(jsp);

		table = new JTable(new SimulationDataTableModel(getSimulationData()));
		dataPanel = new JScrollPane(table);
		tabPane.add("Data", dataPanel);

		updateStatus();

		executor = Executors.newCachedThreadPool();

	}

	private JynaSimulationData getSimulationData() {

		return simulationData;
	}

	private JFreeChart getEmptyChart() {
		return ChartFactory.createXYLineChart("Simulation Results", "time",
				"value", (XYDataset) getSimulationData(),
				PlotOrientation.VERTICAL, true, true, false);
	}

	private void setStatus(int n) {
		status = n;
		// System.out.println(n);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MetaSim2 app = new MetaSim2();
		app.setTitle("MetaSim");
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setMinimumSize(new Dimension(1000, 480));
		app.setResizable(true);
		app.pack();
		app.setLocationRelativeTo(null);
		app.setVisible(true);

	}

	class alBtnRun implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			executor.execute(new SimulationRun(model, Integer
					.valueOf(txtStepCount.getText()), Double
					.valueOf(txtStepSize.getText()), Integer
					.valueOf(txtStepDelay.getText())));

		}
	}

	class alBtnLoadModel implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			jfc.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					String ext = null;
					String s = f.getName();
					int i = s.lastIndexOf('.');

					if (i > 0 && i < s.length() - 1) {
						ext = s.substring(i + 1).toLowerCase();
					}
					return (new String(
							MetaModelInstanceStorer.META_MODEL_INSTANCE_EXTENSION)
							.equals(ext) || f.isDirectory());
				}

				@Override
				public String getDescription() {
					return "MetaModel instances";
				}

			});
			int returnVal = jfc.showDialog(MetaSim2.this, "Load model");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				txtFile.setText(jfc.getSelectedFile().getAbsolutePath());
				MetaModelInstanceStorer storer = new DefaultMetaModelInstanceStorerJDOM();
				// simulationData = new DefaultSimulationData();
				try {
					model = storer.loadFromFile(jfc.getSelectedFile());
					setStatus(STATUS_IDDLE);

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MetaSim2.this, e1
							.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					txtFile.setText("");
					model = null;

				} finally {
					tree.setModel(new MetaModelInstanceTreeModel(model));
					grafo.setModel(new MetaModelGraphModel(model));
					if (!(model instanceof MetaModelInstance)) {
						setStatus(STATUS_NOMODEL);
					}

				}
			}
			updateStatus();
		}

		public void reset() {

		}

	}

	class DataGenerator extends Timer implements ActionListener {
		private JynaSimulation simulation;

		// private XYDataset data;

		/**
		 * Constructor.
		 * 
		 * @param interval
		 *            the interval (in milliseconds)
		 */
		DataGenerator(int interval, JynaSimulation simulation, XYDataset data) {

			super(interval, null);
			this.simulation = simulation;
			// this.data = data;
			addActionListener(this);
		}

		/**
		 * Adds a new free/total memory reading to the dataset.
		 * 
		 * @param event
		 *            the action event. CHAPTER 10. DYNAMIC CHARTS 75
		 */
		public void actionPerformed(ActionEvent event) {
			try {
				// System.out.println(status);
				if (status == STATUS_RUNNING) {
					simulation.step();
					simulation.register();

					if (simulation.getTime() >= simulation.getProfile()
							.getFinalTime()) {
						stop();
						setStatus(STATUS_FINISHED);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class SimulationRun implements Runnable {

		private MetaModelInstance model;
		private Integer scount;
		private Double ssize;
		private Integer sdelay;
		private XYDataset xyData;

		public SimulationRun(MetaModelInstance model, Integer scount,
				Double ssize, Integer sdelay) {
			this.model = model;
			this.scount = scount;
			this.ssize = ssize;
			this.sdelay = sdelay;
		}

		@Override
		public void run() {

			JynaSimulation simulation = new DefaultMetaModelInstanceSimulation();
			JynaSimulationMethod method;
			JynaSimulationProfile profile = new DefaultSimulationProfile();
			switch (jcoMethod.getSelectedIndex()) {
			case 1:
				method = new DefaultMetaModelInstanceRK4Method();
				break;

			default:
				method = new DefaultMetaModelInstanceEulerMethod();
				break;
			}
			simulation.setProfile(profile);
			simulation.getProfile().setTimeLimits(this.ssize, this.scount);
			try {
				simulation.setMethod(method);
				getSimulationData().removeAll();
				simulation.setModel(this.model);
				simulation.setSimulationData(getSimulationData());
				simulation.reset();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			// SimpleSimulationData data = new SimpleSimulationData();
			for (Entry<String, ClassInstance> ciEntry : this.model
					.getClassInstances().entrySet()) {
				for (Entry<String, ClassInstanceItem> ciiEntry : ciEntry
						.getValue().entrySet()) {
					if (ciiEntry.getValue() instanceof ClassInstanceStock) {
						// simulationData.add(ciEntry.getKey() + "." +
						// ciiEntry.getKey(),
						// (ValuedClassInstanceItem) ciiEntry.getValue());
						getSimulationData().add(
								ciEntry.getKey() + "." + ciiEntry.getKey(),
								(JynaValued) ciiEntry.getValue());
					}
				}
			}

			// for (FiniteStock flvl : this.model.getFiniteLevels()) {
			// data.add(flvl.getName(), flvl);
			// }
			simulation.setSimulationData(simulationData);

			simulation.getProfile().setTimeSteps(this.scount);

			try {
				getSimulationData().register(0.0);
				if (sdelay > 0) {
					setStatus(STATUS_RUNNING);
					// System.out.println("La vai");
					new DataGenerator(sdelay, simulation, xyData).start();
				} else {
					setStatus(STATUS_RUNNING);
					simulation.run();
					setStatus(STATUS_FINISHED);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(MetaSim2.this, e.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}

		}

	}

	public void updateStatus() {
		switch (status) {
		case STATUS_NOMODEL:
			btnRun.setEnabled(false);
			btnLoad.setEnabled(true);
			btnPause.setEnabled(false);
			btnStop.setEnabled(false);
			btnReset.setEnabled(false);
			txtStepCount.setEnabled(false);
			txtStepSize.setEnabled(false);
			txtStepDelay.setEnabled(false);
			jcoMethod.setEnabled(false);
			simulationData = new DefaultSimulationData();
			break;
		case STATUS_RUNNING:
			btnRun.setEnabled(false);
			btnLoad.setEnabled(false);
			btnPause.setEnabled(true);
			btnStop.setEnabled(true);
			btnReset.setEnabled(true);
			txtStepCount.setEnabled(false);
			txtStepSize.setEnabled(false);
			txtStepDelay.setEnabled(true);
			jcoMethod.setEnabled(false);
			break;
		case STATUS_PAUSED:
			btnRun.setEnabled(false);
			btnLoad.setEnabled(false);
			btnPause.setEnabled(true);
			btnStop.setEnabled(true);
			btnReset.setEnabled(true);
			txtStepCount.setEnabled(false);
			txtStepSize.setEnabled(false);
			txtStepDelay.setEnabled(true);
			jcoMethod.setEnabled(false);
			break;
		case STATUS_FINISHED:
			btnRun.setEnabled(false);
			btnLoad.setEnabled(true);
			btnPause.setEnabled(false);
			btnStop.setEnabled(true);
			btnReset.setEnabled(true);
			txtStepCount.setEnabled(true);
			txtStepSize.setEnabled(true);
			txtStepDelay.setEnabled(true);
			jcoMethod.setEnabled(true);
			break;
		default:
		case STATUS_IDDLE:
			btnRun.setEnabled(true);
			btnLoad.setEnabled(true);
			btnPause.setEnabled(true);
			btnStop.setEnabled(true);
			btnReset.setEnabled(true);
			txtStepCount.setEnabled(true);
			txtStepSize.setEnabled(true);
			txtStepDelay.setEnabled(true);
			jcoMethod.setEnabled(true);
			break;
		}

	}

	public void setChart(JFreeChart createXYLineChart) {
		this.chartpanel.setChart(createXYLineChart);
	}
}
