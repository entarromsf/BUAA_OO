package top.buaaoo.main;

public class Erromatch {

	public boolean erro (int i, String erropoly) {
		if(i == 0){
			return false;
		}
		else if (i == 1) {
			System.out.println("ERROR"+"\n"+"#输入格式错误"+erropoly);
			return true;
		}
		else if(i == 2)
		{
			System.out.println("ERRO"+"\n"+"#重复幂次"+erropoly);
			return true;
		}
		else {
			System.out.println("ERRO"+"\n"+"#幂次为负数"+erropoly);
			return true;
		}
	}
	public boolean numpo (int n) {
		if (n>20) {
			System.out.println("ERROR"+"\n"+"#多项式过多");
			return true;
		}
		return false;
	}

}
