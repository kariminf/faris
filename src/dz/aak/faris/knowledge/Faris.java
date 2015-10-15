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

import dz.aak.faris.knowledge.Mind.Truth;
import dz.aak.faris.linguistic.Adjective;
import dz.aak.faris.linguistic.Verb;
import dz.aak.faris.linguistic.Verb.Aspect;
import dz.aak.faris.linguistic.Verb.Tense;
import dz.aak.faris.philosophical.Action;
import dz.aak.faris.philosophical.Quality;
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
		
		HashMap<String, RRolePlayer> _rplayers = parser.getPlayers();
		HashMap<String, RAction> _ractions = parser.getActions();
		
		HashMap<String, Substance> _players = new HashMap<String, Substance>();
		//HashMap<String, Action> _actions = new HashMap<String, Action>();
		
		Mind defaultMind = minds.get("Default");
		
		for (String _actionID : _ractions.keySet()){
			RAction raction = _ractions.get(_actionID);
			Verb verb = new Verb(raction.getVerbSynSet());
			verb.setTense(Tense.getTense(raction.getTense()));
			verb.setAspect(Aspect.getAspect(raction.getAspect()));
			
			Action action = Action.getNew(verb);
			
			for (String subjID: raction.getSubjects()){
				
				Substance subject;
				if (_players.containsKey(subjID)){
					subject = _players.get(subjID);
					subject.addAction(action);
					action.addSubject(subject);
					continue;
				}
				
				RRolePlayer rsubject = _rplayers.get(subjID);
				subject = new Substance(rsubject.getNounSynSet());
				
				for(RAdjective radjective : rsubject.getAdjectives()){ 
					Quality quality = 
							new Quality(new Adjective(radjective.getAdjSynSet()));
					quality.setAdverbsInt(radjective.getAdvSynSets());
					subject.addQuality(quality);
				}
				
				subject.addAction(action);
				substances.add(subject);
				action.addSubject(subject);
				_players.put(subjID, subject);
			}
			
			for (String objID: raction.getObjects()){
				Substance object;
				if (_players.containsKey(objID)){
					object = _players.get(objID);
					object.addAffection(action);
					action.addObject(object);
					continue;
				}
				
				RRolePlayer robject = _rplayers.get(objID);
				object = new Substance(robject.getNounSynSet());
				
				for(RAdjective radjective : robject.getAdjectives()){ 
					Quality quality = 
							new Quality(new Adjective(radjective.getAdjSynSet()));
					quality.setAdverbsInt(radjective.getAdvSynSets());
					object.addQuality(quality);
				}
				
				object.addAffection(action);
				substances.add(object);
				action.addObject(object);
				_players.put(objID, object);
			}
			
			actions.add(action);
			
			defaultMind.addAction(Truth.FACT, action);

		}
			
		return true;
	}
	
	
	public String getNoAdjectives(String mindLabel){
		
		if (! minds.containsKey(mindLabel)) return "";
		
		Mind mind = minds.get(mindLabel);
		
		return mind.getNoAdjectives();
	}
	
	/**
	 * 
	 * @return
	 */
	public String info(){
		
		String result = "Faris:\n";
		result += "There are " + minds.size() + " pricipal mind(s)\n";
		result += "There are " + substances.size() + " different substances\n";
		result += "There are " + actions.size() + " different actions\n";
		
		return result;
	}


}
