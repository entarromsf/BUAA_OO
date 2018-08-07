package taxi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

public class Main {
	/**
     * @Overview:Main类模拟整个打车过程.
     */

	public static TaxiGUI gui = new TaxiGUI();
	public static Light light = new Light();
	private static PrintStream d;

	public static void main(String[] args) {
		/*@ Requires: String类型的地图路径;
		@ Modifies: None;
		@ Effects:  Normal behavior
					读入地图，读入线程不断读入请求，调度器线程执行，分配给100个出租车的线程执行
					过程中出现任何异常操作 ==> exceptional_behavior(e)
		*/
		try {

			FileOutputStream out = new FileOutputStream(new File("data.txt"));
			d = new PrintStream(out);

			Map ma = new Map();
			ma.readmap("map.txt");
			gui.LoadMap(ma.mapinfo, 80);

			ma.initmatrix();
			
			light.setma(ma);
			
			light.readlight("light.txt");
			
			
			Car[] cars = new Car[100];
			for (int i = 0; i < 100; i++) {
				cars[i] = new Car(i, ma);

			}

			RequestQue rq = new RequestQue();
			Requestadd rad = new Requestadd(rq,ma,cars);

			rad.start();

			Dispatch d = new Dispatch(rq, cars, ma);

			d.start();
			
			light.start();
			
			for (int i = 0; i < 100; i++) {
				cars[i].start();
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}

	public static synchronized PrintStream data() {
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == d;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return d;
	}

	public static Vector<Integer> statetaxi(int state, Car[] cars) {
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == taxiId;
		*/
		Vector<Integer> taxiId = new Vector<Integer>();
		for (int i = 0; i < cars.length; i++) {
			if (cars[i].getstate() == state) {
				taxiId.add(cars[i].getid());
			}
		}
		return taxiId;
	}
}
