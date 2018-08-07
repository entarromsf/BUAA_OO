package taxi;

import java.awt.Point;
import java.util.Date;
import java.util.Scanner;

public class Requestadd extends Thread{
	static Scanner sc = new Scanner(System.in);
	private String str_in;
	private static long time;
	private RequestQue req;
	private boolean same;
	private Point src,dst;
	
	
	public Requestadd(RequestQue req_t) {
		req = req_t;
	}
	
	public void run() {
		try {
			do {
				str_in = sc.nextLine();
				str_in = str_in.replaceAll(" +", "");
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
				}else {
					System.out.println("Invalid Request");
					System.out.println("----------------------------------------------------");
				}
				
			}while(true);
		} catch (Exception e) {}
	}
	
	
	
}
