package taxi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Map {
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
					graph[i * 80 + j][i * 80 + j + 1] = 1;
					graph[i * 80 + j + 1][i * 80 + j] = 1;
				}
				if (mapinfo[i][j] == 2 || mapinfo[i][j] == 3) {
					graph[i * 80 + j][(i + 1) * 80 + j] = 1;
					graph[(i + 1) * 80 + j][i * 80 + j] = 1;
				}
			}
		}
	}
	
	public int[] pointbfs(int root, int aim) {// 单点广度优先搜索
		
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
				if (next >= 0 && next < 6400 && view[next] == false && graph[n.NO][next] == 1) {
					view[next] = true;
					queue.add(new node(next, n.depth + 1));// 加入遍历队列
					D[next] = n.depth + 1;
				}
			}
			queue.remove(0);// 退出队列
		}
		
		int road[] = new int[D[aim]+1];
		int now = aim;
		int nowdepth = D[aim]-1;
		
		while(nowdepth>0) {
			for (int i = 1; i <= 4; i++) {
				int next = now + offset[i];
				if(next >= 0 && next < 6400 && graph[now][next]==1 && D[next]==nowdepth) {
					now = next;
					road[nowdepth] = next;
					nowdepth--;
					break;
				}
			}
		}
		
		road[0] = root;
		road[D[aim]] = aim;
		
		return road;
	}
	
	public int mindis(int root, int aim) {// 单点广度优先搜索
		
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
				if (next >= 0 && next < 6400 && view[next] == false && graph[n.NO][next] == 1) {
					view[next] = true;
					queue.add(new node(next, n.depth + 1));// 加入遍历队列
					D[next] = n.depth + 1;
				}
			}
			queue.remove(0);// 退出队列
		}
		
		return D[aim];
	}
	
}
