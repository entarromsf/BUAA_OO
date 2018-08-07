package lajidianti;

import java.util.Vector;

public class Requestque {
	
	private int Num = 0;
	private Vector<Integer> eleid = new Vector<Integer>();
	private Vector<Integer> floor = new Vector<Integer>();
	private Vector<Integer> type = new Vector<Integer>();
	private Vector<Long> time = new Vector<Long>();
	private Vector<Boolean> use = new Vector<Boolean>();	
	public synchronized void get_imfor(int flo,int id,int typ, long tim) {
		floor.add(flo);
		eleid.add(id);
		type.add(typ);
		use.add(true);
		time.add(tim);
		Num++;
	}
	
	public synchronized int getNum() {
		return Num;
	}
	
	public synchronized int getEleid(int i) {
		if(!use.get(i)) return -1;
		return eleid.get(i);
	}
	
	public synchronized int getFloor(int i) {
		if(!use.get(i)) return -1;
		return floor.get(i);
	}
	
	public synchronized int getType(int i) {
		if(!use.get(i)) return -1;
		return type.get(i);
	}
	

	public synchronized long getTime(int i) {
		if(!use.get(i)) return -1;
		return time.get(i);
	}
	
	public synchronized boolean getUse(int i) {
		return use.get(i);
	}
	
	public synchronized void Use(int i) {
		use.set(i,false);
	}
}
