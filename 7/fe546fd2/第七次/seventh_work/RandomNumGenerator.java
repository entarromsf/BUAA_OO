package seventh_work;
import java.util.Random;
public class RandomNumGenerator {

	private int [] num;
	
	RandomNumGenerator()
	{
		num = new int [101];
	}
	
	public void Generate()
	{
		for(int i = 1;i <= 100;i++)
		{
				Random rand = new Random();
			num[i] = rand.nextInt(6400);
		}
	}
	
	public int[] get_num()
	{
		return num;
	}
	
	
}
