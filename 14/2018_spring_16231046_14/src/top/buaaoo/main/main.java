package top.buaaoo.main;
import java.util.regex.*;
import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;

public class main {

	public static void main(String[] args) {
		try {
			Floor floor = new Floor();
			Elevator elevator = new Elevator();
			Input input = new Input();
			Controler_incidentally controler = new Controler_incidentally();
			input.Inputhandle();
			controler.schedule(input, floor, elevator);
		} catch (Exception e) {
			System.exit(0);
		}
	}

}
