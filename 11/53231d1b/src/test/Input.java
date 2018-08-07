package test;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector.Matcher;
public class Input extends Thread{
	/**@overview:
	 * Judge whether the window and file input conform to the format, change taxi_list, light, city, change.
	 */
	private ArrayList<int[]> request = new ArrayList<int[]>();
	private ArrayList<taxi> taxi_list=new ArrayList<taxi>();
	private TaxiGUI gui;
	private Light light;
	private boolean[] change= new boolean[100];
	private City city;
	private City city2;
	public Input(ArrayList<int[]> request,ArrayList<taxi> taxi_list,TaxiGUI gui,City city,City city2, Light light) {
		this.request=request;
		this.taxi_list=taxi_list;
		this.gui=gui;
		this.city=city;
		this.city2=city2;
		this.light=light;
		for(int i=0;i<100;i++) change[i]=false;
	}
	public boolean repOK() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:(request.size!=0 && taxi_list.size!=0 && gui!=NULL && city!=NULL && change.size!=0 => \result=true);
		 */
		if(request==null ||taxi_list==null || gui==null||city==null||light==null||change==null) {
			return false;
		}
		return true;
	}
	public void run() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:this;
		 * @EFFECTS:None;
		 */
		while(true) {
			String str =new String();
			Scanner sc = new Scanner(System.in);
			str=sc.nextLine();
			while(!str.equals("END")) {
				check(str);
				str=sc.nextLine();
			}
			System.exit(0);
		}
	}
	public void check(String str) {		
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:taxi_list,change,city,light;
		 * @EFFECTS:None;
		 */
		int[] req =new int[4];
		String reg = "\\((\\d){1,2},(\\d){1,2}\\),\\((\\d){1,2},(\\d){1,2}\\)";
		str = str.replaceAll(" ", "");
		
		if(!str.matches(reg)) {
			reg = "LoadFile";
			if(!str.contains(reg)) {
				reg = "ROAD\\((\\d){1,2},(\\d){1,2}\\),\\((\\d){1,2},(\\d){1,2}\\),[1|0]";
				if(!str.matches(reg)) {
					reg = "gettime";
					if(str.matches(reg)) {
						System.out.println(System.currentTimeMillis());
						return;
					}
					reg = "get_car(\\d){1,2}";
					if(str.matches(reg)) {
						str = str.replaceAll("get_car","");
						int no=Integer.parseInt(str);
						for(int i=0;i<taxi_list.size();i++) {
							if(taxi_list.get(i).get_id()==no) {
								System.out.println("No: "+no+" x: "+taxi_list.get(i).get_x()+" y: "+taxi_list.get(i).get_y()+" state: "+taxi_list.get(i).get_state());	
								return;
							}
						}
					}
					reg = "state_car";
					if(str.contains(reg)) {
						str = str.replaceAll("state_car", "");
						for(int i=0;i<taxi_list.size();i++) {
							if(taxi_list.get(i).get_state().equals(str)) {
								System.out.println("No: "+taxi_list.get(i).get_id()+" state: "+str);
							}
						}
						return;
					}
					
					reg ="previous(\\d){1,2}";
					if(str.matches(reg)) {
						str=str.replaceAll("previous", "");
						int id=Integer.parseInt(str);
						if(id<0 || id>=30) {
							System.out.println("error");
							return;
						}
						for(int i=0;i<taxi_list.size();i++) {
							if(taxi_list.get(i).get_id()==id) {
								((SpecialTaxi)taxi_list.get(i)).getmessage(0);
							}
						}
						return ;
					}
					
					reg="next(\\d){1,2}";
					if(str.matches(reg)) {
						str=str.replaceAll("next", "");
						int id=Integer.parseInt(str);
						if(id<0 || id>=30) {
							System.out.println("error");
							return;
						}
						for(int i=0;i<taxi_list.size();i++) {
							if(taxi_list.get(i).get_id()==id) {
								((SpecialTaxi)taxi_list.get(i)).getmessage(1);
							}
						}
						return;
					}
					System.out.println("error");
					return ;
				}
				else {
					str =str.replaceAll("ROAD", "");
					str =str.replaceAll("\\(", "");
					str =str.replaceAll("\\)", "");
					String[] st = str.split(",");
					city.Road(Integer.parseInt(st[0]), Integer.parseInt(st[1]), Integer.parseInt(st[2]), Integer.parseInt(st[3]), Integer.parseInt(st[4]));
					for(int i=0;i<100;i++) change[i]=true;
					return ;
				}
			}
			else {
				String[] st = str.split("File");
				System.out.println(st[st.length-1]);
				File f = new File(st[st.length-1]);
				if(f.exists()==false) {
					System.out.println("loadFile  not exist");
					return ;
				}
				else {
					try {
						Scanner scan = new Scanner(f);
						String string = scan.nextLine();
						string = string.replaceAll(" ", "");

						while(scan.hasNextLine()) {
							if(string.equals("#map")) {
								string = scan.nextLine();
								string = string.replaceAll(" ", "");
								System.out.println("string");
								while(!string.equals("#end_map")) {
									
									if(string.equals("")) {
										string = scan.nextLine();
										string = string.replaceAll(" ", "");
										continue;
									}
									File file = new File(string);
									if(file.exists()==false) {
										System.out.println("map file not exist");
									}
									else {
										//读入地图文件.
										mapInfo mi=new mapInfo();
										mi.readmap(string);
										gui.LoadMap(mi.map, 80);
										city.init(mi.map);
									}

									string = scan.nextLine();
									string = string.replaceAll(" ", "");
								}
							}
							else if(string.equals("#light")) {							
								string = scan.nextLine();
								string = string.replaceAll(" ", "");
								while(!string.equals("#end_light")) {
									if(string.equals("")) {
										string =scan.nextLine();
										string = string.replaceAll(" ", "");
										continue;
									}
									reg = "\\((\\d){1,2},(\\d){1,2}\\),(0|1|2)";
									if(string.matches(reg)) {
										string=string.replaceAll("\\(", "");
										string=string.replaceAll("\\)", "");
										String[] stt = string.split(",");
										
										int wrong=0;
										if(Integer.parseInt(stt[0])<0 ||Integer.parseInt(stt[0])>79 || Integer.parseInt(stt[1])<0 ||Integer.parseInt(stt[1])>79) wrong=1;
										if(wrong==0) {
											light.set_light(Integer.parseInt(stt[0]), Integer.parseInt(stt[1]), Integer.parseInt(stt[2]));
											gui.SetLightStatus(new Point(Integer.parseInt(stt[0]),Integer.parseInt(stt[1])), Integer.parseInt(stt[2]));
										}
										else {
											System.out.println("wrong "+string);
										}
									}
									else {
										System.out.println("wrong "+string);
									}
									string = scan.nextLine();
									string = string.replaceAll(" ", "");
								}
								light.start();
							}
							else if(string.equals("#flow")) {
								string = scan.nextLine();
								string = string.replaceAll(" ", "");
								while(!string.equals("#end_flow")) {
									if(string.equals("")) {
										string = scan.nextLine();
										string = string.replaceAll(" ", "");
										continue;
									}
									//判断点和 值得格式是否正确
									reg = "\\((\\d){1,2},(\\d){1,2}\\),\\((\\d){1,2},(\\d){1,2}\\),(\\d){1,2}";
									if(string.matches(reg)) {
										string = string.replaceAll("\\(", "");
										string = string.replaceAll("\\)", "");
										String[] stt = string.split(",");
										int wrong=0;
										for(int i=0;i<4;i++) {
											if(Integer.parseInt(stt[i])>79 ||Integer.parseInt(stt[i])<0 ) {
												System.out.println(string+"position wrong");
												wrong=1;
												break;
											}
										}
										//设置流量
										if(wrong==0) {
											city.set_flow(Integer.parseInt(stt[0]), Integer.parseInt(stt[1]), Integer.parseInt(stt[2]), Integer.parseInt(stt[3]), Integer.parseInt(stt[4]));						
										}
									}
									else System.out.println("wrong"+string);
									string = scan.nextLine();
									string = string.replaceAll(" ", "");
								}
							}
							else if(string.equals("#taxi")) {
								ArrayList<Integer> had_build = new ArrayList<Integer>();
								string = scan.nextLine();
								string = string.replaceAll(" ", "");
								while(!string.equals("#end_taxi")) {
									if(string.equals("")) {
										string = scan.nextLine();
										string = string.replaceAll(" ", "");
										continue;
									}
									reg = "(\\d){1,2},(\\d){1,2},(\\d){1,2},\\((\\d){1,2},(\\d){1,2}\\)";
									if(string.matches(reg)) {
										string = string.replaceAll("\\(", "");
										string = string.replaceAll("\\)", "");
										String[] stt = string.split(",");		
										int wrong=0;
										if(Integer.parseInt(stt[0])<0 || Integer.parseInt(stt[0])>99) wrong=1;
										else if(Integer.parseInt(stt[3])<0 || Integer.parseInt(stt[3])>79 || Integer.parseInt(stt[4])<0 || Integer.parseInt(stt[4])>79) wrong=1;
										else if(Integer.parseInt(stt[1])<0 || Integer.parseInt(stt[1])>3) wrong=1;
										else if(had_build.contains(Integer.parseInt(stt[0]))) wrong=1;
										if(wrong==0) {
											//车辆信息建立 出租车线程建立 
											had_build.add(Integer.parseInt(stt[0]));
											taxi taxi;
											if(Integer.parseInt(stt[0])<30) {
												taxi = new SpecialTaxi(Integer.parseInt(stt[0]),Integer.parseInt(stt[3]),Integer.parseInt(stt[4]),gui,city2,change,light);
												gui.SetTaxiType(Integer.parseInt(stt[0]), 1);
											}
											else {
												taxi = new taxi(Integer.parseInt(stt[0]),Integer.parseInt(stt[3]),Integer.parseInt(stt[4]),gui,city,change,light);
												gui.SetTaxiType(Integer.parseInt(stt[0]), 0);
											}
											switch(Integer.parseInt(stt[1])) {
											case 0:
												taxi.set_state("serve");
												break;
											case 1:
												taxi.set_state("order");
												break;
											case 2:
												taxi.set_state("wait");
												break;
											case 3:
												taxi.set_state("stop");
												break;
											}
											taxi.set_credit(Integer.parseInt(stt[2]));
											taxi_list.add(taxi);
											taxi.start();
										}
										else {
											System.out.println("wrong"+string);	
										}
									}
									else System.out.println("wrong"+string);			
									string = scan.nextLine();
									string = string.replaceAll(" ", "");
								}			
									Taxi_thread taxi_thread = new Taxi_thread();
									taxi_thread.creat_taxilist(gui,taxi_list,city,city2,had_build,change,light);
							}
							else if(string.equals("#request")) {
								string = scan.nextLine();
								string = string.replaceAll(" ", "");
								while(!string.equals("#end_request")) {
									if(string.equals("")) {
										string = scan.nextLine();
										string = string.replaceAll(" ", "");
										continue;
									}
									reg = "\\((\\d){1,2},(\\d){1,2}\\),\\((\\d){1,2},(\\d){1,2}\\)";
									if(string.matches(reg)) {
										string = string.replaceAll("\\(", "");
										string = string.replaceAll("\\)", "");
										String[] stt = string.split(",");
										//读入请求
										int wrong=0;
										int[] reqq = new int[4];
										for(int i=0;i<stt.length;i++) {
											reqq[i]=Integer.parseInt(stt[i]);
										}
										
										if(reqq[0]==reqq[2] && reqq[1]==reqq[3]) wrong=1;
										for(int i=0;i<stt.length;i++) {
											if(reqq[i]<0 || reqq[i]>79 ) {
												wrong=1;
												break;
											}
										}
										if(wrong==0) {
											request.add(reqq);
											control con =new control(taxi_list,reqq,gui);
											con.start();
											sleep(5);  //用于防止各个要求之间得竞争 所以在进入线程时 将 各个要求 进行排序 进入防止 进程得阻碍
										}
										else {
											 System.out.println("wrong"+string);
										}
									}
									else System.out.println("wrong"+string);
									string = scan.nextLine();
									string = string.replaceAll(" ", "");
								}
								break;
							}
							string = scan.nextLine();
							string = string.replaceAll(" ", "");
						}
						
						scan.close();
					} catch (Exception e) {
						System.out.println("LoadFile load wrong");
					}
				}
				return;
			}
		}
		else {		
			str = str.replaceAll("\\(", "");
			str = str.replaceAll("\\)", "");
			String[] st = str.split(",");

			for(int i=0;i<st.length;i++) {
				req[i]=Integer.parseInt(st[i]);
			}
			if(req[0]==req[2] && req[1]==req[3]) {
				System.out.println("the end == start ");
				return ;
			}
			for(int i=0;i<st.length;i++) {
				if(req[i]<0 || req[i]>79 ) {
					System.out.println("error");
					return ;
				}
			}
		}
		/*
		for(int i=0;i<request.size();i++) {
			if(Arrays.equals(request.get(i),req)) {
					System.out.println("this order has exist");
					return ;
			}	
		}
		*/
		request.add(req);
		control con =new control(taxi_list,req,gui);
		con.start();
		return ;
	}
	
	public void sleep(int time) {
		/**
		 * @REQUIRES:(0<=time);
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
}
	