package simulatedElevator;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {

	final static int FLOORSUM = 20; // 电梯楼层数
	final static int MAX_INST_NUM = 10; // 最大指令数
	final static int MAXREQ = 50; // 允许处理最大请求数

	static int effectiveReqCount = 0; // 实际有效请求数
	static long start_time = 0; // 开始 的时间
	static Floor[] floor = new Floor[FLOORSUM+1];
	static Elevator[] elevator = new Elevator[3];
	static Areader areader = new Areader();
	static Scheduler ascheduler = new Scheduler();

	static Thread read = new Thread(areader);
	static Thread scheduler = new Thread(ascheduler);
	static Thread[] elevator_ = new Thread[3];

	public static void main(String[] args) {
		try {
			System.setOut(new PrintStream(new FileOutputStream("result.txt")));
			String[] name = { "1", "2", "3" };
			int i;
			for (i = 0; i < FLOORSUM + 1; i++) {
				floor[i] = new Floor();
			}
			for (i = 0; i < 3; i++) {
				elevator[i] = new Elevator();
				elevator_[i] = new Thread(elevator[i], name[i]);
			}
			read.start();
			scheduler.start();
			for (i = 0; i < 3; i++) {
				elevator_[i].start();
			}
		} catch (Exception e) { }
	}
}