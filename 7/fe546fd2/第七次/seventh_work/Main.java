package seventh_work;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



public class Main {

	public static void main(String[] args)
	{	
		mapInfo M = new mapInfo();
		TaxiGUI gui = new TaxiGUI();
		M.readmap("map.txt");
		gui.LoadMap(M.get_map(),80);
		int  map[][] = M.get_map();
		RequestQueue Q = new RequestQueue();
		RandomNumGenerator R = new RandomNumGenerator();
		R.Generate();
		int [] ini_pos = new int [100];
		ini_pos = R.get_num();
		Taxi [] t  = new Taxi[101];
		for(int i = 1;i <= 100;i++)
		{
			t[i] = new Taxi(ini_pos[i],Q,map,i,gui);
		}
		InputHandler in = new InputHandler(Q,map,gui);
		in.start();
		for(int i = 1;i <= 100;i++)t[i].start();
	}
}
