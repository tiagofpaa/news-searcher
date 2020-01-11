package server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import struct.Article;
import struct.Message;
import struct.Request;
import struct.WorkerQueue;

public class Server {
	
	private final File dir = new File("news29out");
	private File[] files;
	public static final int PORT = 8080;
	private ServerSocket serverSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private ThreadIn ti;
	private HashMap<Integer,DealWithRequest> dwr_list;
	private ArrayList<Article> articles;
	private WorkerQueue worker_queue;
	private int sequence_number = 0;
	
	public Server() throws InterruptedException{
		loadArticles();
		init();
	}

	
	private void loadArticles() throws InterruptedException{
		dwr_list = new HashMap<Integer,DealWithRequest>();
		articles = new ArrayList<>();
		files = dir.listFiles();
		for(File f : files){
			articles.add(new Article(f));
		}
	}
	
	
	private void init() {
		try {
			worker_queue = new WorkerQueue();
			serverSocket = new ServerSocket(PORT);
			while(true){
				System.out.println("Awaiting connections...");
				Socket socket = serverSocket.accept();
				System.out.println("Connected!");
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
				ti= new ThreadIn(this, in, out);
				ti.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}


	public synchronized void newRequestArrived(Request req) {
		if(getWorkerQueue().getDispatcher_map().size()>0){
			DealWithRequest dwr = new DealWithRequest(this.articles, req.getRequestString(), req.getDisp(), this, this.sequence_number);
			dwr_list.put(sequence_number,dwr);
			this.sequence_number++;
			dwr.start();
		}else{
			final JDialog dialog = new JDialog();
			dialog.setAlwaysOnTop(true);    
			JOptionPane.showMessageDialog(dialog, "NO WORKERS");
				System.out.println("NO WORKERS");
		}
	}


	public synchronized void leadWithResult(Message m) {
		DealWithRequest dwr = dwr_list.get(m.getDwr_sequence());
		dwr.reciveResponse(m);
	}
	
	
	public HashMap<Integer, DealWithRequest> getDwr_list() {
		return dwr_list;
	}

	public WorkerQueue getWorkerQueue() {
		return this.worker_queue;
	}
}