package gui;

import javax.swing.JFrame;

import simulation.Robot;
import simulation.Simulation;

public class MainWindow extends JFrame {
	SimulationPanel simulationPanel;
	
	public MainWindow(Simulation simulation) {
		simulationPanel = new SimulationPanel(simulation);
		
		this.setTitle("Perceval control app");
		this.setContentPane(simulationPanel);
		this.setSize(simulationPanel.getPreferredSize());
		this.setVisible(true);
	}
}
