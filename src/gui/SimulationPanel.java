package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import simulation.Node;
import simulation.Robot;
import simulation.Simulation;

public class SimulationPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	private final float ratio = 2.3f;
	private Simulation simulation;
	private float initX, initY, mouseX, mouseY;
	private boolean drawLine = false;

	public SimulationPanel(Simulation simulation) {
		this.setPreferredSize(new Dimension((int) (150 * ratio), (int) (360 * ratio)));
		this.simulation = simulation;

		// Ecoute du clic
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		// Ajout du panel à la simulation
		this.simulation.setSimulationPanel(this);
	}

	/**
	 * Affichage (taille normale : 300 cm x 150 cm - distance entre bouteilles : 70 cm)
	 */
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		// Bouteilles
		for (int i = 0; i < 5; i += 1) {
			g.fillOval(convert(75 - 4), convert(70 * i + 16), convert(8), convert(8));
		}

		// Ligne
		if (drawLine) {
			g2d.drawLine((int) initX, (int) initY, (int) mouseX, (int) mouseY);
		}

		// Noeuds
		Node nodes[] = simulation.getArea().getNodes();

		for (int i = 0; i < nodes.length; i++) {
			g2d.drawLine(convert(nodes[i].getX()), convert(nodes[i].getY()),
					convert(nodes[i].getOutput().getX()), convert(nodes[i].getOutput().getY()));
		}

		// Noeud le plus proche
		Node node = simulation.getArea().getNextNode(simulation.getRobot());

		g2d.setColor(new Color(0, 255, 255));
		g2d.drawRect(convert(node.getX()), convert(node.getY()), 10, 10);

		// Robot
		g2d.setColor(new Color(255, 0, 0));

		g2d.translate(convert(simulation.getRobot().getX()), convert(simulation.getRobot().getY()));
		g2d.rotate(simulation.getRobot().getAngle());
		g2d.fillRect(convert(- simulation.getRobot().getWidth() / 2), convert(- simulation.getRobot().getHeight() / 2),
				convert(simulation.getRobot().getWidth()), convert(simulation.getRobot().getHeight()));

		g2d.setColor(new Color(0, 0, 0));
		g2d.drawLine(0, 0, convert(simulation.getRobot().getWidth() / 2), 0);

	}

	/**
	 * Renvoie la position en fonction de la position réelle
	 * @param point
	 * @return
	 */
	public int convert(double point) {
		return (int) (point * ratio);
	}

	public double revert(double point) {
		return point / ratio;
	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent event) {
		initX = event.getX();
		initY = event.getY();

		drawLine = true;
	}

	public void mouseReleased(MouseEvent event) {
		simulation.getRobot().setPosition(revert(initX), revert(initY), false);
		simulation.getRobot().setAngle(Math.atan2(event.getY() - initY, event.getX() - initX));//Math.PI / 2 - Math.atan2(event.getX() - initX, event.getY() - initY));

		simulation.reset();
		simulation.getArea().resetVisited();
		
		drawLine = false;

		System.out.println(Math.round(revert(event.getX())) + " " + Math.round(revert(event.getY())));
		
		this.repaint();
	}

	public void mouseClicked(MouseEvent arg0) {

	}

	public void mouseDragged(MouseEvent event) {
		mouseX = event.getX();
		mouseY = event.getY();

		this.repaint();
	}

	public void mouseMoved(MouseEvent event) {

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == ' ') {
			simulation.setManualMode(true);
		} else {
			
		}
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}
}
