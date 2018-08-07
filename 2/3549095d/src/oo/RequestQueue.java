package oo;
import java.math.*;
import java.util.*;
public class RequestQueue {
	private ArrayList<Request> requests;
	public RequestQueue() {
		requests = new ArrayList<Request>();
	}
	public boolean Request_add(Request r) {
		BigDecimal zero = new BigDecimal("0");
		if (requests.isEmpty()) {
			if (r.get_request_time().compareTo(zero)!=0) 
				return false;
			else 
				return requests.add(r);
			
		}
		else {
			if (r.get_request_time().compareTo(requests.get(requests.size()-1).get_request_time())>=0) 
				return requests.add(r);
			else return false;
		}
	}
	public Request Request_get(int index){
		return requests.get(index);
	}
	public int Request_number_get() {
		return requests.size();
	}
	public void Request_delete(Request r) {
		requests.remove(r);
	}
	public void Request_delete(int index) {
		requests.remove(index);
	}	
}