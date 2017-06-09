package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Area {
	private Node nodes[];
	private int endNode, visitToken = 0;
	
	/**
	 * Ouvre un fichier au format .area
	 * Format :
	 * nombre_noeuds
	 * noeud_arrivee
	 * output_1 x1 y1
	 * output_2 x2 y2
	 * [...]
	 * @param name
	 * @throws FileNotFoundException
	 */
	public void open(String name) throws FileNotFoundException {
		File file = new File("data/" + name + ".area");
		Scanner sc = new Scanner(file);
		
		String read = sc.nextLine();
		
		nodes = new Node[Integer.parseInt(read)];
		
		// Initialisation des neuds
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(i);
		}
		
		endNode = sc.nextInt();
		
		// Ajout des coordon�es
		for (int i = 0; i < nodes.length; i++) {
			// Noeud de sortie
			nodes[i].setOutput(nodes[sc.nextInt()]);
			
			// Coordonn�es
			nodes[i].setCoordinates(sc.nextDouble(), sc.nextDouble());
		}
		
		// Fermeture des flux
		sc.close();
	}

	/**
	 * Renvoie le noeud le plus proche du robot et de la fin
	 * @return
	 */
	public Node getNextNode(Robot r) {
		int minId = 0;
		double score, minScore = getScore(nodes[0], r.getX(), r.getY());
		Node node;
		
		for (int i = 1; i < nodes.length; i++) {
			score = getScore(nodes[i], r.getX(), r.getY());
			
			if (score < minScore) {
				minId = i;
				minScore = score;
			}
		}
		
		// On oublie les noeuds d�ja "visit�s"
		node = nodes[minId];

		// On s'arr�te si le noeud est d�ja visit� ou qu'on se trouve � la fin
		while (node.isVisited(visitToken) && node.getId() != endNode) {
			node = node.getOutput();
		}

		return node;
	}
	
	/**
	 * Renvoie la distance entre le robot et le noeud
	 * @param n
	 * @param x
	 * @param y
	 * @return
	 */
	protected double getScore(Node n, double x, double y) {
		double distance = Math.hypot(x - n.getX(), y - n .getY());
		
		if (distance < 5) {
			n.setVisited(visitToken);
		}
		
		return distance;
	}
	
	public void resetVisited() {
		visitToken = visitToken + 1;
	}
	
	public Node[] getNodes() {
		return nodes;
	}

	public boolean isLastNode(Node n) {
		return n.getId() == this.endNode;
	}

	public int getVisitToken() {
		return visitToken;
	}
}
