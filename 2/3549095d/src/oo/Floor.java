package oo;
import java.math.*;
public class Floor {
	private boolean []up_buttons;
	private boolean [] down_buttons;
	private BigDecimal[] end_time_up;
	private BigDecimal[] end_time_down;
	public Floor() {
		up_buttons = new boolean[10];
		down_buttons = new boolean[10];
		end_time_up = new BigDecimal[10];
		end_time_down = new BigDecimal[10];
		for (int i=0;i<10;i++) {
			up_buttons[i] = false;
			down_buttons[i] = false;
			end_time_up[i] = new BigDecimal(Integer.toString(0));
			end_time_down[i] = new BigDecimal(Integer.toString(0));
		}
	}
	public void update_end_time_up(int floor,BigDecimal time) {
		end_time_up[floor-1] = time;
	}
	public void update_end_time_down(int floor,BigDecimal time) {
		end_time_down[floor-1] = time;
	}
	public void update_up_buttons(int floor,boolean b) {
		up_buttons[floor-1] = b;
	}
	public void update_down_buttons(int floor,boolean b) {
		down_buttons[floor-1] = b;
	}
	public BigDecimal get_end_time_up(int floor) {
		return end_time_up[floor-1];
	}
	public BigDecimal get_end_time_down(int floor) {
		return end_time_down[floor-1];
	}
	public boolean get_up_button(int floor) {
		return up_buttons[floor-1];
	}
	public boolean get_down_button(int floor) {
		return down_buttons[floor-1];
	}
}
