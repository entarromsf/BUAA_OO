package top.buaaoo.main;
import java.util.regex.*;
import java.util.Scanner;
import java.lang.Math;
import java.sql.Time;
import java.util.ArrayList;

public class Elevator {
	double opendoor(double time) {
		return time+1;
	}
	double running(double time,int nowfloor,int targetfloor) {
		return (double) (time+Math.abs((targetfloor-nowfloor)*0.5)+1);
	}
}
