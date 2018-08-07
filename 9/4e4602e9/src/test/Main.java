package test;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
class mapInfo{
	int[][] map=new int[80][80];
	public void readmap(String path){//读入地图信息
		//Requires:String类型的地图路径,System.in
		//Modifies:System.out,map[][]
		//Effects:从文件中读入地图信息，储存在map[][]中
		Scanner scan=null;
		File file=new File(path);
		if(file.exists()==false){
			System.out.println("地图文件不存在,程序退出");
			System.exit(1);
			return;
		}
		try {
			scan = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			
		}
		for(int i=0;i<80;i++){
			String[] strArray = null;
			try{
				strArray=scan.nextLine().split("");
			}catch(Exception e){
				System.out.println("地图文件信息有误，程序退出");
				System.exit(1);
			}
			for(int j=0;j<80;j++){
				try{
					this.map[i][j]=Integer.parseInt(strArray[j]);
				}catch(Exception e){
					System.out.println("地图文件信息有误，程序退出");
					System.exit(1);
				}
			}
		}
		scan.close();
	}
}

public class Main {
	public static void main(String[] args) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 * @THREAD_EFFECTS:safe_data;
		 */
		try {
				
			PrintStream ps;
			try {
				ps = new PrintStream("result.txt");
				System.setOut(ps);
			} catch (FileNotFoundException e) {
			} 
			
			TaxiGUI gui=new TaxiGUI();
			mapInfo mi=new mapInfo();
			mi.readmap("map.txt");//在这里设置地图文件路径
			gui.LoadMap(mi.map, 80);
			
			
			City city =new City(gui);
			city.init(mi.map);
			
			ArrayList<int[]> request = new ArrayList<int[]>();
			ArrayList<taxi> taxi_list= new ArrayList<taxi>();
			safe_data  safe_data = new safe_data(request,taxi_list,city);
			synchronized(safe_data) {
				Input input = new Input(safe_data.get_request(),safe_data.get_taxilist(),gui,safe_data.getcity());
				input.start();
			}	
		}
		catch(Exception e) {
		}
	/*	

		*/
		
//		City city =new City(mi.map);
//		city.init();

		/*
		ArrayList<Integer> path = new ArrayList<Integer>();
		path = bfs(814,813,city);
		for(int i=0;i<path.size();i++) {
			gui.SetTaxiStatus(1, new Point(path.get(i)/80,path.get(i)%80), 1);
			gv.stay(100);
		}
		*/
	/*
		
		ArrayList<int[]> request = new ArrayList<int[]>();
		ArrayList<taxi> taxi_list= new ArrayList<taxi>();
		safe_data  safe_data = new safe_data(request,taxi_list,city);
		
		synchronized (safe_data){
		Taxi_thread taxi_thread = new Taxi_thread();
		taxi_thread.creat_taxilist(gui,safe_data.get_taxilist(),safe_data.getcity());
		
		Input input = new Input(safe_data.get_request(),safe_data.get_taxilist(),gui);
		input.start();
			
		}
		*/
	}
    	
    }
	   