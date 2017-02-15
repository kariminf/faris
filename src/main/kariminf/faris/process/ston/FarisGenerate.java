package kariminf.faris.process.ston;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import kariminf.faris.knowledge.Idea;
import kariminf.faris.knowledge.Mind;
import kariminf.faris.knowledge.Thought;
import kariminf.faris.knowledge.Mind.MentalState;
import kariminf.faris.linguistic.Verb;
import kariminf.faris.philosophical.Action;
import kariminf.faris.philosophical.Quality;
import kariminf.faris.philosophical.QuantSubstance;
import kariminf.sentrep.ston.request.ReqCreator;

//TODO search a substance and generate just its actions 
public class FarisGenerate {
	
	
	public static String getSynsetIdeas(Mind mind, int synSet){
		
		ReqCreator rq = new ReqCreator();
		//Affecting a label for each substance: subjects and objects
		int numRoles = 0;
		int numActions = 0;
		HashMap<QuantSubstance, String> roles = new HashMap<>();
		//HashMap<Action, String> actions = new HashMap<Action, String>();

		//Searching for substances that contain this synSet
		Set<Thought> ideas = mind.getThoughts(MentalState.FACT);

		for (Idea idea : ideas){
			//Ideas which are thoughts
			if (! (idea instanceof Thought)) continue;
			Action action = ((Thought) idea).getAction();
			
			//The subjects and the objects
			ArrayList<ArrayList<QuantSubstance>> subjects = new ArrayList<>();
			ArrayList<ArrayList<QuantSubstance>> objects = new ArrayList<>();
			
			//boolean found = false;
			for (ArrayList<QuantSubstance> _subjects: action.getAgents()){
				ArrayList<QuantSubstance> conjunctions = new ArrayList<>();
				for (QuantSubstance subject: _subjects){
					if (subject.getSubstance().getNounSynSet() != synSet)
						continue;
					//found = true;
					if(! roles.containsKey(subject)){
						String roleId = "srole-" + numRoles;
						roles.put(subject, roleId);
						conjunctions.add(subject);
						rq.addRolePlayer(roleId, subject.getSubstance().getNounSynSet());
						for(Quality quality: subject.getSubstance().getQualities()){
							rq.addAdjective(roleId, quality.getAdjective().getSynSet(), quality.getAdverbsInt());
						}

						numRoles++;
					}
					if (!conjunctions.isEmpty())
						subjects.add(conjunctions);
				}
			}

			for (ArrayList<QuantSubstance> _objects: action.getThemes()){
				ArrayList<QuantSubstance> conjunctions = new ArrayList<>();
				for(QuantSubstance object: _objects){
					if (object.getSubstance().getNounSynSet() != synSet)
						continue;
					//found = true;
					if(! roles.containsKey(object)){
						String roleId = "srole-" + numRoles;
						roles.put(object, roleId);
						conjunctions.add(object);
						rq.addRolePlayer(roleId, object.getSubstance().getNounSynSet());
						for(Quality quality: object.getSubstance().getQualities()){
							rq.addAdjective(roleId, quality.getAdjective().getSynSet(), quality.getAdverbsInt());
						}
						numRoles++;
					}

				}
				if (!conjunctions.isEmpty())
					objects.add(conjunctions);
			}

			if (!(subjects.isEmpty() && objects.isEmpty())){//found
				String actionId = "action-" + numActions;
				//actions.put(action, actionId);
				Verb verb = action.getVerb();
				rq.addAction(actionId, verb.getSynSet());
				
				rq.addSentence("AFF");
				ArrayList<String> act = new ArrayList<>();
				act.add(actionId);
				rq.addSentActionConjunctions(act);
				numActions++;

				ArrayList<ArrayList<QuantSubstance>> substances = 
						(subjects.isEmpty())?action.getAgents():subjects;

				for (ArrayList<QuantSubstance> _subjects: substances){
					ArrayList<String> subjectsIDs = new ArrayList<String>();
					for (QuantSubstance subject: _subjects){
						String roleId = "role-" + numRoles;

						if ( roles.containsKey(subject)){
							roleId = roles.get(subject);
						}
						else{
							roles.put(subject, roleId);
							rq.addRolePlayer(roleId, subject.getSubstance().getNounSynSet());
							numRoles++;
						}
					}
					rq.addAgentConjunctions(actionId, subjectsIDs);
				}

				substances = (objects.isEmpty())?action.getThemes():objects;

				for (ArrayList<QuantSubstance> _objects: substances){
					ArrayList<String> objectsIDs = new ArrayList<String>();
					for(QuantSubstance object: _objects){
						String roleId = "role-" + numRoles;

						if ( roles.containsKey(object)){
							roleId = roles.get(object);
						}
						else{
							roles.put(object, roleId);
							rq.addRolePlayer(roleId, object.getSubstance().getNounSynSet());
							numRoles++;
						}
					}
					rq.addThemeConjunctions(actionId, objectsIDs);
				}


			}


		}


		return rq.getStructuredRequest();
	}
	
	
public static String getAllIdeas(Mind mind){
		
		ReqCreator rq = new ReqCreator();
		//Affecting a label for each substance: subjects and objects
		int numRoles = 0;
		int numActions = 0;
		HashMap<QuantSubstance, String> roles = new HashMap<>();
		//HashMap<Action, String> actions = new HashMap<Action, String>();

		//Searching for substances that contain this synSet
		Set<Thought> ideas = mind.getThoughts(MentalState.FACT);

		for (Idea idea : ideas){
			//Ideas which are thoughts
			if (! (idea instanceof Thought)) continue;
			Action action = ((Thought) idea).getAction();
			
			//The subjects and the objects
			ArrayList<ArrayList<QuantSubstance>> subjects = new ArrayList<>();
			ArrayList<ArrayList<QuantSubstance>> objects = new ArrayList<>();
			
			//boolean found = false;
			for (ArrayList<QuantSubstance> _subjects: action.getAgents()){
				ArrayList<QuantSubstance> conjunctions = new ArrayList<>();
				for (QuantSubstance subject: _subjects){
					//found = true;
					if(! roles.containsKey(subject)){
						String roleId = "srole-" + numRoles;
						roles.put(subject, roleId);
						conjunctions.add(subject);
						rq.addRolePlayer(roleId, subject.getSubstance().getNounSynSet());
						for(Quality quality: subject.getSubstance().getQualities()){
							rq.addAdjective(roleId, quality.getAdjective().getSynSet(), quality.getAdverbsInt());
						}

						numRoles++;
					}
					if (!conjunctions.isEmpty())
						subjects.add(conjunctions);
				}
			}

			for (ArrayList<QuantSubstance> _objects: action.getThemes()){
				ArrayList<QuantSubstance> conjunctions = new ArrayList<>();
				for(QuantSubstance object: _objects){
					//found = true;
					if(! roles.containsKey(object)){
						String roleId = "srole-" + numRoles;
						roles.put(object, roleId);
						conjunctions.add(object);
						rq.addRolePlayer(roleId, object.getSubstance().getNounSynSet());
						for(Quality quality: object.getSubstance().getQualities()){
							rq.addAdjective(roleId, quality.getAdjective().getSynSet(), quality.getAdverbsInt());
						}
						numRoles++;
					}

				}
				if (!conjunctions.isEmpty())
					objects.add(conjunctions);
			}

			if (!(subjects.isEmpty() && objects.isEmpty())){//found
				String actionId = "action-" + numActions;
				//actions.put(action, actionId);
				Verb verb = action.getVerb();
				rq.addAction(actionId, verb.getSynSet());
				
				rq.addSentence("AFF");
				ArrayList<String> act = new ArrayList<>();
				act.add(actionId);
				rq.addSentActionConjunctions(act);
				numActions++;

				ArrayList<ArrayList<QuantSubstance>> substances = 
						(subjects.isEmpty())?action.getAgents():subjects;

				for (ArrayList<QuantSubstance> _subjects: substances){
					ArrayList<String> subjectsIDs = new ArrayList<String>();
					for (QuantSubstance subject: _subjects){
						String roleId = "role-" + numRoles;

						if ( roles.containsKey(subject)){
							roleId = roles.get(subject);
						}
						else{
							roles.put(subject, roleId);
							rq.addRolePlayer(roleId, subject.getSubstance().getNounSynSet());
							numRoles++;
						}
					}
					rq.addAgentConjunctions(actionId, subjectsIDs);
				}

				substances = (objects.isEmpty())?action.getThemes():objects;

				for (ArrayList<QuantSubstance> _objects: substances){
					ArrayList<String> objectsIDs = new ArrayList<String>();
					for(QuantSubstance object: _objects){
						String roleId = "role-" + numRoles;

						if ( roles.containsKey(object)){
							roleId = roles.get(object);
						}
						else{
							roles.put(object, roleId);
							rq.addRolePlayer(roleId, object.getSubstance().getNounSynSet());
							numRoles++;
						}
					}
					rq.addThemeConjunctions(actionId, objectsIDs);
				}


			}


		}


		return rq.getStructuredRequest();
	}

}
