package simulatedElevator;

import java.util.ArrayList;

class Elevator implements Dispatch, Runnable {
	private boolean[] light = new boolean[Main.FLOORSUM];
	// 0:er 1:up 2:down
	private int floor_now;
	private int target_floor;
	private short sta; // 0:still 1:up 2:down 3:wait
	private int amount_of_run;
	private ArrayList<Request> req_list = new ArrayList<Request>();
	private String name;
	private double timeNow;

	Elevator() {
		this.floor_now = 1;
		this.target_floor = 1;
		this.sta = 3;
		this.amount_of_run = 0;
		int i;
		for (i = 0; i < Main.FLOORSUM; i++) {
			this.light[i] = false;

		}
		this.timeNow = 0;
	}

	int get_amount() {
		return this.amount_of_run;
	}

	void add_req(Request a_req) {
		synchronized (this) {
			this.req_list.add(a_req);
		}
	}

	boolean get_light(int i) {
		return this.light[i - 1];
	}

	void set_light(int i, boolean change) {
		this.light[i - 1] = change;
	}

	int getFloor() {
		return floor_now;
	}

	short get_sta() {
		synchronized (this) {
			return this.sta;
		}
	}

	void set_target(int a) {
		synchronized (this) {
			target_floor = a;
			if (floor_now == a) {
				this.sta = 0;
			} else if (floor_now > a) {
				this.sta = 2;
			} else {
				this.sta = 1;
			}
		}
	}

	int get_target() {
		return target_floor;
	}

	public void flat_run() {
		if (floor_now == target_floor) {
			return;
		} else if (floor_now > target_floor) {
			floor_now -= 1;
			this.amount_of_run++;
		} else {
			floor_now += 1;
			this.amount_of_run++;
		}
		this.timeNow += 3;
	}

	private void printRes(Request aReq, long theTime) {
		
		if (this.get_sta() == 0) {
			System.out.printf("%d:[%s]/(#%s,%d,STILL,%d,%.1f)\r\n", theTime, aReq, name, this.floor_now,
					this.amount_of_run, this.timeNow);
		} else
			System.out.printf("%d:[%s]/(#%s,%d,%s,%d,%.1f)\r\n", theTime, aReq, name, this.floor_now,
					(this.get_sta() == 1 ? "UP" : "DOWN"), this.amount_of_run, this.timeNow);
					
	}

	private boolean stop_or_not() {
		synchronized (this) {
			for (int i = 0; i < this.get_size(); i++) {
				if (this.get_sta() != 3 && this.req_list.get(i).get_req_floor() == this.getFloor()) {
					return true;
				}
			}
			return false;
		}
	}

	private int get_size() {
		synchronized (this) {
			return this.req_list.size();
		}
	}

	public void run() {
		name = Thread.currentThread().getName();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) { }

		while (true) {
			// synchronized(this )
			{

				if (this.stop_or_not()) {
					// 在当前楼层有指令
					long theTime = System.currentTimeMillis();
					Request[] cache = new Request[3];
					int top = 0;
					synchronized (this) {
						for (int i = 0; i < this.get_size(); i++) {
							if (this.req_list.get(i).flag == 0
									&& this.req_list.get(i).get_req_floor() == this.getFloor()) {
								cache[top++] = this.req_list.get(i); 
							}
						}
					} 
					if (top > 1) {
						Request aReq;
						if (cache[0].num > cache[1].num) {
							aReq = cache[0];
							cache[0] = cache[1];
							cache[1] = aReq;
						}
						if (top > 2) {
							if (cache[0].num > cache[2].num) {
								aReq = cache[0];
								cache[0] = cache[2];
								cache[2] = aReq;
							}
							if (cache[1].num > cache[2].num) {
								aReq = cache[1];
								cache[1] = cache[2];
								cache[2] = aReq;
							}
							this.printRes(cache[0], theTime);
							cache[0].flag = 1;
							this.printRes(cache[1], theTime);
							cache[1].flag = 1;
							this.printRes(cache[2], theTime);
							cache[2].flag = 1;
						} else {
							this.printRes(cache[0], theTime);
							cache[0].flag = 1;
							this.printRes(cache[1], theTime);
							cache[1].flag = 1;
						}

					} else if (this.get_sta() != 0) {
						this.printRes(cache[0], theTime);
						cache[0].flag = 1;
					}

					try {
						Thread.sleep(5995);
					} catch (InterruptedException e) { }
					this.timeNow += 6;
					// Main.scheduler.interrupt();
					Main.ascheduler.restart = true;
					if (this.get_sta() == 0 && top <= 1) {
						this.printRes(cache[0], theTime + 6000);
						cache[0].flag = 1;
					}

					synchronized (this) {
						for (int i = 0; i < this.req_list.size(); i++) {
							// 熄灭相应的灯
							if (this.req_list.get(i).flag == 1) {
								if (this.req_list.get(i).get_req_type() == 0) {
									this.set_light(this.floor_now, false);
								} else {
									Main.floor[this.floor_now].set_light(this.req_list.get(i).get_req_type() - 1,
											false);
								}
								this.req_list.remove(i);
								i--;

							}
						}
						if (this.target_floor == this.floor_now && this.req_list.size() > 0) {
							this.set_target(this.req_list.get(0).get_req_floor());
						}
					}

				}
			}
			try {
				Thread.sleep(5);
			} catch (Exception e1) { }

			// this.flat_run();
			Main.ascheduler.restart = true;
			// Main.scheduler.interrupt();
			try {
				if (this.target_floor == this.floor_now && this.req_list.size() == 0)
					this.sta = 3;
				Thread.sleep(2990);
			} catch (InterruptedException e) { }
			this.flat_run();
			if (this.get_sta() == 3 && this.get_size() > 0) {
				this.set_target(this.req_list.get(0).get_req_floor());
				if (this.timeNow - this.req_list.get(0).get_req_time() < 0.05) {
					this.timeNow = this.req_list.get(0).get_req_time();
				}
				Main.ascheduler.restart = true;
			}
			synchronized (this) {
				if (!Main.scheduler.isAlive() && this.req_list.size() == 0) {
					// 当调度器关闭同时电梯请求队列为空时删除
					break;
				}

			}
		}

	} 
}
