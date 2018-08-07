package IFTTT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Summary {

	private static PrintStream s;

	public Summary() {
		try {
			FileOutputStream sum = new FileOutputStream(new File("Summary.txt"));
			s = new PrintStream(sum);
		} catch (Exception e) {
		}
	}

	public synchronized void outPut(SafeFile old_t, SafeFile new_t, int summaryNum_t, int rename_t, int mod_t,
			int path_t, int size_t) {
		try {

			printl("---------TRIGGER---------");
			printl("TriggerNum: " + summaryNum_t);
			printl("RenamedNum: " + rename_t);
			printl("ModifiedNum: " + mod_t);
			printl("Path-changed: " + path_t);
			printl("Size-changed: " + size_t);

			printl("oldfile:");
			printl("path: " + old_t.prepath());
			printl("size: " + old_t.presize());
			printl("modified: " + old_t.premodify());

			printl("newfile:");
			printl("path: " + new_t.getPath());
			printl("size: " + new_t.length());
			printl("modified: " + new_t.lastModified());

			printl("-----------END-----------");

		} catch (Exception e) {
		}
	}

	private void printl(String str_t) {
		s.println(str_t);
		System.out.println(str_t);
	}

}
