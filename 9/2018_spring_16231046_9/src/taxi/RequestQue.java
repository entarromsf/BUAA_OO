package taxi;

import java.awt.Point;
import java.util.Vector;

class Ava_Car{
	public Car[] cars = new Car[100];
	public int ava_num = 0;
}


public class RequestQue {

	private Vector<Point> srcp = new Vector<Point>();
	
	private Vector<Point> dstp = new Vector<Point>();
	
	private Vector<Long> time = new Vector<Long>();
	private Vector<Boolean> use = new Vector<Boolean>();
	
	private Vector<Ava_Car> ava_cars = new Vector<Ava_Car>();
	
	public synchronized void add_ele(Point src_t,Point dst_t,long time_t,boolean use_t) {
		/*@ REQUIRES: (choice.get(i) exists);
		@ MODIFIES: time, srcp, dstp, time, use, c_temp, ava_cars
		@ EFFECTS:  为队列添加各种元素信息
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		time.add(time_t);
		
		srcp.add(src_t);
		
		dstp.add(dst_t);

		use.add(use_t);
		
		Ava_Car c_temp = new Ava_Car();
		
		ava_cars.add(c_temp);
	}
	public synchronized int getNum(){
		/*@ REQUIRES: (srcp.size(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == srcp.size(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return srcp.size();
	} 
	
	public synchronized Point getsrcp(int i){
		/*@ REQUIRES: (srcp.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == srcp.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return srcp.get(i);
	}

	
	public synchronized Point getdstp(int i){
		/*@ REQUIRES: (dstp.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == dstp.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return dstp.get(i);
	}
	
	public synchronized long gettime(int i){
		/*@ REQUIRES: (time.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == time.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return time.get(i);
	}
	
	public synchronized boolean getuse(int i){
		/*@ REQUIRES: (use.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == use.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return use.get(i);
	}
	
	public synchronized Ava_Car getcars(int i) {
		/*@ REQUIRES: (ava_cars.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == ava_cars.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return ava_cars.get(i);
	}
	
	public synchronized void setcars(Ava_Car c_t,int i) {
		/*@ REQUIRES: None;
		@ MODIFIES: ava_cars;
		@ EFFECTS: ava_cars.set(i, c_t);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		ava_cars.set(i, c_t);
	}
	
	public synchronized void setuse(int i,boolean temp) {
		/*@ REQUIRES: None;
		@ MODIFIES: use;
		@ EFFECTS: use.set(i, c_t);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		use.set(i, temp);
	}
}
