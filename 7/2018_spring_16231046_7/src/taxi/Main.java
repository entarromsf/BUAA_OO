package taxi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

public class Main {

	public static TaxiGUI gui = new TaxiGUI();
	private static PrintStream d;

	public static void main(String[] args) {
		try {

			FileOutputStream out = new FileOutputStream(new File("data.txt"));
			d = new PrintStream(out);

			Map ma = new Map();
			ma.readmap("map.txt");
			gui.LoadMap(ma.mapinfo, 80);

			ma.initmatrix();

			Car[] cars = new Car[100];
			for (int i = 0; i < 100; i++) {
				cars[i] = new Car(i, ma);

			}

			RequestQue rq = new RequestQue();
			Requestadd rad = new Requestadd(rq);

			rad.start();

			Dispatch d = new Dispatch(rq, cars, ma);

			d.start();

			for (int i = 0; i < 100; i++) {
				cars[i].start();
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}

	public static synchronized PrintStream data() {
		return d;
	}

	public static Vector<Integer> statetaxi(int state, Car[] cars) {
		Vector<Integer> taxiId = new Vector<Integer>();
		for (int i = 0; i < cars.length; i++) {
			if (cars[i].getstate() == state) {
				taxiId.add(cars[i].getid());
			}
		}
		return taxiId;
	}
}
