package seventh_work;

import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Point;
public class InputHandler extends Thread{
	private RequestQueue Q;
	private int[][] map;
	TaxiGUI gui;
	public InputHandler(RequestQueue q,int [][] m,TaxiGUI g)
	{
		Q = q;
		map = m;
		gui = g;
	}
	public void run()
	{
		Scanner in = new Scanner(System.in);
		String pattern="^\\[CR(,\\((([0-9])|([1-7][0-9])),(([0-9])|([1-7][0-9]))\\)){2}\\]$";
		Pattern p = Pattern.compile(pattern);int num = 0;
		while(in.hasNext())
		{
			String temp = in.nextLine();
			//System.out.println(temp);
			temp = temp.replace(" ","");
			temp = temp.replace("\t","");
			System.out.println(temp);
			Matcher m = p.matcher(temp);
			if(m.matches())
			{
				String s[] = temp.split(",");
				s[1] = s[1].replace("(","");
				s[2] = s[2].replace(")","");
				s[3] = s[3].replace("(","");
				s[4] = s[4].replace(")]","");
				int start_x = Integer.parseInt(s[1]);
				int start_y = Integer.parseInt(s[2]);
				int end_x = Integer.parseInt(s[3]);
				int end_y = Integer.parseInt(s[4]);
				if(start_x > 80 || start_x < 0 || start_y > 80 || start_y < 0 ||
						end_x > 80 || end_x < 0 || end_y > 80 || end_y < 0){
					System.out.println("Invalid Request");
					continue;
				}
				if(start_x == end_x && start_y == end_y) {
					System.out.println("Invalid Request");
					continue;
				}
				num++;
				Date d = new Date();
				long T = d.getTime();
				T = T - (T % 100);
				Request r = new Request(start_x,start_y,end_x,end_y,T,Q,map,gui,num);
				//System.out.println(Q.get_tot());
				r.start();
			}
			else System.out.println("Invalid Request");
		}
	}
}
