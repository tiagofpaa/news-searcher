package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import struct.Message;
import struct.Request;


public class ThreadIn extends Thread {

	private ObjectInputStream in;
	private transient ObjectOutputStream out;
	private Server s;
	private Dispatcher disp;

	public ThreadIn(Server s, ObjectInputStream in, ObjectOutputStream out) {
		this.s = s;
		this.in = in;
		this.out = out;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Message m = (Message) in.readObject();
				switch (m.getType()) {
					case CLIENT:
						this.disp = new Dispatcher(out);
						this.s.newRequestArrived(new Request(m.getRequest().getRequestString(), this.disp));
						break;
					case WORKER:
						this.disp = new Dispatcher(out, m.getID());
						s.getWorkerQueue().getDispatcher_map().put(m.getID(), this.disp);
						break;
					case RESULT:
						s.leadWithResult(m);
						break;
					
					default:
						break;
				}
			} catch (IOException | ClassNotFoundException e) {
		}
	}
}
	
}
