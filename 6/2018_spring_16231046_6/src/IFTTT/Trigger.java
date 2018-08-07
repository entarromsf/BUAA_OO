package IFTTT;

import java.io.File;
import java.util.Vector;

public class Trigger extends Thread {

	SafeFile file, parent;

	private int monitorNum, missionNum;

	private int summaryNum;

	private static int ren, mon, pan, sin;

	private Vector<SafeFile> AllFile = new Vector<SafeFile>();

	private Summary summary;

	private Detail detail;

	public Trigger(int monitor_t, int mission_t, String path_t, Summary sum_t, Detail de_t) {
		summaryNum = 0;
		ren = 0;
		mon = 0;
		pan = 0;
		sin = 0;
		monitorNum = monitor_t;
		missionNum = mission_t;
		file = new SafeFile(path_t);
		if (file.initcon()) {
			parent = new SafeFile(file.getParent());
		} else {
			parent = file;
		}
		summary = sum_t;
		detail = de_t;
		scan(parent);
	}

	private void scan(SafeFile f_t) {
		String[] fileQue_t;
		SafeFile tempfile;
		if (f_t.isFile()) {
			AllFile.add(f_t);
		} else {
			tempfile = new SafeFile(f_t.getPath());
			fileQue_t = tempfile.list();
			for (int i = 0; i < fileQue_t.length; i++) {
				SafeFile a = new SafeFile(fileQue_t[i]);
				if (a.isFile()) {
					AllFile.add(a);
				} else {
					scan(a);
				}
			}
		}
	}

	@Override
	public void run() {
		System.out.println("Begin trigger");
		if (file.initcon()) {
			do {
				try {
					Thread.sleep(1024);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				filehandle(file, parent);
			} while (true);
		} else {
			do {
				try {
					Thread.sleep(1024);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				directhandle(file, parent);
			} while (true);
		}
	}

	private boolean whetherexist(SafeFile temp) {
		for (int i = 0; i < AllFile.size(); i++) {
			if (AllFile.get(i).equals(temp))
				return true;
		}
		return false;
	}

	private void filehandle(SafeFile f_t, SafeFile p_t) {
		if (monitorNum == 0) {// rename
			if (f_t.exists()) {
				f_t.setInfo();
				if (file.initcon()) {
					AllFile.clear();
					scan(parent);
				}
			} else {
				SafeFile pFile = new SafeFile(f_t.preparent());
				String[] templist = pFile.list();
				for (int i = 0; i < templist.length; i++) {
					SafeFile t = new SafeFile(templist[i]);
					if (t.isFile()) {
						if (t.length() == f_t.presize() && t.lastModified() == f_t.premodify()
								&& !(t.getName().equals(f_t.prename())) && !whetherexist(t)) {
							// 名字不同、大小、修改时间相同且之前不存在
							addren();
							Out(f_t, t);
							if (file.initcon()) {
								file = t;
								AllFile.clear();
								scan(parent);
							}
						}
					}
				}
			}
		} else if (monitorNum == 1) {// modified
			if (f_t.exists()) {
				if (f_t.lastModified() != f_t.premodify()) {
					addmon();
					Out(f_t, f_t);
					f_t.setInfo();
					if (file.initcon()) {
						AllFile.clear();
						scan(parent);
					}
				}
			}
		} else if (monitorNum == 2) {// path_change
			if (f_t.exists()) {
				f_t.setInfo();
				if (file.initcon()) {
					AllFile.clear();
					scan(parent);
				}
			} else {
				String[] templist = p_t.list();
				for (int i = 0; i < templist.length; i++) {
					SafeFile t = new SafeFile(templist[i]);
					if (t.isFile()) {
						if (t.length() == f_t.presize() && t.lastModified() == f_t.premodify()
								&& t.getName().equals(f_t.prename()) && !(t.getParent().equals(f_t.preparent()))) {
							// 名字、大小、修改时间相同但位置不同
							addpan();
							Out(f_t, t);
							if (file.initcon()) {
								file = t;
								AllFile.clear();
								scan(parent);
							}
						}
					} else {
						filehandle(f_t, t);
					}
				}
			}
		} else {// size_change
			if (f_t.exists()) {
				if (f_t.presize() != f_t.length()) {
					addsin();
					Out(f_t, f_t);
					f_t.setInfo();
					if (file.initcon()) {
						AllFile.clear();
						scan(parent);
					}
				}
			}
		}
	}

	private void directhandle(SafeFile d_t, SafeFile p_t) {
		for (int i = 0; i < AllFile.size(); i++) {
			filehandle(AllFile.get(i), d_t);
		}
		AllFile.clear();
		scan(d_t);
	}

	private void Out(SafeFile old_t, SafeFile new_t) {
		if (missionNum == 0) {
			summaryNum++;
			summary.outPut(old_t, new_t, summaryNum, ren, mon, pan, sin);
		} else if (missionNum == 1) {
			detail.outPut(old_t, new_t);
		} else {// recover
			if (monitorNum != 2 && monitorNum != 0) {
				System.out.println("Invalid");
			} else {
				recover(old_t, new_t);
				System.out.println("recover");
			}
		}
	}
	
	private synchronized void addren() {
		ren++;
	}
	
	private synchronized void addmon() {
		mon++;
	}
	
	private synchronized void addpan() {
		pan++;
	}
	
	private synchronized void addsin() {
		sin++;
	}

	private void recover(SafeFile old_t, SafeFile new_t) {
		new_t.renameTo(old_t.prepath());
	}
}
