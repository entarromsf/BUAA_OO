package test;
import java.awt.Point;
import java.util.*;
public class taxi extends Thread{
	private int id;
	private int positonx;
	private int positony;
	private int credit;
	private String state;
	private long time;
	private int go_x1;
	private int go_y1;
	private int go_x2;
	private int go_y2;
	private boolean change;
	private City city;
	private TaxiGUI gui;
	public taxi(int id,int x,int y,TaxiGUI gui,City city,boolean change) {
		this.id =id;
		this.positonx=x;
		this.positony=y;
		this.credit=0;
		this.state="wait";
		this.gui=gui;
		this.go_x1=x;
		this.city=city;
		this.go_y1 =y;
		this.go_x2=x;
		this.go_y2 =y;
		gui.SetTaxiStatus(id, new Point(x,y), 3);
		this.time=System.currentTimeMillis();
	}
	
	public void run() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		while(true) {
			if(state.equals("order")) {
				ArrayList<Integer> path = new ArrayList<Integer>();
				path=bfs(positonx*80+positony,go_x1*80+go_y1,city);
				for(int i=0;i<path.size();i++) {
					if(change) {
						path=bfs(positonx*80+positony,go_x1*80+go_y1,city);
						i=0;
						change=false;
					}
					gui.SetTaxiStatus(id, new Point(path.get(i)/80,path.get(i)%80), 0);
					this.set_positon(path.get(i)/80,path.get(i)%80);
					city.change_weight(path.get(i), 0);
					gv.stay(500);					
					System.out.println("No: "+this.id +"x: "+this.positonx+" y: "+this.positony+"time: "+System.currentTimeMillis()/100*100);
				}
				System.out.println("No: "+this.id+"start serve");
				this.set_state("serve");
			}
			else if(state.equals("serve")) {
				ArrayList<Integer> path = new ArrayList<Integer>();
				path=bfs(positonx*80+positony,go_x2*80+go_y2,city);
				for(int i=0;i<path.size();i++) {
					if(change) {
						path=bfs(positonx*80+positony,go_x1*80+go_y1,city);
						i=0;
						change=false;
					}
					gui.SetTaxiStatus(id, new Point(path.get(i)/80,path.get(i)%80), 1);

					this.set_positon(path.get(i)/80,path.get(i)%80);
					gv.stay(500);
					System.out.println("No: "+this.id +"x: "+this.positonx+" y: "+this.positony+"time: "+System.currentTimeMillis()/100*100);
				}
				System.out.println("No: "+this.id+" arrive");
				this.set_state("stop");
			}
			else if(state.equals("wait")){
				long lasttime = System.currentTimeMillis();
				if(lasttime>time+20000) {
					this.state="stop";
				}
				stroll_taxi();
			}
			else if(state.equals("stop")) {
				gui.SetTaxiStatus(id, new Point(positonx,positony), 2);
				sleep(1000);
				this.state="wait";
				this.time=System.currentTimeMillis();
			}
		}
	}
	
	public int get_id() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		return this.id;
	}
	public int get_x() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		return this.positonx;
	}
	public int get_y() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		return this.positony;
	}
	
	public void set_go(int x1,int y1,int x2,int y2) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:go_x1,go_x2,go_y1,go_y2;
		 * @EFFECTS:None;
		 */
		this.go_x1=x1;
		this.go_x2=x2;
		this.go_y1=y1;
		this.go_y2=y2;
	}
	public String get_state() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		return this.state;
	}
	public void set_state(String state) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		this.state=state;
	}
	public void set_positon(int x,int y) {
		/**
		 * @REQUIRES:(all x,y;0<=x<80,0<=y<80)
		 * @MODIFIES:positonx,positony;
		 * @EFFECTS:None;
		 */
		this.positonx=x;
		this.positony=y;
	}
	public int get_credit() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		return this.credit;
	}
	public void set_credit(int k) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:credit;
		 * @EFFECTS:None;
		 */
		this.credit = this.credit + k;
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
			e.printStackTrace();
		}
	}
	public void stroll_taxi() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:gui,city;
		 * @EFFECTS:None;
		 */
		
		int i = city.getmixweight(get_x()*80+get_y());

		switch(i) {
		case 0:
			city.change_weight(this.get_x()*80+this.get_y(), 0);
			this.set_positon(this.get_x(), this.get_y()+1);
			gui.SetTaxiStatus(this.id, new Point(this.get_x(),this.get_y()), 3);
			city.change_weight(this.get_x()*80+this.get_y(), 2);
			gv.stay(500);
			break;
		case 1:
			city.change_weight(this.get_x()*80+this.get_y(), 1);
			this.set_positon(this.get_x()+1, this.get_y());
			gui.SetTaxiStatus(this.id, new Point(this.get_x(),this.get_y()), 3);
			city.change_weight(this.get_x()*80+this.get_y(), 3);
			gv.stay(500);
			break;
		case 2:
			city.change_weight(this.get_x()*80+this.get_y(), 2);
			this.set_positon(this.get_x(), this.get_y()-1);
			gui.SetTaxiStatus(this.id, new Point(this.get_x(),this.get_y()), 3);
			city.change_weight(this.get_x()*80+this.get_y(), 0);
			gv.stay(500);
			break;
		case 3:
			city.change_weight(this.get_x()*80+this.get_y(), 3);
			this.set_positon(this.get_x()-1, this.get_y());
			gui.SetTaxiStatus(this.id, new Point(this.get_x(),this.get_y()), 3);
			city.change_weight(this.get_x()*80+this.get_y(), 1);
			gv.stay(500);
			break;
		}
	}

	public static ArrayList<Integer> bfs(int src,int dst,City city) {
		/**
		 * @REQUIRES:(\all src,dst;0<=src<6400,0<=dst<6400)
		 * @MODIFIES:None;
		 * @EFFECTS:(\all i;0<=i<path.length;0<=path[i]<80)
		 */
		ArrayList<Integer> path =new ArrayList<Integer>();
		int max =80;
    	int start = dst;
    	int begin = src;
    	boolean[] s = new boolean[max*max];
    	int[] dis = new int[max*max];
    	int[] next = new int[max*max];
    	Queue<Integer> queue = new LinkedList<>();
    	for(int i=0;i<max*max;i++) {
    		dis[i] = Integer.MAX_VALUE;
    	}
    	s[start] =true;
    	dis[start] =0;
    	queue.offer(start);
    	int now,cost;
    	while(!queue.isEmpty()) {
    		now = queue.poll();
    		int to = 0;
    		for(int i=0;i<4;i++) {
    			if(city.getmap(now)[i]) {
	        		if(i == 0) to = now + 1;
	        		else if(i == 1) to = now + 80;
	        		else if(i == 2) to = now - 1;
	        		else to = now - 80;
	    			cost = 1+dis[now];
	    			if(dis[to]>cost) {
	    				next[to]=now;
	    				dis[to]=cost;
	    				if(!s[to]) {
	    					s[to]=true;
	    					queue.offer(to);
	    				}
	    			}
	    		}
    		}
    		s[now]=false;
    	}
    		while(begin!=dst) {
    			path.add(begin);
    			begin = next[begin];
    		}
    		path.add(dst);
    		return path;
    	}
}

class Taxi_thread{
	public void creat_taxilist(TaxiGUI gui,ArrayList<taxi> taxi_list,City city,ArrayList<Integer> had_build) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:taxi_list;
		 * @EFFECTS:None;
		 */
		Random rand = new Random();
		for(int i=0;i<100;i++) {
			if(had_build.contains(i)) continue;
			int x= rand.nextInt(80);
			int y= rand.nextInt(80); 
			taxi taxi = new taxi(i,x,y,gui,city,false);
			taxi_list.add(taxi);
			taxi.start();			
		}
	}	
}
