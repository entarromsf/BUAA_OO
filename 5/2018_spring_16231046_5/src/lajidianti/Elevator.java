package lajidianti;

import java.text.DecimalFormat;
import java.util.Date;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class Elevator extends Thread{
	
	private int id;
	
	private Requestque req;
	
	private String state = "WFS";
	
	private int nfloor, nid, ntype;
	
	private int floor;
	
	private int moved;
	
	private long ntime;
	
	
	public Elevator(int idin,Requestque requestque) {
		id = idin;
		req = requestque;
		nfloor = nid = ntype = -1;
		moved = 0;
		state = "WFS";
		floor = 1;
	}
	
	public synchronized void setnreq(int flo,int id,int type,long time) {
		nfloor = flo;
		nid = id;
		ntype = type;
		ntime = time;
		if(nfloor == floor)
			state = "STILL";
		else if(nfloor > floor)
			state = "UP";
		else 
			state = "DOWN";
	}
	
	@Override
	public void run() {
		do {
			int temp;
			synchronized (this) {
				temp = nfloor;
			}
			
			if(temp==-1) {
				continue;
			}
			runnning();
			goonestep();
		}while(true);
	}
	
	public void runnning() {
		boolean dooropen = false;
		boolean whethers = false;
		if(nfloor == floor && nfloor != -1) {//到达主请求楼层
			if(state.equals("STILL")) {
				open();
				dooropen = true;
	//			System.out.println(1);
			}
	//		else {///

				Main.Output().println((new Date().getTime())+":"+toString(nfloor, nid, ntype));
				//灭按钮
				if(ntype==3) Button.setEleb(id, floor, false);
				else if(ntype==1) Button.setUpb(floor, false);
				else Button.setDownb(floor, false);
				
				//处理同层捎带
				for(int i = 0;i < req.getNum(); i++) {
					if(floor == req.getFloor(i) && ntype != req.getType(i)) {
						shaodai(i);
					}
				}
			if(!dooropen) open();	
	//		}///
		}//处理主请求完毕
		else{//未到主请求楼层,寻找同层是否开门
			for(int i = 0; i < req.getNum(); i++) {
				if(nfloor!=-1 && floor==req.getFloor(i)) {
					shaodai(i);
					whethers = true;
					for(int j = i+1; j < req.getNum(); j++) {
						if(floor == req.getFloor(j) && req.getType(j)!=req.getType(i)) {
							shaodai(j);
							whethers = true;
						}
					}
					break;
				}
			}
			if(whethers) open();
		}

		boolean wfs_flag = true;
		for(int opq = 0; opq < req.getNum();opq++) {
			if(req.getFloor(opq) != -1) {
				wfs_flag = false;
				break;
			}
		}
		if(wfs_flag && !state.equals("WFS") && nfloor == floor && nfloor != -1) {
			setWFS();
		}
	}
	
	public void shaodai(int m) {
		Main.Output().println(new Date().getTime()+":"+toString(req.getFloor(m), req.getEleid(m), req.getType(m)));
		
		if(req.getType(m)==3) Button.setEleb(id, floor, false);
		else if(req.getType(m)==1) Button.setUpb(floor, false);
		else Button.setDownb(floor, false);
		req.Use(m);
	}
	
	public synchronized void goonestep() {
		if(nfloor == -1) return;
		
		if(nfloor > floor) {
			up();
			return;
		}else if (nfloor < floor) {
			down();
			return;
		}else {
			state = "STILL";
			nfloor = nid = ntype = -1;
			return;
		}
	}
	
	public void up() {
		try {
			state = "UP";
			floor++;
			moved++;
			Thread.sleep(2999);
		} catch (Exception e) {}
	}
	
	public void down() {
		try {
			state = "DOWN";
			floor--;
			moved++;
			Thread.sleep(2999);
		} catch (Exception e) {}
	}
	
	public void open() {
		try {
			Thread.sleep(5999);
		} catch (Exception e) {}
	}
	
	public void setmainreq(int flot,int idt,int typt,long timet) {
		nfloor = flot;
		nid = idt;
		ntype = typt;
		ntime = timet;
		if(nfloor > floor) state = "UP";
		else if (nfloor == floor) state = "STILL";
		else state = "DOWN";
	}
		
	public String toString(int flo_t,int id_t,int typ_t) {
		String str;
		DecimalFormat decimalFormat = new DecimalFormat("##0.0");
		if(typ_t == 1) {//UP
			str = "[FR,"+flo_t+","+"UP"+","+decimalFormat.format((ntime - Request.startTime)/1000.0)+"]";
		}else if (typ_t == 2) {//DOWN
			str = "[FR,"+flo_t+","+"DOWN"+","+decimalFormat.format((ntime - Request.startTime)/1000.0)+"]";
		}else {//ER
			str = "[ER,#"+id_t+","+flo_t+","+decimalFormat.format((ntime-Request.startTime)/1000.0)+"]";
		}
		str += "/(#"+id+","+floor+","+state+","+moved+","+decimalFormat.format((new Date().getTime()-Request.startTime)/1000.0)+")";
		return str;
	}

	
	public int getid() {
		return id;
	}

	public String getstate() {
		return state;
	}

	public int getNfloor() {
		return nfloor;
	}

	public int getNid() {
		return nid;
	}

	public int getNtype() {
		return ntype;
	}

	public int getFloor() {
		return floor;
	}

	public int getMoved() {
		return moved;
	}

	public long getNtime() {
		return ntime;
	}
    public void setWFS() {
    	state = "WFS";
    	nfloor = -1;
    }
    
    
}
