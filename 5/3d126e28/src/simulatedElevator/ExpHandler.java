package simulatedElevator;

class ExpHandler {

	static void err(int code, String a) {
		
		System.out.printf("%d:INVALID[%s,%.1f]\r\n", System.currentTimeMillis(), a,
				(System.currentTimeMillis() - Main.start_time) / 1000.0);
				
	}
}
