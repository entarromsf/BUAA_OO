package top.buaaoo.main;

import static org.junit.Assert.*;
import org.junit.Test;

public class RequestTest {
	private Request input = new Request();
	
	@Test
	public void testMatchD() {
		System.out.println("(FR,001,UP,0)");//bug测试
		assertEquals(2, input.MatchD("(FR,1,UP,0)", 0));
		
		System.out.println("(FR,2,DOWN,0)");
		assertEquals(-1, input.MatchD("(FR,2,DOWN,0)", 0));
		
		System.out.println("(FR,2,DOWN,2)");
		assertEquals(1, input.MatchD("(FR,2,DOWN,2)", 1));
		
		System.out.println("(ER,2,2)");
		assertEquals(3, input.MatchD("(ER,2,2)", 1));
		
		System.out.println("(++FR,++1,++UP,++0)");//bug测试
		assertEquals(-1, input.MatchD("((++FR,++1,++UP,++0)", 1));
		
		System.out.println("RUN");
		assertEquals(0, input.MatchD("RUN", 1));
	}
	
	
	@Test
	public void testRepOk(){
		assertEquals(true, input.repOK());
	}

}

