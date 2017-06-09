package simulation;

import bluetooth.BluetoothConnexion;

public class Robot {
	private double x = -1, y = -1, angle = 0; // position du robot
	private double width = 20, height = 20;
	private double speed, rotationSpeed;
	
	private Direction currentDirection;
	
	private BluetoothConnexion remoteConnection;
	
	public void setPosition(double x, double y, boolean centered) {
		this.x = x;
		this.y = y;
		
		if (centered) {
			this.x -= width / 2;
			this.y -= height / 2;
		}
	}
	
	/**
	 * Calcule une nouvelle position en fonction d'un temps d'action donné
	 * @param time
	 */
	public void calculatePosition(double time) {
		switch(currentDirection) {
		case FORWARD:
			x += Math.cos(angle) * speed * time;
			y += Math.sin(angle) * speed * time;
			break;
		case BACKWARD:
			x -= Math.cos(angle) * speed * time;
			y -= Math.sin(angle) * speed * time;
			break;
		case LEFT:
			angle -= rotationSpeed * time; // sens positif : sens horaire
			break;
		case RIGHT:
			angle += rotationSpeed * time;
			break;
		}
		
		// Correction de l'angle entre -pi et pi
		while (angle > Math.PI) {
			angle -= Math.PI * 2;
		}
		
		while (angle < -Math.PI) {
			angle += Math.PI * 2;
		}
	}
	
	public void move(Direction dir) {
		if (remoteConnection != null) {
			remoteConnection.send(dir.leftMotor, dir.rightMotor, dir.speed);
		}
		
		System.out.println("Now moving " + dir);
		currentDirection = dir;
	}
	
	public void setBluetoothConnexion(BluetoothConnexion connexion) {
		this.remoteConnection = connexion;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		System.out.println(180 / Math.PI * angle);
		this.angle = angle;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setRotationSpeed(double rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	public double getSpeed() {
		return speed;
	}

	public double getRotationSpeed() {
		return rotationSpeed;
	}
	
	
}
