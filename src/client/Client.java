package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import struct.Message;
import struct.Request;
import struct.Type;


public class Client {
	
	private int PORT = 8080;
	public ObjectInputStream in;
	public ObjectOutputStream out;
	public Socket socket;
	private Gui gui;
	private int k;
	
	public Client() throws IOException {
		this.gui = new Gui(this);
		connectToServer();
	}
	
	
	private void connectToServer() {
		try {
			InetAddress address = InetAddress.getByName(null);
			socket = new Socket(address, PORT);
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			DealWithServer s =  new DealWithServer(socket, in , this);
			s.start();
		} catch (IOException e) {
		if(k == 0){
			System.out.println("Connection refused! Trying to reconnect... in 5sec");
			int n = JOptionPane.showConfirmDialog(gui,"Try again?","Connection refused!",JOptionPane.YES_NO_OPTION);
			k = n;
		try {
			Thread.sleep(5000);
			connectToServer();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		}
	}
}
	
	
	public void makeRequest(String req) {
		try {
			Message msg = new Message(Type.CLIENT, new Request(req));
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void leadWithMessage(Message message) {
		gui.setPreview(message.getHashMap());
		gui.enable();
	}
}
