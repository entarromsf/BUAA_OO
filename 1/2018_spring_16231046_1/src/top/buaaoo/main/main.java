package top.buaaoo.main;


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
		String[] polyterms = polymain.split(regex);//分割多项式
		poly Sum = new poly();	
		Erromatch E = new Erromatch();
		int num = 0;
		for (String polyterm : polyterms){
			num++;
			if(E.numpo(num)) {		
				return;
			}
			poly A = new poly();
			String ERROpoly = polyterm;
			polyterm = polyterm+",";
			if(E.erro(A.Initpoly(polyterm), ERROpoly)){
				return;
			};
			Sum.Operation(A);
		}
		Sum.selectSort();
		Sum.Output();
		return;
	}
}
