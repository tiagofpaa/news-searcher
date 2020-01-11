package struct;

import java.io.Serializable;

import server.Dispatcher;

public class Request implements Serializable{
	
	private static final long serialVersionUID = -5941465274308594059L;
	private String request;
	private Dispatcher disp;
	
	
	public Request(String request, Dispatcher disp) {
		this.request = request;
		this.disp = disp;
	}
	
	
	public Request(String request) {
		this.request = request;
	}

	
	public String getRequestString() {
		return request;
	}

	public Dispatcher getDisp() {
		return disp;
	}
	
}
