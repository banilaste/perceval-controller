package simulation;

public class Node {
	private Node output;
	private int id, visitToken = -1;
	private double x, y;

	public Node(int id) {
		this.id = id;
	}

	public void setOutput(Node output) {
		this.output = output;
	}

	public Node getOutput() {
		return output;
	}

	public void setCoordinates(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getId() {
		return id;
	}

	public void setVisited(int visitToken) {
		this.visitToken = visitToken;
	}

	public boolean isVisited(int visitToken) {
		return this.visitToken == visitToken;
	}
}
