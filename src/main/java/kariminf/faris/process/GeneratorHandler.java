package kariminf.faris.process;

import java.time.LocalDateTime;
import java.util.Set;

import kariminf.faris.linguistic.Adjective;
import kariminf.faris.linguistic.Adverb;
import kariminf.faris.linguistic.Noun;
import kariminf.faris.linguistic.Verb;
import kariminf.sentrep.types.Comparison;
import kariminf.sentrep.types.Relation.Adpositional;


/*
 * I did split this handler from Generator, because each time I modify in
 * processor interface, the implementation does to the extended class of Generator. 
 * Why this is not an interface? because I want to keep protected access mode
 */


public abstract class GeneratorHandler<T> {

	/**
	 * When an action is found, this method will be called
	 * @param id the ID of the action
	 * @param verb the verb describing the action
	 * @param adverbs the adverbs modifying the verb
	 */
	protected abstract void beginActionHandler(String id, Verb verb, Set<Adverb> adverbs);

	/**
	 * This is called when the action ends (all its components have been processed
	 * @param id The ID of the action
	 */
	protected abstract void endActionHandler(String id, Verb verb, Set<Adverb> adverbs);

	/**
	 * This is called to mark the start of the current action's agents enumeration
	 */
	protected abstract void beginAgentsHandler(String actID);

	/**
	 * This is called to mark the end of current action's agents enumeration
	 */
	protected abstract void endAgentsHandler(String actID);

	/**
	 * This is called to mark the start of current action's themes enumeration
	 */
	protected abstract void beginThemesHandler(String actID);

	/**
	 * This is called to mark the end of current action's themes enumeration
	 */
	protected abstract void endThemesHandler(String actID);

	/**
	 * This is called whenever there is an enumeration; each time it is called 
	 * it marks a disjunction "OR". The components called after are conjunctions "AND"
	 */
	protected abstract void beginDisjunctionHandler();

	/**
	 * This is called when the disjunction of elements is over, and to start a new disjunction
	 * if there is any
	 */
	protected abstract void endDisjunctionHandler();

	//If the substance has a noun with synset 0, so it is the pronoun it
	//for example it is believed
	/**
	 * This is called when a substance is found; A substance and a quantified substance
	 * identical to it are considered as two distinct substances
	 * @param id the ID of the substance
	 * @param noun the noun, which can of type ProperNoun as well
	 */
	protected abstract void beginSubstanceHandler(String id, Noun noun);

	/**
	 * This is called when a substance is found, but it was already processed earlier
	 * @param id the ID of the substance
	 */
	protected abstract void substanceFoundHandler(String id);

	/**
	 * This is called when an action is found, but it was already processed earlier
	 * @param id
	 */
	protected abstract void actionFoundHandler(String id);

	/**
	 * This marks the end of a substance processing
	 */
	protected abstract void endSubstanceHandler(String id, Noun noun);

	/**
	 * This is called when we want to add a quantity to the current substance
	 * @param nbr The quantity
	 * @param unit the unit of the quantity
	 */
	protected abstract void addQuantityHandler(double nbr, Noun unit, boolean cardinal);

	/**
	 * This is called when we want to add a quantity to the current substance. 
	 * It is used to say the substance is plural
	 * @param unit the unit of the quantity
	 */
	protected abstract void addQuantityHandler(Noun unit);

	/**
	 * This is called when we want to add a quality to the current substance
	 * @param adjective the adjective that describes this quality
	 * @param adverbs the adverbs modifying this adjective
	 */
	protected abstract void addQualityHandler(Adjective adjective, Set<Adverb> adverbs);

	/**
	 * This is called when an Idea has been found
	 */
	protected abstract void addIdeaHandler(String actionID);

	/**
	 * This is called when the current substance has a probable action state in one of 
	 * the relative actions in a substance
	 * @param isAgent if true, then the current substance is an agent, 
	 * otherwise it is a theme
	 * @param relIDs a list of probable relative actions
	 */
	protected abstract void addStateHandler(boolean isAgent, String stateID);

	protected abstract void beginStateHandler(String subID, String actID);

	protected abstract void endStateHandler(String subID, String actID);

	protected abstract void beginPlaceHandler(Adpositional relation, Adverb adv);

	protected abstract void endPlaceHandler(Adpositional relation, Adverb adv);

	protected abstract void beginTimeHandler(Adpositional relation, Adverb adv, LocalDateTime datetime);

	protected abstract void endTimeHandler(Adpositional relation, Adverb adv, LocalDateTime datetime);

	protected abstract void beginActionRelativeHandler(String actID);

	protected abstract void endActionRelativeHandler(String actID);

	protected abstract void beginSubstanceRelativeHandler(String subID);

	protected abstract void endSubstanceRelativeHandler(String subID);

	/**
	 * 
	 * @param cmp if it is null, then it is a relative OF between a substance and another
	 * @param adjective
	 * @param relID
	 */
	protected abstract void addRelativeHandler (Comparison cmp, Adjective adjective, String relID);

	/**
	 * This is called to generate a representation of a given type 
	 * @return
	 */
	public abstract T generate();

}
