package taxi;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class testRead {
	/**
     * @Overview:testRead类能处理读入的初始化文件并加入请求队列。
     */
	private String path;
	private Map ma;
	private RequestQue req;
	private Car[] cars;
	private static long time;
	private Point src, dst;
	private boolean same;

	public testRead(String str_t, RequestQue req_t, Map map_t, Car[] cars_t) {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: path, ma, cars, req;
		 * 
		 * @ EFFECTS: path = str_t.replaceAll("Load", ""); req = req_t; ma = map_t; cars
		 * = cars_t;
		 */
		path = str_t.replaceAll("Load", "");
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

	public void handle() {
		/*
		 * @ REQUIRES: System.in contains a line of text
		 * 
		 * @ MODIFIES: System.out, strin, time, req,same, sr, dst, ma, initinput, cars;
		 * 
		 * @ EFFECTS: 读入文件，直到末尾，并根据文件内指令进行更改和分配。
		 */
		Scanner scan = null;
		File file = new File(path);
		if (file.exists() == false) {
			System.out.println("测试文件不存在");
			return;
		}
		try {
			scan = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
		}

		String strin = null;

		do {
			strin = scan.nextLine();
			strin = strin.replaceAll(" +", "");
			if (strin.equals("#map")) {
				do {
					strin = scan.nextLine();
					strin = strin.replaceAll(" +", "");
					if (!strin.equals("#end_map") && strin != null) {
						ma.readmap(strin);
						Main.gui.LoadMap(ma.mapinfo, 80);
						ma.initmatrix();
					}
				} while (!strin.equals("#end_map"));
			} else if (strin.equals("#light")) {
				do {
					strin = scan.nextLine();
					strin = strin.replaceAll(" +", "");
					if (!strin.equals("#end_light") && strin != null) {
						Main.light.readlight(strin);
					}
				} while (!strin.equals("#end_light"));
			} else if (strin.equals("#flow")) {
				do {
					strin = scan.nextLine();
					strin = strin.replaceAll(" +", "");
					if (!strin.equals("#end_flow") && strin != null) {
						// (x,y)(x,y)value
						String[] strings = strin.split("[,()]");
						int srcx, srcy, dstx, dsty, flow;
						srcx = Integer.parseInt(strings[1]);
						srcy = Integer.parseInt(strings[2]);
						dstx = Integer.parseInt(strings[4]);
						dsty = Integer.parseInt(strings[5]);
						flow = Integer.parseInt(strings[6]);

						ma.setFlow(srcx, srcy, dstx, dsty, flow);
					}
				} while (!strin.equals("#end_flow"));
			} else if (strin.equals("#taxi")) {
				do {// 78,0,876,(3,2) or 78,0,876
					strin = scan.nextLine();
					strin = strin.replaceAll(" +", "");
					if (!strin.equals("#end_taxi") && strin != null) {
						int no, state, credit, posx, posy;
						String[] strings = strin.split("[,()]");
						if (strings.length == 6) {
							no = Integer.parseInt(strings[0]);
							state = Integer.parseInt(strings[1]);
							credit = Integer.parseInt(strings[2]);
							posx = Integer.parseInt(strings[4]);
							posy = Integer.parseInt(strings[5]);
							cars[no].setstate(state);
							cars[no].setcredit(credit);
							cars[no].setpos(posx, posy);
						} else {
							no = Integer.parseInt(strings[0]);
							state = Integer.parseInt(strings[1]);
							credit = Integer.parseInt(strings[2]);
							cars[no].setstate(state);
							cars[no].setcredit(credit);
						}
					}
				} while (!strin.equals("#end_taxi"));
			} else if (strin.equals("#request")) {
				do {
					strin = scan.nextLine();
					strin = strin.replaceAll(" +", "");
					if (!strin.equals("#end_request") && strin != null) {
						Request request = new Request(strin);
						request.handle();

						src = request.getSrc();
						dst = request.getDst();

						time = new Date().getTime();
						time = time / 100;
						time = time * 100;
						same = false;
						if (request.getsign()) {
							for (int i = 0; i < req.getNum(); i++) {
								if (src.equals(req.getsrcp(i)) && dst.equals(req.getdstp(i))
										&& time == req.gettime(i)) {
									same = true;
									break;
								}
							}
							if (same) {
								System.out.println("Same Request");
								System.out.println("----------------------------------------------------");
							} else {
								req.add_ele(src, dst, time, false);
								Main.gui.RequestTaxi(src, dst);
								System.out.println("Get request");
								System.out.println("----------------------------------------------------");
							}
						} else if (request.getsignopen()) {
							ma.openRoad(ptn(src), ptn(dst));
							ma.chagenum++;
							Main.gui.SetRoadStatus(src, dst, 1);
						} else if (request.getsignclose()) {
							ma.closeRoad(ptn(src), ptn(dst));
							ma.chagenum++;
							Main.gui.SetRoadStatus(src, dst, 0);
						} else {
							System.out.println("Invalid Request");
							System.out.println("----------------------------------------------------");
						}
					}
				} while (!strin.equals("#end_request"));
			}
		} while (!strin.equals("#end_request"));
	}

	public int ptn(Point p_t) {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: \result == (p_t.x) * 80 + p_t.y;
		 */
		return (p_t.x) * 80 + p_t.y;
	}

	public Point ntp(int n_t) {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: \result == new Point((int) (n_t / 80.0), n_t % 80);
		 */
		Point p_t = new Point((int) (n_t / 80.0), n_t % 80);
		return p_t;
	}
}
