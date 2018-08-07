package taxi;

import java.awt.Point;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

public class test {
	public static TaxiGUI gui = new TaxiGUI();

	public static void main(String[] args) {

		Map ma = new Map();
		ma.readmap("map.txt");
		gui.LoadMap(ma.mapinfo, 80);
		
		Point root = new Point(1, 2);
		Point dst = new Point(79, 79);
		ma.initmatrix();
		
		RequestQue rq = new RequestQue();
		rq.add_ele(root, dst, new Date().getTime(), false);
		rq.getcars(0).ava_num++;
		System.out.println(rq.gettime(0));
	}
}
