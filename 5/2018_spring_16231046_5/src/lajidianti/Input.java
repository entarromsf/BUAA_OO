package lajidianti;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Input {
	private int maxFloor = 20, minFloor = 1;
	private int type, floor;
	private int ele_id;

	private boolean sign = true;
	private String str;
	Input(String s){
		str = s;
	}
	void handle(){
	  try{
		  Pattern p1 = Pattern.compile("^\\(FR,\\+?\\d+,(UP|DOWN)\\)$");
		  Pattern p2 = Pattern.compile("^\\(ER,#\\+?\\d+,\\+?\\d+\\)$");
		  
		  Matcher m1 = p1.matcher(str);
		  Matcher m2 = p2.matcher(str);
		  
		  if(!m1.find() && !m2.find()) sign = false;
		  
		  String[] strings = str.split("[,()]");
		  
		  if(strings[1].equals("FR")){
			  floor = Integer.parseInt(strings[2]);//floor
			  if(floor>maxFloor || floor < minFloor) sign = false;
			  if(strings[3].equals("UP")) {
				  type = 1;
				  if(floor==20) sign = false;
			  }
			  else if(strings[3].equals("DOWN")) {
				  type = 2;
				  if(floor==1) sign = false;
			  }
			  else{
				  sign = false;
			  }
		  }
		  if(strings[1].equals("ER")){//"(ER"
		
			  floor = Integer.parseInt(strings[3]);
			  type = 3;
			  ele_id = Integer.parseInt(strings[2].substring(1));
			  if(ele_id>3||ele_id<1) sign = false;
			  if(floor>maxFloor || floor < minFloor) sign = false;
		  }
		  
		  
	  }
	  catch (Exception e){
		  sign = false;
		  return;
	  }
	}
	public int getType() {
		return type;
	}
	public int getFloor() {
		return floor;
	}
	public int getEle_id() {
		return ele_id;
	}
	public boolean isSign() {
		return sign;
	}
}
