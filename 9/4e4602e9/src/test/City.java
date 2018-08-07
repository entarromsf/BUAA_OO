package test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class City {
	boolean[][] graph = new boolean[6400][4]; //右下左上
	int[][] graph_weight = new int[6400][4];
	private TaxiGUI gui;
	
	public City (TaxiGUI gui) {
		this.gui=gui;
	}
	
	public  void init(int[][] map) {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:graph;
		 * @EFFECTS:None;
		 */
		int max=80;
		for(int i=0;i<max;i++) {
			for(int j=0;j<max;j++) {
				if(map[i][j]==1) {
					graph[80*i+j][0]=true;
					graph[80*i + j + 1][2] = true;
				}
				else if (map[i][j]==2){
					graph[80*i + j][1] = true;
					graph[80*(i+1) + j][3] = true;
				}
				else if(map[i][j]==3) {
					graph[80*i + j][0] = true;
					graph[80*i + j][1] = true;
					graph[80*i + j + 1][2] = true;
					graph[80*(i+1) + j][3] = true;
				}
				else continue;
			}
		}
		/*
		for(int i=0;i<80*80;i++) {
			for(int j=0;j<4;j++) {
				if(graph[i][j]) System.out.printf("1 ");
				else System.out.printf("0 ");
			}
			System.out.println();
		}
		*/
	}
	
	
	public void Road(int x1,int y1,int x2,int y2,int op) {
		/**
		 * @REQUIRES:(\all x1,x2,y1,y2,op;0=<x1<80,0<=x2<80,0<=y1<80,0<=y2<80,op=1|op=0);
		 * @MODIFIES:graph
		 * @EFFECTS:None;
		 * 
		 */
		int temp1 = x1*80+y1;
		int temp2 = x2*80+y2;
		int i=3;
		int j=1;
		if(temp2-temp1==1) {
			i=0;
			j=2;
		}
		else if(temp2-temp1==80) {
			i=1;
			j=3;
		}
		else if(temp2-temp1==-1) {
			i=2;
			j=0;
		}
		else {
			System.out.println("the point is wrong");
			return ;
		}
		if(op==1) { //加路
			graph[temp1][i]=true;
			graph[temp2][j]=true;
			gui.SetRoadStatus(new Point(x1,y1),new Point(x2,y2),1);
		}
		else {
			graph[temp1][i]=false;
			graph[temp2][j]=false;
			gui.SetRoadStatus(new Point(x1,y1),new Point(x2,y2), 0);
		}
	}
	public boolean[] getmap(int src) {
		/**
		 * @REQUIRES:(\all src;0<=src<6400)
		 * @MODIFIES:None;
		 * @EFFECTS:(\result = graph[src])
		 */
		return graph[src];
	}
	public int[] getweight(int src) {
		/**
		 * @REQUIRES:(\all src;0<=src<6400)
		 * @MODIFIES:None;
		 * @EFFECTS: (\result = graph_weight[src])
		 */
		return graph_weight[src];
	}
	public int getmixweight(int src) {
		/**
		 * @REQUIRES:(\all src;0<=src<6400)
		 * @MODIFIES:None;
		 * @EFFECTS:(\exist j; graph_weight[i] =\min graph_weight ; \result=j)
		 */
		ArrayList<Integer> list = new ArrayList<Integer>();		
		boolean[] flag =new  boolean[4];
		for(int i=0;i<4;i++) {
			if(graph[src][i]) {
				flag[i] =true;
			}
		}

		int max_w =99999;
		for(int i=0;i<4;i++) {
			if(flag[i]) {
				if( graph_weight[src][i] < max_w ) {
					list.clear();
					list.add(i);
					max_w = graph_weight[src][i];
				}
				else if(graph_weight[src][i] == max_w) {
					list.add(i);
				}
			}
		}
		Random rand = new Random();
		int j = rand.nextInt(list.size());
		return list.get(j);
	}
	
	public void change_weight(int src,int i) {
		/**
		 * @REQUIRES:(\all i,src;0<=src<6400,0<=i<4)
		 * @MODIFIES:graph_weight;
		 * @EFFECTS:None;
		 */
		this.graph_weight[src][i]++;
	}
	
	public void set_flow(int x1,int y1,int x2,int y2,int num) {
		/**
		 * @REQUIRES:(\all x1,x2,y1,y2;0=<x1<80,0<=x2<80,0<=y1<80,0<=y2<80);
		 * @MODIFIES:graph_weight;
		 * @EFFECTS:None;
		 */
		int temp1 = x1*80+y1;
		int temp2 = x2*80+y2;
		int i=3;
		int j=1;
		if(temp2-temp1==1) {
			i=0;
			j=2;
		}
		else if(temp2-temp1==80) {
			i=1;
			j=3;
		}
		else if(temp2-temp1==-1) {
			i=2;
			j=0;
		}
		else {
			System.out.println("the  point is wrong");
			return;
		}
		graph_weight[temp1][i]=num;
		graph_weight[temp2][j]=num;
	}
}
