package taxi;

import java.awt.Point;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import javax.print.attribute.standard.MediaName;

public class Dispatch extends Thread {
	/**
     * @Overview:调度器类处理分配用户请求的作用。
     */
	private RequestQue rq;
	private Car[] cars;
	private long now_time;
	private Map ma;
	private Point src;
	private boolean[] output_flag;

	public Dispatch(RequestQue rq_t, Car[] c_t, Map map_t) {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: rq, cars, ma;
		 * 
		 * @ EFFECTS: rq = rq_t; cars = c_t; ma = map_t;
		 */
		rq = rq_t;
		cars = c_t;
		ma = map_t;
	}

	public boolean repOK() {
		/**
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ Effects: \result == invariant(this);
		 */
		if (rq == null)
			return false;
		if (cars == null)
			return false;
		if (ma == null)
			return false;
		return true;
	}

	@Override
	public void run() {
		/**
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: System.out, rq, cars, now_time, ma, src;
		 * 
		 * @ EFFECTS: 把请求分配到每一个车上
		 */
		do {
			////////////////////////////////////////////////////////////////////

			now_time = new Date().getTime();

			for (int i = 0; i < rq.getNum(); i++) {

				Ava_Car ava_car = rq.getcars(i);

				if (!rq.getuse(i) && now_time <= rq.gettime(i) + 7500) {

					/////////////////////////////////////
					for (int j = 0; j < 100; j++) {
						boolean flag = false;

						if ((cars[j].getpos().distance(rq.getsrcp(i)) < 2.9) && (cars[j].getstate() == 2)) {

							for (int k = 0; k < ava_car.ava_num; k++) {
								if ((ava_car.cars[k].getid()) == j) {
									flag = true;
									break;
								}
							}

							if (!flag) {
								ava_car.cars[ava_car.ava_num] = cars[j];
								cars[j].addcredit();
								if(j < 30) {
									////////////////记录追踪出租车抢单
									cars[j].Info.add("出租车抢单----请求发出时刻：" + rq.gettime(i) + " 请求坐标：(" + rq.getsrcp(i).x + "," + rq.getsrcp(i).y + ")"
											+ " 目的地坐标：(" + rq.getdstp(i).x + "," + rq.getdstp(i).y + ")"+"出租车抢单时刻："+new Date().getTime()+"出租车所处位置："+"("+cars[j].getpos().x+","+cars[j].getpos().y+")");
								}
								ava_car.ava_num++;
							}
						}
					}
					rq.setcars(ava_car, i);
					////////////////////////////////////
				}
			}
			/////////////////////////////////////////////////////////////////////
			now_time = new Date().getTime();

			for (int i = 0; i < rq.getNum(); i++) {

				Ava_Car ava_car = rq.getcars(i);

				src = rq.getsrcp(i);

				if ((!rq.getuse(i)) && (now_time > rq.gettime(i) + 7500)) {

					if (ava_car.ava_num == 0) {
						Main.data().println(
								"----------------------------------------------------------------------------");
						Main.data().println("乘客请求内容----发出时刻：" + rq.gettime(i) + " 请求坐标：(" + src.x + "," + src.y + ")"
								+ " 目的地坐标：(" + rq.getdstp(i).x + "," + rq.getdstp(i).y + ")");
						Main.data().println("窗口结束");
						Main.data().println("无出租车接单");
						Main.data().println(
								"----------------------------------------------------------------------------");
						rq.setuse(i, true);
						System.out.println("No Answer");
						continue;
					}
					Main.data().println("----------------------------------------------------------------------------");
					Main.data().println("乘客请求内容----发出时刻：" + rq.gettime(i) + " 请求坐标：(" + src.x + "," + src.y + ")"
							+ " 目的地坐标：(" + rq.getdstp(i).x + "," + rq.getdstp(i).y + ")");
					Main.data().println("窗口结束");
					Car[] car_t = new Car[ava_car.ava_num];
					for (int j = 0; j < ava_car.ava_num; j++) {
						car_t[j] = ava_car.cars[j];
						Main.data()
								.println("出租车信息----车辆编号：" + car_t[j].getid() + " 所在位置：(" + car_t[j].getpos().x + ","
										+ car_t[j].getpos().y + ")" + " 车辆状态：" + car_t[j].getstate() + " 车辆信用信息："
										+ car_t[j].getcredit());
					}
					///////// 排序 /////////////////
					Arrays.sort(car_t, new Comparator<Car>() {
						@Override
						public int compare(Car o1, Car o2) {
							if (o1.getcredit() > o2.getcredit()) {// 信用高的排前
								return -1;
							} else if (o1.getcredit() < o2.getcredit()) {
								return 1;
							} else {// 信用相同时按路程
								if ((ma.mindis(o1.ptn(o1.getpos()), o1.ptn(src))) < (ma.mindis(o2.ptn(o2.getpos()),
										o2.ptn(src)))) {
									return -1;
								} else if ((ma.mindis(o1.ptn(o1.getpos()), o1.ptn(src))) > (ma
										.mindis(o2.ptn(o2.getpos()), o2.ptn(src)))) {
									return 1;
								} else {
									return 0;
								}
							}
						}
					});
					//////// 分配任务给出租 /////////////////
					boolean flag = false;
					for (int j = 0; j < car_t.length; j++) {
						if (car_t[j].getstate() == 2) {
							car_t[j].setroot(src);
							car_t[j].setdst(rq.getdstp(i));
							flag = true;
							rq.setuse(i, true);
							cars[j].Info.add("出租车成功抢单----出租车分配时刻："+new Date().getTime());
							break;
						}
					}
					if (!flag) {
						rq.setuse(i, true);
						Main.data().println("无出租车接单");
						System.out.println("No Answer");
					}
					Main.data().println("----------------------------------------------------------------------------");
				}
			}
			/////////////////////////////////////////////////////////////////////
		} while (true);
	}
}
