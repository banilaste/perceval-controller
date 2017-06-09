package datasave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import simulation.Direction;
import simulation.Robot;

public class DataRecordManager {
	public long startTime;
	public ArrayList<DataRecord> records;
	
	
	public void startRecord() {
		startTime = System.currentTimeMillis();
		records = new ArrayList<DataRecord>();
	}
	
	public void startSeeding(Robot r) {
		long lastTime = 0;
		for (DataRecord record : records) {
			
			try {
				Thread.sleep(record.time - lastTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			r.move(record.command);
			lastTime = record.time;
		}
		
		r.move(Direction.STOP);
	}
	
	public void addRecord(Direction command) {
		DataRecord record = new DataRecord();
		
		record.command = command;
		record.time = System.currentTimeMillis() - startTime;
		
		records.add(record);
	}
	
	public void save() {
		try {
			PrintWriter writer = new PrintWriter(new File("data.txt"));
			
			for (DataRecord record : records) {
				writer.write(record.toString() + "\n");
			}
			
			writer.flush();
			
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
		try {
			Scanner sc = new Scanner(new File("data.txt"));
			records = new ArrayList<DataRecord>();
			String line;
			
			while(sc.hasNextLine()) {
				line = sc.nextLine();
				if (!line.equals("")) {
					records.add(new DataRecord().fromString(line));
				}
				
			}
			
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
