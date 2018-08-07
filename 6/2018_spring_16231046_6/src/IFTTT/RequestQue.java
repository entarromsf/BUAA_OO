package IFTTT;

import java.util.Vector;

public class RequestQue {
	Vector<Integer> monitor = new Vector<Integer>();
	Vector<Integer> mission = new Vector<Integer>();
	Vector<String> path = new Vector<String>();
	
	public void addmonitor(int monitor_t) {
		monitor.add(monitor_t);
		System.out.println("add monitor: "+monitor_t);
	}
	
	public void addmission(int mission_t) {
		mission.add(mission_t);
		System.out.println("add mission: "+mission_t);
	}
	
	public void addpath(String path_t) {
		path.add(path_t);
		System.out.println("add path: "+path_t);
	}
	
	public int getmonitor(int i) {
		System.out.println("get monitor: " + monitor.get(i));
		return monitor.get(i);
	}
	
	public int getmission(int i) {
		System.out.println("get mission: " + mission.get(i));
		return mission.get(i);
	}
	
	public String getpath(int i) {
		System.out.println("get path: " + path.get(i));
		return path.get(i);
	}
}
