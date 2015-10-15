/* FARIS : Factual Arrangement and Representation of Ideas in Sentences
 * FAris : Farabi & Aristotle
 * Faris : A knight (in Arabic)
 * --------------------------------------------------------------------
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
package dz.aak.faris.knowledge;

import java.util.HashMap;
import java.util.HashSet;

import dz.aak.faris.linguistic.Adjective;
import dz.aak.faris.linguistic.Verb;
import dz.aak.faris.linguistic.Verb.Aspect;
import dz.aak.faris.linguistic.Verb.Tense;
import dz.aak.faris.philosophical.Action;
import dz.aak.faris.philosophical.Substance;
import dz.aak.faris.ston.Parser;
import dz.aak.faris.ston.RAction;
import dz.aak.faris.ston.RAdjective;
import dz.aak.faris.ston.RRolePlayer;

/**
 * The interface class,
 * 
 * @author Abdelkrime Aries (kariminfo0@gmail.com)
 *         <br>
 *         Copyright (c) Abdelkrime Aries
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
public class Faris {

	private HashSet<Substance> substances = new HashSet<Substance>();
	private HashSet<Action> actions = new HashSet<Action>();
	private HashMap<String, Mind> minds = new HashMap<String, Mind>();
	
	
	public Faris() {
		minds.put("Default", new Mind("Default"));
	}
	
	public boolean addStonDescription(String description){
		
		Parser parser = new Parser(description);
		
		if (! parser.parsed()) return false;
		
		HashMap<String, RRolePlayer> _players = parser.getPlayers();
		HashMap<String, RAction> _actions = parser.getActions();
		
		Mind defaultMind = minds.get("Default");
		
		for (String _actionID : _actions.keySet()){
			RAction raction = _actions.get(_actionID);
			Verb verb = new Verb(raction.getVerbSynSet());
			verb.setTense(Tense.getTense(raction.getTense()));
			verb.setAspect(Aspect.getAspect(raction.getAspect()));
			
			Action action = Action.getNew(verb);
			
			for (String subjID: raction.getSubjects()){
				RRolePlayer rsubject = _players.get(subjID);
				Substance subject = new Substance(rsubject.getNounSynSet());
				
				for(RAdjective radj : rsubject.getAdjectives()){
					
					//Adjective adjective = new Adjective();
				}
				
				subject.addAction(action);
				substances.add(subject);
				action.addSubject(subject);
			}
			
			actions.add(action);

		}
			
		return true;
	}


}
