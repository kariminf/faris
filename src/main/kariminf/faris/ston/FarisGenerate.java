package kariminf.faris.ston;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kariminf.faris.knowledge.Idea;
import kariminf.faris.knowledge.Mind;
import kariminf.faris.knowledge.Thought;
import kariminf.faris.knowledge.Mind.MentalState;
import kariminf.faris.linguistic.Verb;
import kariminf.faris.philosophical.Action;
import kariminf.faris.philosophical.Quality;
import kariminf.faris.philosophical.Substance;
import kariminf.sentrep.ston.request.ReqCreator;

public class FarisGenerate {
	
	
	public static String getSynsetIdeas(Mind mind, int synSet){
		ReqCreator rq = new ReqCreator();
		//Affecting a label for each substance: subjects and objects
		int numRoles = 0;
		int numActions = 0;
		HashMap<Substance, String> roles = new HashMap<Substance, String>();
		//HashMap<Action, String> actions = new HashMap<Action, String>();

		//Searching for substances that contain this synSet
		List<Idea> ideas = mind.getIdeas(MentalState.FACT);

		for (Idea idea : ideas){
			//Ideas which are thoughts
			if (! (idea instanceof Thought)) continue;
			Action action = ((Thought) idea).getAction();
			
			//The subjects and the objects
			ArrayList<ArrayList<Substance>> subjects = new ArrayList<ArrayList<Substance>>();
			ArrayList<ArrayList<Substance>> objects = new ArrayList<ArrayList<Substance>>();
			
			//boolean found = false;
			for (ArrayList<Substance> _subjects: action.getSubjects()){
				ArrayList<Substance> conjunctions = new ArrayList<Substance>();
				for (Substance subject: _subjects){
					if (subject.getNounSynSet() != synSet)
						continue;
					//found = true;
					if(! roles.containsKey(subject)){
						String roleId = "srole-" + numRoles;
						roles.put(subject, roleId);
						conjunctions.add(subject);
						rq.addRolePlayer(roleId, subject.getNounSynSet());
						for(Quality quality: subject.getQualities()){
							rq.addAdjective(roleId, quality.getAdjective().getSynSet(), quality.getAdverbsInt());
						}

						numRoles++;
					}
					if (!conjunctions.isEmpty())
						subjects.add(conjunctions);
				}
			}

			for (ArrayList<Substance> _objects: action.getObjects()){
				ArrayList<Substance> conjunctions = new ArrayList<Substance>();
				for(Substance object: _objects){
					if (object.getNounSynSet() != synSet)
						continue;
					//found = true;
					if(! roles.containsKey(object)){
						String roleId = "srole-" + numRoles;
						roles.put(object, roleId);
						conjunctions.add(object);
						rq.addRolePlayer(roleId, object.getNounSynSet());
						for(Quality quality: object.getQualities()){
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
				numActions++;

				ArrayList<ArrayList<Substance>> substances = 
						(subjects.isEmpty())?action.getSubjects():subjects;

				for (ArrayList<Substance> _subjects: substances){
					ArrayList<String> subjectsIDs = new ArrayList<String>();
					for (Substance subject: _subjects){
						String roleId = "role-" + numRoles;

						if ( roles.containsKey(subject)){
							roleId = roles.get(subject);
						}
						else{
							roles.put(subject, roleId);
							rq.addRolePlayer(roleId, subject.getNounSynSet());
							numRoles++;
						}
					}
					rq.addAgentConjunctions(actionId, subjectsIDs);
				}

				substances = (objects.isEmpty())?action.getObjects():objects;

				for (ArrayList<Substance> _objects: substances){
					ArrayList<String> objectsIDs = new ArrayList<String>();
					for(Substance object: _objects){
						String roleId = "role-" + numRoles;

						if ( roles.containsKey(object)){
							roleId = roles.get(object);
						}
						else{
							roles.put(object, roleId);
							rq.addRolePlayer(roleId, object.getNounSynSet());
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
