package test;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Light extends Thread{
	/**@overview:
	 * Light is an array of street lamps. You can read the street lamp information from the document, 
	 * modify the street lamp information, establish the street lamp thread, and change the street lamp status.
	 */
	private int[] light = new int[6400]; //0表示没有红绿灯 1 表示东西方向绿灯 2表示南北方向绿灯
	private TaxiGUI gui;
	private City city;
	public Light(TaxiGUI gui) {
		this.gui=gui;
	}
	public boolean repOK() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:(gui!=NULL =>\result=true);
		 */
		if(gui==null) return false;
		return true;
	}
	
	public void init(String path) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:this;
		 * @EFFECTS:
		 * (path not exist => \result = null);
		 * (\all x,y;Point(x,y) is junction of three roads or Crossroads =>light[point(x,y)]=state);
		 */
		Scanner scan=null;
		File file=new File(path);
		if(file.exists()==false){
			System.out.println("红绿灯文件不存在,程序退出");
			System.exit(1);
			return;
		}
		try {
			scan = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
		}
		String  str= scan.nextLine();
		while(scan.hasNextLine()) {
			String[] st= str.split(" ");
			if(check_light(Integer.parseInt(st[0]),Integer.parseInt(st[1]))){
				this.light[Integer.parseInt(st[0])*80+Integer.parseInt(st[1])]=Integer.parseInt(st[2]);
				gui.SetLightStatus(new Point(Integer.parseInt(st[0]),Integer.parseInt(st[1])), Integer.parseInt(st[2]));
			}
				str = scan.nextLine();
		}
	scan.close();
		
	}
	
	public void set_city(City city) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:this;
		 * @EFFECTS:(this.city=city);
		 */
		this.city=city;
	}
	
	public boolean check_light(int x,int y) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:
		 * (\all x,y;0<=x<80,0<=y<80;graph(point(x,y) connect with 3 or 4 point =>\result=true)
		 */
		if(x<0 ||x>79 ||y<0||y>79) return false;
		int num=0;
		for(int i=0;i<4;i++) {
			if(city.getmap(x*80+y)[i]==true) {
				num++;
			}
		}
		if(num>2) return true;
		else return false;
	}
	
	public void set_light(int x,int y,int state) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:this;
		 * @EFFECTS:(light(point(x,y).state=state;
		 */
		if(check_light(x,y)) {
			this.light[x*80+y] = state;
		}
	}
	
	public int[] get_light() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:(\result=light);
		 */
		return this.light;
	}
	
	public boolean judge_light_wait(int dst,int src,String driection) {
		/**
		 * @REQUIRES:(0<=dst<6400,0<=src<6400,driection=up||down||left||right);
		 * @MODIFIES:None;
		 * @EFFECTS:(light=red && go.driection=left || go.driection=right || light=green =>\result=true);
		 * (ligt=red &&go.driection=right =>\result=false)
		 */
		int i=src-dst;
		switch(i) {
		case -1://左
			if(driection.equals("down") ) {
				return false;
			}
			if(driection.equals("right")) {
				return false;
			}
			if(light[dst]==0) {	
				return false;		
			}
			if(light[dst]==1) {
				return false;
			}
			if(light[dst]==2 && driection.equals("up")) {
				return false;
			}
			
			break;
		case 1://右
			if(driection.equals("up")) {
				return false;
			}
			if(driection.equals("left")) {
				return false;
			}
			if(light[dst]==0) {		
				return false;
			}
			if(light[dst]==1) {
				return false;
			}
			if(light[dst]==2 && driection.equals("down")) {
				return false;
			}
			break;
		case 80://下
			if(driection.equals("right")) {
				return false;
			}
			if(driection.equals("up")) {
				return false;
			}
			if(light[dst]==0) {	
				return false;
			}
			if(light[dst]==2) {
				return false;
			}
			if(light[dst]==1 && driection.equals("left")) {
				return false;
			}
			break;
		case -80://上
			if(driection.equals("left")) {
				return false;
			}
			if(driection.equals("down")) {
				return false;
			}
			if(light[dst]==0) {	
				return false;
			}
			if(light[dst]==2) {
				return false;
			}
			if(light[dst]==1 && driection.equals("right")) {
				return false;
			}
			break;
		}
		return true;
	}
	
	public void run() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:this;
		 * @EFFECTS:
		 * (\all i;0<=i<6400;light[i]=green=>light=red)
		 * (\all i;0<=i<6400;light[i]=red=>light=green)
		 */
		while(true) {
			Random rand = new Random();
			int time = rand.nextInt(500)+500;
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(int i=0;i<6400;i++) {
				if(light[i]==1) {
					light[i]=2;
					gui.SetLightStatus(new Point(i/80,i%80), 2);
				}
				else if(light[i]==2) {
					light[i]=1;
					gui.SetLightStatus(new Point(i/80,i%80), 1);
				}
			}
		}
	}
}
