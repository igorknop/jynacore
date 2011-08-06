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
package br.ufjf.mmc.jynacore.gui.jynasim;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

/**
 * @author Knop
 *
 */
@SuppressWarnings("serial")
public class JynaSim extends JFrame {
	
	private MDIDesktopPane contentPane;

	public JynaSim() {
		contentPane = new MDIDesktopPane();
		setContentPane(contentPane);
		setTitle("JynaSim");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame app = new JFrame();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setPreferredSize(new Dimension(800,600));
		app.pack();
		app.setLocationRelativeTo(null);
		app.setVisible(true);
		JInternalFrame filha = new JInternalFrame("Filha 1");
		filha.setSize(new Dimension(320,240));
		filha.setResizable(true);
		filha.setVisible(true);
		filha.setLocation(10, 10);
		app.getContentPane().add(filha);
		filha = new JInternalFrame("Filha 2");
		filha.setSize(new Dimension(320,240));
		filha.setResizable(true);
		filha.setVisible(true);
		filha.setLocation(10, 10);
		app.getContentPane().add(filha);
		((MDIDesktopPane)app.getContentPane()).cascadeFrames();
	}

}
