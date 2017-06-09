package bluetooth;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class BluetoothConnexion implements Runnable {
	private BluetoothDevice device;
	private OutputStream output;
	private InputStream input;
	
	
	public BluetoothConnexion(BluetoothDevice device) {
		this.device = device;
		
		try {
			// initialisation de la connexion
			StreamConnection connection = (StreamConnection) Connector.open(device.getServices().get(0).getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false));
		
			output = connection.openOutputStream();
			input = connection.openInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendInit() {
		try {
			output.write("I\n".getBytes());
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(byte sens1, byte sens2, byte speed) {
		byte[] array =  {1, 1, 127};
		try {
			// On envoie: les sens des deux moteurs, la vitesse, un caractère de fin
			output.write(new byte[]{sens1, sens2, speed});
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		// On ferme les connexions
		try {
			output.close();
		} catch (IOException e) {}
		
		try {
			input.close();
		} catch (IOException e) {}
	}
	
	public void run() {
		// TODO : si on a un capteur
	}
}
