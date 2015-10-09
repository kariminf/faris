package dz.aak.faris.ston;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RAction {

	/**
	 * @return the verbSynSet
	 */
	public int getVerbSynSet() {
		return verbSynSet;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the subjects
	 */
	public Set<String> getSubjects() {
		return subjects;
	}

	/**
	 * @return the objects
	 */
	public Set<String> getObjects() {
		return objects;
	}

	/**
	 * @return the tense
	 */
	public int getTense() {
		return tense;
	}

	/**
	 * @return the aspect
	 */
	public int getAspect() {
		return aspect;
	}


	private int verbSynSet;
	private String id;
	private Set<String> subjects = new HashSet<String>();
	private Set<String> objects = new HashSet<String>();
	private static Set<String> ids = new HashSet<String>();
	
	private int tense = 0;
	private int aspect = 0;
	
	private RAction(String id, int verbSynSet) {
		this.verbSynSet = verbSynSet;
		this.id = id;
	}
	
	public static RAction create(String id, int verbSynSet){
		//protection for same ids
		if(ids.contains(id)) return null;
		ids.add(id);
		return new RAction(id, verbSynSet);
	}
	
	public void addVerbSpecif(int i, int j){
		if (i > 0 && i < 3) this.tense = i;
		if (j > 0 && j < 3) this.aspect = j;
	}
	
	public void addSubject(String roleId){
		subjects.add(roleId);
	}
	
	public void addObject(String roleId){
		objects.add(roleId);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "act:{";
		
		
		result += "id:" + id ;
		result += ",synSet:" + verbSynSet ;
		if (tense != 0 )
			result += ",tense:" + tense;
		if (aspect != 0 )
			result += ",aspect:" + aspect;
		if(! subjects.isEmpty()) {
			result += ",subjects:";
		}
		
		if(! objects.isEmpty()){
			result += ",objects:";
			result += objects;
		}
		
		result += "act:}";
		
		return result;
	}
	
	
	public String structuredString() {
		
		String result = "\tact:{\n";
		
		
		result += "\t\tid: " + id ;
		result += ",\n\t\tsynSet: " + verbSynSet ;
		if (tense != 0 )
			result += ",\n\t\ttense: " + tense;
		if (aspect != 0 )
			result += ",\n\t\taspect: " + aspect;
		if(! subjects.isEmpty()) {
			result += ",\n\t\tsubjects: ";
			result += subjects;
		}
		
		if(! objects.isEmpty()){
			result += ",\n\t\tobjects: ";
			result += objects;
		}
		
		result += "\n\tact:}";
		
		return result;
	}

	

}
