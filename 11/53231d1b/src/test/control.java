package test;
import java.awt.Point;
import java.util.*;
public class control extends Thread {
	/**@Overview:According to the state of each vehicle, the vehicle is added to the queue. 
	 * The queue is detected and the vehicle is selected according to the credit value and distance.
	 */
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
	public boolean repOK() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:(taxi_list.size!=0 && position!=null && gui!=null=>\result=true);
		 */
		if(taxi_list==null || position==null||gui==null) return false;
		for(int i=0;i<taxi_list.size();i++) {
			if(taxi_list.get(i)==null) return false;
		}
		
		return true;
	}
	
	public void run() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:order_list,taxi.credit;
		 * @EFFECTS:(\exist x,y; taxi_list[point(x,y)] is within the range of point(position[0],position[1]) by 2 => order_list.add(taxi_list(point(x,y)) && taxi_list(point(x,y).credit=\old(taxi_list(point(x,y).credit)+1 ))));
		 * @THREAD_EFFECTS:\locked(taxi_list);
		 */	
		String str="("+position[0]+","+position[1]+","+position[2]+","+position[3]+") time: "+System.currentTimeMillis()/100*100;
		System.out.println(str);
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
			check_order(str);
		}
	}
	public void check_order(String str) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:order_taxi,order_taxi(max_taxi);  
		 * @EFFECTS:(\exist x,y;order_taxi(point(x,y).credit is max of order_taxi && point(x,y) to point(positionx,positiony) is shortest;
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
			if(short_taxi>temp && order_taxi.get(i).get_credit()==max_credit) {
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
			order_taxi.get(max_taxi).add_message(str);
			str=order_taxi.get(max_taxi).get_id()+"号 接取("+position[0]+" "+position[1]+") ("+position[2]+" "+position[3]+")"+"time: "+System.currentTimeMillis()/100*100;
			System.out.println(str);
		}
		
	}
	public boolean four_four(int taxi_x,int taxi_y,int x,int y) {
		/**
		 * @REQUIRES:(\all taxi_x,taxi_y,x,y;0<=taxi_x<80,0<=taxi_y<80,0<=x<80,0<=y<80);
		 * @MODIFIES:None;
		 * @EFFECTS:(\all taxi_x,taxi_y,x,y;point(taxi_x,taxi_y) within the range of point(x,y) by 2 => \result=true);
		 */
		if( taxi_x< x+3 && taxi_x > x-3 && taxi_y <y+3 && taxi_y >y-3) return true;
		return false;
	}
	public void sleep(int time) {
		/**
		 * @REQUIRES:(\all time; 0<=time);
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
}
