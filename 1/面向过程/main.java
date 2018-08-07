package top.buaaoo.first.main;


import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String polyin = scanner.nextLine();
		scanner.close();
		String polymain = polyin.replaceAll(" +", "");//去除空格
		String regex = "}";
		int[] C2 = new int[1000];
		int[] N2 = new int[1000];
		int num = 0;
		int NUM = 0;
		int i = 0;
		int j = 0;
		String[] polyterms = polymain.split(regex);//分割多项式 
		for (String polyterm : polyterms) {
			int[] C1 = new int[1000];
			int[] N1 = new int[1000];
			String errooutputs = polyterm;
			polyterm = polyterm+",";
			if(!Matchpoly(polyterm)) {
				System.out.println("ERROR"+"\n"+"#输入格式错误"+errooutputs);
				return;
			}
			char first = polyterm.charAt(0);
			if(first == '+' || first == '{')
			{
				String[] terms = polyterm.split("\\),");
				for (String term : terms) {
					String result1 = null,result2 = null;
					int ct = 0,nt = 0;
					Pattern p = Pattern.compile("([+-]?\\d+),([+]?\\d+)");
					Matcher m = p.matcher(term);
					if(m.find()) {
						result1 = m.group(1);						
						ct = Integer.parseInt(result1);
						result2 = m.group(2);						
						nt = Integer.parseInt(result2);
					}//提取出term中的两个int
					for(i=0; i<num; i++) {
						if(N1[i]==nt) {
							System.out.println("ERRO"+"\n"+"#重复幂次"+nt);
							return;
						}											
					}
					if(i==num) {
						num++;
						C1[i] = ct;
						N1[i] = nt;
					}					
				}
				for(i=0; i<num; i++) {
					for(j=0; j<NUM; j++) {
						if(N2[j]==N1[i]) {
							C2[j]=C1[i]+C2[j];
							break;
						}
					}
					if(j==NUM) {
						NUM++;
						C2[j] = C1[i];
						N2[j] = N1[i];
					}
				}
			} 
			else {
				String[] terms = polyterm.split("\\),");
				for (String term : terms) {
					String result1 = null,result2 = null;
					int ct = 0,nt = 0;
					Pattern p = Pattern.compile("([+-]?\\d+),([+]?\\d+)");
					Matcher m = p.matcher(term);
					if(m.find()) {
						result1 = m.group(1);						
						ct = Integer.parseInt(result1);
						result2 = m.group(2);						
						nt = Integer.parseInt(result2);
					}//提取出term中的两个int 
					for(i=0; i<num; i++) {
						if(N1[i]==nt) {
							System.out.println("ERRO"+"\n"+"#重复幂次"+nt);
							return;
						}											
					}
					if(i==num) {
						num++;
						C1[i] = -ct;
						N1[i] = nt;
					}
				}
				for(i=0; i<num; i++) {
					for(j=0; j<NUM; j++) {
						if(N2[j]==N1[i]) {
							C2[j]=C1[i]+C2[j];
							break;
						}
					}
					if(j==NUM) {
						NUM++;
						C2[j] = C1[i];
						N2[j] = N1[i];
					}
				}
			}
		}
		selectSort(N2, C2,NUM);
		System.out.print("{");
		boolean outflag = false;
		for(i=0;i < NUM;i++) {
			if(C2[i]!=0) {
				outflag = true;
			    System.out.print("("+C2[i]+","+N2[i]+")");
			    if(i<NUM-1) {
				   System.out.print(",");
			    }
			}
		}
		if(!outflag) {
			System.out.print("("+0+","+0+")");
		}
		System.out.print("}");

	}
	public static boolean Matchpoly(String polyterm)
	{
		String regex = "[+-]?\\{(\\([+-]?[0-9]{1,6},[+]?[0-9]{1,6}\\),){1,50}";
		boolean b = polyterm.matches(regex);
		return b;
	}
	public static void selectSort(int[]a,int[]b,int Num)
	{
	    int minIndex=0;
	    int temp=0;
	    for(int i=0;i<Num-1;i++)
	    {
	        minIndex=i;//无序区的最小数据数组下标
	        for(int j=i+1;j<Num;j++)
	        {
	            //在无序区中找到最小数据并保存其数组下标
	            if(a[j]<a[minIndex])
	            {
	                minIndex=j;
	            }
	        }
	        if(minIndex!=i)
	        {
	            //如果不是无序区的最小值位置不是默认的第一个数据，则交换之。
	            temp=a[i];
	            a[i]=a[minIndex];
	            a[minIndex]=temp;
	            temp=b[i];
	            b[i]=b[minIndex];
	            b[minIndex]=temp;
	        }
	    }
	}

}
