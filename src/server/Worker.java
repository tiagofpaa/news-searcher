package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import struct.Article;
import struct.Data;
import struct.Message;
import struct.Type;

public class Worker implements Serializable, Runnable{
	
	private static final long serialVersionUID = 4649923511486779585L;
	private int PORT;
	private InetAddress adress;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	private Dispatcher disp;
	private boolean inUse;
	private int ID;
	
	public Worker(InetAddress address, int port, int id) {
		this.adress = address;
		this.PORT = port;
		this.ID = id;
		connectToServer();
	}

	
	public void connectToServer(){
		try {
			socket = new Socket(this.adress, this.PORT);
			this.in = new ObjectInputStream(socket.getInputStream());
			this.out = new ObjectOutputStream(socket.getOutputStream());
			auth();
				System.out.println("Worker " + this.ID + " Connected!");
		}catch(IOException e){
			try {
				Thread.sleep(5000);
				connectToServer();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	
	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Message m = (Message) in.readObject();
				if(m.getType() == Type.SERVER){
					findExpression(m);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public synchronized void findExpression(Message m) throws IOException{
		Article article = m.getArticle();
		ArrayList<String> article_text = article.getArticle();
		String[] expressions = m.getRequest_string().split(" ");
		HashMap<String, Data> res_set = new HashMap<>();
			for(String str : expressions){
				int ocurr_num = 0;
					for(int i = 0; i < article_text.size(); i++){
						String[] line = article_text.get(i).split(" ");
						for(String word : line){
							if(word.contains(str)){
								ocurr_num++;
								if(!res_set.containsKey(article.getHeader())){
									res_set.put(article.getHeader(), new Data(0, article));
								}
							}
						}
					}
					if(ocurr_num>0){
						res_set.get(article.getHeader()).setOcurr_number(ocurr_num);}
					}
		this.out.writeObject(new Message(Type.RESULT, res_set, m.getID(), m.getSequenceNumber()));
	}
	
	private void auth(){
		try {
			out.writeObject(new Message(Type.WORKER, this.ID));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public Dispatcher getDisp(){
		return this.disp;
	}

	public boolean isInUse() {
		return inUse;
	}
	
}
