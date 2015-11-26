/* Farest : Facts representation of sentences
 * ------------------------------------------
 * Copyright (C) 2015 Abdelkrime Aries (kariminfo0@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dz.aak.faris.philosophical;

import java.util.Set;

/**
 * Having or state, condition (ἔχειν, echein, to have or be). 
 * The examples Aristotle gives indicate that he meant a condition of rest resulting 
 * from an affection (i.e. being acted on): ‘shod’, ‘armed’. The term is, however, 
 * frequently taken to mean the determination arising from the physical accoutrements 
 * of an object: one's shoes, one's arms, etc. Traditionally, this category is also 
 * called a habitus (from Latin habere, to have).
 * 
 * @author kariminf
 *
 */
public class State {

	private Action action;
	
	private State(Action action) {
		this.action = action;
	}
	
	
	/**
	 * 
	 * @param action the action which represents a state. 
	 * This action must not have subjects or objects.<br/>
	 * Example: a car which is stopped on the road and have an old engine. 
	 * The car have two states: "stopped" and "having old engine".
	 * @param haver The substance which have this state
	 * @param relatives The subjects or the objects that share this action with the haver.
	 * If the haver is a subject, the relatives are objects, and vis-versa.
	 * @param affected if true, then the haver is an object, otherwise it is a subject
	 * @return true if the affectation is successful 
	 */
	public static boolean affectState (Action action, Substance haver, Set<Substance> relatives, boolean affected){
		if(action.getObjects().size() > 0)
			return false;
		if(action.getObjects().size() > 0)
			return false;
		
		if (affected){
			action.addObject(haver);
			action.addSubjects(relatives);
		}
		else {
			action.addSubject(haver);
			action.addObjects(relatives);
		}
		
		State state = new State(action);
		haver.addState(state);
		
		return true;
	}
	
	public Action getAction(){
		return action;
	}

}
