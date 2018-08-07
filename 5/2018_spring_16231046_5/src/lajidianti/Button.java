package lajidianti;

import java.util.Arrays;

public class Button {
	private static boolean[] upb = new boolean[50];
	private static boolean[] downb = new boolean[50];
	private static boolean[][] eleb = new boolean[5][50];
	
	public Button() {
		Arrays.fill(downb, false);
		Arrays.fill(upb, false);
		Arrays.fill(eleb, false);
	}

	public synchronized static boolean getUpb(int i) {
		return upb[i];
	}

	public synchronized static void setUpb(int i,boolean t) {
		upb[i] = t;
	}

	public synchronized static boolean getDownb(int i) {
		return downb[i];
	}

	public synchronized static void setDownb(int i,boolean t) {
		downb[i] = t;
	}

	public synchronized static boolean getEleb(int id, int floor) {
		return eleb[id][floor];
	}

	public synchronized static void setEleb(int id,int floor,boolean t) {
		eleb[id][floor] = t;
	}
	
}
