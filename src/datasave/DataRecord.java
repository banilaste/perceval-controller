package datasave;

import simulation.Direction;

public class DataRecord {
	public long time;
	public Direction command;
	
	public String toString() {
		return time + " " + command;
	}
	
	public DataRecord fromString(String s) {
		String parts[] = s.split(" ");
		System.out.println(s);
		time = Long.parseLong(parts[0]);
		
		if (parts[1].equals("BACKWARD")) {
			command = Direction.BACKWARD;
			
		} else if (parts[1].equals("FORWARD")) {
			command = Direction.FORWARD;
			
		} else if (parts[1].equals("RIGHT")) {
			command = Direction.RIGHT;
			
		} else if (parts[1].equals("LEFT")) {
			command = Direction.LEFT;
		
		} else if (parts[1].equals("STOP")) {
			command = Direction.STOP;
		} 
		return this;
	}
}
