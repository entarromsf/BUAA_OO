package test;
import java.util.*;
public class safe_data {
	private ArrayList<int[]> request;
	private ArrayList<taxi> taxi_list;
	private City city;
	public safe_data(ArrayList<int[]> request,ArrayList<taxi> taxi_list,City city) {
		this.request=request;
		this.taxi_list=taxi_list;
		this.city=city;
	}
	public synchronized City getcity() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		return city;
	}
	
	public ArrayList<int[]> get_request() {
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		return this.request;
	}
	public ArrayList<taxi> get_taxilist(){
		/**
		 * @REQUIRES:None;
		 * @MODIFIES:None;
		 * @EFFECTS:None;
		 */
		return this.taxi_list;
	}
}
