package lajidianti;

import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.Date;

public class Request extends Thread{
	static Scanner scanner = new Scanner(System.in);
	public static long startTime = 0;
	private Requestque reque;
	
	public Request(Requestque rq) {
		reque = rq;
	}
	
	public void run() {
		try {
			String str_in;
			DecimalFormat decimalFormat = new DecimalFormat("##0.0");
			do {
				str_in = scanner.nextLine();
				str_in = str_in.replaceAll(" +", "");
				if(startTime == 0) startTime = new Date().getTime();
				
				if(str_in.equals("END")) {
					System.exit(0);
				}else {
					int oneline = 0;
					String[] strs = str_in.split("[;]",-1);
					for(String str : strs) {
						
						Input input = new Input(str);
						input.handle();
						if(oneline < 10 && input.isSign()) {
							reque.get_imfor(input.getFloor(), input.getEle_id(), input.getType(), new Date().getTime());
							oneline++;
//							System.out.println(reque.getFloor(reque.getNum()-1));
						} else {
							oneline++;
							Main.Output().println(new Date().getTime()+":"+"INVALID"+"["+str+","+decimalFormat.format((new Date().getTime()-startTime)/1000.0)+"]");
						}
					}
				}
				
			}while(true);
		} catch (Exception e) {}	
	}
}
