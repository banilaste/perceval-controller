package datasave;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import simulation.Direction;
import simulation.Simulation;

public class KeyListeningFrame extends JFrame implements KeyListener {
	boolean listening = false;
	private Simulation sim;
	
	public KeyListeningFrame(Simulation sim) {
		this.setTitle("Esc pour terminer");
		this.setSize(100, 100);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		
		this.addKeyListener(this);
		this.sim = sim;
	}

	public void keyPressed(KeyEvent e) {
		Direction d = null;
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			sim.endRecord();
			return;
		}
		
		switch(e.getKeyChar()) {
		case 'z':
			d = Direction.FORWARD;
			break;
		case 'q':
			d = Direction.LEFT;
			break;
		case 's':
			d = Direction.BACKWARD;
			break;
		case 'd':
			d = Direction.RIGHT;
			break;
		case ' ':
			d = Direction.STOP;
			break;
		default:
			return;
		}
		System.out.println(d);
		sim.getRecordManager().addRecord(d);
		sim.getRobot().move(d);
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}
}
