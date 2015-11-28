package dz.aak.faris.knowledge;

import dz.aak.faris.philosophical.Action;

public class Thought extends Idea {

	Action action;
	
	public Thought(Action action) {
		this.action = action;
	}
	
	public Action getAction(){
		return action;
	}

}
