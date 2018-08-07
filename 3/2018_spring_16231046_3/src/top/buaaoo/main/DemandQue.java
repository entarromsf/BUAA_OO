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
public class DemandQue {
	private double[] Time = new double[1000];
	private int[] Floor = new int[1000];
	private int[] Method = new int[1000];
	private int Num = 0;
	
	double[] getTime() {
		return Time;
	}


	int[] getFloor() {
		return Floor;
	}


	int[] getMethod() {
		return Method;
	}


	int getNum() {
		return Num;
	}


	void Input() {
		Scanner scanner = new Scanner(System.in);
		String Order = null;
		String floor = null;
		String time = null;
		int temp;
		int num = 0;
		try {
			
		
		do {
				num++;
				Order = scanner.nextLine();
				Order = Order.replaceAll(" ", "");
				temp = MatchD(Order,Num);
				Order = Order.replaceAll("\\+", "");
				Pattern p = Pattern.compile("(\\d+)");
				Matcher m = p.matcher(Order);
				if(temp == -1) {
					expHandler.err(Order);
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
						expHandler.err(Order);
					}
				}
				if(num>100) {
					System.out.println("#输入大于100行，不等待RUN并进行运算");
					break;
				}
			}while(temp != 0);
			scanner.close();
		} catch (Exception e) {
		}
		return;
	}
	
	
    int MatchD(String str,int Num)
	{
		String regex1 = "\\(FR,[+]?\\d+,DOWN,[+]?\\d+\\)";
		String regex2 = "\\(FR,[+]?\\d+,UP,[+]?\\d+\\)";
		String regex3 = "\\(ER,[+]?\\d+,[+]?\\d+\\)";
		String regex4 = "RUN";
		boolean b1,b2,b3,b4;
		if (Num==0) {
			b1 = false;
			b2 = str.matches("\\(FR,[+]?[0]?1,UP,[+]?[0]+\\)");
			b3 = false;
			b4 = false;
		}else {
			b1 = str.matches(regex1);
			b2 = str.matches(regex2);
			b3 = str.matches(regex3);
			b4 = str.matches(regex4);
		}	
		if(b1) {
			return 1;
		}else if(b2) {
			return 2;
		}else if(b3) {
			return 3;
		}else if(b4){
			return 0;
		}else{
			return -1;
		}
	}
}
