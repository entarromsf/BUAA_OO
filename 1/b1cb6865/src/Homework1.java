import java.util.Scanner;
import java.util.regex.Pattern;

class Poly {
	int[] coe = new int[1024];
	int[] exp = new int[1024];
	int num = 0;
	
	public void Insert(String s) {
		int c, n;
		int flag = 1, sym;			// + or -
		String[] lit = s.split("}");
		for (int i=0 ; i<lit.length ; i++) {
			if (lit[i].charAt(0) == '-') {
				flag = -1;
			}
			else {
				flag = 1;
			}
			String[] les = lit[i].split("\\)");
			for (int j=0 ; j<les.length ; j++) {
				c = n = 0;
				sym = flag;
				for (int k=0 ; k<les[j].length() ; k++) {
					if (les[j].charAt(k) == '(') {
						k++;
						if (les[j].charAt(k) == '-') {
							sym = sym * -1;
							k++;
						}
						else if (les[j].charAt(k) == '+')
						    k++;
						while (les[j].charAt(k) <='9' && les[j].charAt(k) >='0') {
							c = c*10 + (int) (les[j].charAt(k) - '0');
							k++;
						}
						coe[num] = c * sym;
						while (k<les[j].length()) {
							if (les[j].charAt(k) <='9' && les[j].charAt(k) >='0') {
								n = n*10 + (int) (les[j].charAt(k) - '0');
							}
							k++;
						}
						exp[num++] = n;
					}
				}
			}
		}
	}
	
	public void Sort() {
		int cmin, nmin, min;
		int amount = 0;
		int flag = 0;			// 0 or poly
		int[][] result = new int[1024][2];
		for (int i=0 ; i<num ;i++) {
			if (exp[i] != -1) {
				cmin = coe[i];
				nmin = exp[i];
				min = i;
				for (int j=i+1 ; j<num ;j++) {
					if (nmin > exp[j]) {
						cmin = coe[j];
						nmin = exp[j];
						min = j;
					}
					else if (nmin == exp[j]) {
						cmin += coe[j];
						coe[min] += coe[j];
						exp[j] = -1;
					}
					else 
						continue;
				}
				coe[min] = coe[i];
				exp[min] = exp[i];
				coe[i] = cmin;
				exp[i] = nmin;
				if (cmin != 0 && nmin != -1) {
					result[amount][0] = cmin;
					result[amount++][1] = nmin;
					flag = 1;
				}
			}
		}
		if (flag == 0 )
			System.out.print("0");
		else {
			System.out.print("{");
			for (int i=0 ; i<amount ;i++) {
				if (result[i][1] != -1 && result[i][0] != 0) {
					System.out.print("("+result[i][0]+","+result[i][1]+")");
					if (i<amount-1) 
						System.out.print(",");
				}
			}
			System.out.print("}");
		}
	}
	
	public int Same(String s) {
		int n, m = 0;
		String[] lit = s.split("}");
		for (int i=0 ; i<lit.length ; i++) {
			m = 0;
			int[] e = new int[1000];
			String[] les = lit[i].split("\\)");
			for (int j=0 ; j<les.length ; j++) {
				n = 0;
				for (int k=0 ; k<les[j].length() ; k++) {
					if (les[j].charAt(k) == '(') {
						do {
							k++;
						} while(les[j].charAt(k) != ','); 
						while (k<les[j].length()) {
							if (les[j].charAt(k) <='9' && les[j].charAt(k) >='0') {
								n = n*10 + (int) (les[j].charAt(k) - '0');
							}
							k++;
						}
						e[m++] = n;
					}
				}
			}
			for (int j=0 ; j<m-1 ; j++) {
				for (int k=j+1 ; k<m ;k++) {
					if (e[j] == e[k])
						return 1;
				}
			}
		}
		return 0;
	}
}

public class Homework1 {
	public static void main(String[] args) { 
		Scanner in = new Scanner(System.in);
		Poly p = new Poly();
		String s = in.nextLine();
		s = s.replace(" ", "");
		Boolean b = Pattern.matches("-?\\{(\\((\\+|-)?\\d{1,6},(\\+?\\d|(\\+|-)?[0]){1,6}\\),)*\\((\\+|-)?\\d{1,6},(\\+?\\d|(\\+|-)?[0]){1,6}\\)\\}((\\+|-)\\{(\\((\\+|-)?\\d{1,6},(\\+?\\d|(\\+|-)?[0]){1,6}\\),)*\\((\\+|-)?\\d{1,6},(\\+?\\d|(\\+|-)?[0]){1,6}\\)\\})*", s);
		if (b == false) 
			System.out.print("ERROR");
		else {
			int a = p.Same(s);
			if (a == 1)
				System.out.print("ERROR");
			else {
				p.Insert(s);
				p.Sort();
			}
		}
	}
}
