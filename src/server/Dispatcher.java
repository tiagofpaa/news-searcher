package server;

import java.io.IOException;
import java.io.ObjectOutputStream;

import struct.Message;

public class Dispatcher{

	private ObjectOutputStream out;
	private boolean in_use;
	private int ID;
	
	public Dispatcher(ObjectOutputStream out, int id) {
		this.out = out;
		this.in_use = false;
		this.ID = id;
	}
	
	public Dispatcher(ObjectOutputStream out) {
		this.out = out;
		this.in_use = false;
	}

	public synchronized void sendMessage(Message message) {
		try {
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isIn_use() {
		return in_use;
	}

	public void setIn_use(boolean in_use) {
		this.in_use = in_use;
	}

	public int getID() {
		return ID;
	}
}
