package taxi;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Light extends Thread {
	/**
     * @Overview:红绿灯类能根据读入的文件初始化红绿灯系统并按周期变化。
     */

	int[][] light = new int[80][80];
	private long ctime;
	Map ma;

	public Light() {
		/*@ REQUIRES: None;
		  @ MODIFIES: ctime;
		  @ Effects: ctime = 500 + random.nextInt(1000);
		*/
		Random random = new Random();
		ctime = 500 + random.nextInt(1000);
	}

	public void setma(Map ma_t) {
		/*@ REQUIRES: None;
		  @ MODIFIES: ma;
		  @ Effects: ma = ma_t;
		*/
		ma = ma_t;
	}

	@Override
	public void run() {
		/*
		 * @ Requires: None;
		 * 
		 * @ Modifies: ctime;
		 * 
		 * @ Effects: normal behavior 周期改变红绿灯  出现异常 ==> exceptional_behavior (e);
		 * 
		 * @ THREAD_REQUIRES: \locked(this);
		 * 
		 * @ THREAD_EFFECTS: \locked();
		 */
		while (true) {
			try {
				changlight();
				sleep(ctime);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void readlight(String path) {//读入红绿灯信息
		//Requires:String类型的地图路径,System.in
		//Modifies:System.out,map[][]
		//Effects:从文件中读入红绿灯信息，储存在light[][]中
		Scanner scan = null;
		File file = new File(path);
		if (file.exists() == false) {
			System.out.println("红绿灯文件不存在,程序退出");
			System.exit(1);
			return;
		}
		try {
			scan = new Scanner(new File(path));
		} catch (FileNotFoundException e) {

		}
		for (int i = 0; i < 80; i++) {
			String[] strArray = null;
			String strin = null;
			try {
				strin = scan.nextLine();
				strin = strin.replaceAll("[ \\t]", "");
				strArray = strin.split("");
			} catch (Exception e) {
				System.out.println("红绿灯文件信息有误，程序退出");
				System.exit(1);
			}
			for (int j = 0; j < 80; j++) {
				try {
					if (wheadd(i, j)) {
						this.light[i][j] = Integer.parseInt(strArray[j]);
						Main.gui.SetLightStatus(new Point(i, j), this.light[i][j]);
					}
				} catch (Exception e) {
					System.out.println("红绿灯文件信息有误，程序退出");
					System.exit(1);
				}
			}
		}
		scan.close();
	}

	public void changlight() {
		/*@ REQUIRES: None;
		@ MODIFIES: this.light;
		@ Effects: (\all \old light[i][j]==1)==>(light[i][j] = 2);
				   (\all \old light[i][j]==2)==>(light[i][j] = 1);
		*/
		for (int i = 0; i < 80; i++) {
			for (int j = 0; j < 80; j++) {
				if (this.light[i][j] == 1) {
					this.light[i][j] = 2;
					Main.gui.SetLightStatus(new Point(i, j), this.light[i][j]);
				} else if (this.light[i][j] == 2) {
					this.light[i][j] = 1;
					Main.gui.SetLightStatus(new Point(i, j), this.light[i][j]);
				} else {
					continue;
				}
			}
		}
	}

	public boolean repOK() {
		/*@ REQUIRES: None;
		  @ MODIFIES: None;
		  @ Effects: \result == invariant(this);
		*/
		if (ctime > 1000 || ctime < 500)
			return false;
		return true;
	}

	public boolean wheadd(int i, int j) {
		/*@ REQUIRES: 0<=i<=79,0<=j<=79;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		*/
		int num = 0;
		if (i - 1 >= 0) {
			if (ma.graph[(i - 1) * 80 + j][i * 80 + j] >= 1024 && ma.graph[(i - 1) * 80 + j][i * 80 + j] < gv.MAXNUM) {
				num++;
			}
		}
		if (i + 1 < 80) {
			if (ma.graph[(i + 1) * 80 + j][i * 80 + j] >= 1024 && ma.graph[(i + 1) * 80 + j][i * 80 + j] < gv.MAXNUM) {
				num++;
			}
		}
		if (j - 1 >= 0) {
			if (ma.graph[i * 80 + j - 1][i * 80 + j] >= 1024 && ma.graph[i * 80 + j - 1][i * 80 + j] < gv.MAXNUM) {
				num++;
			}
		}
		if (j + 1 < 80) {
			if (ma.graph[i * 80 + j + 1][i * 80 + j] >= 1024 && ma.graph[i * 80 + j + 1][i * 80 + j] < gv.MAXNUM) {
				num++;
			}
		}
		if (num > 2) {
			return true;
		} else {
			return false;
		}
	}

}
