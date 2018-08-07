package oo;
import java.math.*;
public class Schedule {
	public void Run_elevator(Elevator e,RequestQueue rq,Floor f) {
		while(rq.Request_number_get()!=0) {
			Request r_latest = rq.Request_get(0);
			String who = r_latest.get_who_request();
			int target_floor = r_latest.get_target();
			String direction = r_latest.get_direction();
			BigDecimal time = r_latest.get_request_time();
			if (who.equals("ER")) {
				if (e.get_button(target_floor)&&e.get_end_time(target_floor).compareTo(time)>=0) {
					System.out.println("#It is a same request.");
				}
				else e.update(who, target_floor, time, f);
			}
			else {
				if (direction.equals("UP")) {
					if (f.get_up_button(target_floor)&&f.get_end_time_up(target_floor).compareTo(time)>=0) 
						System.out.println("#It is a same request.");
					else e.update(who, target_floor, direction, time, f);
				}
				else {
					if (f.get_down_button(target_floor)&&f.get_end_time_down(target_floor).compareTo(time)>=0) 
						System.out.println("#It is a same request.");
					else e.update(who, target_floor, direction, time, f);
				}
			}
			rq.Request_delete(0);
		}
	}
}
