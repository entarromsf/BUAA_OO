package oo;

import java.math.BigDecimal;

public interface Elevator_interface {
	public void update(String who,int target,String direction,BigDecimal request_time,Floor f,int num);
	public void update(String who,int target,BigDecimal request_time,Floor f,int num);
	public void update(int target,BigDecimal this_end_time,BigDecimal rt,Floor f,int num);
	public void update(int target,BigDecimal this_end_time,String r_direction,BigDecimal rt,Floor f,int num);
	public void release(Floor f);
	public void releaseAll(Floor f);
}
