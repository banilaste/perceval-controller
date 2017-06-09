package bluetooth;
import java.util.ArrayList;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class BluetoothDevice {
	private RemoteDevice device;
	private ArrayList<ServiceRecord> services = new ArrayList<ServiceRecord>();

	protected BluetoothDevice(RemoteDevice device) {
		this.device = device;
	}
	
	public void addService(ServiceRecord service) {
		this.services.add(service);
	}

	public RemoteDevice getDevice() {
		return device;
	}

	public ArrayList<ServiceRecord> getServices() {
		return services;
	}
	
	
}
