package kariminf.faris.knowledge;

import kariminf.faris.philosophical.Action;

public class Thought extends Idea {

	Action action;
	
	public Thought(Action action) {
		this.action = action;
	}
	
	public Action getAction(){
		return action;
	}

}
