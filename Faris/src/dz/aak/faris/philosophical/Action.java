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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dz.aak.faris.linguistic.Verb;

/**
 * Action (poiein, to make or do) — examples: to lance, to heat, to cool (something)
 * Affection, passion (paschein, to suffer or undergo) — examples: to be lanced, 
 * to be heated, to be cooled
 * 
 * @author Abdelkrime Aries
 *
 */
public class Action {
	
	public static enum ActionRelation {
		IMPLY,
		CAUSE,
		After,
		Before
	}
	
	//An action is defined by a verb
	private Verb verb;
	//- A verb has tense, indicating the time that the sentence describes
	//- A verb has mood, indicating whether the sentence describes reality or 
	//expresses a command, a hypothesis, a hope, etc.
	
	//An Action can have many doers (we can't duplicate a doer)
	private Set<Substance> subjects = new HashSet<Substance>(); 
	
	//An Action can affecte many predicates 
	private Set<Substance> objects = new HashSet<Substance>(); 
	
	//An action can have relations with other Actions
	
	private HashMap<Action, ActionRelation> relations = new HashMap<Action, ActionRelation>();
	
	//An action can have locations
	private Set<Place> locations = new HashSet<Place>(); 
	
	//An action can have times
	private Set<Time> times = new HashSet<Time>(); 

	private Action(Verb verb){ 
		
		this.verb = verb;
		//We have to treat the verb before adding it
		//Or we enter the infinitive form, and before that we transform it 
		//to infinitive in the global system
	}
	
	public static Action getNew (Verb verb){
		return new Action(verb);
	}
	
	public void addSubject(Substance noun){
		subjects.add(noun);
	}
	
	public void addObject(Substance noun){
		objects.add(noun);
	}
	
	
	public List<Substance> getSubjects(){
		List<Substance> result = new ArrayList<Substance>();
		result.addAll(subjects);
		return result;
	}
	
	public List<Substance> getObjects(){
		List<Substance> result = new ArrayList<Substance>();
		result.addAll(objects);
		return result;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
