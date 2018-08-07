package top.buaaoo.main;
import java.util.regex.*;
import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controler_old {
	/**
     * @Overview:旧控制类，拥有控制电梯的功能。
     */
	protected double RunTime = 0;
	protected String status = null;
	protected int InitBu = 105;
	void schedule (Input De,Floor F,Elevator E) {
		/**
		 * @ REQUIRES: De != null, F != null, E != null;
		 * 
		 * @ MODIFIES: System.out,RunTime,status;
		 * 
		 * @ EFFECTS: 执行傻瓜调度
		 */
		double EleTime[] = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		double UpTime[] = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		double DownTime[] = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		int Num = De.getNum();
		double[] Time = De.getTime();
		int[] Floor = De.getFloor();
		int[] Method = De.getMethod();
		int i = 0;
		for(i = 0;i < Num;i++) {
			if(Method[i] == 1) {
				if(DownTime[Floor[i]] >= Time[i]) {
					System.out.println("#同质请求");
					continue;
				}
				else {
					DownTime[Floor[i]] = (double) (Max(RunTime, Time[i])+Math.abs(F.getFloor()-Floor[i])*0.5+1);
				}
			}
			else if(Method[i] == 2) {
				if(UpTime[Floor[i]] >= Time[i]) {
					System.out.println("#同质请求");
					continue;
				}
				else {
					UpTime[Floor[i]] = (double) (Max(RunTime, Time[i])+Math.abs(F.getFloor()-Floor[i])*0.5+1);
				}
			}
			else if(Method[i] == 3) {
				if(EleTime[Floor[i]] >= Time[i]) {
					System.out.println("#同质请求");
					continue;
				}
				else {
					EleTime[Floor[i]] = (double) (Max(RunTime, Time[i])+Math.abs(F.getFloor()-Floor[i])*0.5+1);
				}
			}
			
			
						
			if(RunTime < Time[i]) RunTime = Time[i];
/*			if(Method[i] == 1) {
				if(Floor[i] == F.getFloor()) status = "STILL";
				else status = "DOWN";
			}
			else if(Method[i] == 2) {
				if(Floor[i] == F.getFloor()) status = "STILL";
				else status = "UP";
			}
			else if(Method[i] == 3){
				if(Floor[i] > F.getFloor()) status = "UP";
				else if(Floor[i] < F.getFloor()) status = "DOWN";
				else status = "STILL";
			}*/
			if(Floor[i] > F.getFloor()) status = "UP";
			else if(Floor[i] < F.getFloor()) status = "DOWN";
			else status = "STILL";
			RunTime = E.running(RunTime, F.getFloor(), Floor[i]);
			F.setFloor(Floor[i]);
			output(F);
			
			
		}
	}
	void output(Floor F) {
		/**
		 * @ REQUIRES: F != null;
		 * 
		 * @ MODIFIES: System.out;
		 * 
		 * @ EFFECTS: 按格式输出电梯状态;
		 */
		if (status == "STILL") {
			System.out.print("("+F.getFloor()+","+status+",");
			System.out.printf("%.1f)\n",RunTime);
		}
		else {
			double Run = RunTime-1;
			System.out.print("("+F.getFloor()+","+status+",");
			System.out.printf("%.1f)\n",Run);
		}
	}
	protected void setRunTime(double t) {
		/**
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: RunTime;
		 * 
		 * @ EFFECTS: RunTime == t;
		 */
		RunTime = t;
	}
	double Max(double i, double j) {
		/**
		 * @ REQUIRES: i != null, j != null;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: i>j ==> \result == i;
		 * 			 i<=j ==> \result == j;
		 */
		if(i > j) return i;
		else return j;
	}
}
