package top.buaaoo.main;
import java.util.regex.*;
import java.util.Scanner;
import java.lang.Math;
import java.sql.Time;
import java.util.ArrayList;

public class Elevator {
	public boolean repOK(){
		/**@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		*/
		return true;
	}
	
	double opendoor(double time) {
		/**@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == time + 1;
		*/
		return time + 1;
	}
	double running(double time,int nowfloor,int targetfloor) {
		/**@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == time+Math.abs((targetfloor-nowfloor)*0.5)+1;
		*/
		return (double)(time+Math.abs((targetfloor-nowfloor)*0.5)+1);
	}
}
