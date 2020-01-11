package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import struct.Message;

public class DealWithServer extends Thread {

	private ObjectInputStream in;
	private Client client;

	public DealWithServer(Socket socket, ObjectInputStream in, Client client) {
		this.in = in;
		this.client = client;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Message message = (Message) in.readObject();
				client.leadWithMessage(message);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}