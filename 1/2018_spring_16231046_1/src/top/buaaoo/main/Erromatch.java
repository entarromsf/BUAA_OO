package top.buaaoo.main;

public class Erromatch {

	public boolean erro (int i, String erropoly) {
		if(i == 0){
			return false;
		}
		else if (i == 1) {
			System.out.println("ERROR"+"\n"+"#�����ʽ����"+erropoly);
			return true;
		}
		else if(i == 2)
		{
			System.out.println("ERRO"+"\n"+"#�ظ��ݴ�"+erropoly);
			return true;
		}
		else {
			System.out.println("ERRO"+"\n"+"#�ݴ�Ϊ����"+erropoly);
			return true;
		}
	}
	public boolean numpo (int n) {
		if (n>20) {
			System.out.println("ERROR"+"\n"+"#����ʽ����");
			return true;
		}
		return false;
	}

}
