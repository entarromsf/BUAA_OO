package taxi;

public class Test extends Thread{
	/**
	 * @Overview:用于测试。
	 */
	
	private Car[] cars;
	
	public Test(Car[] cars_t) {
		/*
		 * @ REQUIRES: None;
		 * 
		 * @ MODIFIES: cars;
		 * 
		 * @ EFFECTS:cars = cars_t;
		 */
		cars = cars_t;
	}
	
	@Override
	public void run() {
//		
//		while(true) {
//			try {
//				// your code here
//				sleep(1);
//				if(cars[3].hasNext()) {
//					System.out.println(cars[3].next());
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}
}
