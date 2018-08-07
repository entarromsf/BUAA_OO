package oo;
import java.math.*;
public class Floor {
	private boolean []up_buttons;
	private boolean [] down_buttons;
	private BigDecimal[] end_time_up;
	private BigDecimal[] end_time_down;
	private int num_for_sort_up[];
	private int num_for_sort_down[];
	private BigDecimal r_t_u[];
	private BigDecimal r_t_d[];
	public Floor() {
		up_buttons = new boolean[10];
		down_buttons = new boolean[10];
		end_time_up = new BigDecimal[10];
		end_time_down = new BigDecimal[10];
		num_for_sort_up = new int [10];
		num_for_sort_down = new int [10];
		r_t_u = new BigDecimal[10];
		r_t_d = new BigDecimal[10];
		for (int i=0;i<10;i++) {
			up_buttons[i] = false;
			down_buttons[i] = false;
			end_time_up[i] = new BigDecimal(Integer.toString(0));
			end_time_down[i] = new BigDecimal(Integer.toString(0));
			r_t_u[i] = new BigDecimal(Integer.toString(0));
			r_t_d[i] = new BigDecimal(Integer.toString(0));
			num_for_sort_up[i] = 0;
			num_for_sort_down[i] = 0;
		}
	}
	public void update_end_time_up(int floor,BigDecimal time) {
		end_time_up[floor-1] = time;
	}
	public void update_end_time_down(int floor,BigDecimal time) {
		end_time_down[floor-1] = time;
	}
	public void update_num_up(int floor,int num) {
		num_for_sort_up[floor-1] = num;
	}
	public void update_num_down(int floor,int num) {
		num_for_sort_down[floor-1] = num;
	}
	public void update_up_buttons(int floor,boolean b) {
		up_buttons[floor-1] = b;
	}
	public void update_down_buttons(int floor,boolean b) {
		down_buttons[floor-1] = b;
	}
	public void update_r_t_u(int floor,BigDecimal t) {
		r_t_u[floor-1] = t;
	}
	public void update_r_t_d(int floor,BigDecimal t) {
		r_t_d[floor-1] = t;
	}
	public BigDecimal get_r_t_u(int floor) {
		return r_t_u[floor-1];
	}
	public BigDecimal get_r_t_d(int floor) {
		return r_t_d[floor-1];
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
	public int get_num_up(int floor) {
		return num_for_sort_up[floor-1];
	}
	public int get_num_down(int floor) {
		return num_for_sort_down[floor-1];
	}
}
