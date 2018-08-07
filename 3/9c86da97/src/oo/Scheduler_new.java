package oo;

import java.math.BigDecimal;

public class Scheduler_new extends Schedule {
	private Elevator e;
	private Floor f;
	public Scheduler_new(Elevator e,Floor f) {
		this.e = e;
		this.f = f;
	}
	public void Run_elevator(RequestQueue rq) {
		while (rq.Request_number_get()!=0) {
			if (!e.get_Havemain()) {
				Request r_latest = rq.Request_get(0);
				String who = r_latest.get_who_request();
				int target_floor = r_latest.get_target();
				String direction = r_latest.get_direction();
				BigDecimal time = r_latest.get_request_time();
				if (who.equals("ER")) {
					e.update(who, target_floor, time, f,r_latest.get_num());;
				}
				else if (who.equals("FR")){
					e.update(who, target_floor, direction, time, f, r_latest.get_num());
				}
				rq.Request_delete(0);
			}
			else {
				int i;
				for (i=0;i<rq.Request_number_get();i++) {
					Request r = rq.Request_get(i);
					String who = r.get_who_request();
					int r_target_floor = r.get_target();
					String r_direction = r.get_direction();
					BigDecimal r_time = r.get_request_time();
					int r_num = r.get_num();
					boolean IsDel = false;
					if (who.equals("ER")) {
						if (e.get_button(r_target_floor)&&e.get_end_time(r_target_floor).compareTo(r_time)>=0) {
							System.out.println("#SAME[ER,"+r_target_floor+","+r_time.setScale(0)+"]");
							rq.Request_delete(i);
							i--;
							continue;
						}
						else {
							BigDecimal this_end_time = this.be_taken_ER(r_target_floor, r_time);
							if (this_end_time.compareTo(new BigDecimal("-1"))>0) {
								IsDel = true;
								e.update(r_target_floor, this_end_time, r_time,f,r_num);
							}	
						}
					}
					else {
						if (r_direction.equals("UP")) {
							if (f.get_up_button(r_target_floor)&&f.get_end_time_up(r_target_floor).compareTo(r_time)>=0) {
								System.out.println("#SAME[FR,"+r_target_floor+",UP,"+r_time.setScale(0)+"]");
								rq.Request_delete(i);
								i--;
								continue;
							}
							else {
								BigDecimal this_end_time=this.be_taken_FR(r_target_floor, r_time, r_direction);
								if (this_end_time.compareTo(new BigDecimal("-1"))>0) {
									IsDel = true;
									e.update(r_target_floor, this_end_time, r_direction,r_time, f,r_num);
								}	
							}
						}
						else {
							if (f.get_down_button(r_target_floor)&&f.get_end_time_down(r_target_floor).compareTo(r_time)>=0) {
								System.out.println("#SAME[FR,"+r_target_floor+",DOWN,"+r_time.setScale(0)+"]");
								rq.Request_delete(i);
								i--;
								continue;
							}
							else {
								BigDecimal this_end_time=this.be_taken_FR(r_target_floor, r_time, r_direction);
								if (this_end_time.compareTo(new BigDecimal("-1"))>0) {
									IsDel = true;
									e.update(r_target_floor, this_end_time, r_direction, r_time,f,r_num);
								}
							}
						}
					}
					if (IsDel) {
						rq.Request_delete(i);
						i--;
						break;
					}
				}
				if (i==rq.Request_number_get()) {
					e.release(f);
				}
			}
		}
		e.releaseAll(f);
	}
	private BigDecimal be_taken_ER(int r_target_floor,BigDecimal r_time) {
		String e_direction = e.get_direction();
		if (e_direction.equals("UP")) {
			if (r_target_floor>e.get_current_floor()&&r_target_floor<=e.get_main_target_floor()) {
				for(int j = r_target_floor;j<=e.get_main_target_floor();j++) {
					if (e.get_button(j)) {
						BigDecimal temp_t = new BigDecimal(Double.toString(((double)j-(double)r_target_floor)*0.5+1));
						BigDecimal arrive_time = e.get_end_time(j).subtract(temp_t);
						if (arrive_time.compareTo(r_time)>0) {
							return arrive_time.add(new BigDecimal("1"));
						}
						else break;
					}
					if (f.get_up_button(j)) {
						BigDecimal temp_t = new BigDecimal(Double.toString(((double)j-(double)r_target_floor)*0.5+1));
						BigDecimal arrive_time = f.get_end_time_up(j).subtract(temp_t);
						if (arrive_time.compareTo(r_time)>0) {
							return arrive_time.add(new BigDecimal("1"));
						}
						else break;
					}
					if (f.get_down_button(j)) {
						BigDecimal temp_t = new BigDecimal(Double.toString(((double)j-(double)r_target_floor)*0.5+1));
						BigDecimal arrive_time = f.get_end_time_down(j).subtract(temp_t);
						if (arrive_time.compareTo(r_time)>0) {
							return arrive_time.add(new BigDecimal("1"));
						}
						else break;
					}
				}
			}
			else if (r_target_floor>e.get_main_target_floor()) {
				if (r_time.compareTo(e.get_main_end_time().subtract(new BigDecimal("1")))<0) {
					for(int j = r_target_floor;j>=e.get_main_target_floor();j--) {
						if (e.get_button(j)) {
							BigDecimal temp_t = new BigDecimal(Double.toString(((double)r_target_floor-(double)j)*0.5));
							BigDecimal arrive_time = e.get_end_time(j).add(temp_t);
							return arrive_time.add(new BigDecimal("1"));	
						}
						if (f.get_up_button(j)) {
							BigDecimal temp_t = new BigDecimal(Double.toString(((double)r_target_floor-(double)j)*0.5));
							BigDecimal arrive_time = f.get_end_time_up(j).add(temp_t);
							return arrive_time.add(new BigDecimal("1"));

						}
						if (f.get_down_button(j)) {
							BigDecimal temp_t = new BigDecimal(Double.toString(((double)r_target_floor-(double)j)*0.5));
							BigDecimal arrive_time = f.get_end_time_down(j).add(temp_t);
							return arrive_time.add(new BigDecimal("1"));
						}
					}
				}
			}
		}
		else if (e.get_direction().equals("DOWN")) {
			if (r_target_floor<e.get_current_floor()&&r_target_floor>=e.get_main_target_floor()) {
				for(int j = r_target_floor;j>=e.get_main_target_floor();j--) {
					if (e.get_button(j)) {
						BigDecimal temp_t = new BigDecimal(Double.toString(((double)r_target_floor-(double)j)*0.5+1));
						BigDecimal arrive_time = e.get_end_time(j).subtract(temp_t);
						if (arrive_time.compareTo(r_time)>0) {
							return arrive_time.add(new BigDecimal("1"));
						}
						else break;
					}
					if (f.get_up_button(j)) {
						BigDecimal temp_t = new BigDecimal(Double.toString(((double)r_target_floor-(double)j)*0.5+1));
						BigDecimal arrive_time = f.get_end_time_up(j).subtract(temp_t);
						if (arrive_time.compareTo(r_time)>0) {
							return arrive_time.add(new BigDecimal("1"));
						}
						else break;
					}
					if (f.get_down_button(j)) {
						BigDecimal temp_t = new BigDecimal(Double.toString(((double)r_target_floor-(double)j)*0.5+1));
						BigDecimal arrive_time = f.get_end_time_down(j).subtract(temp_t);
						if (arrive_time.compareTo(r_time)>0) {
							return arrive_time.add(new BigDecimal("1"));
						}
						else break;
					}
				}
			}
			else if (r_target_floor<e.get_main_target_floor()) {
				if (r_time.compareTo(e.get_main_end_time().subtract(new BigDecimal("1")))<0) {
					for(int j = r_target_floor;j<=e.get_main_target_floor();j++) {
						if (e.get_button(j)) {
							BigDecimal temp_t = new BigDecimal(Double.toString(((double)j-(double)r_target_floor)*0.5));
							BigDecimal arrive_time = e.get_end_time(j).add(temp_t);
							return arrive_time.add(new BigDecimal("1"));
						}
						if (f.get_up_button(j)) {
							BigDecimal temp_t = new BigDecimal(Double.toString(((double)j-(double)r_target_floor)*0.5));
							BigDecimal arrive_time = f.get_end_time_up(j).add(temp_t);
							return arrive_time.add(new BigDecimal("1"));
						}
						if (f.get_down_button(j)) {
							BigDecimal temp_t = new BigDecimal(Double.toString(((double)j-(double)r_target_floor)*0.5));
							BigDecimal arrive_time = f.get_end_time_down(j).add(temp_t);
							return arrive_time.add(new BigDecimal("1"));
						}
					}
				}
			}
		}
		return new BigDecimal("-1");
	}
	private BigDecimal be_taken_FR(int r_target_floor,BigDecimal r_time,String r_direction) {
		String e_direction = e.get_direction();
		if (e_direction.equals(r_direction)) {
			if (e_direction.equals("UP")&&r_target_floor<=e.get_main_target_floor()&&r_target_floor>e.get_current_floor()) {
				for(int j = r_target_floor;j<=e.get_main_target_floor();j++) {
					if (e.get_button(j)) {
						BigDecimal temp_t = new BigDecimal(Double.toString(((double)j-(double)r_target_floor)*0.5+1));
						BigDecimal arrive_time = e.get_end_time(j).subtract(temp_t);
						if (arrive_time.compareTo(r_time)>0) {
							return arrive_time.add(new BigDecimal("1"));
						}
						else break;
					}
					if (f.get_up_button(j)) {
						BigDecimal temp_t = new BigDecimal(Double.toString(((double)j-(double)r_target_floor)*0.5+1));
						BigDecimal arrive_time = f.get_end_time_up(j).subtract(temp_t);
						if (arrive_time.compareTo(r_time)>0) {
							return arrive_time.add(new BigDecimal("1"));
						}
						else break;
					}
					if (f.get_down_button(j)) {
						BigDecimal temp_t = new BigDecimal(Double.toString(((double)j-(double)r_target_floor)*0.5+1));
						BigDecimal arrive_time = f.get_end_time_down(j).subtract(temp_t);
						if (arrive_time.compareTo(r_time)>0) {
							return arrive_time.add(new BigDecimal("1"));
						}
						else break;
					}
				}
			}
			else if (e_direction.equals("DOWN")&&r_target_floor>=e.get_main_target_floor()&&r_target_floor<e.get_current_floor()) {
				for(int j = r_target_floor;j>=e.get_main_target_floor();j--) {
					if (e.get_button(j)) {
						BigDecimal temp_t = new BigDecimal(Double.toString(((double)r_target_floor-(double)j)*0.5+1));
						BigDecimal arrive_time = e.get_end_time(j).subtract(temp_t);
						if (arrive_time.compareTo(r_time)>0) {
							return arrive_time.add(new BigDecimal("1"));
						}
						else break;
					}
					if (f.get_up_button(j)) {
						BigDecimal temp_t = new BigDecimal(Double.toString(((double)r_target_floor-(double)j)*0.5+1));
						BigDecimal arrive_time = f.get_end_time_up(j).subtract(temp_t);
						if (arrive_time.compareTo(r_time)>0) {
							return arrive_time.add(new BigDecimal("1"));
						}
						else break;
					}
					if (f.get_down_button(j)) {
						BigDecimal temp_t = new BigDecimal(Double.toString(((double)r_target_floor-(double)j)*0.5+1));
						BigDecimal arrive_time = f.get_end_time_down(j).subtract(temp_t);
						if (arrive_time.compareTo(r_time)>0) {
							return arrive_time.add(new BigDecimal("1"));
						}
						else break;
					}
				}
			}
		}
		return new BigDecimal("-1");
	}
}
