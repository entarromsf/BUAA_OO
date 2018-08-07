package lajidianti;

import java.text.DecimalFormat;
import java.util.Date;



public class Schedul extends Thread{
	private Elevator[] ele_three;
	private Requestque mainque;
	private Requestque [] rq;
	
	public Schedul(Requestque mainq_t,Requestque[] elerq,Elevator[] ele) {
		mainque = mainq_t;
		ele_three = ele;
		rq = elerq;
	}
	
	@Override
	public synchronized void run() {
	try {
		
		
		do {
			
			for(int i = 1; i <= 3; i++) {//分配主请求
				for(int j = 0; j < rq[i].getNum(); j++) {
					if(ele_three[i].getNfloor()==-1 && rq[i].getFloor(j)!=-1) {
						ele_three[i].setmainreq(rq[i].getFloor(j), rq[i].getEleid(j), rq[i].getType(j), rq[i].getTime(j));
						rq[i].Use(j);
						break;
					}
				}
			}
			
			int flo,typ,id;
			long tim;
			//扫描大队列
			for(int i = 0; i < mainque.getNum();i++) {
				flo = mainque.getFloor(i);
				typ = mainque.getType(i);
				id = mainque.getEleid(i);
				tim = mainque.getTime(i);
				if(flo == -1) continue;
				
				
//////////////////////////////////////////////////////////////////////////////////////////////////////				
				if(typ == 1) {//分配UP
					if(Button.getUpb(flo)) {//按钮已经亮了
						Main.Output().println("#"+new Date().getTime()+":"+"SAME"+tzsc(flo, typ, id, tim));
						mainque.Use(i);
						break;
					}
					
					int carryid = 0;
					carryid = Carryidup(flo);
					if(carryid!=0) {
						if(ele_three[carryid].getNfloor() != -1) {
							if(typ==ele_three[carryid].getNtype() && ele_three[carryid].getNfloor()==flo) {
								Main.Output().println("#"+new Date().getTime()+":SAME"+tzsc(flo, typ, id, tim));
								mainque.Use(i);
								break;
							}
							for(int k = 0; k < rq[carryid].getNum();k++) {
								if(flo == rq[carryid].getFloor(k)) {
									Main.Output().println("#"+new Date().getTime()+":SAME"+tzsc(flo, typ, id, tim));
									mainque.Use(i);
									break;
								}
							}
						}
						rq[carryid].get_imfor(flo, id, typ, tim);
						mainque.Use(i);
						Button.setUpb(flo, true);
						continue;
					}
				}else if(typ== 2) {//分配DOWN
					if(Button.getDownb(flo)) {//按钮已经亮了
						Main.Output().println("#"+new Date().getTime()+":"+"SAME"+tzsc(flo, typ, id, tim));
						mainque.Use(i);
						break;
					}
					int carryid = 0;
					carryid = Carryiddown(flo);
					if(carryid!=0) {
						if(ele_three[carryid].getNfloor() != -1) {
							if(typ==ele_three[carryid].getNtype() && ele_three[carryid].getNfloor()==flo) {
								Main.Output().println("#"+new Date().getTime()+":SAME"+tzsc(flo, typ, id, tim));
								mainque.Use(i);
								break;
							}
							for(int k = 0; k < rq[carryid].getNum();k++) {
								if(flo == rq[carryid].getFloor(k)) {
									Main.Output().println("#"+new Date().getTime()+":SAME"+tzsc(flo, typ, id, tim));
									mainque.Use(i);
									break;
								}
							}				
						}
						rq[carryid].get_imfor(flo, id, typ, tim);
						mainque.Use(i);
						Button.setDownb(flo, true);
						continue;
					}	
				}else {//分配ER
					if(Button.getEleb(id, flo)) {//按钮已经亮了
						Main.Output().println("#"+new Date().getTime()+":"+"SAME"+tzsc(flo, typ, id, tim));
						mainque.Use(i);
						break;
					}
					rq[id].get_imfor(flo, id, typ, tim);
					mainque.Use(i);
					Button.setEleb(id, flo, true);
					////加入电梯
					addtoele(id);				
					////
					break;
				}
				
				int chooseid = 0;
				boolean elechose;
				for(int j = 1;j < 4;j++) {
					elechose = (ele_three[j].getstate().equals("WFS")&&ele_three[j].getNfloor()==-1);
					if(elechose) {
						if(chooseid==0) {
							chooseid = j;
						}else {
							if(ele_three[j].getMoved()<ele_three[chooseid].getMoved()) {
								chooseid = j;
							}
						}
					}
				}
				if(chooseid!=0) {
					if(ele_three[chooseid].getNfloor()!=-1) {
						for(int k = 0;k < rq[chooseid].getNum();k++) {
							if(ele_three[chooseid].getNtype()==typ && flo==rq[chooseid].getFloor(k)) {
								Main.Output().println("#"+new Date().getTime()+":"+"SAME"+tzsc(flo, typ, id, tim));
								mainque.Use(i);
								break;
							}
						}
					}
					rq[chooseid].get_imfor(flo, id, typ, tim);
					addtoele(chooseid);
					mainque.Use(i);
					if(typ==1) Button.setUpb(flo, true);
					if(typ==2) Button.setDownb(flo, true);
					continue;
				}
				
				break;
/////////////////////////////////////////////////////////////////////////////////////////////////////////									
			}
		}while(true);	
	} catch (Exception e) {}
	}
	
	
	public synchronized void addtoele(int id) {
		if(ele_three[id].getNfloor() == -1) {
			for(int i = 0; i < rq[id].getNum(); i++) {
				if(rq[id].getFloor(i) != -1) {
					ele_three[id].setmainreq(rq[id].getFloor(i), rq[id].getEleid(i), rq[id].getType(i), rq[id].getTime(i));

					rq[id].Use(i);
					return;
				}
			}
		}
	}
	
	public int Carryidup(int flo) {
		//寻找可捎带电梯
//		boolean carryflag = false;
		int elecsnum = 0;
		int[] carryid = new int[5];
		int crrynum = 0;
		for(int j = 1; j < 4; j++) {
			if(ele_three[j].getNfloor()!=-1) {
				if(ele_three[j].getstate().equals("UP")) {
					if(flo>ele_three[j].getFloor() && flo<=ele_three[j].getNfloor()) {
//						carryflag = true;
						carryid[crrynum] = j;
						crrynum++;
					}
				}
/*						if(ele_three[j].getstate().equals("DOWN")) {
					if(flo<ele_three[j].getFloor() && flo>=ele_three[j].getNfloor()) {
						carryflag = true;
						carryid[crrynum] = j;
						crrynum++;
					}
				}*/
			}
		}
		int mintime;
		if(carryid[0]!=0) {
			mintime = ele_three[carryid[0]].getMoved();
			for(int k = 0;k < crrynum; k++) {
				if(ele_three[carryid[k]].getMoved()<mintime) {
					mintime = ele_three[carryid[k]].getMoved();
					elecsnum = k;
				}
			}
		}
		return carryid[elecsnum];
		//寻找可捎带电梯
	}

	public int Carryiddown(int flo) {
		//寻找可捎带电梯
//		boolean carryflag = false;
		int elecsnum = 0;
		int[] carryid = new int[5];
		int crrynum = 0;
		for(int j = 1; j < 4; j++) {
			if(ele_three[j].getNfloor()!=-1) {
/*				if(ele_three[j].getstate().equals("UP")) {
					if(flo>ele_three[j].getFloor() && flo<=ele_three[j].getNfloor()) {
//						carryflag = true;
						carryid[crrynum] = j;
						crrynum++;
					}
				}*/
				if(ele_three[j].getstate().equals("DOWN")) {
					if(flo<ele_three[j].getFloor() && flo>=ele_three[j].getNfloor()) {
//						carryflag = true;
						carryid[crrynum] = j;
						crrynum++;
					}
				}
			}
		}
		int mintime;
		if(carryid[0]!=0) {
			mintime = ele_three[carryid[0]].getMoved();
			for(int k = 0;k < crrynum; k++) {
				if(ele_three[carryid[k]].getMoved()<mintime) {
					mintime = ele_three[carryid[k]].getMoved();
					elecsnum = k;
				}
			}
		}
		return carryid[elecsnum];
		//寻找可捎带电梯
	}

		
		
	public String tzsc(int flo,int typ,int id,long tim) {
		String str;
		DecimalFormat decimalFormat = new DecimalFormat("##0.0");
		if(typ == 3) {
			str = "[ER,#"+id+","+flo+","+decimalFormat.format((tim - Request.startTime)/1000.0)+"]";
		}else if (typ == 2) {
			str = "[FR,"+flo+","+"DOWN"+","+decimalFormat.format((tim - Request.startTime)/1000.0)+"]";
		}else {
			str = "[FR,"+flo+","+"UP"+","+decimalFormat.format((tim - Request.startTime)/1000.0)+"]";	
		}
		return str;
	}
	
}
