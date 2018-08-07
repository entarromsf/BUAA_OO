package IFTTT;

import java.util.Scanner;

public class Requestadd {

	String str_in;
	String[] strs;
	String[] monitor = { "renamed", "Modified", "path-changed", "size-changed" };
	String[] mission = { "record-summary", "record-detail", "recover" };

	int monitor_t;
	int mission_t;
	String path_t;

	boolean first = true;

	RequestQue rq;

	SafeFile file;

	public Requestadd(RequestQue rq_t) {
		rq = rq_t;
	}

	void handle() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Waiting for input");
		while (true) {
			try {
				str_in = scanner.nextLine();
				if (str_in.equals("END")) {
					System.out.println("Input Finished");
					break;
				}

				strs = str_in.split(",");

				if (strs.length != 5 || !strs[0].equals("IF") || !strs[3].equals("THEN")) {
					System.out.println("Invalid Input");
					continue;
				} else {
					monitor_t = monitorToint(strs[2]);
					mission_t = missionToint(strs[4]);
					path_t = strs[1];

					if (monitor_t == -1 || mission_t == -1) {
						System.out.println("Invalid Input");
						continue;
					}

					if (first) {
						file = new SafeFile(path_t);
						if (file.exists()) {
							rq.addpath(path_t);
						} else {
							System.out.println("Invalid File Path");
							continue;
						}

						rq.addmission(mission_t);
						rq.addmonitor(monitor_t);

						first = false;
					} else {
						if (Same()) {
							System.out.println("Same Input");
							continue;
						} else {
							file = new SafeFile(path_t);
							if (file.exists()) {
								rq.addpath(path_t);
							} else {
								System.out.println("Invalid File Path");
								continue;
							}
							rq.addmission(mission_t);
							rq.addmonitor(monitor_t);
						}
					}

					if (rq.monitor.size() >= 10) {
						System.out.println("Too Many Instructions");
						break;
					}
				}

			} catch (Exception e) {
				System.out.println("Error");
			}
		}
		scanner.close();
	}

	int monitorToint(String temp) {
		for (int i = 0; i < 4; i++) {
			if (temp.equals(monitor[i])) {
				return i;
			}
		}
		return -1;
	}

	int missionToint(String temp) {
		for (int i = 0; i < 4; i++) {
			if (temp.equals(mission[i])) {
				return i;
			}
		}
		return -1;
	}

	boolean Same() {
		for (int i = 0; i < rq.monitor.size(); i++) {
			if (monitor_t == rq.monitor.get(i) && mission_t == rq.mission.get(i) && path_t.equals(rq.path.get(i)))
				return true;
		}
		return false;
	}

}
