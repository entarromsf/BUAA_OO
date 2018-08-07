package top.buaaoo.main;

public class Request {
	public boolean repOK(){
		/**@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		*/
		return true;
	}
	
	public static int MatchD(String str,int Num)
	{
    	/**
		 * @ REQUIRES: str!=null, Num!=null;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ EFFECTS: b1 == true ==> \result == 1;
		 * 			  b2 == true ==> \result == 2;
		 * 			  b3 == true ==> \result == 3;
		 * 			  b4 == true ==> \result == 4;
		 * 		      !(b1||b2||b3||b4) == false ==> \result == -1;
		 */
		String regex1 = "\\(FR,[+]?\\d+,DOWN,[+]?\\d+\\)";
		String regex2 = "\\(FR,[+]?\\d+,UP,[+]?\\d+\\)";
		String regex3 = "\\(ER,[+]?\\d+,[+]?\\d+\\)";
		String regex4 = "RUN";
		boolean b1,b2,b3,b4;
		
		
		if (Num==0) {
			b1 = false;
			b2 = str.matches("\\(FR,[+]?[0]*1,UP,[+]?[0]+\\)");
			b3 = false;
			b4 = false;
		}else {
			b1 = str.matches(regex1);
			b2 = str.matches(regex2);
			b3 = str.matches(regex3);
			b4 = str.matches(regex4);
		}	
		if(b1) {
			return 1;
		}else if(b2) {
			return 2;
		}else if(b3) {
			return 3;
		}else if(b4){
			return 0;
		}else{
			return -1;
		}
	}
}
