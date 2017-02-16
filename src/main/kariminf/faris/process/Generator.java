/* FARIS : Factual Arrangement and Representation of Ideas in Sentences
 * FAris : Farabi & Aristotle
 * Faris : A knight (in Arabic)
 * --------------------------------------------------------------------
 * Copyright (C) 2017 Abdelkrime Aries (kariminfo0@gmail.com)
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

package kariminf.faris.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import kariminf.faris.knowledge.Mind.MentalState;
import kariminf.faris.linguistic.*;
import kariminf.faris.philosophical.*;

/**
 * A generator which generates a text or any thing from a Faris representation (model)
 * @author Abdelkrime Aries
 *
 */
public abstract class Generator {

	private HashMap<Action, Integer> actionIDs = new HashMap<Action, Integer>();
	private int actionsNbr = 0;
	
	private HashMap<Substance, Integer> substanceIDs = new HashMap<>();
	private HashMap<QuantSubstance, Integer> qsubstanceIDs = new HashMap<>();
	
	private int substancesNbr = 0;

	/**
	 * 
	 * @param action
	 */
	public void processAction(Action action){

		//We don't add an action, already there
		if (actionIDs.containsKey(action)) return;

		actionIDs.put(action, actionsNbr);
		String actID = "act" + actionsNbr;
		actionsNbr++;

		beginActionHandler(actID, action.getVerb(), action.getAdverbs());

		beginAgentsHandler();
		processDisjunctions(action.getAgents());
		endAgentsHandler();

		beginThemesHandler();
		processDisjunctions(action.getThemes());
		endThemesHandler();

		endActionHandler();


	}

	private void processDisjunctions(ArrayList<ArrayList<QuantSubstance>> disj){
		//Disjunctions 
		for(ArrayList<QuantSubstance> conj: disj){
			beginDisjunctionHandler();
			for (QuantSubstance substance: conj){
				substance.generate(this);
			}

			endDisjunctionHandler();
		}
	}
	
	private void addSubstance(String id, Substance sub, Quantity q){
		
		beginSubstanceHandler(id, sub.getNoun());
		
		if (q != null) q.generate(this);
		for (Quality ql : sub.getQualities()) ql.generate(this);
		endSubstanceHandler();
	}
	
	public void processQuality(Quality ql){
		addQualityHandler(ql.getAdjective(), ql.getAdverbs());
	}
	
	public void processQuantity(Quantity q){
		Noun unit = (q.getUnit() == null)? null: q.getUnit().getNoun();
		addQuantityHandler(q.getNumber(), unit);
	}

	public void processSubstance(QuantSubstance qsub){
		String id = "r" + substancesNbr;
		if (qsubstanceIDs.containsKey(qsub)){
			substanceFoundHandler(id);
			return;
		}
		substancesNbr++;
		qsubstanceIDs.put(qsub, substancesNbr);
		addSubstance(id, qsub.getSubstance(), qsub.getQuantity());
	}

	public void processSubstance(Substance sub){
		String id = "r" + substancesNbr;
		if (substanceIDs.containsKey(sub)){
			substanceFoundHandler(id);
			return;
		}
		substancesNbr++;
		substanceIDs.put(sub, substancesNbr);
		addSubstance(id, sub, null);
	}
	
	public void processMind(QuantSubstance s){
		//TODO When we already processed a substance, and found it was referenced again
	}
	
	public void processMentalState(MentalState ms){
		//A mental state of a mind
	}
	
	
	//Abstract methods
	
	protected abstract void beginActionHandler(String id, Verb verb, Set<Adverb> adverbs);

	protected abstract void endActionHandler();

	protected abstract void beginAgentsHandler();

	protected abstract void endAgentsHandler();

	protected abstract void beginThemesHandler();

	protected abstract void endThemesHandler();

	protected abstract void beginDisjunctionHandler();

	protected abstract void endDisjunctionHandler();

	//If the substance has a noun with synset 0, so it is the pronoun it
	//for example it is believed
	protected abstract void beginSubstanceHandler(String id, Noun noun);
	
	//
	protected abstract void substanceFoundHandler(String id);
	
	protected abstract void actionFoundHandler(String id);
	
	protected abstract void endSubstanceHandler();

	protected abstract void addQuantityHandler(double nbr, Noun unit);
	
	protected abstract void addQualityHandler(Adjective adjective, ArrayList<Adverb> adverbs);
	
	protected abstract void beginIdeaHandler();
	
	protected abstract void endIdeaHandler();
	
	public abstract String generate();
}
