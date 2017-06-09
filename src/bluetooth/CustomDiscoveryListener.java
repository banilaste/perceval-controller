package bluetooth;
import java.util.ArrayList;

import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class CustomDiscoveryListener implements DiscoveryListener {
	private ArrayList<RemoteDevice> devices;
	private BluetoothDevice device;
	
	private Object lock;

	public CustomDiscoveryListener(Object lock, ArrayList<RemoteDevice> devices) {
		this.lock = lock;
		this.devices = devices;
	}
	
	public CustomDiscoveryListener(Object lock, BluetoothDevice device) {
		this.device = device;
		this.lock = lock;
	}
	
	public void servicesDiscovered(int arg0, ServiceRecord[] services) {
		for (int i = 0; i < services.length; i++) {
			String url = services[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
			if (url == null) {
				continue;
			}

			DataElement serviceName = services[i].getAttributeValue(0x0100);
			if (serviceName != null) {
				System.out.println("service " + serviceName.getValue() + " found " + url);
			} else {
				System.out.println("service found " + url);
			}
			
			device.addService(services[i]);
		}
	}

	public void serviceSearchCompleted(int arg0, int arg1) {
		// Termine la recherche
		synchronized (lock) {
			lock.notify();
		}
	}

	public void inquiryCompleted(int arg0) {
		// Termine la recherche
		synchronized (lock) {
			lock.notify();
		}
	}

	public void deviceDiscovered(RemoteDevice device, DeviceClass arg1) {
		devices.add(device);
	}
}
