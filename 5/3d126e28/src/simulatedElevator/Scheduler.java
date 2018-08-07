package simulatedElevator;

class Scheduler_1 {
	boolean restart;

	Scheduler_1() {
		this.restart = false;
	}

	public void doit() {
		int i ;
		for (i = 0; i < Requestqueue.get_size(); i++) {
			System.out.println(Requestqueue.getReqList(i));
		}

	}
	
}

class Scheduler_2 extends Scheduler_1{

	Scheduler_2() {
		super();
		this.restart = false;
	}

	protected boolean isCarryUpEr(Elevator elev, Request aReq) {
		synchronized (elev) {
			if (aReq.get_req_floor() > elev.getFloor()) {
				return true;
			}
			return false;
		}
	}

	protected boolean isCarryUpFr(Elevator elev, Request aReq) {
		synchronized (elev) {
			if ( aReq.get_req_type()==1 && aReq.get_req_floor() > elev.getFloor() && aReq.get_req_floor() <= elev.get_target()) {
				return true;
			}
			return false;
		}
	}

	protected boolean isCarryDownEr(Elevator elev, Request aReq) {
		synchronized (elev) {
			if (aReq.get_req_floor() < elev.getFloor()) {
				return true;
			}
			return false;
		}
	}

	protected boolean isCarryDownFr(Elevator elev, Request aReq) {
		synchronized (elev) {
			if ( aReq.get_req_type()==2 && aReq.get_req_floor() < elev.getFloor() && aReq.get_req_floor() >= elev.get_target()) {
				return true;
			}
			return false;
		}
	}

	protected int getElevForReqEr(Request aReq) {
		synchronized(Main.elevator[ aReq.get_eleva_num() - 1]) {
		if(Main.elevator[ aReq.get_eleva_num() - 1].get_sta()==1) {
			if(this.isCarryUpEr(Main.elevator[ aReq.get_eleva_num() -1 ], aReq)) {
				return  aReq.get_eleva_num() ;
			} 
		}
		else if(Main.elevator[ aReq.get_eleva_num() - 1].get_sta()==2) {
			if(this.isCarryDownEr(Main.elevator[ aReq.get_eleva_num() -1 ], aReq)) {
				return  aReq.get_eleva_num() ;
			} 
		} 
		return 0;
		}
	}

	protected int getElevForReqFr(Request aReq) {
		int flag = 10 , j ;
		boolean bool = false ;
		for (j = 0; j < 3; j++) {
			synchronized(Main.elevator[j]) {
			
			bool = false ;
			
			if(Main.elevator[j].get_sta()==1)
				bool = this.isCarryUpFr(Main.elevator[j], aReq);
			else if(Main.elevator[j].get_sta()==2)
				bool = this.isCarryDownFr(Main.elevator[j], aReq);
			else {
				bool = false;
			}
				
			if ( bool ) {
				if (flag > 3)
					flag = j;
				else {
					if (Main.elevator[j].get_amount() < Main.elevator[flag].get_amount()) {
						flag = j;
					}
				}
			}
			}
		}
		if (flag < 3) {
			return flag+1;
		}
		return 0;
	}

	protected int getElevForReqWFS(Request aReq) {
		if (aReq.get_req_type() == 0) {
			synchronized(Main.elevator[aReq.get_eleva_num() - 1]) {
			if (Main.elevator[aReq.get_eleva_num() - 1].get_sta() == 3) {
				return aReq.get_eleva_num() ;
			}
			}
		} else {
			int flag = 4 , j ;
			for (j = 0; j < 3; j++) {
				synchronized(Main.elevator[j]) {
				if (Main.elevator[j].get_sta() == 3) {
					if (flag > 3)
						flag = j;
					else {
						if (Main.elevator[j].get_amount() < Main.elevator[flag].get_amount()) {
							flag = j;
						}
					}
				}
				}
			}
			if (flag < 3) {
				return flag + 1;
			}
			
		}
		return 0;
	}
	
	protected int getElevCanCarryOn(Request aReq) {
		if(aReq.get_req_type()==0) {
			return this.getElevForReqEr(aReq);
		}
		else {
			return this.getElevForReqFr(aReq);
		}

	}

	public void doit() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		while (Main.read.isAlive() || Requestqueue.get_size() > 0) {
			if (this.restart) {
				this.restart = false;

				int i ;
				for (i = 0; i < Requestqueue.get_size(); i++) {
					if(this.restart==true) {
						break;
					}
					int is_it_can_piggyback = 0;
					is_it_can_piggyback = this.getElevCanCarryOn(Requestqueue.getReqList(i));
					if(is_it_can_piggyback>0) {
						Main.elevator[is_it_can_piggyback-1].add_req(Requestqueue.getReqList(i));
						
					}
					else {
						is_it_can_piggyback = this.getElevForReqWFS( Requestqueue.getReqList(i) );
						if( is_it_can_piggyback>0 ) {
							Main.elevator[is_it_can_piggyback-1].add_req(Requestqueue.getReqList(i));
							Main.elevator_[is_it_can_piggyback-1].interrupt();
							try {
								Thread.sleep(20);
							} catch (InterruptedException e) { }
						}
					}
					
					if (is_it_can_piggyback > 0) {
						Requestqueue.remove_a_req(i);
						//this.restart = true;
						//break;
						i = -1 ;
					}
				}

			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
}


class Scheduler  extends Scheduler_2 implements Runnable {

	Scheduler() {
		super();
		this.restart = false;
	}

	public void run() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		while (Main.read.isAlive() || Requestqueue.get_size() > 0) {
			if (this.restart) {
				this.restart = false;

				int i ;
				for (i = 0; i < Requestqueue.get_size(); i++) {
					if(this.restart==true) {
						break;
					}
					int is_it_can_piggyback = 0;
					is_it_can_piggyback = this.getElevCanCarryOn(Requestqueue.getReqList(i));
					if(is_it_can_piggyback>0) {
						Main.elevator[is_it_can_piggyback-1].add_req(Requestqueue.getReqList(i));
						
					}
					else {
						is_it_can_piggyback = this.getElevForReqWFS( Requestqueue.getReqList(i) );
						if( is_it_can_piggyback>0 ) {
							Main.elevator[is_it_can_piggyback-1].add_req(Requestqueue.getReqList(i));
							Main.elevator_[is_it_can_piggyback-1].interrupt();
							try {
								Thread.sleep(20);
							} catch (InterruptedException e) { }
						}
					}
					
					if (is_it_can_piggyback > 0) {
						Requestqueue.remove_a_req(i);
						//this.restart = true;
						//break;
						i = -1 ;
					}
				}

			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
}
