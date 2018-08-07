package lajidianti;

import java.io.*;

public class Main {

		private static PrintStream p;
		public static void main(String[] args) {
			try{
			 	FileOutputStream fs = new FileOutputStream(new File("result.txt"));
			 	p = new PrintStream(fs);
				
			 	Requestque requestque = new Requestque();
				Requestque rq[] = new Requestque[4];
			 	
				Request request = new Request(requestque);
			 	Elevator elevator[] = new Elevator[4];
			 	
			 	rq[1] = new Requestque();
			 	rq[2] = new Requestque();
			 	rq[3] = new Requestque();
			 	
			 	elevator[1] = new Elevator(1, rq[1]);
			 	elevator[2] = new Elevator(2, rq[2]);
			 	elevator[3] = new Elevator(3, rq[3]);
			 	
			 	Schedul schedul = new Schedul(requestque, rq, elevator);
			 	
			 	elevator[1].start();
			 	elevator[2].start();
			 	elevator[3].start();
			 	request.start();
			 	schedul.start();
			 	
			 	
			 	
			}catch (Exception e){
				System.exit(0);
			}
		}
		public static PrintStream Output(){
			return p;
		}
	}

