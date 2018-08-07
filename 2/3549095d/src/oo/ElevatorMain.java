package oo;

import java.util.*;

public class ElevatorMain {
	public static void main(String[] args) {
		try {
			Scanner scan = new Scanner(System.in);
			Floor f =new Floor();
			Schedule schedule = new Schedule();
			RequestQueue rq = new RequestQueue();
			Elevator e = new Elevator();
			int i;
			for(i = 0;i<100;i++){
				String s = scan.nextLine();
				if (s.equals("RUN")) {
					schedule.Run_elevator(e, rq, f);
					break;
				}
				else {
					Request r = new Request(s);
					if (r.get_isError()) System.out.println("ERROR\n#Your request is illegal.");
					else {
						if(!rq.Request_add(r)) 
							System.out.println("ERROR\n#Your request time is illegal.");
					}
				}
			}
			if (i==100) schedule.Run_elevator(e, rq, f);
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
