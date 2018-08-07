package taxi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Vector;

public class Map {
	public int chagenum = 0;
	int[][] mapinfo=new int[80][80];
	int[][] graph = new int[6405][6405];// 邻接矩阵
	public void readmap(String path){//读入地图信息
		//Requires:String类型的地图路径,System.in
		//Modifies:System.out,map[][]
		//Effects:从文件中读入地图信息，储存在map[][]中
		Scanner scan=null;
		File file=new File(path);
		if(file.exists()==false){
			System.out.println("地图文件不存在,程序退出");
			System.exit(1);
			return;
		}
		try {
			scan = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			
		}
		for(int i=0;i<80;i++){
			String[] strArray = null;
			String strin = null;
			try{
		//		strArray=scan.nextLine().split("");
				strin = scan.nextLine();
				strin = strin.replaceAll("[ \\t]", "");
				strArray=strin.split("");
			}catch(Exception e){
				System.out.println("地图文件信息有误，程序退出");
				System.exit(1);
			}
			for(int j=0;j<80;j++){
				try{
					this.mapinfo[i][j]=Integer.parseInt(strArray[j]);
				}catch(Exception e){
					System.out.println("地图文件信息有误，程序退出");
					System.exit(1);
				}
			}
		}
		scan.close();
	}
	
	public void initmatrix() {// 初始化邻接矩阵
		// Requires:无
		// Modifies:graph[][]
		// Effects:对邻接矩阵赋初值
		int MAXNUM = gv.MAXNUM;
		for (int i = 0; i < 6400; i++) {
			for (int j = 0; j < 6400; j++) {
				if (i == j)
					graph[i][j] = 0;
				else
					graph[i][j] = MAXNUM;
			}
		}
		for (int i = 0; i < 80; i++) {
			for (int j = 0; j < 80; j++) {
				if (mapinfo[i][j] == 1 || mapinfo[i][j] == 3) {
					graph[i * 80 + j][i * 80 + j + 1] = 1024;
					graph[i * 80 + j + 1][i * 80 + j] = 1024;
				}
				if (mapinfo[i][j] == 2 || mapinfo[i][j] == 3) {
					graph[i * 80 + j][(i + 1) * 80 + j] = 1024;
					graph[(i + 1) * 80 + j][i * 80 + j] = 1024;
				}
			}
		}
	}
	
	public void openRoad(int i,int j) {
		 /*
         * @REQUIRES:
         *      0<= i <=6399;
         *      0<= j <=6399;
         * @MODIFIES:
         *      \this.graph;
         * @EFFECTS:
         *      如果i与j邻接,则
         *          \this.graph[i][j] = 1024;
         *          \this.graph[j][i] = 1024;
         *      否则不对graph做任何操作
         *
         */
		if(i-j!=-1 && i-j!= 1 && i-j!=80 && i-j!=-80) return;
		if(graph[i][j] >= 1024 && graph[i][j] < gv.MAXNUM) {
			System.out.println("Already Open");
		}else {
			graph[i][j] = 1024;
			graph[j][i] = 1024;
			System.out.println("Road Open");
		}
		
	}
	
	public void setFlow(int x1,int y1,int x2,int y2,int flow) {
		/*
         * @REQUIRES:
         *      0<= x1 <=79;
         *      0<= x2 <=79;
         *      0<= y1 <=79;
         *      0<= y2 <=79;
         * @MODIFIES:
         *      \this.graph;
         * @EFFECTS:
         *      如果两点间有路,则修改流量
         *      否则不对graph做任何操作
         *
         */
		int t1,t2;
		t1 = x1*80+y1;
		t2 = x2*80+y2;
		if(graph[t1][t2] >= 1024 && graph[t1][t2] < gv.MAXNUM) {
			graph[t1][t2] = 1024 + flow;
			graph[t2][t1] = 1024 + flow;
		}else {
			System.out.println("Wrong Flow Change");
		}
	}
	
	
	public void closeRoad(int i,int j) {
		/*
         * @REQUIRES:
         *      0<= i <=6399;
         *      0<= j <=6399;
         * @MODIFIES:
         *      \this.graph;
         * @EFFECTS:
         *      \this.graph[i][j] = gv.MAXNUM;
         *      \this.graph[j][i] = gv.MAXNUM;
         *
         */
		graph[i][j] = gv.MAXNUM;
		graph[j][i] = gv.MAXNUM;
		System.out.println("Road Close");
	}
	
//	public boolean checkway(int now,int next) {
//		if(graph[now][next]!=1) return false;
//		else return true;
//	}
	
//	public int[] pointbfs(int root, int aim) {// 单点广度优先搜索
//		
//		int[] offset = new int[] { 0, 1, -1, 80, -80 };
//		Vector<node> queue = new Vector<node>();
//		boolean[] view = new boolean[6405];
//		int D[] = new int[6405];
//		
//		int x = root;// 开始进行单点bfs
//		for (int i = 0; i < 6400; i++)
//			view[i] = false;
//		queue.add(new node(x, 0));
//		while (queue.size() > 0) {
//			node n = queue.get(0);
//			view[n.NO] = true;
//			for (int i = 1; i <= 4; i++) {
//				int next = n.NO + offset[i];
//				if (next >= 0 && next < 6400 && view[next] == false && graph[n.NO][next] == 1) {
//					view[next] = true;
//					queue.add(new node(next, n.depth + 1));// 加入遍历队列
//					D[next] = n.depth + 1;
//				}
//			}
//			queue.remove(0);// 退出队列
//		}
//		
//		int road[] = new int[D[aim]+1];
//		int now = aim;
//		int nowdepth = D[aim]-1;
//		
//		while(nowdepth>0) {
//			for (int i = 1; i <= 4; i++) {
//				int next = now + offset[i];
//				if(next >= 0 && next < 6400 && graph[now][next]==1 && D[next]==nowdepth) {
//					now = next;
//					road[nowdepth] = next;
//					nowdepth--;
//					break;
//				}
//			}
//		}
//		
//		road[0] = root;
//		road[D[aim]] = aim;
//		
//		return road;
//	}

//	public int[] SPFA(int _start, int _end) {
//
//        int start = _end;//反转起始点与终止点,从终点开始找最短路径
//        int begin = _start;
//        int[] offset = new int[] { 0, 1, -1, 80, -80 };
//        boolean[] inQ = new boolean[6400];//inQ[i]表示点i是否在队列中
//        int[] dis = new int[6400];//距离数组,表示点i到起点的距离
//        int[] flow = new int[6400];//流量数组,表示点i到起点的流量之和
//        int[] next = new int[6400];//next[i]表示点i的下一个点
//        Queue<Integer> queue = new LinkedList<>();
//        for (int i = 0; i < 6400; i++) {
//            dis[i] = Integer.MAX_VALUE;
//            flow[i] = Integer.MAX_VALUE;
//        }
//        inQ[start] = true;
//        dis[start] = 0;
//        flow[start] = 0;
//        queue.offer(start);
//        int now, cost, sumFlow;
//        while (!queue.isEmpty()) {
//            now = queue.poll();
//
//            for (int i = 1; i <= 4; i++) {
//                int nextt = now + offset[i];
//                if(nextt >= 0 && nextt < 6400 && graph[now][nextt]==1){
//                    cost = 1000 + dis[now] + guigv.GetFlow(now/80, now%80, nextt/80, nextt%80);
//                    sumFlow = flow[now] + guigv.GetFlow(now/80, now%80, nextt/80, nextt%80);
//                    if (dis[i] > cost || (dis[i] == cost &&
//                            flow[i] > sumFlow)) {
//                        next[i] = now;
//                        dis[i] = cost;
//                        flow[i] = sumFlow;
//                        if (!inQ[i]) {
//                            inQ[i] = true;
//                            queue.offer(i);
//                        }
//                    }
//                }
//            }
//            inQ[now] = false;
//        }
//        
//        int road[] = new int[6400];
//        int i = 1;
//        road[0] = _start;
//        while(begin != _end){
//            road[i] = next[begin];
//            i++;
//            begin = next[begin];
//        }
//        road[i] = _end;
//        return road;
//    }
	
	public int mindis(int root, int aim) {
		/*
         * @REQUIRES:
         *      0<=root<=6399;
         *      0<=aim<=6399;
         * @MODIFIES:None;
         * @EFFECTS:
         *      使用dfs算法找到最近的出租车
         *      \result == D[aim];
         *
         */
		
		int[] offset = new int[] { 0, 1, -1, 80, -80 };
		Vector<node> queue = new Vector<node>();
		boolean[] view = new boolean[6405];
		int D[] = new int[6405];
		
		int x = root;// 开始进行单点bfs
		for (int i = 0; i < 6400; i++)
			view[i] = false;
		queue.add(new node(x, 0));
		while (queue.size() > 0) {
			node n = queue.get(0);
			view[n.NO] = true;
			for (int i = 1; i <= 4; i++) {
				int next = n.NO + offset[i];
				if (next >= 0 && next < 6400 && view[next] == false && graph[n.NO][next] >= 1024 && graph[n.NO][next]<gv.MAXNUM) {
					view[next] = true;
					queue.add(new node(next, n.depth + 1));// 加入遍历队列
					D[next] = n.depth + 1;
				}
			}
			queue.remove(0);// 退出队列
		}
		
		return D[aim];
	}
	
	public synchronized void addFlow(int p1, int p2) {
		/*
         * @REQUIRES:
         *      0<=p1<=6399;
         *      0<=p2<=6399;
         * @MODIFIES:
         *      \this.graph;
         * @EFFECTS:
         * 		graph[p1][p2]++;
         * 		graph[p2][p1]++;
         * @THREAD_REQUIRES:
         *      \locked(\this);
         * @THREAD_EFFECTS:
         *      \locked();
         *
         */
		graph[p1][p2]++;
		graph[p2][p1]++;
	}
	
	public synchronized void clearFlow(int p1, int p2) {
		/*
         * @REQUIRES:
         *      0<=p1<=6399;
         *      0<=p2<=6399;
         * @MODIFIES:
         *      \this.graph;
         * @EFFECTS:
         * 		graph[p1][p2]--;
         * 		graph[p2][p1]--;
         * @THREAD_REQUIRES:
         *      \locked(\this);
         * @THREAD_EFFECTS:
         *      \locked();
         *
         */
		if(graph[p1][p2]<gv.MAXNUM && graph[p1][p2]>1024) {
			graph[p1][p2]--;// = 1024;
			graph[p2][p1]--;// = 1024;
		}
	}
	
	public int[] pointbfs(int root, int aim) {
		/*
         * @REQUIRES:
         *      0<=root<=6399;
         *      0<=aim<=6399;
         * @MODIFIES:None;
         * @EFFECTS:
         *      使用dj算法，将流量作为权重的一部分，找出流量最小的最短路径
         *      \result == road;
         *
         */
		
		int[] prev = new int[6400];
		int[] dist = new int[6400];
		boolean[] flag = new boolean[6400];
		for (int i = 0; i < 6400; i++) {
			flag[i] = false;
			prev[i] = root;
			dist[i] = graph[root][i];
		}
		
		flag[root] = true;
		dist[root] = 0;
		
		int k = 0;
		for (int i = 1; i < 6400; i++) {
			int min = gv.MAXNUM;
			for (int j = 0; j < 6400; j++) {
				if (!flag[j] && dist[j] < min) {
					min = dist[j];
					k = j;
				}
			}
			
			flag[k] = true;
			
			for(int j = 0; j < 6400; j++) {
				int tmp = (graph[k][j] == gv.MAXNUM ? gv.MAXNUM : (min + graph[k][j]));
				if (!flag[j] && (tmp < dist[j])) {
					dist[j] = tmp;
					prev[j] = k;
				}
			}
			if(flag[aim]) {
				break;
			}	
		}
		
		int npoint = aim;
		int ndepth = (dist[aim]>>10) + 1;
		int[] road = new int[ndepth];
		road[--ndepth] = npoint;
		while(ndepth > 0) {
			npoint = prev[npoint];
			road[--ndepth] = npoint;
		}
		return road;
	}
	

}
