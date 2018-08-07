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
		time.add(time_t);
		
		srcp.add(src_t);
		
		dstp.add(dst_t);

		use.add(use_t);
		
		Ava_Car c_temp = new Ava_Car();
		
		ava_cars.add(c_temp);
	}
	public synchronized int getNum(){
		return srcp.size();
	} 
	
	public synchronized Point getsrcp(int i){
		return srcp.get(i);
	}

	
	public synchronized Point getdstp(int i){
		return dstp.get(i);
	}
	
	public synchronized long gettime(int i){
		return time.get(i);
	}
	
	public synchronized boolean getuse(int i){
		return use.get(i);
	}
	
	public synchronized Ava_Car getcars(int i) {
		return ava_cars.get(i);
	}
	
	public synchronized void setcars(Ava_Car c_t,int i) {
		ava_cars.set(i, c_t);
	}
	
	public synchronized void setuse(int i,boolean temp) {
		use.set(i, temp);
	}
}
