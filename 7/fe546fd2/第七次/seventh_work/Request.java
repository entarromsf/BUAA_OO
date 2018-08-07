package seventh_work;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class Request extends Thread{

	
	private int start_x;
	private int start_y;
	private int end_x;
	private int end_y;
	private long time;
	private Taxi[] CalledTaxi;
	private int Called_tot;
	private int rank;
	
	static final int dx[] = {0,1,-1,0};
	static final int dy[] = {1,0,0,-1};
	
	private int [] dis;
	private boolean [] mark;
	private int [] queue;
	private int [] last;
	private int [][] map;
	RequestQueue q;
	TaxiGUI gui;
	Request(int sx,int sy,int ex,int ey,long t,RequestQueue Q,int [][] m,TaxiGUI g,int r)
	{
		start_x = sx;
		start_y = sy;
		end_x = ex;
		end_y = ey;
		time = t;
		CalledTaxi = new Taxi[1000];
		Called_tot = 0;
		q = Q;
		map = m;
		dis = new int [6400];
		mark = new boolean [6400];
		last = new int [6400];
		queue = new int [8000];
		gui = g;
		rank = r;
	}
	public synchronized int get_start_x(){return start_x;}
	public synchronized int get_start_y(){return start_y;}
	public synchronized int get_end_y(){return end_y;}
	public synchronized int get_end_x(){return end_x;}
	public synchronized void new_called(Taxi t){Called_tot++;CalledTaxi[Called_tot] = t;}
	public synchronized long get_time(){return time;}
	public synchronized int get_Called_tot() {return Called_tot;}
	public synchronized Taxi get_Taxi(int i) {return CalledTaxi[i];}
	public synchronized void get_path(int root)
	{
		for(int i = 0;i < 6400;i++) {dis[i] = Integer.MAX_VALUE;mark[i] = false;last[i] = -1;}
		dis[root] = 0;
		int head = 0;int tail = 1;
		queue[++head] = root;mark[root] = true;
		while(head <= tail)
		{
			int e = queue[head];head++;//System.out.println(e);
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
					tail++;//System.out.println(tail);
					queue[tail] = node;
					dis[node] += dis[e] + 1;
					last[node] = e;
					mark[node] = true;
				}
			}
		}
	}
	public void run()
	{
		String name = "details[" + rank + "].txt";
		
		synchronized(q) {
			if(q.get_same(this) == true) {System.out.println("Same Request");return;}
			}
			try {sleep(1);}catch(InterruptedException e) {}
			
			gui.RequestTaxi(new Point(start_x,start_y), new Point (end_x,end_y));
			System.out.println("Time:"+time+":New Request:From ("+start_x+","+start_y+") to ("+end_x+","+end_y+")");
			try {
				BufferedWriter output = new BufferedWriter(new FileWriter(name,true));
				output.write("Time:"+time+":New Request:From ("+start_x+","+start_y+") to ("+end_x+","+end_y+")"+System.lineSeparator());
				output.close();
				}catch(IOException e) {}
	
		synchronized(q) {
		q.new_request(this);
		}
		try {sleep(1);}catch(InterruptedException e) {}
		try{sleep(3000);}
		catch(InterruptedException e)
		{}
		if(Called_tot == 0) {
			synchronized(q) {
			q.pop();}
			try {sleep(1);}catch(InterruptedException e) {}
			Date d = new Date();
			long time = d.getTime() - (d.getTime() % 100);
			System.out.println("Time:"+time+":The request is not received by any taxi.");
			return;}
		//System.out.println(Called_tot);
		int node = start_x * 80 + start_y;
		get_path(node);
		
		
		System.out.println("Called Taxi List:");
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(name,true));
			output.write("Called Taxi List:"+System.lineSeparator());
			output.close();
			}catch(IOException e) {}
		
		for(int i = 1;i <= Called_tot;i++)
		{
			Taxi t = CalledTaxi[i];
			int num = t.get_number();
			int x = t.get_x();
			int y = t.get_y();
			int cre = t.get_credit();
			String s = (Taxi.get_state(t) == Taxi.status.wfs) ? "wfs" :
						(Taxi.get_state(t) == Taxi.status.service) ? "service":
							(Taxi.get_state(t) == Taxi.status.called) ? "called" :
								"stop";
			System.out.println("Num:"+num+" Position:("+x+","+y+") State:"+s+" Credit:"+cre);
			try {
				BufferedWriter output = new BufferedWriter(new FileWriter(name,true));
				output.write("Num:"+num+" Position:("+x+","+y+") State:"+s+" Credit:"+cre+System.lineSeparator());
				output.close();
				}catch(IOException e) {}
		}
		while(true) {
			int num = -1;int c = 0;int d = Integer.MAX_VALUE;
			for(int i = 1;i <= Called_tot;i++)
			{
				synchronized(CalledTaxi[i])
				{
					if(c < CalledTaxi[i].get_credit() && Taxi.get_state(CalledTaxi[i]) == Taxi.status.wfs)
					{
						num = i;
						c = CalledTaxi[i].get_credit();
						d = dis[CalledTaxi[i].get_pos()];
					}
					else if(c == CalledTaxi[i].get_credit() && d > dis[CalledTaxi[i].get_pos()]  
							&& Taxi.get_state(CalledTaxi[i]) == Taxi.status.wfs)
					{
						num = i;
						c = CalledTaxi[i].get_credit();
						d = dis[CalledTaxi[i].get_pos()];
					}
				}
				try{
					sleep(1);
				}catch(InterruptedException e) {}
			}
			if(num == -1) 
			{
				Date D = new Date();
				long time = D.getTime() - (D.getTime() % 100);
				System.out.println("Time:"+time+":The request is not received by any taxi.");
				return;
			}
			int start = get_start_x() * 80 + get_start_y();
			int end = get_end_x() * 80 + get_end_y();
			synchronized(CalledTaxi[num]) {
				if(Taxi.get_state(CalledTaxi[num]) != Taxi.status.wfs)continue;
				CalledTaxi[num].process(name,start,end);
				Date D = new Date();
		
				long time = D.getTime() - (D.getTime() % 100);
		
				System.out.println("Time:"+time+":The request is processed by N0."+CalledTaxi[num].get_number()+" taxi.");
		
				try {
					BufferedWriter output = new BufferedWriter(new FileWriter(name,true));
					output.write("Time:"+time+":The request is processed by N0."+CalledTaxi[num].get_number()+" taxi."+System.lineSeparator());
					output.close();
				}catch(IOException e) {}
		
			}
			try {sleep(1);}catch(InterruptedException e) {}
			synchronized(q) {
				q.pop();}
				try {sleep(1);}catch(InterruptedException e) {}
				break;
		}
	}
}
