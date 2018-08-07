package taxi;

import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
	/**
     * @Overview:输入请求类，拥有处理输入的功能。
     */
	private String str;
	Point src, dst;
	private int srcx, srcy, dstx, dsty;
	private boolean sign = true, signopen = true, signclose = true;

	public Request(String str_t) {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: str, srcx, srcy, dstx, dsty, src, dst;
		 * 
		 * @ EFFECTS: 初始化各个变量
		 */
		str = str_t;
		srcx = 0;
		srcy = 0;
		dstx = 0;
		dsty = 0;
		src = new Point();
		dst = new Point();
	}

	public boolean repOK() {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: None;
		 * 
		 * @ Effects: \result == invariant(this);
		 */
		if (str == null)
			return false;
		return true;
	}

	public void handle() {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: srcx, srcy, dstx, dsty, src, dst, sign, signopen, signclose;
		 * 
		 * @ EFFECTS: 查询这个字符串的合法性并提取指令信息
		 */
		try {
			Pattern p = Pattern.compile("\\[CR,\\(\\+?\\d+,\\+?\\d+\\),\\(\\+?\\d+,\\+?\\d+\\)\\]");
			Matcher m = p.matcher(str);
			Pattern p1 = Pattern.compile("\\[OPEN,\\(\\+?\\d+,\\+?\\d+\\),\\(\\+?\\d+,\\+?\\d+\\)\\]");
			Pattern p2 = Pattern.compile("\\[CLOSE,\\(\\+?\\d+,\\+?\\d+\\),\\(\\+?\\d+,\\+?\\d+\\)\\]");
			Matcher m1 = p1.matcher(str);
			Matcher m2 = p2.matcher(str);

			if (!m.find())
				sign = false;
			if (!m1.find())
				signopen = false;
			if (!m2.find())
				signclose = false;

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

			if (src.equals(dst)) {
				sign = false;
				signopen = false;
				signclose = false;
			}
		} catch (Exception e) {
			sign = false;
			signopen = false;
			signclose = false;
			return;
		}
	}

	public void whether(int x) {
		if (x < 0 || x > 79) {
			sign = false;
			signopen = false;
			signclose = false;
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

	public boolean getsignopen() {
		return signopen;
	}

	public boolean getsignclose() {
		return signclose;
	}
}
