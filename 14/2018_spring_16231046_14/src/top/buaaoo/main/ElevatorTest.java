package top.buaaoo.main;

import static org.junit.Assert.*;
import org.junit.Test;

public class ElevatorTest {
	private Elevator ele = new Elevator();
	
	@Test
	public void testRepOk(){
		assertEquals(true, ele.repOK());
	}

	@Test
	public void testopendoor() {
		System.out.println("2.0s");
		ele.opendoor(1);
	}

	@Test
	public void testrunning() {
		System.out.println("0s,2F->6F");
		ele.running(0, 2, 6);
	}

}
