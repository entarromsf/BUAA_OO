package taxi;

import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
	private String str;
	Point src, dst;
	private int srcx,srcy,dstx,dsty;
	private boolean sign = true;
	public Request(String str_t) {
		str = str_t;
		srcx = 0;
		srcy = 0;
		dstx = 0;
		dsty = 0;
		src = new Point();
		dst = new Point();
	}
	
	public void handle() {//[CR,(80,80),(1,1)]
		try {
			Pattern p = Pattern.compile("\\[CR,\\(\\+?\\d+,\\+?\\d+\\),\\(\\+?\\d+,\\+?\\d+\\)\\]");
			Matcher m = p.matcher(str);
			
			if(!m.find()) sign = false;
			
			String[] strings = str.split("[,()]");
			
			srcx = Integer.parseInt(strings[2]);
			srcy = Integer.parseInt(strings[3]);
			dstx = Integer.parseInt(strings[6]);
			dsty = Integer.parseInt(strings[7]);
			
			whether(srcx);
			whether(srcy);
			whether(dstx);
			whether(dsty);
			
			src.setLocation(srcx, srcy);
			dst.setLocation(dstx, dsty);
			
			if(src.equals(dst)) sign = false;
		} catch (Exception e) {sign = false; return;}
	}
	
	public void whether(int x) {
		if(x<0||x>79) {
			sign = false;
		}
	}
	
	public Point getSrc() {
		return src;
	}

	public Point getDst() {
		return dst;
	}

	public boolean getsign() {
		return sign;
	}
	
	
}
