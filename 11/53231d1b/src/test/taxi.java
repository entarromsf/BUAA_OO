package test;
import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class taxi extends Thread{
	/**@overview:
	 * Store the status, location and target of each taxi. Scheduling the taxi, changing the state of the taxi, and doing different behaviors for the car rental.
	 */
	private int id;
	private int positonx;
	private int positony;
	private int credit;
	private String driection;
	private String state;
	private long time;
	private int go_x1;
	private int go_y1;
	private int go_x2;
	private int go_y2;
	private boolean[] change;
	protected ArrayList<String> message=new ArrayList<String>();
	private City city;
	private Light light;
	private TaxiGUI gui;
	public taxi(int id,int x,int y,TaxiGUI gui,City city,boolean[] change,Light light) {
		this.id =id;
		this.positonx=x;
		this.positony=y;
		this.credit=0;
		this.state="wait";
		this.gui=gui;
		this.go_x1=x;
		this.city=city;
		this.light=light;
		this.go_y1 =y;
		this.go_x2=x;
		this.go_y2 =y;
		this.change=change;
		this.driection="up";
		gui.SetTaxiStatus(id, new Point(x,y), 3);
		this.time=System.currentTimeMillis();
	}
	public boolean repOK() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:
		 * (state=wait|order|serve|stop && gui!=NULL && city!=NULL && change.size!=0 && light.size!=0 )
		 * (&& driection=up|down|left|right => \result=true)
		 * (\exist id,x,y,credit;id<0|id>100 || point(x,y) beyond range || credit<0 =>\result=false) 
		 */
		if(state==null||gui==null||city==null||light==null||change==null||driection==null) {
			return false;
		}
		if(id<0 ||id>100) return false;
		if(positonx<0 || positony>80 || go_x1<0 || go_x1>80 || go_x2<0 || go_x2>80 || go_y1<0 || go_y1>80 || go_y2<0 || go_y2>80) return false;
		if(credit<0) return false;
		return true;
	}
	public void run() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:this;
		 * @EFFECTS:
		 * (\all i;0<=i<path.size;state=(state.order||state.serve) && path from point(positonx,positiony) to point(go_x,go_y) is shortest => point(positionx,positiony)=path[i];)
		 * (\exist x,y; state=state.wait && point(x,y) connect with point(positionx,positiony) && shortest weight from point(positionx,positiony) to point(x,y)=>point(positionx,positiony)=point(x,y))
		 */
		while(true) {
			if(state.equals("order")) {
				ArrayList<Integer> path = new ArrayList<Integer>();
				path=bfs(positonx*80+positony,go_x1*80+go_y1,city);
				for(int i=1;i<path.size();i++) {
					if(change[id]) {
						path=bfs(positonx*80+positony,go_x1*80+go_y1,city);
						i=1;
						change[id]=false;
					}
					if(light.judge_light_wait(positonx*80+positony,path.get(i),this.driection)) {
						while(light.judge_light_wait(positonx*80+positony,path.get(i),this.driection));
						i=i-1;
						continue;
					}
					gui.SetTaxiStatus(id, new Point(path.get(i)/80,path.get(i)%80), 0);
					this.set_positon(path.get(i)/80,path.get(i)%80);
					city.change_weight(path.get(i), 0);
					gv.stay(500);					
					String str = "No: "+this.id +"x: "+this.positonx+" y: "+this.positony+"time: "+System.currentTimeMillis()/100*100;
					System.out.println(str);
					this.add_message(str);
				}
				System.out.println("No: "+this.id+"start serve");
				this.set_state("serve");
			}
			else if(state.equals("serve")) {
				ArrayList<Integer> path = new ArrayList<Integer>();
				path=bfs(positonx*80+positony,go_x2*80+go_y2,city);
				for(int i=1;i<path.size();i++) {
					if(change[id]) {
						path=bfs(positonx*80+positony,go_x2*80+go_y2,city);
						i=1;
						change[id]=false;
					}
					if(light.judge_light_wait(positonx*80+positony,path.get(i),this.driection)) {
						while(light.judge_light_wait(positonx*80+positony,path.get(i),this.driection));
						i=i-1;
						continue;
					}
					gui.SetTaxiStatus(id, new Point(path.get(i)/80,path.get(i)%80), 1);
					this.set_positon(path.get(i)/80,path.get(i)%80);
					gv.stay(500);
					String str="No: "+this.id +"x: "+this.positonx+" y: "+this.positony+"time: "+System.currentTimeMillis()/100*100;
					System.out.println(str);
					this.add_message(str);
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
	
	public void add_message(String str) {
		this.message.add(str);
	}
	
	public int get_id() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:(\result=id);
		 */
		return this.id;
	}
	public int get_x() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:(\result=positionx);
		 */
		return this.positonx;
	}
	public int get_y() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:(\result=positony);
		 */
		return this.positony;
	}
	
	public void set_go(int x1,int y1,int x2,int y2) {
		/**
		 * @REQUIRES:(\all x1,x2,y1,y2;0<=x1<80,0<=x2<80,0<=y1<80,0<=y2<80);
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
		 * @EFFECTS:(\result=state);
		 */
		return this.state;
	}
	public void set_state(String state) {
		/**
		 * @REQUIRES:(state=serve||state=order||state=stop||state=wait);
		 * @MODIFIES:state;
		 * @EFFECTS:(this.state=state;
		 */
		this.state=state;
	}
	public void set_positon(int x,int y) {
		/**
		 * @REQUIRES:(all x,y;0<=x<80,0<=y<80)
		 * @MODIFIES:positonx,positony;
		 * @EFFECTS:(point(positonx,positony)=point(x,y));
		 */
		int i=(x-this.positonx)*80+(y-this.positony);
		switch(i) {
		case -1:
			this.driection="left";
			break;
		case 1:
			this.driection="right";
			break;
		case -80:
			this.driection="up";
			break;
		case 80:
			this.driection="down";
			break;
		}
		this.positonx=x;
		this.positony=y;
	}
	public int get_credit() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:(\result=credit);
		 */
		return this.credit;
	}
	public void set_credit(int k) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:credit;
		 * @EFFECTS:(credit=old(credit)+k);
		 */
		this.credit = this.credit + k;
	}
	public void sleep(int time) {
		/**
		 * @REQUIRES:(0<=time);
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
		 * @EFFECTS:(\exist x,y; point(x,y)connect with point(positonx,positony) && least  weight from point(x,y)to point(positonx,positony) =>point(positonx,positony)=point(x,y) );
		 */
		
		int i = city.getmixweight(get_x()*80+get_y(),this.driection);

		switch(i) {
		case 0:
			city.change_weight(this.get_x()*80+this.get_y(), 0);
			this.set_positon(this.get_x(), this.get_y()+1);
			this.driection="right";
			gui.SetTaxiStatus(this.id, new Point(this.get_x(),this.get_y()), 3);
			city.change_weight(this.get_x()*80+this.get_y(), 2);
			gv.stay(500);
			break;
		case 1:
			city.change_weight(this.get_x()*80+this.get_y(), 1);
			this.set_positon(this.get_x()+1, this.get_y());
			this.driection="down";
			gui.SetTaxiStatus(this.id, new Point(this.get_x(),this.get_y()), 3);
			city.change_weight(this.get_x()*80+this.get_y(), 3);
			gv.stay(500);
			break;
		case 2:
			city.change_weight(this.get_x()*80+this.get_y(), 2);
			this.set_positon(this.get_x(), this.get_y()-1);
			this.driection="left";
			gui.SetTaxiStatus(this.id, new Point(this.get_x(),this.get_y()), 3);
			city.change_weight(this.get_x()*80+this.get_y(), 0);
			gv.stay(500);
			break;
		case 3:
			city.change_weight(this.get_x()*80+this.get_y(), 3);
			this.set_positon(this.get_x()-1, this.get_y());
			this.driection="up";
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


class SpecialTaxi extends taxi {
	/**@overview:
	 * For special taxi to inherit ordinary taxi function, and can output taxi information.
	 */
	ListIterator it ;
	taxigen taxigen = new taxigen(this.message);
	public SpecialTaxi(int id, int x, int y, TaxiGUI gui, City city, boolean[] change, Light light) {
		super(id, x, y, gui, city, change, light);
	}
	
	private static class taxigen implements Iterator{
		private ArrayList<String> message;
		private int n=0;
		taxigen(ArrayList<String> message){
			this.message=message;
		}
		public boolean hasNext() {
			/**
			 * @REQUIRES:None;
			 * @MODIFIES:None;
			 * @EFFECTS:(\result=(this.index<\this.size));
			 */
			if(n<message.size()) return true;
			return false;
		}
		public String next(){
			/**
			 * @REQUIRES:None;
			 * @MODIFIES:this;
			 * @EFFECTS: 
			 * (\old(\this).hasNext)==>(\old(\this).contains(\result) && 
             (\this.index == (\old(\this).index+1))
             !(\old(\this).hasNext)==>exceptional_behavior(NoSuchElementException)
             */
			
			if(n<0) n=0;
			for(int e=n;e<message.size();e++) {
				n=e+1;
				return message.get(e);
			}
			return "null";
		}
		public boolean hasPrevious() {
			/**
			 * @REQUIRES:None;
			 * @MODIFIES:None;
			 * @EFFECTS:(\result=(this.index>\this.size);
			 */
			if(n>=0) return true;
			return false;
		}
		public String previous() {
			/**
			 * @REQUIRES:None;
			 * @MODIFIES:this;
			 * @EFFECTS:
			 * * (\old(\this).hasPrevious)==>(\old(\this).contains(\result) && 
             (\this.index == (\old(\this).index-1))
             !(\old(\this).hasPrevious)==>exceptional_behavior(NoSuchElementException);
			 */
			if(n>=message.size()) n=message.size()-1;
			for(int e=n;e>=0 && e<message.size();e--) {
				n=e-1;
				return message.get(e);
			}
			return "null";
		}
		
	}
	public void getmessage(int op) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		String content = "out range";
		String path=this.get_id()+"taxi.txt";
		try {
			FileWriter fw = new FileWriter(path,true);
			if(op==1) {
				if(taxigen.hasNext()) {
					content=taxigen.next();
		//			System.out.println(taxigen.next());
				}
			}
			else {
				if(taxigen.hasPrevious()) {
					content=taxigen.previous();
			//		System.out.println(taxigen.privious());
				}
			}
			fw.write(content);
			fw.write("\r\n");
			fw.close();
		} catch (IOException e) {
			System.out.println("文件写入失败");
		}

		
		/*
		it= message.listIterator();
		if(op==1) {
			while(it.hasNext()) {
				System.out.println(it.next());
			}
		}
		else {
			while(it.hasPrevious()) {
				System.out.println(it.previous());
			}
		}
		*/
	}
}


class Taxi_thread{
	/**@overview:
	 * For uninitialized taxi initialization operation, randomly set taxi location, establish taxi threads.
	 */
	public boolean repOK() {
		//没有构造函数.没有不变式，没有repOK
		return true;
	}
	public void creat_taxilist(TaxiGUI gui,ArrayList<taxi> taxi_list,City city,City city2,ArrayList<Integer> had_build,boolean[] change,Light light) {
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
			if(i<30) {
				SpecialTaxi taxi = new SpecialTaxi(i,x,y,gui,city2,change,light);
				gui.SetTaxiType(i, 1);
				taxi_list.add(taxi);
				taxi.start();
			}
			else {
				taxi taxi = new taxi(i,x,y,gui,city,change,light);
				gui.SetTaxiType(i, 0);
				taxi_list.add(taxi);
				taxi.start();	
			}
		
		}
	}	
}
