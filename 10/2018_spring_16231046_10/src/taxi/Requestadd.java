package taxi;

import java.awt.Point;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.reflect.generics.tree.Tree;

public class Requestadd extends Thread{
	/**
     * @Overview:添加请求类，拥有添加输入至队列的功能。
     */
	static Scanner sc = new Scanner(System.in);
	private String str_in;
	private static long time;
	private RequestQue req;
	private boolean same;
	private Point src,dst;
	private Map ma;
	boolean initinput = true;
	private Car[] cars;
	
	
	public Requestadd(RequestQue req_t, Map map_t, Car[] cars_t) {
		/*@ REQUIRES: None;
		@ MODIFIES: req, ma, cars;
		@ EFFECTS: req = req_t;
				   ma = map_t;
				   cars = cars_t;
		*/
		req = req_t;
		ma = map_t;
		cars = cars_t;
	}
	
	public boolean repOK() {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ Effects: \result == invariant(this);
		 */
		if (req == null)
			return false;
		if (cars == null)
			return false;
		if (ma == null)
			return false;
		return true;
	}
	
	public void run() {
		/* @ REQUIRES: System.in contains a line of text
	    @ MODIFIES: System.out, str_in, time, req,same, sr, dst, ma, initinput, cars;
	    @ EFFECTS: 读入一行，并用Request判断其是否合法，并输出相应信息。合法的话把该请求加入到请求队列里。
	    */
		try {
			do {
				str_in = sc.nextLine();
				str_in = str_in.replaceAll(" +", "");
				
				Pattern p = Pattern.compile("^Load");
				Matcher m = p.matcher(str_in);
				boolean loadsign = true;
				
				if(!m.find()) loadsign = false;
				
				if(loadsign && initinput) {//文件输入
					testRead tRead = new testRead(str_in,req,ma,cars);
					tRead.handle();
					initinput = false;
				}else {//正常指令
					Request request = new Request(str_in);
					request.handle();
					
					src = request.getSrc();
					dst = request.getDst();
					
					time = new Date().getTime();
					time = time/100;
					time = time*100;
					same = false;
					if(request.getsign()) {
						for (int i = 0;i < req.getNum(); i++) {
							if(src.equals(req.getsrcp(i)) && dst.equals(req.getdstp(i)) && time==req.gettime(i)) {
								same = true;
								break;
							}
						}
						if(same) {
							System.out.println("Same Request");
							System.out.println("----------------------------------------------------");
						}else {
							req.add_ele(src, dst, time, false);
							Main.gui.RequestTaxi(src, dst);
							System.out.println("Get request");
							System.out.println("----------------------------------------------------");
						}
					}else if (request.getsignopen()) {
						ma.openRoad(ptn(src), ptn(dst));
						ma.chagenum++;
						Main.gui.SetRoadStatus(src, dst, 1);
					}else if (request.getsignclose()) {
						ma.closeRoad(ptn(src), ptn(dst));
						ma.chagenum++;
						Main.gui.SetRoadStatus(src, dst, 0);
					}else {
						System.out.println("Invalid Request");
						System.out.println("----------------------------------------------------");
					}
				}
			}while(true);
		} catch (Exception e) {}
	}
	
	public int ptn(Point p_t) {
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == (p_t.x) * 80 + p_t.y;
		*/
		return (p_t.x) * 80 + p_t.y;
	}

	public Point ntp(int n_t) {
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == new Point((int) (n_t / 80.0), n_t % 80);
		*/
		Point p_t = new Point((int) (n_t / 80.0), n_t % 80);
		return p_t;
	}
	
}
