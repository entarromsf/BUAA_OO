package simulatedElevator;

import java.util.ArrayList;

class Requestqueue {
	private static ArrayList<Request> req_queue = new ArrayList<Request>();
	static int num = 0;
	
	static Request getReqList(int i) {
		synchronized(Requestqueue.req_queue) {
			if(i<req_queue.size() ) {
				return req_queue.get(i);
			}
			else
				return null;
		}
	}
	
	static int get_size() {
		synchronized(Requestqueue.req_queue) {
			return req_queue.size()  ;
		}
	}
	
	static void add_a_req(Request a_req) {
		synchronized(Requestqueue.req_queue) {
			a_req.num = num++;
			req_queue.add(a_req) ;
		}
	}
	
	static void remove_a_req(int a_req) {
		synchronized(Requestqueue.req_queue) {
			req_queue.remove (a_req) ;
		}
	}
}
