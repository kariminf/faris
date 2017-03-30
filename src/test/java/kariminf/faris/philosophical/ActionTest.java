package kariminf.faris.philosophical;

import static org.junit.Assert.*;

import org.junit.Test;

import kariminf.faris.linguistic.Verb;

public class ActionTest {

	@Test
	public void getNewTest(){
		Verb verb = new Verb(937208);//sing
		try {
			Action.getNew(verb);
		} catch (Exception e){
			fail("Can't create new Action");
		}
	}
	
	@Test
	public void adjectiveTest(){
		Verb verb = new Verb(937208);//sing
		Action action = Action.getNew(verb);
		
	}
	
	
	
	

}
