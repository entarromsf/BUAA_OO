package test;
import java.awt.Point;
import java.util.*;
public class control extends Thread {
	private long start_time;
	private long end_time;
	private int[] position;
	private ArrayList<taxi> taxi_list;
	private ArrayList<taxi> order_taxi=new ArrayList<taxi>();
	private TaxiGUI gui;
	public control(ArrayList<taxi> taxi_list,int[] position,TaxiGUI gui) {
		this.taxi_list=taxi_list;
		this.position=position;
		this.start_time=System.currentTimeMillis();
		this.gui=gui;
	}
	public void run() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		findcar();
	}
	public void findcar() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:order_list;
		 * @EFFECTS:None;
		 * @THREAD_EFFECTS:\locked(taxi_list);
		 */		
		System.out.println("("+position[0]+","+position[1]+","+position[2]+","+position[3]+") time: "+System.currentTimeMillis()/100*100);
		System.out.println("抢单队列:");
		for(int j=0;j<15;j++) {
			for(int i=0;i<taxi_list.size();i++) {
				if(four_four(taxi_list.get(i).get_x(),taxi_list.get(i).get_y(),position[0],position[1])) {
					if(order_taxi.indexOf(taxi_list.get(i))==-1) {
						order_taxi.add(taxi_list.get(i));
						System.out.println("id:"+taxi_list.get(i).get_id()+"credit:"+taxi_list.get(i).get_credit()+"x: "+taxi_list.get(i).get_x()+" y: "+taxi_list.get(i).get_y());
					}		
				}
			}
			sleep(490);
		}

		for(int i=0;i<order_taxi.size();i++) {
			order_taxi.get(i).set_credit(1);
		}
		synchronized(taxi_list) {
			check_order();
		}
	}
	public void check_order() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:order_taxi;
		 * @EFFECTS:None;
		 * @THREAD_REQUIRES:\locked(taxi_list);
		 */
		int max_credit=0;
		int max_taxi=-1;
		int short_taxi=9999999;
		for(int i=0;i<order_taxi.size();i++) {
			if(!order_taxi.get(i).get_state().equals("wait")) {
				order_taxi.remove(i);
				i--;
			}
		}
		for(int i=0;i<order_taxi.size();i++) {
			if(max_credit < order_taxi.get(i).get_credit()) {
				max_credit = order_taxi.get(i).get_credit();
			}
		}
		for(int i=0;i<order_taxi.size();i++) {
			int temp=gui.requestTaxi(new Point(position[0],position[1]), new Point(order_taxi.get(i).get_x(),order_taxi.get(i).get_y()));
			if(short_taxi>temp) {
				max_taxi=i;
				short_taxi =temp;
			}
		}
	//	System.out.println(max_taxi);
		if(max_taxi==-1) {
			System.out.println("("+position[0]+","+ position[1]+")("+position[2]+","+ position[3] + ")没有找到taxi");
		}
		else {
			order_taxi.get(max_taxi).set_go(position[0], position[1], position[2], position[3]);
			order_taxi.get(max_taxi).set_state("order");
			order_taxi.get(max_taxi).set_credit(3);
			System.out.println(order_taxi.get(max_taxi).get_id()+"号 接取("+position[0]+" "+position[1]+") ("+position[2]+" "+position[3]+")"+"time: "+System.currentTimeMillis()/100*100);
		}
		
	}
	public boolean four_four(int taxi_x,int taxi_y,int x,int y) {
		/**
		 * @REQUIRES:(\all taxi_x,taxi_y,x,y;0<=taxi_x<80,0<=taxi_y<80,0<=x<80,0<=y<80);
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		if( taxi_x< x+3 && taxi_x > x-3 && taxi_y <y+3 && taxi_y >y-3) return true;
		return false;
	}
	public void sleep(int time) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
}
