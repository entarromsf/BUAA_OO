package IFTTT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Detail {
	
	private static PrintStream d;
	
	public Detail() {
		try {
			FileOutputStream det = new FileOutputStream(new File("Detail.txt"));
			d = new PrintStream(det);
		} catch (Exception e) {}
	}
	
	public synchronized void outPut(SafeFile old_t, SafeFile new_t) {
		try {
			
			printl("---------TRIGGER---------");

			printl("oldfile:");
			printl("path: "+old_t.prepath());
			printl("size: "+old_t.presize());
			printl("modified: "+old_t.premodify());
			
			printl("newfile:");
			printl("path: "+new_t.getPath());
			printl("size: "+new_t.length());
			printl("modified: "+new_t.lastModified());
			
			printl("-----------END-----------");
			
		} catch (Exception e) {}
	}
	
	private void printl(String str_t) {
		d.println(str_t);
		System.out.println(str_t);
	}
}
