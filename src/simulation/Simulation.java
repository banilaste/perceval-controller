package simulation;

import java.io.FileNotFoundException;
import java.util.Scanner;

import datasave.DataRecordManager;
import datasave.KeyListeningFrame;
import gui.SimulationPanel;

public class Simulation implements Runnable {
	private Area area, areas[];
	private Robot robot;
	private Thread thread;
	private SimulationPanel panel;
	private Scanner sc;
	
	private Object lock;
	
	private boolean reset = false, calibrate = true, manualMode = false;
	private int areaIndex = 0;
	
	private DataRecordManager dataManager = new DataRecordManager();
	
	public Simulation(Scanner sc) {
		this.sc = sc;
		
		robot = new Robot();

		// Chargement des zones
		try {
			areas = new Area[5];
			
			for (int i = 0; i < 5; i++) {
				areas[i] = new Area();
				areas[i].open("area" + (i + 1));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		areaIndex = 0;
		area = areas[0];
	}

	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public Robot getRobot() {
		return robot;
	}
	public void setRobot(Robot robot) {
		this.robot = robot;
	}

	public void startSimulation(boolean calibrate) {
		this.calibrate = calibrate;

		if (thread == null) {
			thread = new Thread(this);

			thread.start();
		}
	}
	
	public void startRecording() {
		int command = 0;
		
		while (command != 1 && command != 2){
			System.out.println("Restaurer les données (1) ou enregistrer (2) ?");
			command = sc.nextInt();
		}
		
		if (command == 1) {
			dataManager.load();
			
			System.out.println("Appuyez sur entrée pour commencer.");
			
			sc.nextLine();
			sc.nextLine();
			
			dataManager.startSeeding(robot);
			
			System.out.println("Fin de simulation");
		} else {
			System.out.println("Appuyez sur entrée pour commencer.");
			
			sc.nextLine();
			sc.nextLine();
			
			dataManager.startRecord();
			
			KeyListeningFrame frame = new KeyListeningFrame(this);
			
			lock = new Object();
			synchronized(lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lock = null;
			
			System.out.println("Saving...");
			dataManager.save();
			
			frame.setVisible(false);
			frame = null;
		}
	}

	public void run() {
		double angle = -1, time, step;
		Node nextNode;

		try {
			// Calibrations
			if (calibrate) {
				// Calibrage de la vitesse
				calibrateSpeed(sc);

				// Calibrage de la vitesse de rotation
				calibrateRotationSpeed(sc);

				//robot.setSpeed(170.0/5000);
				//robot.setRotationSpeed(2.2 * 2 * Math.PI / 5000);
			}
			
			System.out.println("Calibration terminée, entrez les coordonnées du robot sur l'interface pour commencer");
			
			// On attend les coordonnées
			lock = new Object();
			synchronized(lock) {
				lock.wait();
			}
			lock = null;
			
			// Boucle principale
			while (true) {
				nextNode = area.getNextNode(robot);

				// Si le noeud est atteint et que c'est le dernier
				if (nextNode.isVisited(area.getVisitToken()) && area.isLastNode(nextNode)) {
					if (areaIndex + 1 >= areas.length) {
						robot.move(Direction.STOP);
						break;
					} else {
						areaIndex += 1;
						area = areas[areaIndex];
						continue;
					}
					
				}

				// Sinon on regarde s'il faut tourner
				// Angle entre le robot et le point suivant
				angle = Math.atan2(nextNode.getY() - robot.getY(), nextNode.getX() - robot.getX());

				System.out.println("Angle noeud / robot : " + angle / Math.PI * 180 + ", angle robot : " + robot.getAngle() / Math.PI * 180);

				angle = angle - robot.getAngle();

				System.out.println("Angle à parcourir : " + (angle / Math.PI * 180));

				// Angle supérieur à pi / 50
				if (Math.abs(angle) > Math.PI / 50) {
					// Correction de l'angle entre -pi et pi
					while (angle > Math.PI) {
						angle -= Math.PI * 2;
					}

					while (angle < -Math.PI) {
						angle += Math.PI * 2;
					}

					// On tourne à gauche
					if (angle < 0) {
						robot.move(Direction.LEFT);
					} else { // ou à droite
						robot.move(Direction.RIGHT);
					}

					time = Math.abs(angle / robot.getRotationSpeed()); // temps + 100 ms

				}

				// Sinon
				else {
					// On avance vers la cible
					robot.move(Direction.FORWARD);

					time = Math.hypot(nextNode.getX() - robot.getX(), nextNode.getY() - robot.getY()) / robot.getSpeed();
				}

				// On attend par étapes de temps (pour un affichage plus fluide)
				/*step = time / Math.log(time) / 20;
				while (time > 0 && !reset) {
					Thread.sleep((long) step);
					time -= step;
					robot.calculatePosition(step);
					panel.repaint();
				}*/
				Thread.sleep((long) time);
				robot.calculatePosition(time);
				panel.repaint();
				reset = false;
			}

			System.out.println("Point d'arrivée atteint...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void calibrateSpeed(Scanner sc) throws InterruptedException {
		double input = -1;

		System.out.println("Calibrage de la vitesse, le robot va avancer pendant 5 secondes.");
		System.out.println("Appuyez sur entrée pour démarrer.");
		sc.nextLine();
		sc.nextLine();

		robot.move(Direction.FORWARD);
		Thread.sleep(5000);

		System.out.println("Distance parcourue : ");

		robot.move(Direction.STOP);
		
		while (input == -1) {
			try {
				input = sc.nextDouble();
			} catch (Exception e) {
				System.err.println("Erreur de lecture du nombre, recommencez : ");
				sc.nextLine();
				input = -1;
			}
		}

		robot.setSpeed(input / 5000); // cm / ms
	}

	protected void calibrateRotationSpeed(Scanner sc) throws InterruptedException {
		double input = -1;

		System.out.println("Calibrage de la vitesse de rotation, le robot va tourner pendant 5 secondes.");
		System.out.println("Appuyez sur entrée pour démarrer.");
		sc.nextLine();
		sc.nextLine();

		robot.move(Direction.LEFT);
		Thread.sleep(5000);

		System.out.println("Nombre de tours réalisés : ");

		robot.move(Direction.STOP);
		
		while (input == -1) {
			try {
				input = sc.nextDouble();
			} catch (Exception e) {
				System.err.println("Erreur de lecture du nombre, recommencez : ");
				sc.nextLine();
				input = -1;
			}
		}

		robot.setRotationSpeed(input * 2 * Math.PI / 5000); // rad / ms
	}

	public void reset() {
		if (thread != null){
			if (lock != null) {
				synchronized (lock) {
					lock.notify();
				}
			} else if (thread.isAlive()) {
				reset = true;
			} else {
				thread = null;
				startSimulation(false);
			}
		}
	}

	public void setSimulationPanel(SimulationPanel simulationPanel) {
		this.panel = simulationPanel;
	}

	public void setManualMode(boolean b) {
		
	}

	public DataRecordManager getRecordManager() {
		return dataManager;
	}

	public void endRecord() {
		synchronized (lock) {
			lock.notify();
		}
	}
}
