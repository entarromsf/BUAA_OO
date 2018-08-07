package oo;

import java.util.*;

public class ElevatorMain {
	public static void main(String[] args) {
		try {
			Scanner scan = new Scanner(System.in);
			Floor f =new Floor();
			
			RequestQueue rq = new RequestQueue();
			Elevator e = new Elevator();
			Scheduler_new schedule = new Scheduler_new(e,f);
			int i;
			for(i = 0;i<100;i++){
				String s = scan.nextLine();
				if (s.equals("RUN")) {
					schedule.Run_elevator(rq);
					break;
				}
				else {
					Request r = new Request(s);
					if (r.get_isError()) System.out.println("INVALID["+s.replaceAll(" ", "")+"]");
					else {
						if(!rq.Request_add(r)) 
							System.out.println("INVALID["+s.replaceAll(" ", "")+"]");
					}
				}
			}
			if (i==100) {
				System.out.println("#The elevator has got 100 requests.");
				schedule.Run_elevator(rq);
			}
			scan.close();
		}catch (Exception e) {
			System.out.println("ERROR");
			System.out.println("#Exception");
		}catch(Error e) {
			System.out.println("ERROR");
			System.out.println("#Error");
		}
	}

}
