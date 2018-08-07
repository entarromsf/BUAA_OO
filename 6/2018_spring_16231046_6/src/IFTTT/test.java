package IFTTT;

public class test extends Thread{
	@Override
	public void run() {
		try {
			sleep(5000);
		} catch (Exception e) {}
		synchronized (Trigger.class) 
		{
			//第一次测试
			/*
			 * 你可以在下面输入支持的线程安全类操作
			 */
			SafeFile a,b;
			a = new SafeFile("E:\\test\\b\\a.txt");
			b = new SafeFile("E:\\test\\b\\d\\a.txt");
			a.writeAppend("s");
			b.renameTo("E:\\test\\b\\d\\b.txt");
			try{				
				sleep(5000);//Thread.sleep(5000);
			}
			catch (Exception e) {}
			
			//第二次测试
			/*
			 * 你可以在下面输入支持的线程安全类操作
			 */
			//代码。。。
			try{				
				sleep(5000);//Thread.sleep(5000);
			}
			catch (Exception e) {}
			//以此类推
		}	
	}
}
