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


package dz.aak.faris.ston;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import dz.aak.faris.knowledge.Mind;
import dz.aak.faris.knowledge.Mind.Truth;
import dz.aak.faris.linguistic.Adjective;
import dz.aak.faris.linguistic.Verb;
import dz.aak.faris.linguistic.Verb.Aspect;
import dz.aak.faris.linguistic.Verb.Tense;
import dz.aak.faris.philosophical.Action;
import dz.aak.faris.philosophical.Quality;
import dz.aak.faris.philosophical.Substance;
import dz.aak.sentrep.ston.Parser;

/**
 * 
 * @author Abdelkrime Aries (kariminfo0@gmail.com)
 *         <br>
 *         Copyright (c) 2015 Abdelkrime Aries
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
public class FarisParse extends Parser {

	private HashSet<Substance> substances;
	private HashSet<Action> actions;
	private HashMap<String, Mind> minds;
	
	private Substance currentPlayer;
	//private String currentPlayerID;
	private HashMap<String, Substance> _players = new HashMap<String, Substance>();
	
	private Action currentAction;
	//private String currentPlayerID;
	private HashMap<String, Action> _actions = new HashMap<String, Action>();
	
	private Set<Substance> conjunctions = new HashSet<Substance>();
	private boolean subject = true;
	
	public FarisParse(HashSet<Substance> substances, HashSet<Action> actions, HashMap<String, Mind> minds){
		this.substances = substances;
		this.actions = actions;
		this.minds = minds;
	}

	@Override
	protected void beginAction(String id, int synSet) {
		Verb verb = new Verb(synSet);
		currentAction = Action.getNew(verb);
		_actions.put(id, currentAction);
		
	}

	@Override
	protected void addVerbSpecif(String tense, String aspect) {
		Verb verb = currentAction.getVerb();
		verb.setTense(Tense.valueOf(tense));
		verb.setAspect(Aspect.valueOf(aspect));
	}

	@Override
	protected void endAction() {
	}

	@Override
	protected void actionFail() {
	}

	@Override
	protected void beginRole(String id, int synSet) {
		currentPlayer = new Substance(synSet);
		_players.put(id, currentPlayer);
		/*
		if (! players.containsKey(id)){
			players.put(id, currentPlayer);
		}*/
	}

	@Override
	protected void addAdjective(int synSet, Set<Integer> advSynSets) {
		Adjective adj = new Adjective(synSet);
		Quality quality = new Quality(adj);
		quality.setAdverbsInt(advSynSets);
		currentPlayer.addQuality(quality);
		
	}

	@Override
	protected void endRole() {
		
	}

	@Override
	protected void adjectiveFail() {
	}

	@Override
	protected void roleFail() {
		
	}

	@Override
	protected void parseSuccess() {
		
		for(Substance sub : _players.values()){
			substances.add(sub);
		}
		
		for(Action action: _actions.values()){
			actions.add(action);
			Mind defaultMind = minds.get("Default");
			defaultMind.addAction(Truth.FACT, action);
		}
		
		
	}

	@Override
	protected void beginActions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void endActions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void beginRoles() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void endRoles() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void beginSubject() {
		subject = true;
	}

	@Override
	protected void beginObject() {
		subject = false;
	}

	@Override
	protected void beginDisjunction() {
		conjunctions = new HashSet<Substance>();
	}

	@Override
	protected void addConjunction(String roleID) {
		if (_players.containsKey(roleID)){
			Substance role = _players.get(roleID);
			conjunctions.add(role);
		}
	}

	@Override
	protected void endDisjunction() {
		if (subject){
			currentAction.addConjunctSubjects(conjunctions);
		} else {
			currentAction.addConjunctObjects(conjunctions);
		}
	}

	@Override
	protected void endSubject() {
	}

	@Override
	protected void endObject() {
		
	}

}
