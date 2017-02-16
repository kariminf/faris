package kariminf.faris.process.ston;

import java.util.ArrayList;
import java.util.Set;

import kariminf.faris.linguistic.Adjective;
import kariminf.faris.linguistic.Adverb;
import kariminf.faris.linguistic.Noun;
import kariminf.faris.linguistic.Verb;
import kariminf.faris.process.Generator;
import kariminf.sentrep.ston.request.ReqCreator;

public class StonGenerator extends Generator {
	
	private ReqCreator rc = new ReqCreator();
	
	private ArrayList<String> conj = null;
	
	//0 nothing, 1: action, 2: substance
	private int type = 0;

	@Override
	protected void beginActionHandler(String id, Verb verb, Set<Adverb> adverbs) {
		rc.addAction(id, verb.getSynSet());
		//TODO action adverbs 
		
	}

	@Override
	protected void endActionHandler() {
	}

	@Override
	protected void beginAgentsHandler() {
		
		
	}

	@Override
	protected void endAgentsHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void beginThemesHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void endThemesHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void beginDisjunctionHandler() {
		conj = new ArrayList<>();
		
	}

	@Override
	protected void endDisjunctionHandler() {
		conj = null;
		
	}

	@Override
	protected void beginSubstanceHandler(String id, Noun noun) {
		rc.addRolePlayer(id, noun.getSynSet());
		
	}

	@Override
	protected void endSubstanceHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addQuantityHandler(double nbr, Noun unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addQualityHandler(Adjective adjective, ArrayList<Adverb> adverbs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String generate() {
		return rc.getStructuredRequest();
	}

	@Override
	protected void substanceFoundHandler(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void actionFoundHandler(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void beginIdeaHandler() {
		//AFF, //Affirmation
		//EXC, //exclamation
		//QST, //Question
		//IMP //imperative
		
		rc.addSentence("AFF");
		
	}

	@Override
	protected void endIdeaHandler() {
		// TODO Auto-generated method stub
		
	}
	
	

	


}
