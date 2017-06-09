package simulation;

public enum Direction {
		BACKWARD((byte) 1, (byte) 1, (byte) 127),
		FORWARD((byte) 0, (byte) 0, (byte) 127),
		LEFT((byte) 1, (byte) 0, (byte) 127),
		RIGHT((byte) 0, (byte) 1, (byte) 127),
		STOP((byte) 1, (byte) 1, (byte) 0);
	
	byte leftMotor, rightMotor, speed;
	
	Direction(byte leftMotor, byte rightMotor, byte speed) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.speed = speed;
	}
}
