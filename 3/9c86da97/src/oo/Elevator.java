package oo;
import java.math.*;
import java.util.*;
class print {
	private String who;
	private BigDecimal request_time;
	private BigDecimal end_time;
	private int target_floor;
	private String direction;
	private String e_direction;
	private int num;
	public print(String who,String direction,BigDecimal request_time,BigDecimal end_time,int target_floor,String e_direction,int num) {
		this.who = who;
		this.direction = direction;
		this.request_time = request_time;
		this.end_time = end_time;
		this.target_floor = target_floor;
		this.e_direction = e_direction;
		this.num = num;
	}
	public int get_num() {
		return num;
	}
	public String toString() {
		String s ;
		if (who.equals("ER")) {
			s = "[ER,"+Integer.toString(target_floor)+","+request_time.setScale(0).toString()+"]/("+Integer.toString(target_floor)+","+e_direction+","+end_time.setScale(1).toString()+")";
		}
		else {
			s = "[FR,"+Integer.toString(target_floor)+","+direction+","+request_time.setScale(0).toString()+"]/("+Integer.toString(target_floor)+","+e_direction+","+end_time.setScale(1).toString()+")";;
		}
		return s;
	}
}
public class Elevator implements Elevator_interface {
	private int current_floor;
	private BigDecimal current_time;
	private BigDecimal []end_time;
	private BigDecimal request_time[];
	private boolean []buttons;
	private String direction;
	private boolean Havemain;
	private int main_target_floor;
	private int num_for_sort[];
	private BigDecimal main_end_time;
	public Elevator() {
		this.current_floor = 1;
		this.main_target_floor = 1;
		this.num_for_sort = new int[10];
		this.end_time = new BigDecimal[10];
		this.request_time = new BigDecimal [10];
		this.main_end_time = new BigDecimal ("0");
		for (int i=0;i<10;i++) {
			this.end_time[i] = new BigDecimal("0");
			this.request_time[i] = new BigDecimal("0");
			this.num_for_sort[i] = 0;
		}
		this.current_time = new BigDecimal("0");
		this.direction = "";
		this.buttons = new boolean[10];
		for (int i=0;i<10;i++) this.buttons[i]=false;
		this.Havemain = false;
	}
	public void update(String who,int target,String direction,BigDecimal request_time,Floor f) {};
	public void update(String who,int target,BigDecimal request_time,Floor f) {};
	public void update(String who,int target,String direction,BigDecimal request_time,Floor f,int num) {
			if (this.current_time.compareTo(request_time)<0) 
				this.current_time = request_time;
			double temp = Math.abs(((double)target-(double)this.current_floor))*0.5+1;
			BigDecimal bd_temp = new BigDecimal(Double.toString(temp));
			this.main_end_time = current_time.add(bd_temp);
			if (direction.equals("UP")) {
				f.update_end_time_up(target,current_time.add(bd_temp));
				f.update_up_buttons(target, true);
				f.update_r_t_u(target, request_time);
				f.update_num_up(target, num);
				
				if (target>this.current_floor) 
					this.direction = "UP";
				else if(target<this.current_floor) 
					this.direction = "DOWN";
				else 
					this.direction = "STILL";
			}
			else {
				f.update_end_time_down(target,this.current_time.add(bd_temp));
				f.update_down_buttons(target, true);
				f.update_r_t_d(target, request_time);
				f.update_num_down(target, num);
				if (target>this.current_floor) 
					this.direction = "UP";
				else if(target<this.current_floor) 
					this.direction = "DOWN";
				else 
					this.direction = "STILL";
			}
			
			this.Havemain = true;
			this.main_target_floor = target;
	}
	public void update(String who,int target,BigDecimal request_time,Floor f,int num) {
		if (this.current_time.compareTo(request_time)<0) 
			this.current_time = request_time;
		double temp = Math.abs(((double)target-(double)this.current_floor))*0.5+1;
		BigDecimal bd_temp = new BigDecimal(Double.toString(temp));
		this.main_end_time = current_time.add(bd_temp);
		this.end_time[target-1] = this.current_time.add(bd_temp);
		this.buttons[target-1] = true;
		this.request_time[target-1] = request_time;
		if (target>this.current_floor) 
			this.direction = "UP";
		else if(target<this.current_floor) 
			this.direction = "DOWN";
		else 
			this.direction = "STILL";
		this.Havemain = true;
		this.main_target_floor = target;
		this.num_for_sort[target-1] = num;
	}
	public void update(int target,BigDecimal this_end_time,BigDecimal rt,Floor f,int num) {
		if (!this.buttons[target-1]&&!f.get_down_button(target)&&!f.get_up_button(target)) {
			for (int i=0;i<10;i++) {
				if (this.buttons[i]&&this.end_time[i].compareTo(this_end_time)>0) this.end_time[i] = this.end_time[i].add(new BigDecimal("1"));
				if (f.get_up_button(i+1)&&f.get_end_time_up(i+1).compareTo(this_end_time)>0) f.update_end_time_up(i+1, f.get_end_time_up(i+1).add(new BigDecimal("1")));
				if (f.get_down_button(i+1)&&f.get_end_time_down(i+1).compareTo(this_end_time)>0) f.update_end_time_down(i+1, f.get_end_time_down(i+1).add(new BigDecimal("1")));
			}
		}
		if (this.main_end_time.compareTo(this_end_time)>0) this.main_end_time = this.main_end_time.add(new BigDecimal("1"));
		this.num_for_sort[target-1] = num;
		this.buttons[target-1] = true;
		this.end_time[target-1] = this_end_time;
		this.request_time[target-1] = rt;
	}
	public void update(int target,BigDecimal this_end_time,String r_direction,BigDecimal rt,Floor f,int num) {
		if (!this.buttons[target-1]&&!f.get_down_button(target)&&!f.get_up_button(target)) {
			for (int i=0;i<10;i++) {
				if (this.buttons[i]&&this.end_time[i].compareTo(this_end_time)>0) this.end_time[i] = this.end_time[i].add(new BigDecimal("1"));
				if (f.get_up_button(i+1)&&f.get_end_time_up(i+1).compareTo(this_end_time)>0) f.update_end_time_up(i+1, f.get_end_time_up(i+1).add(new BigDecimal("1")));
				if (f.get_down_button(i+1)&&f.get_end_time_down(i+1).compareTo(this_end_time)>0) f.update_end_time_down(i+1, f.get_end_time_down(i+1).add(new BigDecimal("1")));
			}
		}
		if (this.main_end_time.compareTo(this_end_time)>0) this.main_end_time = this.main_end_time.add(new BigDecimal("1"));
		if (r_direction.equals("UP")) {
			f.update_num_up(target, num);
			f.update_up_buttons(target, true);
			f.update_end_time_up(target, this_end_time);
			f.update_r_t_u(target, rt);
		}
		else {
			f.update_num_down(target, num);
			f.update_down_buttons(target, true);
			f.update_end_time_down(target, this_end_time);
			f.update_r_t_d(target, rt);
		}
	}
	public void release(Floor f) {
		if (this.buttons[this.main_target_floor-1]) {
			this.current_time = this.end_time[this.main_target_floor-1];
		}
		else if (f.get_up_button(main_target_floor)) {
			this.current_time = f.get_end_time_up(main_target_floor);
		}
		else this.current_time  =f.get_end_time_down(main_target_floor);
		if (this.direction.equals("UP")) {
			for (int i = this.current_floor;i<=this.main_target_floor;i++) {
				ArrayList<print> p = new ArrayList<print>();
				if (this.buttons[i-1]) {
					print t = new print("ER","",this.request_time[i-1],this.end_time[i-1].subtract(new BigDecimal("1")),i,this.direction,this.num_for_sort[i-1]);
					p.add(t);
					this.buttons[i-1] = false;
				}
				if (f.get_up_button(i)) {
					print t = new print("FR","UP",f.get_r_t_u(i),f.get_end_time_up(i).subtract(new BigDecimal("1")),i,this.direction,f.get_num_up(i));
					p.add(t);
					f.update_up_buttons(i, false);
				}
				if (f.get_down_button(i)) {
					print t = new print("FR","DOWN",f.get_r_t_d(i),f.get_end_time_down(i).subtract(new BigDecimal("1")),i,this.direction,f.get_num_down(i));
					p.add(t);
					f.update_down_buttons(i, false);
				}
				for (int j=0;j<p.size();j++) {
					for (int k=j+1;k<p.size();k++) {
						if (p.get(j).get_num()>p.get(k).get_num()) {
							print t = p.get(j);
							p.set(j, p.get(k));
							p.set(k, t);
						}
					}
				}
				for (int j=0;j<p.size();j++) {
					if (p.get(j)!=null)
						System.out.println(p.get(j).toString());
				}
			}
		}
		else if (this.direction.equals("DOWN")) {
			for (int i = this.current_floor;i>=this.main_target_floor;i--) {
				ArrayList<print> p = new ArrayList<print>();
				if (this.buttons[i-1]) {
					print t = new print("ER","",this.request_time[i-1],this.end_time[i-1].subtract(new BigDecimal("1")),i,this.direction,this.num_for_sort[i-1]);
					p.add( t);
					this.buttons[i-1] = false;
				}
				if (f.get_up_button(i)) {
					print t = new print("FR","UP",f.get_r_t_u(i),f.get_end_time_up(i).subtract(new BigDecimal("1")),i,this.direction,f.get_num_up(i));
					p.add(t);
					f.update_up_buttons(i, false);
				}
				if (f.get_down_button(i)) {
					print t = new print("FR","DOWN",f.get_r_t_d(i),f.get_end_time_down(i).subtract(new BigDecimal("1")),i,this.direction,f.get_num_down(i));
					p.add(t);
					f.update_down_buttons(i, false);
				}
				for (int j=0;j<p.size();j++) {
					for (int k=j+1;k<p.size();k++) {
						if (p.get(j).get_num()>p.get(k).get_num()) {
							print t = p.get(j);
							p.set(j, p.get(k));
							p.set(k, t);
						}
					}
				}
				for (int j=0;j<p.size();j++) {
					System.out.println(p.get(j).toString());
				}
			}
			
		}
		else if (this.direction.equals("STILL")){
			if (this.buttons[this.main_target_floor-1]) {
				this.buttons[this.main_target_floor-1] = false;
				System.out.println("[ER,"+this.main_target_floor+","+this.request_time[this.main_target_floor-1].setScale(0)+"]/("+this.main_target_floor+",STILL,"+this.end_time[this.main_target_floor-1].setScale(1)+")");
			}
			else if(f.get_up_button(main_target_floor)) {
				f.update_up_buttons(main_target_floor, false);
				System.out.println("[FR,"+this.main_target_floor+",UP,"+f.get_r_t_u(main_target_floor)+"]/("+this.main_target_floor+",STILL,"+f.get_end_time_up(main_target_floor).setScale(1)+")");
			}
			else if (f.get_down_button(main_target_floor)){
				f.update_down_buttons(main_target_floor, false);
				System.out.println("[FR,"+this.main_target_floor+",DOWN,"+f.get_r_t_d(main_target_floor)+"]/("+this.main_target_floor+",STILL,"+f.get_end_time_down(main_target_floor).setScale(1)+")");
			}
		}
		this.current_floor = this.main_target_floor;
		this.Havemain = false;
		for (int i=0;i<10;i++) {
			if (this.buttons[i]) {
				this.Havemain = true;
				this.main_target_floor = i+1;
				this.main_end_time = this.end_time[i];
			}
		}
	}
	public void releaseAll(Floor f) {
		if (this.direction.equals("UP")) {
			for (int i = 1;i<=10;i++) {
				ArrayList<print> p = new ArrayList<print>();
				if (this.buttons[i-1]) {
					print t = new print("ER","",this.request_time[i-1],this.end_time[i-1].subtract(new BigDecimal("1")),i,this.direction,this.num_for_sort[i-1]);
					p.add(t);
					this.buttons[i-1] = false;
				}
				if (f.get_up_button(i)) {
					print t = new print("FR","UP",f.get_r_t_u(i),f.get_end_time_up(i).subtract(new BigDecimal("1")),i,this.direction,f.get_num_up(i));
					p.add(t);
					f.update_up_buttons(i, false);
				}
				if (f.get_down_button(i)) {
					print t = new print("FR","DOWN",f.get_r_t_d(i),f.get_end_time_down(i).subtract(new BigDecimal("1")),i,this.direction,f.get_num_down(i));
					p.add(t);
					f.update_down_buttons(i, false);
				}
				for (int j=0;j<p.size();j++) {
					for (int k=j+1;k<p.size();k++) {
						if (p.get(j).get_num()>p.get(k).get_num()) {
							print t = p.get(j);
							p.set(j, p.get(k));
							p.set(k, t);
						}
					}
				}
				for (int j=0;j<p.size();j++) {
					if (p.get(j)!=null)
						System.out.println(p.get(j).toString());
				}
			}
		}
		else if (this.direction.equals("DOWN")) {
			for (int i = 10;i>=1;i--) {
				ArrayList<print> p = new ArrayList<print>();
				if (this.buttons[i-1]) {
					print t = new print("ER","",this.request_time[i-1],this.end_time[i-1].subtract(new BigDecimal("1")),i,this.direction,this.num_for_sort[i-1]);
					p.add( t);
					this.buttons[i-1] = false;
				}
				if (f.get_up_button(i)) {
					print t = new print("FR","UP",f.get_r_t_u(i),f.get_end_time_up(i).subtract(new BigDecimal("1")),i,this.direction,f.get_num_up(i));
					p.add(t);
					f.update_up_buttons(i, false);
				}
				if (f.get_down_button(i)) {
					print t = new print("FR","DOWN",f.get_r_t_d(i),f.get_end_time_down(i).subtract(new BigDecimal("1")),i,this.direction,f.get_num_down(i));
					p.add(t);
					f.update_down_buttons(i, false);
				}
				for (int j=0;j<p.size();j++) {
					for (int k=j+1;k<p.size();k++) {
						if (p.get(j).get_num()>p.get(k).get_num()) {
							print t = p.get(j);
							p.set(j, p.get(k));
							p.set(k, t);
						}
					}
				}
				for (int j=0;j<p.size();j++) {
					System.out.println(p.get(j).toString());
				}
			}
			
		}
		else if (this.direction.equals("STILL")){
			if (this.buttons[this.main_target_floor-1]) {
				this.buttons[this.main_target_floor-1] = false;
				System.out.println("[ER,"+this.main_target_floor+","+this.request_time[this.main_target_floor-1].setScale(0)+"]/("+this.main_target_floor+",STILL,"+this.end_time[this.main_target_floor-1].setScale(1)+")");
			}
			else if(f.get_up_button(main_target_floor)) {
				f.update_up_buttons(main_target_floor, false);
				System.out.println("[FR,"+this.main_target_floor+",UP,"+f.get_r_t_u(main_target_floor)+"]/("+this.main_target_floor+",STILL,"+f.get_end_time_up(main_target_floor).setScale(1)+")");
			}
			else if (f.get_down_button(main_target_floor)){
				f.update_down_buttons(main_target_floor, false);
				System.out.println("[FR,"+this.main_target_floor+",DOWN,"+f.get_r_t_d(main_target_floor)+"]/("+this.main_target_floor+",STILL,"+f.get_end_time_down(main_target_floor).setScale(1)+")");
			}
		}
	}
	public BigDecimal get_end_time(int floor) {
		return this.end_time[floor-1];
	}
	public boolean get_button(int floor) {
		return this.buttons[floor-1];
	}
	public BigDecimal get_current_time() {
		return this.current_time;
	}
	public String get_direction() {
		return this.direction;
	}
	public boolean get_Havemain() {
		return this.Havemain;
	}
	public int get_current_floor() {
		return this.current_floor;
	}
	public int get_main_target_floor() {
		return this.main_target_floor;
	}
	public String toString() {
		return "Direction:"+direction+" Current time:"+this.current_time.toString();
	}
	public BigDecimal get_main_end_time() {
		return this.main_end_time;
	}
}