package bluetooth;
import java.util.ArrayList;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;

public class BluetoothDeviceManager {
	private ArrayList<RemoteDevice> devices = null;
	private DiscoveryAgent agent;

	public BluetoothDeviceManager() {
		LocalDevice localDevice = null;

		// Initialisation de l'agent
		try {
			localDevice = LocalDevice.getLocalDevice();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}

		agent = localDevice.getDiscoveryAgent();
	}

	/**
	 * Renvoie la liste des appareils bluetooth
	 * @return
	 */
	public ArrayList<RemoteDevice> searchDevices() {
		Object lock = new Object();
		ArrayList<RemoteDevice> devices = new ArrayList<RemoteDevice>();

		try {
			// Début de la recherche
			agent.startInquiry(DiscoveryAgent.GIAC, new CustomDiscoveryListener(lock, devices));

			// Attend la fin de la recherche
			synchronized(lock) {
				lock.wait();
			}


		} catch (BluetoothStateException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.devices = devices;

		return devices;
	}

	public BluetoothDevice searchServices(RemoteDevice device) {
		UUID[] uuidSet = new UUID[1];
		uuidSet[0]=new UUID(0x1101); //OBEX Object Push service
		int[] attrIDs =  new int[] {
				0x0100 // Service name
		};
		Object lock = new Object();
		
		BluetoothDevice bluetoothDevice = new BluetoothDevice(device);

		try {
			agent.searchServices(null, uuidSet, device, new CustomDiscoveryListener(lock, bluetoothDevice));
			
			synchronized (lock) {
				lock.wait();
			}
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return bluetoothDevice;
	}

	public ArrayList<RemoteDevice> getDevices() {
		if (this.devices == null) {
			return searchDevices();
		} else {
			return this.devices;
		}
	}
}
