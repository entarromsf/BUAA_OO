package simulatedElevator;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Areader implements Runnable {
	int count;

	private static Request get_a_req(String ss, long req_time) {
		int req_floor;
		int eleva_num;

		String as[] = ss.split("[(),#]+");

		if (as[1].equals("FR")) {
			req_floor = Integer.parseInt(as[2]);
			if (as[3].equals("UP")) {
				if (req_floor >= Main.FLOORSUM || req_floor < 1)
					return null;
				return new Request(req_floor,(double)( req_time / 1000.0), "UP");
			} else {
				if (req_floor <= 1 || req_floor > Main.FLOORSUM)
					return null;
				return new Request(req_floor, (double)(req_time / 1000.0), "DOWN");
			}
		} else {
			eleva_num = Integer.parseInt(as[2]);
			req_floor = Integer.parseInt(as[3]);
			if (req_floor < 1 || req_floor > Main.FLOORSUM)
				return null;
			return new Request(req_floor, (double)(req_time / 1000.0), eleva_num);
		}
	}

	private static boolean judgeTheInput(String a) {
		// 判断输入是否满足格式要求
		Pattern pattern = Pattern.compile("\\(((FR,0*\\d{1,2},((UP)|(DOWN)))|(ER,#0*[1-3],0*\\d{1,2}))\\)");
		Matcher matcher = pattern.matcher(a);
		return matcher.matches();
	}

	private static String removeWhitespace(String a) {
		String b = a.replaceAll("\\s+", "");
		return b;
	}

	private static String[] split_string(String a) {
		return a.split(";",11);
	}

	private static boolean can_it_join_into(Request aReq) {
		if (aReq.get_req_type() == 0) {
			// 电梯内请求
			if (Main.elevator[aReq.get_eleva_num() - 1].get_light(aReq.get_req_floor())) {
				return false;
			}
			Main.elevator[aReq.get_eleva_num() - 1].set_light(aReq.get_req_floor(), true);
		} else {
			// up || down
			if (Main.floor[aReq.get_req_floor() - 1].get_light(aReq.get_req_type() - 1)) {
				return false;
			}
			Main.floor[aReq.get_req_floor() - 1].set_light(aReq.get_req_type() - 1, true);
		}
		return true;
	}

	public void run() {
		count = 0;
		String line = null;
		int i;
		Request aReq;
		long req_time;
		try {
			Scanner reader = new Scanner(System.in);
			line = reader.nextLine();
			Main.start_time = System.currentTimeMillis();
			Main.ascheduler.restart = true;
			// Main.scheduler.interrupt();
			req_time = (System.currentTimeMillis() - Main.start_time);
			line = removeWhitespace(line);
			while (!line.equals("END")) {
				if (count < Main.MAXREQ) {
					String gitt[] = split_string(line);
					for (i = 0; i < gitt.length; i++) {
						if (i < Main.MAX_INST_NUM && judgeTheInput(gitt[i])) {
							aReq = get_a_req(gitt[i], req_time);
							if (aReq == null) {
								ExpHandler.err(0, gitt[i]);
							} else if (can_it_join_into(aReq)) {
								Requestqueue.add_a_req(aReq);
								//Main.ascheduler.restart = true;
								Main.effectiveReqCount++;
							} else {
								System.out.printf("#%d:SAME[%s,%.1f]\r\n", System.currentTimeMillis(), gitt[i],
										(double)((System.currentTimeMillis() - Main.start_time) / 1000.0));
							}
						} else {
							ExpHandler.err(0, gitt[i]);
						}
					}
					//Main.ascheduler.restart = true;
				} else {
					//ExpHandler.err(0, line);
				}
				line = reader.nextLine();
				req_time = System.currentTimeMillis() - Main.start_time;
				// Main.scheduler.interrupt();
				Main.ascheduler.restart = true;
				line = removeWhitespace(line);
				count++;
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("#UNKNOWN ERROR");
			System.exit(1);
		}

	}

}