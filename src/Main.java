import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.bluetooth.RemoteDevice;

import bluetooth.BluetoothConnexion;
import bluetooth.BluetoothDeviceManager;
import gui.MainWindow;
import simulation.Direction;
import simulation.Simulation;

public class Main {
	/**
	 * Début du calvaire
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		BluetoothDeviceManager bluetooth = new BluetoothDeviceManager();
		ArrayList<RemoteDevice> devices = bluetooth.getDevices();
		int index = 1;
		
		System.out.println("Liste des appareils :");
		for (RemoteDevice device : devices) {
			// Affichage des appareils
			try {
				System.out.println(index + ": " + device.getFriendlyName(false) + " (" + device.getBluetoothAddress() + ")");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			index += 1;
		}
		
		System.out.println("Appareil auquel se connecter : ");
		
		index = sc.nextInt();
		
		// Création de la connexion bluetooth
		BluetoothConnexion connection = new BluetoothConnexion(bluetooth.searchServices(devices.get(index - 1)));
		
		// Début du controle avec la simulation
		Simulation sim = new Simulation(sc);
		sim.getRobot().setBluetoothConnexion(connection);
		sim.startRecording();
		
		//sim.getRobot().move(Direction.STOP);
		
	}
}
