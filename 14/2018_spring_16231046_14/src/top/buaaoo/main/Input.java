package top.buaaoo.main;
import java.util.regex.*;
import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;

class expHandler{
	static void err(String Order){
		System.out.println("INVALID"+"["+Order+"]");
	}
}
public class Input {
	/**
     * @Overview:读入类，拥有处理输入的功能。
     */
	private double[] Time = new double[1000];
	private int[] Floor = new int[1000];
	private int[] Method = new int[1000];
	private int Num = 0;
	
	public boolean repOK(){
		/**@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		*/
		return true;
	}
	
	void setTime(double[] t) {
		/**
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: Time;
		 * 
		 * @ EFFECTS: Time == t;
		 */
		Time = t;
	}
	
	double[] getTime() {
		/**
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: \result == Time;
		 */
		return Time;
	}

	void setFloor(int[] t) {
		/**
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: Floor;
		 * 
		 * @ EFFECTS: Floor == t;
		 */
		Floor = t;
	}

	int[] getFloor() {
		/**
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: \result == Floor;
		 */
		return Floor;
	}

	void setMethod(int[] t) {
		/**
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: Method;
		 * 
		 * @ EFFECTS: Method == t;
		 */
		Method = t;
	}
	int[] getMethod() {
		/**
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: \result == Method;
		 */
		return Method;
	}

	void setNum(int t) {
		/**
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: Num;
		 * 
		 * @ EFFECTS: Num == t;
		 */
		Num = t;
	}
	int getNum() {
		/**
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: \result == Num;
		 */
		return Num;
	}


	void Inputhandle() {
		/**
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: Time, Floor, Method, Num;
		 * 
		 * @ EFFECTS:temp != -1 ==> \this.size == \old(\this).size+1;
		 * 			  Num++;
		 */
		Scanner scanner = new Scanner(System.in);
		String Order = null;
		String ordcp = null;
		String floor = null;
		String time = null;
		Request request = new Request();
		int temp;
		int num = 0;
			do {
				num++;
				Order = scanner.nextLine();
				ordcp = Order;
				Order = Order.replaceAll(" ", "");
				temp = request.MatchD(Order,Num);
				Order = Order.replaceAll("\\+", "");
				Pattern p = Pattern.compile("(\\d+)");
				Matcher m = p.matcher(Order);
				if(temp == -1) {
					expHandler.err(ordcp);
				}else if(temp == 0) {
				}
				else{
					try {
						
					
							Method[Num] = temp;
							if(m.find()) {
								floor = m.group(1);
								Floor[Num] = Integer.parseInt(floor);
			//					Order.replaceFirst(floor, "");
							}
							if(m.find()) {
								time = m.group(1);
								Time[Num] = Double.parseDouble(time);
							}
							if(Num!=0&&(Time[Num]<Time[Num-1])) expHandler.err(Order);
							else if(Time[Num]>Math.pow(2, 32)-1) expHandler.err(Order);
							else if((Floor[Num]>10)||(Floor[Num]<1)) expHandler.err(Order);
							else if((Floor[Num]==10)&&(Method[Num]==2)) expHandler.err(Order);
							else if((Floor[Num]==1)&&(Method[Num]==1)) expHandler.err(Order);
							else Num++;
					} catch (Exception e) {
						expHandler.err(ordcp);
					}
				}
				if(num>100) {
					System.out.println("#输入大于100行，不等待RUN并进行运算");
					break;
				}
			}while(temp != 0);
			scanner.close();
		return;
	} 
}
