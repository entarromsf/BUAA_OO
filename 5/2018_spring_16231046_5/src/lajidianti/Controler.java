package lajidianti;
import java.util.regex.*;
import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controler {
	protected double RunTime = 0;
	protected String status = null;
	protected int InitBu = 200;
	void schedule (DemandQue De,Floor F,Elevato E) {
		double EleTime[] = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		double UpTime[] = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		double DownTime[] = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		int Num = De.getNum();
		double[] Time = De.getTime();
		int[] Floor = De.getFloor();
		int[] Method = De.getMethod();
		int i = 0;
		elevat Elevato = new elevat() {
			
			String status = "STILL";
			public String getstatus() {
				return status;
			}
			public void change(String str) {
				status = str;
			}
		}; 
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
	double Max(double i, double j) {
		if(i > j) return i;
		else return j;
	}
}

class Controler_incidentally extends Controler{
	protected String status = "STILL";
	void schedule (DemandQue De,Floor F,Elevato E)  {
		
		int EleBu[] = new int[1000];
		int UpBu[] = new int[1000];
		int DownBu[] = new int[1000];
		int Num = De.getNum();
		double[] Time = De.getTime();
		int[] Floor = De.getFloor();
		int[] Method = De.getMethod();
		int nextIn = 0;
		int Elemin = 0,Upmin = 0,Downmin = 0;
		int Outputnum = 0;
		int dooropen = 0,UP=0,DOWN=0,ELE=0;
		int DST = Floor[0];
		boolean UPsDOWNb = false,DOWNsUPb = false;
		Arrays.fill(EleBu, InitBu);
		Arrays.fill(UpBu, InitBu);
		Arrays.fill(DownBu, InitBu);
	try {
		
		
		do {
			if(Outputnum==Num) break;                                //instruct done
			
			
			for(int j = 0;j<Num;j++) {
				if(Time[j]==super.RunTime) {                   
					if(Method[j]==1) {                           //press the button
						if(DownBu[Floor[j]]!=InitBu||(DOWN!=0&&Floor[j]==F.getFloor())) {System.out.print("#SAME[FR,"+Floor[j]+",DOWN,");System.out.printf("%.0f]\n",Time[j]);Outputnum++;}
						else {DownBu[Floor[j]] = j;}
					}
					else if(Method[j]==2) {
						if(UpBu[Floor[j]]!=InitBu||(UP!=0&&Floor[j]==F.getFloor())) {System.out.println("#SAME[FR,"+Floor[j]+",UP,");System.out.printf("%.0f]\n",Time[j]);Outputnum++;}
						else {UpBu[Floor[j]] = j;}
					}
					else if(Method[j]==3) {
						if(EleBu[Floor[j]]!=InitBu||(ELE!=0&&Floor[j]==F.getFloor())) {System.out.println("#SAME[ER,"+Floor[j]+",");System.out.printf("%.0f]\n",Time[j]);Outputnum++;}
						else {EleBu[Floor[j]] = j;}
					}
				}	
			}
			
			if(dooropen==2) {
				super.RunTime += 0.5;
				if (ELE == 2) ELE = 1;
				if (DOWN == 2) DOWN = 1;
				if (UP == 2) UP = 1;
				dooropen = 1;		
				continue;
			}
			if(dooropen==1) {
				dooropen = 0;
				ELE = 0;
				DOWN = 0;
				UP = 0;
			}
			
					
			if(status == "STILL") {
				Elemin = FindMin(EleBu, 1);
				Upmin = FindMin(UpBu, 1);
				Downmin = FindMin(DownBu, 1);
				int first=Threemin(Elemin, Upmin, Downmin);
				if (first == InitBu) {
					super.RunTime = FindnextTime(Time, Num);
					continue;
				}
				else {
					if(F.getFloor()==Floor[first]) {
						if(Method[first]==1) {DownBu[F.getFloor()] = InitBu;DOWN = 2;}
						else if(Method[first]==2) {UpBu[F.getFloor()] = InitBu;UP = 2;}
						else {EleBu[F.getFloor()] = InitBu;ELE = 2;}
						dooropen = 2;
						super.RunTime += 0.5;              ////////output 
						toStringstill(Floor, Method, Time, first);
						Outputnum++;
						status = "STILL";
						DST = Floor[first];
						continue;
					}
					else if(F.getFloor()>Floor[first]) {
						F.down();
						status = "DOWN";
						super.RunTime += 0.5;
						DST = Floor[first];
						if(Method[first]==2) DOWNsUPb = true;
						continue;
					}
					else {
						F.up();
						status = "UP";
						super.RunTime += 0.5;
						DST = Floor[first];
						if(Method[first]==1) UPsDOWNb = true;
						continue;
					}
				}
			}
			else if(status == "STILLUP") {
				if(DST==F.getFloor()) {
					int nextEleBu = FindEleBuUP(EleBu, Time, F.getFloor());
					if(nextEleBu!=InitBu) {
						DST = nextEleBu;
						status = "UP";
						F.up();
						super.RunTime += 0.5;
						continue;
					}
					else {                         //SAME AS STILL
					///////////////////////////////////////////	
						Elemin = FindMin(EleBu, 1);
						Upmin = FindMin(UpBu, 1);
						Downmin = FindMin(DownBu, 1);
						int first=Threemin(Elemin, Upmin, Downmin);
						if (first == InitBu) {
							super.RunTime = FindnextTime(Time, Num);
							continue;
						}
						else {
							if(F.getFloor()==Floor[first]) {
								if(Method[first]==1) {DownBu[F.getFloor()] = InitBu;DOWN = 2;}
								else if(Method[first]==2) {UpBu[F.getFloor()] = InitBu;UP = 2;}
								else {EleBu[F.getFloor()] = InitBu;ELE = 2;}
								dooropen = 2;
								super.RunTime += 0.5;              ////////output
								status = "STILL";
								toStringstill(Floor, Method, Time, first);
								Outputnum++;
								DST = Floor[first];
								continue;
							}
							else if(F.getFloor()>Floor[first]) {
								F.down();
								status = "DOWN";
								super.RunTime += 0.5;
								DST = Floor[first];
								if(Method[first]==2) DOWNsUPb = true;
								continue;
							}
							else {
								F.up();
								status = "UP";
								super.RunTime += 0.5;
								DST = Floor[first];
								if(Method[first]==1) UPsDOWNb = true;
								continue;
							}
						}
					////////////////////////					
					}
				}
				else {
					status = "UP";
					F.up();
					super.RunTime += 0.5;
					continue;
				}
			}
			else if(status == "STILLDOWN") {
				if(DST==F.getFloor()) {
					int nextEleBu = FindEleBuDOWN(EleBu, Time, F.getFloor());
					if(nextEleBu!=InitBu) {
						DST = nextEleBu;
						status = "DOWN";
						F.down();
						super.RunTime += 0.5;
						continue;
					}
					else {                         //SAME AS STILL
					///////////////////////////////////////////	
						Elemin = FindMin(EleBu, 1);
						Upmin = FindMin(UpBu, 1);
						Downmin = FindMin(DownBu, 1);
						int first=Threemin(Elemin, Upmin, Downmin);
						if (first == InitBu) {
							super.RunTime = FindnextTime(Time, Num);
							continue;
						}
						else {
							if(F.getFloor()==Floor[first]) {
								if(Method[first]==1) {DownBu[F.getFloor()] = InitBu;DOWN = 2;}
								else if(Method[first]==2) {UpBu[F.getFloor()] = InitBu;UP = 2;}
								else {EleBu[F.getFloor()] = InitBu;ELE = 2;}
								dooropen = 2;
								super.RunTime += 0.5;              ////////output 
								status = "STILL";
								toStringstill(Floor, Method, Time, first);
								Outputnum++;
								DST = Floor[first];
								continue;
							}
							else if(F.getFloor()>Floor[first]) {
								F.down();
								status = "DOWN";
								super.RunTime += 0.5;
								DST = Floor[first];
								if(Method[first]==2) DOWNsUPb = true;
								continue;
							}
							else {
								F.up();
								status = "UP";
								super.RunTime += 0.5;
								DST = Floor[first];
								if(Method[first]==1) UPsDOWNb = true;
								continue;
							}
						}
					////////////////////////					
					}
				}
				else {
					status = "DOWN";
					F.down();
					super.RunTime += 0.5;
					continue;
				}
			}
			else if(status == "UP") { /////////////////////////////////UP
				if((DST==F.getFloor())&&UPsDOWNb) {
					UPsDOWNb = false;
					toString(Floor, Method, Time, DownBu[F.getFloor()]);
					Outputnum++;
					DownBu[F.getFloor()] = InitBu;
					DOWN = 2;
					if(((UpBu[F.getFloor()]!=InitBu)&&(Time[UpBu[F.getFloor()]]!=RunTime))||((EleBu[F.getFloor()]!=InitBu)&&(Time[EleBu[F.getFloor()]]!=RunTime))) {
						if(UpBu[F.getFloor()]<EleBu[F.getFloor()]) {					
							toString(Floor, Method, Time, UpBu[F.getFloor()]);
							Outputnum++;
							UpBu[F.getFloor()] = InitBu;                                           ///output
							UP = 2;
							if(((Time[EleBu[F.getFloor()]]<=RunTime+1) && (Time[EleBu[F.getFloor()]]>=RunTime))||EleBu[F.getFloor()]==InitBu) {
								dooropen = 2;
								super.RunTime += 0.5;
								status = "STILLUP";
								continue;
							}
							else {
								toString(Floor, Method, Time, EleBu[F.getFloor()]);
								Outputnum++;
								EleBu[F.getFloor()] = InitBu;
								ELE = 2;
								dooropen = 2;
								super.RunTime += 0.5;
								status = "STILLUP";
								continue;
							}
							
						}
						else{
							toString(Floor, Method, Time, EleBu[F.getFloor()]);
							Outputnum++;
							EleBu[F.getFloor()] = InitBu;
							ELE = 2;
							if(((Time[UpBu[F.getFloor()]]<=RunTime+1) && (Time[UpBu[F.getFloor()]]>=RunTime))||UpBu[F.getFloor()]==InitBu) {
								dooropen = 2;
								super.RunTime += 0.5;
								status = "STILLUP";
								continue;
							}
							else {
								toString(Floor, Method, Time, UpBu[F.getFloor()]);
								Outputnum++;
								UpBu[F.getFloor()] = InitBu;
								UP = 2;
								dooropen = 2;
								super.RunTime += 0.5; 
								status = "STILLUP";
								continue;
							}
						}
					}
					else {
						dooropen = 2;
						super.RunTime += 0.5;
						status = "STILLUP";
						continue;
					}
				}
				else if(((UpBu[F.getFloor()]!=InitBu)&&(Time[UpBu[F.getFloor()]]!=RunTime))||((EleBu[F.getFloor()]!=InitBu)&&(Time[EleBu[F.getFloor()]]!=RunTime))) {
					if(UpBu[F.getFloor()]<=EleBu[F.getFloor()]) {					
						toString(Floor, Method, Time, UpBu[F.getFloor()]);
						Outputnum++;
						UpBu[F.getFloor()] = InitBu;                                           ///open door && output
						UP = 2;
						if(((Time[EleBu[F.getFloor()]]<=RunTime+1) && (Time[EleBu[F.getFloor()]]>=RunTime))||EleBu[F.getFloor()]==InitBu) {
							dooropen = 2;
							super.RunTime += 0.5;
							status = "STILLUP";
							continue;
						}
						else {
							toString(Floor, Method, Time, EleBu[F.getFloor()]);
							Outputnum++;
							EleBu[F.getFloor()] = InitBu;
							ELE = 2;
							dooropen = 2;
							super.RunTime += 0.5;
							status = "STILLUP";
							continue;
						}
						
					}
					else{
						toString(Floor, Method, Time, EleBu[F.getFloor()]);
						Outputnum++;
						EleBu[F.getFloor()] = InitBu;
						ELE = 2;
						if(((Time[UpBu[F.getFloor()]]<=RunTime+1) && (Time[UpBu[F.getFloor()]]>=RunTime))||UpBu[F.getFloor()]==InitBu) {
							dooropen = 2;
							super.RunTime += 0.5;
							status = "STILLUP";
							continue;
						}
						else {
							toString(Floor, Method, Time, UpBu[F.getFloor()]);
							Outputnum++;
							UpBu[F.getFloor()] = InitBu;
							UP = 2;
							dooropen = 2;
							super.RunTime += 0.5; 
							status = "STILLUP";
							continue;
						}
					}
				}
				else {
					super.RunTime += 0.5;
					F.up();
					continue;
				}
			}
			else            //(status == "DOWN")
			{
				if((DST==F.getFloor())&&DOWNsUPb) {
					DOWNsUPb = false;
					toString(Floor, Method, Time, UpBu[F.getFloor()]);
					Outputnum++;
					UpBu[F.getFloor()] = InitBu;
					UP = 2;
					if(((DownBu[F.getFloor()]!=InitBu)&&(Time[DownBu[F.getFloor()]]!=RunTime))||((EleBu[F.getFloor()]!=InitBu)&&(Time[EleBu[F.getFloor()]]!=RunTime))) {
						if(DownBu[F.getFloor()]<=EleBu[F.getFloor()]) {					
							toString(Floor, Method, Time, DownBu[F.getFloor()]);
							Outputnum++;
							DownBu[F.getFloor()] = InitBu;                                            ///open door && output
							DOWN = 2;
							if(((Time[EleBu[F.getFloor()]]<=RunTime+1) && (Time[EleBu[F.getFloor()]]>=RunTime))||EleBu[F.getFloor()]==InitBu) {
								dooropen = 2;
								super.RunTime += 0.5;
								status = "STILLDOWN";
								continue;
							}
							else {
								toString(Floor, Method, Time, EleBu[F.getFloor()]);
								Outputnum++;
								EleBu[F.getFloor()] = InitBu;
								ELE = 2;
								dooropen = 2;
								super.RunTime += 0.5;
								status = "STILLDOWN";
								continue;
							}
						}
						else{
							toString(Floor, Method, Time, EleBu[F.getFloor()]);
							Outputnum++;
							EleBu[F.getFloor()] = InitBu;
							ELE = 2;
							if(((Time[DownBu[F.getFloor()]]<=RunTime+1) && (Time[DownBu[F.getFloor()]]>=RunTime))||DownBu[F.getFloor()]==InitBu) {
								dooropen = 2;
								super.RunTime += 0.5;
								status = "STILLDOWN";
								continue;
							}
							else {
								toString(Floor, Method, Time, DownBu[F.getFloor()]);
								Outputnum++;
								DownBu[F.getFloor()] = InitBu;
								DOWN = 2;
								dooropen = 2;
								super.RunTime += 0.5; 
								status = "STILLDOWN";
								continue;
							}
						}
					}
					else {
						dooropen = 2;
						super.RunTime += 0.5;
						status = "STILLDOWN";
						continue;
					}
				}
				else if(((DownBu[F.getFloor()]!=InitBu)&&(Time[DownBu[F.getFloor()]]!=RunTime))||((EleBu[F.getFloor()]!=InitBu)&&(Time[EleBu[F.getFloor()]]!=RunTime))) {
					if(DownBu[F.getFloor()]<=EleBu[F.getFloor()]) {					
						toString(Floor, Method, Time, DownBu[F.getFloor()]);
						Outputnum++;
						DownBu[F.getFloor()] = InitBu;                                            ///open door && output
						DOWN = 2;
						if(((Time[EleBu[F.getFloor()]]<=RunTime+1) && (Time[EleBu[F.getFloor()]]>=RunTime))||EleBu[F.getFloor()]==InitBu) {
							dooropen = 2;
							super.RunTime += 0.5;
							status = "STILLDOWN";
							continue;
						}
						else {
							toString(Floor, Method, Time, EleBu[F.getFloor()]);
							Outputnum++;
							EleBu[F.getFloor()] = InitBu;
							ELE = 2;
							dooropen = 2;
							super.RunTime += 0.5;
							status = "STILLDOWN";
							continue;
						}
					}
					else{
						toString(Floor, Method, Time, EleBu[F.getFloor()]);
						Outputnum++;
						EleBu[F.getFloor()] = InitBu;
						ELE = 2;
						if(((Time[DownBu[F.getFloor()]]<=RunTime+1) && (Time[DownBu[F.getFloor()]]>=RunTime))||DownBu[F.getFloor()]==InitBu) {
							dooropen = 2;
							super.RunTime += 0.5;
							status = "STILLDOWN";
							continue;
						}
						else {
							toString(Floor, Method, Time, DownBu[F.getFloor()]);
							Outputnum++;
							DownBu[F.getFloor()] = InitBu;
							DOWN = 2;
							dooropen = 2;
							super.RunTime += 0.5; 
							status = "STILLDOWN";
							continue;
						}
					}
				}
				else {
					super.RunTime += 0.5;
					F.down();
					continue;
				}
			}		
		}while(true);
	} catch (Exception e2) {
		
	}
	}
	
	int FindMin(int[] A,int begin){
		int min = A[begin];
		for (int i = begin;i <= 10;i++) {
			if(A[i]!=InitBu) {
				if(A[i]<min) min = A[i];
			}
		}
		return min;
	}
	
	int Threemin(int A,int B,int C) {
		if((A <= B)&&(A <= C)) {
			if(A!=InitBu) return A;
			else {
				if(B <= C) {
					if(B == InitBu) return C;
					else return B;
				}
				else {
					if(C == InitBu) return B;
					else return C;
				}
			}
		}
		else if((B <= A)&&(B <= C)) {
			if(B!=InitBu) return B;
			else {
				if(A <= C) {
					if(A == InitBu) return C;
					else return A;
				}
				else {
					if(C == InitBu) return A;
					else return C;
				}
			}
		}
		else {                             //((C <= B)&&(C <= A)) 
			if(C!=InitBu) return C;
			else {
				if(B <= A) {
					if(B == InitBu) return A;
					else return B;
				}
				else {
					if(A == InitBu) return B;
					else return A;
				}
			}
		}
	}
	int FindEleBuUP(int[] EleBu,double[] Time ,int floor) {
		for(int i = floor;i<=10;i++) {
			if((EleBu[i]!=InitBu)&&(Time[EleBu[i]]<RunTime-1)) return i;
		}
		return InitBu;
	}
	int FindEleBuDOWN(int[] EleBu,double[] Time ,int floor) {
		for(int i = floor;i>0;i--) {
			if((EleBu[i]!=InitBu)&&(Time[EleBu[i]]<RunTime-1)) return i;
		}
		return InitBu;
	}
	
	double FindnextTime(double[] Time,int Num) {
		for(int i = 0;i < Num;i++) {
			if(Time[i]>RunTime) return Time[i];
		}
		return -1;
	}
	void toString (int[] Floor,int[] Method,double[] Time,int nextIn) {
		if(Method[nextIn] == 1) {
			System.out.print("[FR,"+Floor[nextIn]+",DOWN,");
			System.out.printf("%.0f]/(",Time[nextIn]);
			System.out.print(Floor[nextIn]+","+status+",");
			System.out.printf("%.1f)\n",RunTime);
		}
		else if(Method[nextIn] == 2) {
			System.out.print("[FR,"+Floor[nextIn]+",UP,");
			System.out.printf("%.0f]/(",Time[nextIn]);
			System.out.print(Floor[nextIn]+","+status+",");
			System.out.printf("%.1f)\n",RunTime);
		}
		else {
			System.out.print("[ER,"+Floor[nextIn]+",");
			System.out.printf("%.0f]/(",Time[nextIn]);
			System.out.print(Floor[nextIn]+","+status+",");
			System.out.printf("%.1f)\n",RunTime);
		}
	}
	void toStringstill (int[] Floor,int[] Method,double[] Time,int nextIn) {
		double R = RunTime + 0.5;
		if(Method[nextIn] == 1) {
			System.out.print("[FR,"+Floor[nextIn]+",DOWN,");
			System.out.printf("%.0f]/(",Time[nextIn]);
			System.out.print(Floor[nextIn]+","+status+",");
			System.out.printf("%.1f)\n",R);
		}
		else if(Method[nextIn] == 2) {
			System.out.print("[FR,"+Floor[nextIn]+",UP,");
			System.out.printf("%.0f]/(",Time[nextIn]);
			System.out.print(Floor[nextIn]+","+status+",");
			System.out.printf("%.1f)\n",R);
		}
		else {
			System.out.print("[ER,"+Floor[nextIn]+",");
			System.out.printf("%.0f]/(",Time[nextIn]);
			System.out.print(Floor[nextIn]+","+status+",");
			System.out.printf("%.1f)\n",R);
		}
	}
}
