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
	 * Fonction principale du programme
	 * @param args
	 */
	public static void main(String[] args) {
		// Initialisation de l'entrée clavier
		Scanner sc = new Scanner(System.in);

		// Initialisation du gestionnaire d'appareils bluetooth
		BluetoothDeviceManager bluetooth = new BluetoothDeviceManager();

		// Récupération de la liste des appareils disponibles
		ArrayList<RemoteDevice> devices = bluetooth.getDevices();

		int index = 1;

		// Affichage de la liste avec les addresses et les noms
		System.out.println("Liste des appareils :");
		for (RemoteDevice device : devices) {
			try {
				// On affiche le numéro, puis le nom et l'adresse MAC
				System.out.println(index + ": " +
					device.getFriendlyName(false) + " (" +
					device.getBluetoothAddress() + ")");
			} catch (IOException e) {
				e.printStackTrace();
			}

			index += 1;
		}

		// Choix de l'appareil courant
		System.out.println("Appareil auquel se connecter : ");
		index = sc.nextInt();

		// Création de la connexion bluetooth
		BluetoothConnexion connection = new BluetoothConnexion(
			// On prend le premier service série disponible
			bluetooth.searchServices(devices.get(index - 1))
		);

		// Initialisation du gestionnaire de contrôle
		Simulation sim = new Simulation(sc);

		// On ajoute la connexion bluetooth à la simulation pour permettre la
		// communication
		sim.getRobot().setBluetoothConnexion(connection);

		// Démarrage du mode de fonctionnement save&restore
		sim.startRecording();

	}
}
