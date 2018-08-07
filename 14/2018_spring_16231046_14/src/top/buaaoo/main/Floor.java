package top.buaaoo.main;
import java.util.regex.*;
import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;

public class Floor {
	/*Overview:楼层类，负责上下楼。
	 * 表示对象：floor
	 * 
	 */
	private int floor;
	
	public Floor() {
		floor = 1;
	}
    int getFloor() {
		return floor;
	}

	void setFloor(int floor) {
		if(floor >= 1 && floor <= 10)
			this.floor = floor;
	}
	
	public boolean repOK(){
		/**@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: floor >= 1 && floor <= 10 ==> \result == true;
				   floor < 1 || floor > 10 ==> \result == false;
		*/
		if(floor >= 1 && floor <= 10)
			return true;
		else
			return false;
	}
	
	void down() {
		/**@ REQUIRES: None;
		@ MODIFIES: floor;
		@ Effects: (floor != 1)==>(floor == (old)floor--);
		*/
		if(floor == 1) return;
		floor--;
	}
	
	void up() {
		/**@ REQUIRES: None;
		@ MODIFIES: floor;
		@ Effects: (floor != 10)==>(floor == (old)floor++);
		*/
		if(floor == 10) return;
		floor++;
	}
	
}
