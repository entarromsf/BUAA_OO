package seventh_work;

import java.util.Random;
import java.util.*;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
public class Taxi extends Thread{
	enum status{
		service,called,wfs,stop;
	}
	private int pos;
	private status state;
	private long time_wfs;
	private int start;
	private int des;
	private int credit;
	private int number;
	RequestQueue q;
	
	static final int dx[] = {0,1,-1,0};
	static final int dy[] = {1,0,0,-1};
	
	private int [] dis;
	private boolean [] mark;
	private int [] queue;
	private int [] last;
	private int[][] map;
	private TaxiGUI gui;
	private String fileadd;
	Taxi(int Pos,RequestQueue Q,int [][] m,int num,TaxiGUI g){
		pos = Pos;
		q = Q;
		gui = g;
		credit = 0;
		state = status.wfs;
		time_wfs = 0;
		start = -1;
		des = -1;
		dis = new int [6400];
		mark = new boolean [6400];
		queue = new int [8000];
		last = new int [6400];
		map = m;
		number = num;
		gui.SetTaxiStatus(number - 1, new Point(pos/80,pos%80), 2);
	}
	
	public int get_x(){return pos / 80;}
	public int get_y(){return pos % 80;}
	public int get_pos(){return pos;}
	public void set_pos(int p){pos = p;}
	public void move(int x,int y){
		int node = x * 80 + y;
		set_pos(node);
	}
	public int get_credit(){return credit;}
	public void set_credit(int now_credit){credit = now_credit;}
	public void set_start(int s){start = s;}
	public void set_des(int d){des = d;}
	public int get_start(){return start;}
	public int get_des(){return des;}
	public int get_number() {return number;}
	public static status get_state(Taxi a) {return a.state;}
	public void get_path(int root)
	{
		for(int i = 0;i < 6400;i++) {dis[i] = Integer.MAX_VALUE;mark[i] = false;last[i] = -1;}
		dis[root] = 0;
		int head = 0;int tail = 1;
		queue[++head] = root;mark[root] = true;
		while(head <= tail)
		{
			int e = queue[head];head++;
			int x = e / 80;
			int y = e % 80;
			for(int i = 0;i < 4;i++)
			{
				int xx = x + dx[i];
				int yy = y + dy[i];
				int node = xx * 80 + yy;
				boolean flag = false;
				if(xx >= 0 && xx < 80 && yy >= 0 && yy < 80 && mark[node] == false)
				{
					if(i == 0 && (map[x][y] == 1 || map[x][y] == 3))flag = true;
					if(i == 1 && (map[x][y] == 2 || map[x][y] == 3))flag = true;
					if(i == 2 && (map[xx][yy] == 2 || map[xx][yy] == 3))flag = true;
					if(i == 3 && (map[xx][yy] == 1 || map[xx][yy] == 3))flag = true;
				}
				if(flag == true)
				{
					tail++;
					queue[tail] = node;
					dis[node] += dis[e] + 1;
					last[node] = e;
					mark[node] = true;
				}
			}
		}
		
		for(int i = 0;i < 6400;i++) {if(!mark[i]) {System.out.println("Unconnected Graph");System.exit(0);}}
	}
	public void process(String name,int start,int end)
	{
		fileadd = name;
		set_start(start);
		set_des(end);
	}
	public void run()
	{
		while(true)
		{	
			int x = get_x();
			int y = get_y();
			if(state == status.stop) {
				try{sleep(1000);}catch(InterruptedException e) {}
				state = status.wfs;
				gui.SetTaxiStatus(number - 1, new Point(pos/80,pos%80), 2);
			}
			else if(state == status.wfs) {
				if(time_wfs == 100) {state = status.stop;gui.SetTaxiStatus(number - 1, new Point(pos/80,pos%80), 0);
				time_wfs = 0;}
				synchronized(q)
				{
					int valid_head = q.get_valid_head();
					int tail = q.get_tot();
					for(int i = valid_head;i <= tail;i++)
					{
						Request r = q.get_req(i);
						if(r.get_start_x() + 2 >= x && r.get_start_x() - 2 <= x &&
								r.get_start_y() + 2 >= y && r.get_start_y() - 2 <= y)
							{
								boolean f = false;
								for(int j = 1;j <= r.get_Called_tot();j++) {
									Taxi temp = r.get_Taxi(j);
									if(temp.get_number() == this.get_number()) {
										f = true;break;
									}
								}
								if(f == true)continue;
								r.new_called(this);
								set_credit(credit + 1);
							}
					}
				}
				try {
					sleep(1);
				}catch(InterruptedException e) {}
				if(get_start() != -1 && get_des() != -1) {
					state = status.called;
					get_path(get_start());
					gui.SetTaxiStatus(number - 1, new Point(x,y), 1);
					time_wfs = 0;
				}
				else {
					Random r = new Random();
					int dir = r.nextInt(4);
					int xx = x + dx[dir];
					int yy = y + dy[dir];boolean flag = false;
					if(xx >= 0 && xx < 80 && yy >= 0 && yy < 80)
					{
						if(dir == 0 && (map[x][y] == 1 || map[x][y] == 3))flag = true;
						if(dir == 1 && (map[x][y] == 2 || map[x][y] == 3))flag = true;
						if(dir == 2 && (map[xx][yy] == 2 || map[xx][yy] == 3))flag = true;
						if(dir == 3 && (map[xx][yy] == 1 || map[xx][yy] == 3))flag = true;
					}
					if(flag == true)
					{
						try {
							sleep(200);
						}catch(InterruptedException e) {}
						move(xx,yy);
						gui.SetTaxiStatus(number - 1, new Point(xx,yy), 2);
						time_wfs++;
					}
				}
			}
			else if(state == status.called)
			{
				if(get_pos() == get_start()) {
					Date d = new Date();
					long time = d.getTime() - (d.getTime() % 100);
					System.out.println("Time:"+time+":NO."+number+" Taxi Approach the start point");
					try {
					BufferedWriter output = new BufferedWriter(new FileWriter(fileadd,true));
					output.write("Time:"+time+":NO."+number+" Taxi Approach the start point"+System.lineSeparator());
					output.close();
					}catch(IOException e) {}
					
					try {sleep(1000);
					}catch(InterruptedException e) {}
					get_path(get_des());
					state = status.service;
					gui.SetTaxiStatus(number - 1, new Point(x,y), 2);
				}
			    else
			    {
			    	try {
			    		sleep(200);
			    	}catch(InterruptedException e) {}
			    	Date d = new Date();
			    	int xx = last[pos] / 80;
			    	int yy = last[pos] % 80;
			    	long time = d.getTime() - (d.getTime() % 100);
			    	System.out.println("Time:"+time+":NO."+number+" Taxi Called From ("+x+","+y+") to ("+xx+","+yy+")");
			    	try {
						BufferedWriter output = new BufferedWriter(new FileWriter(fileadd,true));
						output.write("Time:"+time+":NO."+number+" Taxi Called From ("+x+","+y+") to ("+xx+","+yy+")"+System.lineSeparator());
						output.close();
						}catch(IOException e) {}
			    	gui.SetTaxiStatus(number - 1, new Point(xx,yy), 1);
			    	move(xx,yy);
			    }
			}
			else
			{
				if(get_pos() == get_des())
				{
					Date d = new Date();
					long time = d.getTime() - (d.getTime() % 100);
					System.out.println("Time:"+time+":NO."+number+" Taxi Approach the destination");
					try {
						BufferedWriter output = new BufferedWriter(new FileWriter(fileadd,true));
						output.write("Time:"+time+":NO."+number+" Taxi Approach the destination"+System.lineSeparator());
						output.close();
						}catch(IOException e) {}
					set_credit(credit + 3);
					set_des(-1);
					set_start(-1);
					state = status.stop;
					gui.SetTaxiStatus(number - 1, new Point(x,y), 0);
				}
				else
			    {
			    	try {
			    		sleep(200);
			    	}catch(InterruptedException e) {}
			    	Date d = new Date();
			    	int xx = last[pos] / 80;
			    	int yy =last[pos] % 80;
			    	long time = d.getTime() - (d.getTime() % 100);
			    	System.out.println("Time:"+time+":NO."+number+" Taxi Service:From ("+x+","+y+") to ("+xx+","+yy+")");
			    	try {
						BufferedWriter output = new BufferedWriter(new FileWriter(fileadd,true));
						output.write("Time:"+time+":NO."+number+" Taxi Service:From ("+x+","+y+") to ("+xx+","+yy+")"+System.lineSeparator());
						output.close();
						}catch(IOException e) {}
			    	gui.SetTaxiStatus(number - 1, new Point(x,y), 1);
			    	move(xx,yy);
			    }
			}
		}
	}
}
