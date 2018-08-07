package taxi;

import java.awt.Point;
import java.util.Date;
import java.util.Random;

public class Car extends Thread {
	private int id, state,credit;// 0:停止 1:服务 2:等待接单 3:接单前去拉客
	private Point pos;
	private Random rand = new Random();
	private long wait_start, now_t;
	private Point dst,root;
	private Point maxp = new Point(6400, 6400);
	private Map ma;
	private int[] offset = new int[] { 1, -1, 80, -80 };
	
	private int[] road = null;
	private int nowroad = 0;
	
	
	
	
	public Car(int id_t, Map map_t) {
		id = id_t;
		pos = new Point(rand.nextInt(80), rand.nextInt(80));
		setstate(2);
		wait_start = new Date().getTime();
		Main.gui.SetTaxiStatus(id, pos, state);
		dst = maxp;
		ma = map_t;
		credit = 0;
	}

	@Override
	public void run() {
		do {
			if (state == 2) {// 2:等待接单
				int[] avai = new int[4];
				int avnum_t = 0;

				for (int i = 0; i < 4; i++) {
					int next = ptn(pos) + offset[i];
					if (next >= 0 && next < 6400 && ma.graph[ptn(pos)][next] == 1) {
						avai[avnum_t] = next;
						avnum_t++;
					}
				}

				pos = ntp(avai[rand.nextInt(avnum_t)]);// 瞎跑
				try {
					sleep(200);
				} catch (Exception e) {
				}
				Main.gui.SetTaxiStatus(id, pos, state);

				now_t = new Date().getTime();
				if (now_t - wait_start >= 20000) {
					setstate(0);
					Main.gui.SetTaxiStatus(id, pos, state);
					try {
						sleep(1000);
					} catch (Exception e) {
					}
					setstate(2);
					Main.gui.SetTaxiStatus(id, pos, state);
					wait_start = new Date().getTime();
				}

				if (!dst.equals(maxp)) {
					Main.data().println("----------------------------------------------------------------------------");
					Main.data().println("开始派单");
					Main.data().println("车辆编号："+id+" 坐标：("+pos.x+","+pos.y+")"+" 派单时间："+new Date().getTime()+" 乘客坐标：("+root.x+","+root.y+")"+" 目的地坐标：("+dst.x+","+dst.y+")");
					Main.data().println("----------------------------------------------------------------------------");
					setstate(3);
					Main.gui.SetTaxiStatus(id, pos, state);
					road = ma.pointbfs(ptn(pos), ptn(root));
					nowroad = 0;
				}
				
			} else if (state == 3) {// 3:接单前去拉客
				
				if(pos.equals(root)) {//到达乘客所在地
					Main.data().println("出租车"+id+" "+"到达乘客所在地时间："+new Date().getTime());
					setstate(0);
					Main.gui.SetTaxiStatus(id, pos, state);
					try {
						sleep(1000);
					} catch (Exception e) {}
					setstate(1);
					Main.gui.SetTaxiStatus(id, pos, state);
					road = ma.pointbfs(ptn(root), ptn(dst));
					nowroad = 0;
					continue;
				}
				
				nowroad++;
				pos = ntp(road[nowroad]);
				Main.data().println("出租车"+id+" "+"途径坐标：("+pos.x+","+pos.y+")"+" 途径时间："+new Date().getTime());
				Main.gui.SetTaxiStatus(id, pos, state);
				try {
					sleep(200);
				} catch (Exception e) {}
				
			} else {// 1:服务
				
				if(pos.equals(dst)) {//到达目的地
					Main.data().println("出租车"+id+" "+"到达目的地时间："+new Date().getTime());
					credit += 3;
					setstate(0);
					Main.gui.SetTaxiStatus(id, pos, state);
					try {
						sleep(1000);
					} catch (Exception e) {}
					setstate(2);
					wait_start = new Date().getTime();
					Main.gui.SetTaxiStatus(id, pos, state);
					road = ma.pointbfs(ptn(root), ptn(dst));
					nowroad = 0;
					dst = maxp;
					continue;
				}
				
				nowroad++;
				pos = ntp(road[nowroad]);
				Main.data().println("出租车"+id+" "+"途径坐标：("+pos.x+","+pos.y+")"+" 途径时间："+new Date().getTime());
				Main.gui.SetTaxiStatus(id, pos, state);
				try {
					sleep(200);
				} catch (Exception e) {}					
			}
		} while (true);
	}

	public int ptn(Point p_t) {
		return (p_t.x) * 80 + p_t.y;
	}

	public Point ntp(int n_t) {
		Point p_t = new Point((int) (n_t / 80.0), n_t % 80);
		return p_t;
	}

	public synchronized void setstate(int state_t) {
		state = state_t;
	}
//////////////////////////////////////////////////	
	public synchronized int getstate() {
		return state;
	}
	
	public synchronized Point getpos() {
		return pos;
	}
	
	public long gettime() {
		return new Date().getTime();
	}
	
	public synchronized int getcredit() {
		return credit;
	}
//////////////////////////////////////////////////	
	public synchronized void addcredit() {
		credit++;
	}
	

	

	
	public synchronized int getid() {
		return id;
	}
	
	public synchronized void setdst(Point dst_t) {
		dst = dst_t;
	}
	
	public synchronized void setroot(Point root_t) {
		root = root_t;
	}
	

}
