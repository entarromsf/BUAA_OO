package taxi;

import java.awt.Point;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

interface MyInterator {
	public boolean repOK();

}

public class Car2 extends Car implements Runnable {

	private int id_son;

	public Car2(int id_t, Map map_t) {
		super(id_t, map_t);
		id_son = id_t;
	}

	@Override
	public boolean repOK() {
		// TODO Auto-generated method stub
		return super.repOK() && id_son >= 0 && id_son < 100;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			do {
				if (state == 2) {// 2:等待接单
					int[] avai0 = new int[4];
					int[] avai1 = new int[4];
					int avnum_t0 = 0;
					int avnum_t1 = 0;

					for (int i = 0; i < 4; i++) {
						int next = ptn(pos) + offset[i];
						if (next >= 0 && next < 6400 && ma.graph2[ptn(pos)][next] >= 1024
								&& ma.graph2[ptn(pos)][next] < gv.MAXNUM) {
							if (ma.graph2[ptn(pos)][next] == 1024) {
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
						info = "";
						info += "开始派单----时间："+new Date().getTime()+"\n";
				//		Info.add("开始派单----时间："+new Date().getTime());
						Main.data()
								.println("车辆编号：" + id + " 坐标：(" + pos.x + "," + pos.y + ")" + " 派单时间："
										+ new Date().getTime() + " 乘客坐标：(" + root.x + "," + root.y + ")" + " 目的地坐标：("
										+ dst.x + "," + dst.y + ")");
						info += "车辆编号：" + id + " 坐标：(" + pos.x + "," + pos.y + ")" + " 派单时间：" + new Date().getTime()
								+ " 乘客坐标：(" + root.x + "," + root.y + ")" + " 目的地坐标：(" + dst.x + "," + dst.y + ")"+"\n";
				//		Info.add("车辆编号：" + id + " 坐标：(" + pos.x + "," + pos.y + ")" + " 派单时间：" + new Date().getTime()
				//				+ " 乘客坐标：(" + root.x + "," + root.y + ")" + " 目的地坐标：(" + dst.x + "," + dst.y + ")");
						Main.data().println(
								"----------------------------------------------------------------------------");
						setstate(3);
						Main.gui.SetTaxiStatus(id, pos, state);
						road = ma.pointbfs2(ptn(pos), ptn(root));
						nowroad = 0;
					}

				} else if (state == 3) {// 3:接单前去拉客

					if (pos.equals(root)) {// 到达乘客所在地
						Main.data().println("出租车" + id + " " + "到达乘客所在地时间：" + new Date().getTime());
						info += "出租车" + id + " " + "到达乘客所在地时间：" + new Date().getTime()+"\n";
					//	Info.add("出租车" + id + " " + "到达乘客所在地时间：" + new Date().getTime());
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
						road = ma.pointbfs2(ptn(root), ptn(dst));
						nowroad = 0;
						continue;
					}

					if (chn != ma.chagenum) {
						road = ma.pointbfs2(ptn(pos), ptn(root));
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
					
					info += "出租车" + id + " " + "途径坐标：(" + pos.x + "," + pos.y + ")" + " 途径时间：" + new Date().getTime()+"\n";
					
		//			Info.add("出租车" + id + " " + "途径坐标：(" + pos.x + "," + pos.y + ")" + " 途径时间：" + new Date().getTime());
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
						
						info += "出租车" + id + " " + "到达目的地时间：" + new Date().getTime()+"\n";
						
						Info.add(info);
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
						road = ma.pointbfs2(ptn(root), ptn(dst));
						nowroad = 0;
						dst = maxp;
						continue;
					}

					if (chn != ma.chagenum) {
						road = ma.pointbfs2(ptn(pos), ptn(dst));
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
					info += "出租车" + id + " " + "途径坐标：(" + pos.x + "," + pos.y + ")" + " 途径时间：" + new Date().getTime()+"\n";
					
				//	Info.add("出租车" + id + " " + "途径坐标：(" + pos.x + "," + pos.y + ")" + " 途径时间：" + new Date().getTime());
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

}
