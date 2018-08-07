package seventh_work;

import java.math.*;
public class RequestQueue {
	
	private int tot;
	private int valid_head;
	Request [] Q ;
	RequestQueue()
	{
		Q = new Request[1000];
		tot = 0;
		valid_head = 1;
	}
	public synchronized void new_request(Request r)
	{
		tot ++;
		Q[tot] = r;
	}
	public synchronized int get_tot()
	{
		return tot;
	}
	public synchronized Request get_req(int i)
	{
		return Q[i];
	}
	public synchronized void pop()
	{
		valid_head++;
	}
	public synchronized boolean empty()
	{
		return valid_head > tot;
	}
	public synchronized int get_valid_head()
	{
		return valid_head;
	}
	public synchronized boolean get_same(Request r)
	{
		for(int i = valid_head;i <= tot;i++)
		{
			boolean a = (Math.abs(r.get_time() - Q[i].get_time()) <= 100);
			boolean b = (r.get_start_x() == Q[i].get_start_x());
			boolean c = (r.get_start_y() == Q[i].get_start_y());
			boolean d = (r.get_end_x() == Q[i].get_end_x());
			boolean e = (r.get_end_y() == Q[i].get_end_y());
			if(a && b && c && d && e)return true;
		}
		return false;
	}
	
}
