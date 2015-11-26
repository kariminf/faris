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

public class FarisParse extends Parser {

	private HashSet<Substance> substances;
	private HashSet<Action> actions;
	private HashMap<String, Mind> minds;
	
	private Substance currentPlayer;
	//private String currentPlayerID;
	HashMap<String, Substance> _players = new HashMap<String, Substance>();
	
	private Action currentAction;
	//private String currentPlayerID;
	HashMap<String, Action> _actions = new HashMap<String, Action>();
	
	
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
	protected void addVerbSpecif(int tense, int aspect) {
		Verb verb = currentAction.getVerb();
		verb.setTense(Tense.valueOf(tense));
		verb.setAspect(Aspect.valueOf(aspect));
	}

	@Override
	protected void addSubject(String subjectID) {
		if (_players.containsKey(subjectID)){
			Substance subject = _players.get(subjectID);
			currentAction.addSubject(subject);
		}
		
	}

	@Override
	protected void addObject(String objectID) {
		if (_players.containsKey(objectID)){
			Substance object = _players.get(objectID);
			currentAction.addObject(object);
		}
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

}
