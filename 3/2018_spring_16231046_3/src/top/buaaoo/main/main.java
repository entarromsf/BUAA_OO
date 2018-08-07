package top.buaaoo.main;
import java.util.regex.*;
import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;

public class main {

	public static void main(String[] args) {
		Floor floor = new Floor();
		Elevator elevator = new Elevator();
		DemandQue demandQue = new DemandQue();
		Controler controler = new Controler_incidentally();
		demandQue.Input();
		controler.schedule(demandQue, floor, elevator);
	}

}
