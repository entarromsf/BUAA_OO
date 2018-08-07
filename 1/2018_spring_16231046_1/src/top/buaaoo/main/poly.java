package top.buaaoo.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class poly {
	private int[] C = new int[1000];
	private int[] N = new int[1000];
	private int NUM = 0;
	
	public void clear(){
		int i = 0;
		for(i = 0; i < NUM; i++){
			C[i] = 0;
			N[i] = 0;
		}
		NUM = 0;
	}
	public int Initpoly (String polyterm) {
		int i = 0; 
		if(!Matchpoly(polyterm)){
			return 1;//格式错误
		}
		char first = polyterm.charAt(0);
		if(first == '+' || first == '{')
		{
			String[] terms = polyterm.split("\\),");
			for (String term : terms) {
				String result1 = null,result2 = null;
				int ct = 0,nt = 0;
				Pattern p = Pattern.compile("([+-]?\\d+),([+-]?\\d+)");
				Matcher m = p.matcher(term);
				if(m.find()) {
					result1 = m.group(1);						
					ct = Integer.parseInt(result1);
					result2 = m.group(2);						
					nt = Integer.parseInt(result2);
					if(nt < 0) return 3;
				}//提取出term中的两个int
				for(i=0; i<NUM; i++) {
					if(N[i]==nt) {
						return 2;
					}											
				}
				if(i==NUM) {
					NUM++;
					C[i] = ct;
					N[i] = nt;
				}					
			 }
		 } 
		 else {
			String[] terms = polyterm.split("\\),");
			for (String term : terms) {
				String result1 = null,result2 = null;
				int ct = 0,nt = 0;
				Pattern p = Pattern.compile("([+-]?\\d+),([+-]?\\d+)");
				Matcher m = p.matcher(term);
				if(m.find()) {
					result1 = m.group(1);						
					ct = Integer.parseInt(result1);
					result2 = m.group(2);						
					nt = Integer.parseInt(result2);
					if(nt < 0) return 3;
				}//提取出term中的两个int 
				for(i=0; i<NUM; i++) {
					if(N[i]==nt) {
						return 2;
					}											
				}
				if(i==NUM) {
					NUM++;
					C[i] = -ct;
					N[i] = nt;
				}
			 }
		 }
		 return 0;
	}
	
	public boolean Matchpoly(String polyterm){
		String regex = "[+-]?\\{(\\([+-]?[0-9]{1,6},[+-]?[0-9]{1,6}\\),){1,50}";
		boolean b = polyterm.matches(regex);
		return b;
	}
	
	public void Operation(poly A){
		int i = 0;
		int j = 0;
		for(i=0; i<A.NUM; i++) {
			for(j=0; j<NUM; j++) {
				if(N[j]==A.N[i]) {
					C[j]=A.C[i]+C[j];
					break;
				}
			}
			if(j==NUM) {
				NUM++;
				C[j] = A.C[i];
				N[j] = A.N[i];
			}
		}
	}
	
	public void selectSort()
	{
	    int minIndex=0;
	    int temp=0;
	    for(int i=0;i<NUM-1;i++)
	    {
	        minIndex=i;//无序区的最小数据数组下标
	        for(int j=i+1;j<NUM;j++)
	        {
	            //在无序区中找到最小数据并保存其数组下标
	            if(N[j]<N[minIndex])
	            {
	                minIndex=j;
	            }
	        }
	        if(minIndex!=i)
	        {
	            //如果不是无序区的最小值位置不是默认的第一个数据，则交换之。
	            temp=N[i];
	            N[i]=N[minIndex];
	            N[minIndex]=temp;
	            temp=C[i];
	            C[i]=C[minIndex];
	            C[minIndex]=temp;
	        }
	    }
	}
	
	public void Output()
	{
		int i = 0;
		boolean outflag = false;
		for(i=0;i < NUM;i++) {
			if(C[i]!=0) {
				outflag = true;
			}
		}
		if(!outflag) {
			System.out.print(0);
		}
		else {
			System.out.print("{");
			for(i=0;i < NUM;i++) {
			if(C[i]!=0) {
			    System.out.print("("+C[i]+","+N[i]+")");
			    if(i<NUM-1) {
				   System.out.print(",");
			    }
			}
		}
		
		System.out.print("}");
	    }
	}

}
