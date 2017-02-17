package kariminf.faris.process.ston;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Set;

import kariminf.faris.linguistic.Adjective;
import kariminf.faris.linguistic.Adverb;
import kariminf.faris.linguistic.Noun;
import kariminf.faris.linguistic.ProperNoun;
import kariminf.faris.linguistic.Verb;
import kariminf.faris.process.Generator;
import kariminf.sentrep.ston.request.ReqCreator;

public class StonGenerator extends Generator<String> {
	
	private ReqCreator rc = new ReqCreator();
	
	private ArrayDeque<String> currentActIDs = new ArrayDeque<>();
	private ArrayDeque<String> currentRoleIDs = new ArrayDeque<>();
	
	
	private static enum Block{
		ACTION,
		ROLE,
		AGENT,
		THEME,
	}
	private ArrayDeque<Block> openBlocks = new ArrayDeque<>();
	
	private ArrayList<String> conj = null;
	

	@Override
	protected void beginActionHandler(String id, Verb verb, Set<Adverb> adverbs) {
		rc.addAction(id, verb.getSynSet());
		currentActIDs.push(id);
		openBlocks.push(Block.ACTION);
		//TODO action adverbs 
		
	}

	@Override
	protected void endActionHandler(String id) {
		if (openBlocks.peek() == Block.ACTION) openBlocks.pop();
		currentActIDs.pop();
	}

	@Override
	protected void beginAgentsHandler() {
		openBlocks.push(Block.AGENT);
		conj = new ArrayList<>();
	}

	@Override
	protected void endAgentsHandler() {
		if (openBlocks.peek() == Block.AGENT) openBlocks.pop();
		
	}

	@Override
	protected void beginThemesHandler() {
		openBlocks.push(Block.THEME);
		conj = new ArrayList<>();
	}

	@Override
	protected void endThemesHandler() {
		if (openBlocks.peek() == Block.THEME) openBlocks.pop();
		
	}

	@Override
	protected void beginDisjunctionHandler() {
		conj = new ArrayList<>();
		
	}

	@Override
	protected void endDisjunctionHandler() {
		
		if (conj == null) return;
		
		switch (openBlocks.peek()) {
		case ACTION:
			break;
		case AGENT:
		{
			if (currentActIDs.isEmpty()) break;
			String currentActID = currentActIDs.peek();
			rc.addAgentConjunctions(currentActID, conj);
			conj = new ArrayList<>();
			break;
		}
			
		case ROLE:
			break;
		case THEME:
		{
			if (currentActIDs.isEmpty()) break;
			String currentActID = currentActIDs.peek();
			rc.addThemeConjunctions(currentActID, conj);
			conj = new ArrayList<>();
			break;
		}
		default:
			break;
		}
		
		return;
		
		
	}

	@Override
	protected void beginSubstanceHandler(String id, Noun noun) {
		rc.addRolePlayer(id, noun.getSynSet());
		if (noun instanceof ProperNoun){
			rc.setRoleProperName(id, ((ProperNoun) noun).getName());
			//System.out.println("proper:" + ((ProperNoun) noun).getName());
		}
		
		currentRoleIDs.push(id);
		
		Block type = openBlocks.peek();
		if ( type == Block.AGENT || type == Block.THEME){
			conj.add(id);
		}
		
		openBlocks.push(Block.ROLE);
		
	}

	@Override
	protected void endSubstanceHandler() {
		if (openBlocks.peek() == Block.ROLE) openBlocks.pop();
		currentRoleIDs.pop();
		
	}

	@Override
	protected void addQuantityHandler(double nbr, Noun unit) {
		if (nbr == 1.0) return;
		if (openBlocks.peek() != Block.ROLE) return;
		if (currentRoleIDs.isEmpty()) return;
		String quantity = "" + nbr;
		rc.setQuantity(currentRoleIDs.peek(), quantity);
		
	}

	@Override
	protected void addQualityHandler(Adjective adjective, ArrayList<Adverb> adverbs) {
		if (openBlocks.peek() != Block.ROLE) return;
		if (currentRoleIDs.isEmpty()) return;
		ArrayList<Integer> advSynSets = new ArrayList<>();
		for (Adverb adv: adverbs) advSynSets.add(adv.getSynSet());
		rc.addAdjective(currentRoleIDs.peek(), adjective.getSynSet(), advSynSets);
		
	}

	@Override
	public String generate() {
		return rc.getStructuredRequest();
	}

	@Override
	protected void substanceFoundHandler(String id) {
		Block type = openBlocks.peek();
		if ( type == Block.AGENT || type == Block.THEME){
			conj.add(id);
			return;
		}
		
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
