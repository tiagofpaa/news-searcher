package main;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.Worker;

public class Run_Workers {
	
	
	public static void main(String[] args) throws UnknownHostException {
	InetAddress address = InetAddress.getByName(null);
	int PORT = 8080;
	int n = 10;
	ExecutorService executor = Executors.newFixedThreadPool(n);
		for (int i = 0; i < n; i++) {
            Runnable worker = new Worker(address, PORT, i);
            executor.execute(worker);
		}
	}
}
