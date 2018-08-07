package test;
import java.util.*;
public class safe_data {
	/**@overview:
	 * The data that each thread needs to be shared is mapped to a class, and the shared data is controlled by calling the object of the class instance.
	 */
	private ArrayList<int[]> request;
	private ArrayList<taxi> taxi_list;
	private City city;
	public safe_data(ArrayList<int[]> request,ArrayList<taxi> taxi_list,City city) {
		this.request=request;
		this.taxi_list=taxi_list;
		this.city=city;
	}
	public boolean repOK() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:(request.size!=0 && taxi_list.size!=0&&city!=null=>\result=true);
		 */
		if(request==null || taxi_list==null|| city==null) return false;
		return true;
	}
	public synchronized City getcity() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:(\result=city);
		 */
		return city;
	}
	
	public ArrayList<int[]> get_request() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:(\result=request);
		 */
		return this.request;
	}
	public ArrayList<taxi> get_taxilist(){
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:(\result=taxi_list);
		 */
		return this.taxi_list;
	}
}
