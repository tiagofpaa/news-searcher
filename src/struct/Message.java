package struct;

import java.io.Serializable;
import java.util.HashMap;

import server.Dispatcher;

public class Message implements Serializable{
	
	private static final long serialVersionUID = -5941465274308094059L;
	private Type type;
	private Dispatcher disp;
	private Request request;
	private HashMap<String, Data> response;
	private Article article;
	private String request_string;
	private int dwr_sequence;
	private int sequence_number;
	private int ID;

	
	public Message(Type type , Request request) {
		this.type = type;
		this.request = request;
	}
	
	public Message(Type type, int id) {
		this.type = type;
		this.ID = id;
	}
	
	public Message(Type type, HashMap<String, Data> response, int sequence_number, int dwr_sequence){
		this.type = type;
		this.response = response;
		this.ID = sequence_number;
		this.dwr_sequence = dwr_sequence;
	}
	
	public Message(Type type, HashMap<String, Data> response){
		this.type = type;
		this.response = response;
	}
	
	public Message(Type type, Article article, String s, int seq_number, int disp_id){
		this.type = type;
		this.article = article;
		this.request_string = s;
		this.sequence_number = seq_number;
		this.ID = disp_id;
	}
	
	
	public int getDwr_sequence() {
		return dwr_sequence;
	}

	public Type getType() {
		return type;
	}

	public Request getRequest() {
		return request;
	}

	public HashMap<String, Data> getHashMap() {
		return response;
	}

	public Dispatcher getDisp() {
		return disp;
	}

	public String getRequest_string() {
		return request_string;
	}

	public Article getArticle() {
		return article;
	}

	public int getSequenceNumber() {
		return sequence_number;
	}

	public int getID() {
		return ID;
	}
}