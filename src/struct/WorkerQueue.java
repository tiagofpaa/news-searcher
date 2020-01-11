package struct;

import java.util.HashMap;

import server.Dispatcher;

public class WorkerQueue{
	
	private HashMap<Integer, Dispatcher> dispathcer_map;
	
	
	public WorkerQueue(){
		dispathcer_map = new HashMap<>();
	}
	
	
	public synchronized Dispatcher getFreeDispather(){
		for(int id : dispathcer_map.keySet()){
			if(!dispathcer_map.get(id).isIn_use()){
				dispathcer_map.get(id).setIn_use(true);
					System.out.println("Worker " + dispathcer_map.get(id).getID() + " LOCKED");
				return dispathcer_map.get(id);
			}
		}
		return null;
	}
	
	public synchronized void setDispathcerFree(int id){
		for(int n : dispathcer_map.keySet()){
			if(n==id){
				dispathcer_map.get(id).setIn_use(false);
					System.out.println("Worker " + dispathcer_map.get(id).getID() + " UNLOCKED");
				return;
			}
		}
		System.out.println("FAILED : " + id);
	}

	public void FreeAll() {
		for(int n : dispathcer_map.keySet()){
			if(dispathcer_map.get(n).isIn_use() == true) {
				dispathcer_map.get(n).setIn_use(false);
					System.out.println("Worker " + dispathcer_map.get(n).getID() + " UNLOCKED");
			}
		}
	}
	
	
	public HashMap<Integer, Dispatcher> getDispatcher_map() {
		return dispathcer_map;
	}
}
