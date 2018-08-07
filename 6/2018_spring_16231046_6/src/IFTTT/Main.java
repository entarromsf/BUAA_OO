package IFTTT;


public class Main {
	
	public static void main(String[] args) {
		
		try {
			Summary summary = new Summary();
			Detail detail = new Detail();
					
			Trigger[] tgs = new Trigger[15];
			
			RequestQue rq = new RequestQue();
			Requestadd rad = new Requestadd(rq);
			
			rad.handle();
			for(int i = 0; i < rq.monitor.size(); i++) {
				tgs[i] = new Trigger(rq.getmonitor(i), rq.getmission(i), rq.getpath(i), summary, detail);
			}
			for (int i = 0; i < rq.monitor.size(); i++) {
				tgs[i].start();
			}
			
//			test t = new test();
//			t.start();
		} catch (Exception e) {
			System.exit(0);
		}
			
	}

}
