package oo;
import java.math.*;
public class Elevator {
	private int start_floor;
	private BigDecimal start_time;
	private BigDecimal []end_time;
	private boolean []buttons;
	private String direction;
	public Elevator() {
		this.start_floor = 1;
		this.end_time = new BigDecimal[10];
		for (int i=0;i<10;i++) this.end_time[i] = new BigDecimal("0");
		this.start_time = new BigDecimal("0");
		this.direction = "";
		this.buttons = new boolean[10];
		for (int i=0;i<10;i++) this.buttons[i]=false;
	}
	public void update(String who,int target,String direction,BigDecimal request_time,Floor f) {
			for (int i = 0;i < 10;i++) {
				if (this.buttons[i]&&this.end_time[i].compareTo(request_time)<=0) 
						this.buttons[i] = false;
				if (f.get_down_button(i+1)&&f.get_end_time_down(i+1).compareTo(request_time)<=0) 
						f.update_down_buttons(i+1, false);
				if (f.get_up_button(i+1)&&f.get_end_time_up(i+1).compareTo(request_time)<=0) 
						f.update_up_buttons(i+1, false);
			}
			if (this.start_time.compareTo(request_time)<0) 
				this.start_time = request_time;
			double temp = Math.abs(((double)target-(double)this.start_floor))*0.5+1;
			BigDecimal bd_temp = new BigDecimal(Double.toString(temp));
			if (direction.equals("UP")) {
				f.update_end_time_up(target,start_time.add(bd_temp));
				f.update_up_buttons(target, true);
				if (target>this.start_floor) 
					this.direction = "UP";
				else if(target<this.start_floor) 
					this.direction = "DOWN";
				else 
					this.direction = "STILL";
			}
			else {
				f.update_end_time_down(target,this.start_time.add(bd_temp));
				f.update_down_buttons(target, true);
				if (target>this.start_floor) 
					this.direction = "UP";
				else if(target<this.start_floor) 
					this.direction = "DOWN";
				else 
					this.direction = "STILL";
			}
			this.start_time = this.start_time.add(bd_temp);
			this.start_floor = target;
			this.print(this.start_floor,this.start_time);
	}
	public void update(String who,int target,BigDecimal request_time,Floor f) {
		for (int i = 0;i < 10;i++) {
			if (this.buttons[i]&&this.end_time[i].compareTo(request_time)<=0) 
					this.buttons[i] = false;
			if (f.get_down_button(i+1)&&f.get_end_time_down(i+1).compareTo(request_time)<=0) 
					f.update_down_buttons(i+1, false);
			if (f.get_up_button(i+1)&&f.get_end_time_up(i+1).compareTo(request_time)<=0) 
					f.update_up_buttons(i+1, false);
		}		
		if (this.start_time.compareTo(request_time)<0) 
			this.start_time = request_time;
		double temp = Math.abs(((double)target-(double)this.start_floor))*0.5+1;
		BigDecimal bd_temp = new BigDecimal(Double.toString(temp));
		this.end_time[target-1] = this.start_time.add(bd_temp);
		this.buttons[target-1] = true;
		if (target>this.start_floor) 
			this.direction = "UP";
		else if(target<this.start_floor) 
			this.direction = "DOWN";
		else 
			this.direction = "STILL";
		this.start_time = this.end_time[target-1];
		this.start_floor = target;
		this.print(this.start_floor, this.start_time);
	}
	private void print(int target,BigDecimal time) {
			if (this.direction.equals("STILL"))
				System.out.println("("+target+","+this.direction+","+time.setScale(1)+")");
			else {
				BigDecimal temp = new BigDecimal(Integer.toString(1));
				temp = time.subtract(temp);
				System.out.println("("+target+","+this.direction+","+temp.setScale(1)+")");
			}
	}
	public BigDecimal get_end_time(int floor) {
		return this.end_time[floor-1];
	}
	public boolean get_button(int floor) {
		return this.buttons[floor-1];
	}
}