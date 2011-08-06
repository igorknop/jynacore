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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.filechooser.FileFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaSimulationProfile;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationData;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationProfile;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModelStorer;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultSystemDynamicsModelStorerJDOM;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulation;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsEulerMethod;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsRK4Method;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsSimulation;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.JFreeChartDataConverter;

/**
 * @author Knop
 * 
 */
@SuppressWarnings("serial")
public class JynaSim extends JFrame {
	private static final String METHOD_RK4 = "Runge-Kutta 4";
	private static final String METHOD_EULER = "Euler";
	private JTextField txtFile;
	private JButton btnLoad;
	private JTextField txtStepSize;
	private JTextField txtStepCount;
	private JTextField txtStepDelay;
	private JComboBox jcoMethod;
	private JButton btnRun;
	private ChartPanel chartpanel;
	private JFreeChart chart;
	public SystemDynamicsModel model;
	private ExecutorService executor;
	public XYDataset dataXY;
	private JTree tree;
	private JGraph grafo;
	private JTabbedPane tabPane;

	public JynaSim() {
		super();
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 3;
		txtFile = new JTextField();
		txtFile.setEditable(false);

		mainPanel.add(txtFile, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 3;
		btnLoad = new JButton("Load model");
		btnLoad.addActionListener(new alBtnLoadModel());
		mainPanel.add(btnLoad, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.3;
		txtStepSize = new JTextField("1.0");
		mainPanel.add(txtStepSize, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.3;
		txtStepCount = new JTextField("100");
		mainPanel.add(txtStepCount, gbc);

		gbc.gridx = 2;
		gbc.weightx = 0.3;
		txtStepDelay = new JTextField("0");
		// add(txtStepDelay, gbc);
		String[] methods = { METHOD_EULER, METHOD_RK4 };
		jcoMethod = new JComboBox(methods);
		jcoMethod.setEditable(false);
		// jcoMethod.setModel(JComboBox.)
		mainPanel.add(jcoMethod, gbc);

		gbc.weightx = 0.0;
		gbc.gridx = 3;
		btnRun = new JButton("Run");
		btnRun.addActionListener(new alBtnRun());
		mainPanel.add(btnRun, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		gbc.weightx = 0.8;
		gbc.fill = GridBagConstraints.VERTICAL;

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
		// tree.setMinimumSize(new Dimension(300,800));
		// treePanel.setMinimumSize(new Dimension(300,800));
		treePanel.setViewportView(tree);

		tabPane = new JTabbedPane();
		tabPane.add("Chart", mainPanel);

		GraphModel modelo = new SystemDynamicsGraphModel(model);
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

		JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel,
				tabPane);
		tree.setModel(new SystemDynamicsModelTreeModel(model));
		grafo.setModel(new SystemDynamicsGraphModel(model));
		jsp.setOneTouchExpandable(true);
		jsp.setDividerLocation(180);
		add(jsp);
		updateStatus();

		executor = Executors.newCachedThreadPool();

	}

	private JFreeChart getEmptyChart() {
		return ChartFactory.createXYLineChart("Simulation Results", "time",
				"value", new DefaultXYDataset(), PlotOrientation.VERTICAL,
				true, true, false);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JynaSim app = new JynaSim();
		app.setTitle("JynaSim");
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setMinimumSize(new Dimension(900, 480));
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
					return (new String("jyna").equals(ext) || f.isDirectory());
				}

				@Override
				public String getDescription() {
					return "JynaCore models";
				}

			});
			int returnVal = jfc.showDialog(JynaSim.this, "Load model");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				txtFile.setText(jfc.getSelectedFile().getAbsolutePath());
				SystemDynamicsModelStorer storer = new DefaultSystemDynamicsModelStorerJDOM();
				try {
					model = storer.loadFromFile(jfc.getSelectedFile());

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(JynaSim.this, e1.getCause(),
							"Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					txtFile.setText("");
					model = null;
				} finally {
					tree.setModel(new SystemDynamicsModelTreeModel(model));
					grafo.setModel(new SystemDynamicsGraphModel(model));
				}
			}
			updateStatus();
		}

	}

	class SimulationRun implements Runnable {

		private SystemDynamicsModel model;
		private Integer scount;
		private Double ssize;

		public SimulationRun(SystemDynamicsModel model, Integer scount,
				Double ssize, Integer sdelay) {
			this.model = model;
			this.scount = scount;
			this.ssize = ssize;
		}

		@Override
		public void run() {
			btnRun.setText("Running");
			btnRun.setEnabled(false);
			SystemDynamicsSimulation simulator = new DefaultSystemDynamicsSimulation();
			SystemDynamicsSimulationMethod method;
			JynaSimulationProfile profile = new DefaultSimulationProfile();
			profile.setTimeLimits(this.ssize, this.scount);
			switch (jcoMethod.getSelectedIndex()) {
			case 1:
				method = new DefaultSystemDynamicsRK4Method();
				break;

			default:
				method = new DefaultSystemDynamicsEulerMethod();
				break;
			}
			try {
				simulator.setMethod(method);
				simulator.setProfile(profile);
				simulator.setModel(this.model);
				simulator.reset();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			JynaSimulationData data = new DefaultSimulationData();
			for (FiniteStock flvl : this.model.getFiniteLevels()) {
				data.add(flvl.getName(), flvl);
			}
			simulator.setSimulationData(data);

			simulator.getProfile().setTimeSteps(this.scount);

			try {
				data.register(0.0);
				simulator.run();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(JynaSim.this, e.getCause(),
						"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}

			chartpanel.setChart(ChartFactory.createXYLineChart(
					"Simulation Results", "time", "value",
					JFreeChartDataConverter.toXYDataSet(data),
					PlotOrientation.VERTICAL, true, true, false));
			// chartpanel.repaint();
			btnRun.setText("Run");
			btnRun.setEnabled(true);
		}

	}

	public void updateStatus() {
		if (model instanceof SystemDynamicsModel) {
			btnRun.setEnabled(true);
			txtStepCount.setEnabled(true);
			txtStepSize.setEnabled(true);
			txtStepDelay.setEnabled(false);
		} else {
			btnRun.setEnabled(false);
			txtStepCount.setEnabled(false);
			txtStepSize.setEnabled(false);
			txtStepDelay.setEnabled(false);
			chartpanel.setChart(getEmptyChart());
		}

	}
}
