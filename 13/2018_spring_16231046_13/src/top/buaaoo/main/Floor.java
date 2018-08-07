package top.buaaoo.main;
import java.util.regex.*;
import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;

public class Floor {
	private int floor = 1;
	
    int getFloor() {
		return floor;
	}

	void setFloor(int floor) {
		this.floor = floor;
	}
	
	public boolean repOK(){
		/**@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: floor >= 1 && floor <= 10 ==> \result == true;
				   floor < 1 || floor > 10 ==> \result == false;
		*/
		return true;
	}
	
	void down() {
		/**@ REQUIRES: None;
		@ MODIFIES: floor;
		@ Effects: floor == (old)floor--;
		*/
		floor--;
	}
	
	void up() {
		/**@ REQUIRES: None;
		@ MODIFIES: floor;
		@ Effects: floor == (old)floor++;
		*/
		floor++;
	}
	
}
