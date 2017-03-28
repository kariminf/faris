package kariminf.faris.process.ston;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import kariminf.faris.linguistic.Adjective;
import kariminf.faris.linguistic.Adverb;
import kariminf.faris.linguistic.Noun;
import kariminf.faris.linguistic.POS;
import kariminf.faris.linguistic.ProperNoun;
import kariminf.faris.linguistic.Verb;
import kariminf.faris.process.Generator;
import kariminf.sentrep.ston.Univ2StonMap;
import kariminf.sentrep.ston.request.ReqCreator;
import kariminf.sentrep.ston.request.ReqRolePlayer;
import kariminf.sentrep.types.Comparison;
import kariminf.sentrep.types.Relation.Adpositional;

public class StonGenerator extends Generator<String> {
	
	private ReqCreator rc = new ReqCreator();
	
	private Univ2StonMap u2sMap = new Univ2StonMap();
	
	private ArrayDeque<String> currentActIDs = new ArrayDeque<>();
	private ArrayDeque<String> currentRoleIDs = new ArrayDeque<>();
	
	
	private static enum Block{
		ACTION,
		ROLE,
		AGENT,
		THEME,
		PLACE,
		TIME
	}
	
	private static final List<Block> conjBlocks = 
			Arrays.asList(Block.AGENT, Block.THEME, Block.PLACE, Block.TIME);
	private ArrayDeque<Block> openBlocks = new ArrayDeque<>();
	
	private ArrayList<String> conj = null;
	
	private ArrayList<String> statesSUB = null;
	private ArrayList<String> statesOBJ = null;
	//This is used when a substance has been used with some action before
	//So if it changes states with another, we must create new substance
	private int stateCounter = 0;
	private HashMap<String, List<String> > linkStates = new HashMap<>();
	private HashMap<String, ReqRolePlayer> prevStates = new HashMap<>();
	
	private Adpositional adpos = null;
	

	@Override
	protected void beginActionHandler(String actID, Verb verb, Set<Adverb> adverbs) {
		rc.addAction(actID, verb.getSynSet());
		{
			String tense = u2sMap.getTense(verb.getTense());
			//TODO modality and negation of a verb
			rc.addVerbSpecif(actID, tense , "NONE" , verb.isProgressive(), verb.isPerfect(), false);
			
		}
		
		currentActIDs.push(actID);
		openBlocks.push(Block.ACTION);
		
		rc.addActionAdverbs(actID, POS.getSynsets(adverbs));
		
	}

	@Override
	protected void endActionHandler(String actID, Verb verb, Set<Adverb> adverbs) {
		if (openBlocks.peek() == Block.ACTION) openBlocks.pop();
		currentActIDs.pop();
	}

	@Override
	protected void beginAgentsHandler(String actID) {
		openBlocks.push(Block.AGENT);
		//conj = new ArrayList<>();
	}

	@Override
	protected void endAgentsHandler(String actID) {
		if (openBlocks.peek() == Block.AGENT) openBlocks.pop();
		
	}

	@Override
	protected void beginThemesHandler(String actID) {
		openBlocks.push(Block.THEME);
		//conj = new ArrayList<>();
	}

	@Override
	protected void endThemesHandler(String actID) {
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
			
		}
		break;
		case ROLE:
			break;
		case THEME:
		{
			if (currentActIDs.isEmpty()) break;
			String currentActID = currentActIDs.peek();
			rc.addThemeConjunctions(currentActID, conj);
			
		}
		break;
		case PLACE:
		{
			if (currentActIDs.isEmpty()) {
				break;
			}
			String currentActID = currentActIDs.peek();
			//System.out.println("place>>>" + u2sMap.getAdposition(adpos, ""));
			rc.addRelative(u2sMap.getAdposition(adpos, ""), currentActID);
			rc.addRelativeConjunctions(conj);
			
		}
		break;
		case TIME:
		{
			if (currentActIDs.isEmpty()) break;
			String currentActID = currentActIDs.peek();
			//System.out.println("time>>>" + u2sMap.getAdposition(adpos, ""));
			rc.addRelative(u2sMap.getAdposition(adpos, ""), currentActID);
			rc.addRelativeConjunctions(conj);
			conj = new ArrayList<>();
		}
		break;
		default:
			break;
		}
		
		conj = null;
	}

	@Override
	protected void beginSubstanceHandler(String subID, Noun noun) {
		rc.addRolePlayer(subID, noun.getSynSet());
		if (noun instanceof ProperNoun){
			rc.setRoleProperName(subID, ((ProperNoun) noun).getName());
			//System.out.println("proper:" + ((ProperNoun) noun).getName());
		}
		
		currentRoleIDs.push(subID);
		
		if (conjBlocks.contains(openBlocks.peek())){
			conj.add(subID);
		}
		
		openBlocks.push(Block.ROLE);
		
	}

	@Override
	protected void endSubstanceHandler(String subID, Noun noun) {
		if (openBlocks.peek() == Block.ROLE) openBlocks.pop();
		currentRoleIDs.pop();
		
	}

	@Override
	protected void addQuantityHandler(double nbr, Noun unit, boolean cardinal) {
		if (cardinal && (nbr == 1.0)) return;
		if (openBlocks.peek() != Block.ROLE) return;
		if (currentRoleIDs.isEmpty()) return;
		String quantity = (cardinal)? "": "O";
		quantity += nbr;
		rc.setQuantity(currentRoleIDs.peek(), quantity);
		
	}
	
	@Override
	protected void addQuantityHandler(Noun unit) {
		if (openBlocks.peek() != Block.ROLE) return;
		if (currentRoleIDs.isEmpty()) return;
		rc.setQuantity(currentRoleIDs.peek());
		
	}

	@Override
	protected void addQualityHandler(Adjective adjective, Set<Adverb> adverbs) {
		if (openBlocks.peek() != Block.ROLE) return;
		if (currentRoleIDs.isEmpty()) return;
		/*ArrayList<Integer> advSynSets = new ArrayList<>();
		for (Adverb adv: adverbs) advSynSets.add(adv.getSynSet());*/
		rc.addAdjective(currentRoleIDs.peek(), adjective.getSynSet(), POS.getSynsets(adverbs));
		
	}

	@Override
	public String generate() {
		return rc.getStructuredRequest();
	}

	@Override
	protected void substanceFoundHandler(String id) {
		if (conjBlocks.contains(openBlocks.peek())){
			conj.add(id);
		}
		
	}

	@Override
	protected void actionFoundHandler(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addIdeaHandler(String actionID) {
		//AFF, //Affirmation
		//EXC, //exclamation
		//QST, //Question
		//IMP //imperative
		rc.addSentence("AFF");
		rc.addSentMainActConjunctions(new String[]{actionID});		
	}

	@Override
	protected void addStateHandler(boolean isAgent, String stateID) {
		if(currentActIDs.isEmpty()) return;
		if(currentRoleIDs.isEmpty()) return;
		//System.out.println("state:" + stateID);
		if (isAgent){
			statesSUB.add(stateID);
			return;
		}
		
		statesOBJ.add(stateID);
		
	}

	@Override
	protected void beginStateHandler(String subID, String actID) {
		statesSUB = new ArrayList<>();
		statesOBJ = new ArrayList<>();
		
	}

	@Override
	protected void endStateHandler(String subID, String actID) {
		
		if (statesOBJ.isEmpty() && statesSUB.isEmpty()) return;
		
		String id = subID;
		
		
		if (prevStates.containsKey(subID)){
			
			//TODO Verify if it has a same state
			
			
			return;
			
		} else {
			
			//Add the request role player without states
			prevStates.put(subID, rc.getReqRolePlayer(subID));
			ArrayList<String> subStates = new ArrayList<>();
			linkStates.put(subID, subStates);
			
			
			//Here we save a copy of substance subID
			//if (rrp != null) prevStates.put(subID, rrp);
		}
		
		//Here, we replace subID with id
		if (statesSUB.isEmpty() && statesOBJ.isEmpty()){
			//rc.replaceRoleInAction(actID, subID, id);
			return;
		}
		
		id = subID + "s" + (stateCounter++) + actID;
		ReqRolePlayer rrp = ReqRolePlayer.create(id, rc.getReqRolePlayer(subID));
		rc.addRolePlayer(rrp);
		rc.replaceRoleInAction(actID, subID, id);
		
		subID = id;
		
		if (currentRoleIDs.peek().equals(subID)){
			currentActIDs.pop();
			currentActIDs.push(id);
		}
		
		if(!statesSUB.isEmpty()){
			rc.addRelative("SBJ", id);
			rc.addRelativeConjunctions(statesSUB);
			
		}
		
		if(!statesOBJ.isEmpty()){
			rc.addRelative("OBJ", id);
			rc.addRelativeConjunctions(statesOBJ);
		}

		
	}

	@Override
	protected void beginPlaceHandler(Adpositional relation, Adverb adv) {
		openBlocks.push(Block.PLACE);
		adpos = relation;
		
		if (adv == null) return;
		if (currentActIDs.isEmpty()) return;
		
		String currentActID = currentActIDs.peek();
		
		rc.addActionAdverb(currentActID, adv.getSynSet());
	}

	@Override
	protected void endPlaceHandler(Adpositional relation, Adverb adv) {
		if (openBlocks.peek() == Block.PLACE) openBlocks.pop();
		adpos = null;
	}

	@Override
	protected void beginTimeHandler(Adpositional relation, Adverb adv, LocalDateTime datetime) {
		openBlocks.push(Block.TIME);
		adpos = relation;
		
		if (currentActIDs.isEmpty()) return;
		String currentActID = currentActIDs.peek();
		
		if (adv != null)
			rc.addActionAdverb(currentActID, adv.getSynSet());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		//String date = datetime.format(dtf);
		//TODO create a new substance with this date
		
		
	}

	@Override
	protected void endTimeHandler(Adpositional relation, Adverb adv, LocalDateTime datetime) {
		if (openBlocks.peek() == Block.TIME) openBlocks.pop();
		adpos = null;
		
	}

	@Override
	protected void beginActionRelativeHandler(String actID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void endActionRelativeHandler(String actID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void beginSubstanceRelativeHandler(String subID) {
		
	}

	@Override
	protected void endSubstanceRelativeHandler(String subID) {
		
	}

	@Override
	protected void addRelative(Comparison cmp, Adjective adjective, String relID) {
		
		//Here it is a relative such as: the mother OF the child
		if (cmp == null) {
			//System.out.println("Relative OF: " + relID);
			if (openBlocks.peek() != Block.ROLE) return;
			String roleID = currentRoleIDs.peek();
			
			rc.addRelative(u2sMap.getAdposition(Adpositional.POSSESSION, ""), roleID);
			rc.addRelativeConjunctions(new String[]{relID});
		}
		
		
		
	}
	

}
