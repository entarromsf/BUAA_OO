package taxi;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;

public class Car implements Runnable {
	// extends Thread {
	/**
	 * @Overview:出租车类会执行分配的任务。
	 */
	protected int id;// 0:停止 1:服务 2:等待接单 3:接单前去拉客
	protected int state;
	protected int credit;
	protected Point pos, pre;
	protected Random rand = new Random();
	protected long wait_start, now_t;
	protected Point dst, root;
	protected Point maxp = new Point(6400, 6400);
	protected Map ma;
	protected int[] offset = new int[] { 1, -1, 80, -80 };
	protected int chn;
	protected int[] road = null;
	protected int nowroad = 0;
	protected int xjbz = gv.MAXNUM;
	public ArrayList<String> Info = new ArrayList<String>();
	protected String info = "";
	protected ListIterator<String> iter;

	public Car(int id_t, Map map_t) {
		/**
		 * @ Requires: None;
		 * 
		 * @ Modifies: id, pos, wait_start, Main.gui, pre, dst, ma, credit, chn;
		 * 
		 * @ Effects: id == id_t; ma == map_t; pos == new Point(rand.nextInt(80),
		 * rand.nextInt(80)); 以及一些构造函数的初始化
		 */
		id = id_t;
		pos = new Point(rand.nextInt(80), rand.nextInt(80));
		pre = new Point(pos.x, pos.y);
		setstate(2);
		wait_start = new Date().getTime();
		Main.gui.SetTaxiStatus(id, pos, state);
		dst = maxp;
		ma = map_t;
		credit = 0;
		chn = 0;
	}

	public boolean repOK() {
		/**
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ Effects: \result == invariant(this);
		 */
		if (id > 100)
			return false;
		if (pos.x < 0 || pos.x > 80 || pos.y < 0 || pos.y > 80)
			return false;
		return true;
	}

	@Override
	public void run() {
		/**
		 * @ Requires: None;
		 * 
		 * @ Modifies: System.out, id, credit, pos, pre, state, Main.gui, ma, chn, road,
		 * wait_start, now_t;
		 * 
		 * @ Effects: normal behavior 进行车的运动 sleep出现异常 ==> exceptional_behavior (e);
		 * 
		 * @ THREAD_REQUIRES: \locked(this);
		 * 
		 * @ THREAD_EFFECTS: \locked();
		 */
		try {
			do {
				if (state == 2) {// 2:等待接单
					int[] avai0 = new int[4];
					int[] avai1 = new int[4];
					int avnum_t0 = 0;
					int avnum_t1 = 0;

					for (int i = 0; i < 4; i++) {
						int next = ptn(pos) + offset[i];
						if (next >= 0 && next < 6400 && ma.graph[ptn(pos)][next] >= 1024
								&& ma.graph[ptn(pos)][next] < gv.MAXNUM) {
							if (ma.graph[ptn(pos)][next] == 1024) {
								avai0[avnum_t0] = next;
								avnum_t0++;
							} else {
								avai1[avnum_t1] = next;
								avnum_t1++;
							}
						}
					}
					if (avnum_t0 != 0)
						xjbz = avai0[rand.nextInt(avnum_t0)];// 瞎跑
					else
						xjbz = avai1[rand.nextInt(avnum_t1)];// 瞎跑

					if (!avlgo(pre, pos, ntp(xjbz))) {
						Thread.sleep(10);
						continue;
					}

					setpre();
					pos = ntp(xjbz);
					try {
						Thread.sleep(500);
						ma.addFlow(ptn(pre), ptn(pos));
					} catch (Exception e) {
					}
					Main.gui.SetTaxiStatus(id, pos, state);

					now_t = new Date().getTime();
					if (now_t - wait_start >= 20000) {
						setstate(0);
						Main.gui.SetTaxiStatus(id, pos, state);
						try {
							Thread.sleep(500);
							ma.clearFlow(ptn(pre), ptn(pos));
							Thread.sleep(500);
						} catch (Exception e) {
						}
						setstate(2);
						Main.gui.SetTaxiStatus(id, pos, state);
						wait_start = new Date().getTime();
					}

					if (!dst.equals(maxp)) {
						Main.data().println(
								"----------------------------------------------------------------------------");
						Main.data().println("开始派单");
						Main.data()
								.println("车辆编号：" + id + " 坐标：(" + pos.x + "," + pos.y + ")" + " 派单时间："
										+ new Date().getTime() + " 乘客坐标：(" + root.x + "," + root.y + ")" + " 目的地坐标：("
										+ dst.x + "," + dst.y + ")");
						Main.data().println(
								"----------------------------------------------------------------------------");
						setstate(3);
						Main.gui.SetTaxiStatus(id, pos, state);
						road = ma.pointbfs(ptn(pos), ptn(root));
						nowroad = 0;
					}

				} else if (state == 3) {// 3:接单前去拉客

					if (pos.equals(root)) {// 到达乘客所在地
						Main.data().println("出租车" + id + " " + "到达乘客所在地时间：" + new Date().getTime());
						setstate(0);
						Main.gui.SetTaxiStatus(id, pos, state);
						try {
							Thread.sleep(500);
							ma.clearFlow(ptn(pre), ptn(pos));
							Thread.sleep(500);
						} catch (Exception e) {
						}
						setstate(1);
						Main.gui.SetTaxiStatus(id, pos, state);
						road = ma.pointbfs(ptn(root), ptn(dst));
						nowroad = 0;
						continue;
					}

					if (chn != ma.chagenum) {
						road = ma.pointbfs(ptn(pos), ptn(root));
						nowroad = 0;
						chn = ma.chagenum;
					}

					if (!avlgo(pre, pos, ntp(road[nowroad]))) {
						Thread.sleep(5);
						continue;
					}

					nowroad++;
					setpre();
					pos = ntp(road[nowroad]);
					Main.data().println(
							"出租车" + id + " " + "途径坐标：(" + pos.x + "," + pos.y + ")" + " 途径时间：" + new Date().getTime());
					Main.gui.SetTaxiStatus(id, pos, state);
					try {
						Thread.sleep(500);
						ma.addFlow(ptn(pre), ptn(pos));
					} catch (Exception e) {
					}

				} else if (state == 0) {// 0:停止
					Main.gui.SetTaxiStatus(id, pos, state);
					try {
						Thread.sleep(500);
						ma.clearFlow(ptn(pre), ptn(pos));
						Thread.sleep(500);
					} catch (Exception e) {
					}
					setstate(2);
					Main.gui.SetTaxiStatus(id, pos, state);
					wait_start = new Date().getTime();
				} else {// 1:服务

					if (pos.equals(dst)) {// 到达目的地
						Main.data().println("出租车" + id + " " + "到达目的地时间：" + new Date().getTime());
						credit += 3;
						setstate(0);
						Main.gui.SetTaxiStatus(id, pos, state);
						try {
							Thread.sleep(500);
							ma.clearFlow(ptn(pre), ptn(pos));
							Thread.sleep(500);
						} catch (Exception e) {
						}
						setstate(2);
						wait_start = new Date().getTime();
						Main.gui.SetTaxiStatus(id, pos, state);
						road = ma.pointbfs(ptn(root), ptn(dst));
						nowroad = 0;
						dst = maxp;
						continue;
					}

					if (chn != ma.chagenum) {
						road = ma.pointbfs(ptn(pos), ptn(dst));
						nowroad = 0;
						chn = ma.chagenum;
					}

					if (!avlgo(pre, pos, ntp(road[nowroad]))) {
						Thread.sleep(5);
						continue;
					}

					nowroad++;
					setpre();
					pos = ntp(road[nowroad]);
					Main.data().println(
							"出租车" + id + " " + "途径坐标：(" + pos.x + "," + pos.y + ")" + " 途径时间：" + new Date().getTime());
					Main.gui.SetTaxiStatus(id, pos, state);
					try {
						Thread.sleep(500);
						ma.addFlow(ptn(pre), ptn(pos));
					} catch (Exception e) {
					}
				}
			} while (true);
		} catch (Exception e) {
			System.out.println("No." + id + "不干了");
			return;
		}
	}

	public void setpre() {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: pre, ma;
		 * 
		 * @ EFFECTS: pre = pos ma.clearFlow(ptn(pre), ptn(pos));
		 */
		ma.clearFlow(ptn(pre), ptn(pos));
		pre.x = pos.x;
		pre.y = pos.y;
	}

	public int ptn(Point p_t) {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: \result == (p_t.x) * 80 + p_t.y;
		 */
		return (p_t.x) * 80 + p_t.y;
	}

	public Point ntp(int n_t) {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: \result == new Point((int) (n_t / 80.0), n_t % 80);
		 */
		Point p_t = new Point((int) (n_t / 80.0), n_t % 80);
		return p_t;
	}

	public synchronized void setstate(int state_t) {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: state = state_t;
		 * 
		 * @ THREAD_REQUIRES: \locked(this);
		 * 
		 * @ THREAD_EFFECTS: \locked();
		 */
		state = state_t;
	}

	//////////////////////////////////////////////////
	public synchronized int getstate() {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: \result == state;
		 * 
		 * @ THREAD_REQUIRES: \locked(this);
		 * 
		 * @ THREAD_EFFECTS: \locked();
		 */
		return state;
	}

	public synchronized Point getpos() {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: \result == pos;
		 * 
		 * @ THREAD_REQUIRES: \locked(this);
		 * 
		 * @ THREAD_EFFECTS: \locked();
		 */
		return pos;
	}

	public long gettime() {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: \result == new Date().getTime();
		 */
		return new Date().getTime();
	}

	public synchronized int getcredit() {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: \result == credit;
		 * 
		 * @ THREAD_REQUIRES: \locked(this);
		 * 
		 * @ THREAD_EFFECTS: \locked();
		 */
		return credit;
	}

	//////////////////////////////////////////////////
	public synchronized void addcredit() {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: credit;
		 * 
		 * @ EFFECTS: credit++;
		 * 
		 * @ THREAD_REQUIRES: \locked(this);
		 * 
		 * @ THREAD_EFFECTS: \locked();
		 */
		credit++;
	}

	public synchronized void setcredit(int c_t) {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: credit;
		 * 
		 * @ EFFECTS: credit = c_t;
		 * 
		 * @ THREAD_REQUIRES: \locked(this);
		 * 
		 * @ THREAD_EFFECTS: \locked();
		 */
		credit = c_t;
	}

	public synchronized void setpos(int i, int j) {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: pos;
		 * 
		 * @ EFFECTS: pos.x = i; pos.y = j;
		 * 
		 * @ THREAD_REQUIRES: \locked(this);
		 * 
		 * @ THREAD_EFFECTS: \locked();
		 */
		pos.x = i;
		pos.y = j;
	}

	public synchronized int getid() {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: \result == id;
		 * 
		 * @ THREAD_REQUIRES: \locked(this);
		 * 
		 * @ THREAD_EFFECTS: \locked();
		 */
		return id;
	}

	public synchronized void setdst(Point dst_t) {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: dst;
		 * 
		 * @ EFFECTS: dst = dst_t;
		 * 
		 * @ THREAD_REQUIRES: \locked(this);
		 * 
		 * @ THREAD_EFFECTS: \locked();
		 */
		dst = dst_t;
	}

	public synchronized void setroot(Point root_t) {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: root;
		 * 
		 * @ EFFECTS: root = root_t;
		 * 
		 * @ THREAD_REQUIRES: \locked(this);
		 * 
		 * @ THREAD_EFFECTS: \locked();
		 */
		root = root_t;
	}

	public boolean avlgo(Point prev, Point now, Point next) {
		// left=1,right=-1,straight=0;
		int direction;
		direction = calcu(prev, now, next);
		int lightstate = Main.light.light[now.x][now.y];
		if (lightstate == 1) {
			if (direction == 1) {
				if (now.x - prev.x == 0) {
					return false;
				} else {
					return true;
				}
			} else if (direction == -1) {
				return true;
			} else {
				if (now.x - prev.x == 0) {
					return true;
				} else {
					return false;
				}
			}
		} else if (lightstate == 2) {
			if (direction == 1) {
				if (now.x - prev.x == 0) {
					return true;
				} else {
					return false;
				}
			} else if (direction == -1) {
				return true;
			} else {
				if (now.x - prev.x == 0) {
					return false;
				} else {
					return true;
				}
			}
		} else {
			return true;
		}
	}

	public int calcu(Point a, Point b, Point c) {
		return (b.x - a.x) * (c.y - b.y) - (c.x - b.x) * (b.x - a.x);
	}

	public void Infoprint() {
		String s = "taxi" + id + ".txt";
		try {
			FileOutputStream out = new FileOutputStream(new File(s));
			PrintStream tax = new PrintStream(out);
			for (int i = 0; i < Info.size(); i++) {
				tax.println(Info.get(i));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
////////实现双向迭代器

	public synchronized String next() {
		/**
		 * @ REQUIRES: None 
		 * @ MODIFIES: this.iter 
		 * @ EFFECTS: None
		 */
		if(iter == null) {
			iter = Info.listIterator();
		}
		else {
			iter = Info.listIterator(iter.nextIndex());
		}
		
		return this.iter.next();
	}

	public synchronized boolean hasNext() {
		/**
		 * @ REQUIRES: None 
		 * @ MODIFIES: this.iter 
		 * @ EFFECTS: None
		 */
		if(iter == null) {
			iter = Info.listIterator();
		}
		else {
			iter = Info.listIterator(iter.nextIndex());
		}
		return this.iter.hasNext();
	}

	public synchronized boolean hasPrevious() {
		/**
		 * @ REQUIRES: None 
		 * @ MODIFIES: this.iter 
		 * @ EFFECTS: None
		 */
		if(iter == null) {
			iter = Info.listIterator();
		}
		else {
			iter = Info.listIterator(iter.nextIndex());
		}
		return this.iter.hasPrevious();
	}

	public synchronized String previous() {
		/**
		 * @ REQUIRES: None 
		 * @ MODIFIES: this.iter 
		 * @ EFFECTS: None
		 */
		if(iter == null) {
			iter = Info.listIterator();
		}
		else {
			iter = Info.listIterator(iter.nextIndex());
		}
		return this.iter.previous();
	}
}
