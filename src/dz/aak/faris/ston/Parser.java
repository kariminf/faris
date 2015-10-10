package dz.aak.faris.ston;

import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	private static String BL = "[\\t \\n\\r]+";
	 
	private static final Pattern BLOC = Pattern.compile("\\{(.+)\\}");
	private static final Pattern SET = Pattern.compile("\\[(.+)\\]");
	private static final Pattern CONT = Pattern.compile("@roles\\:" + SET + "@actions\\:" + SET);
	//private static final Pattern ROLE = Pattern.compile("r\\:\\{(.+)r\\:\\}");
	//private static final Pattern ROLES = 
			//Pattern.compile("^" + ROLE + "$|" + ROLE + ",|," + ROLE + "$");
	
	private HashMap<String, RRolePlayer> players = new HashMap<String, RRolePlayer>();
	private HashMap<String, RAction> actions = new HashMap<String, RAction>();
	
	private boolean success = false;
	
	/**
	 * 
	 * @param description
	 */
	public Parser(String description) {

		description = description.replaceAll(BL, "");
		
		//System.out.println(description);
		Matcher m = CONT.matcher(description);
		if (m.find()) {
			String roles =  m.group(1);
			String actions =  m.group(2);
			success = Roles(roles);
			success &= Actions(actions);
        }
		
	}
	
	public boolean parsed(){
		return success;
	}
	
	public HashMap<String, RRolePlayer> getPlayers(){
		return new HashMap<String, RRolePlayer>(players);
	}
	
	public HashMap<String, RAction> getActions(){
		return new HashMap<String, RAction>(actions);
	}
	
	private boolean Roles(String description){
		
		int idx;
		while ((idx = description.indexOf("r:}")) >= 0) {
			String role =  description.substring(3, idx);
			description = description.substring(idx+3);
			//System.out.println(role);
			if (! Role(role))
				return false;
        }
		
		return true;
	}
	
	private boolean Actions (String description){
		
		int idx;
		while ((idx = description.indexOf("act:}")) >= 0) {
			String action =  description.substring(5, idx);
			description = description.substring(idx+5);
			//System.out.println(role);
			if (! Action(action))
				return false;
			
        }
		
		return true;
	}
	
	
	//TODO complete the action
	private boolean Action(String description){
		
		RAction action;
		
		String[] descs = description.split(";");
		
		String id = "";
		String synSetStr = "";
		String tenseStr = "";
		String aspectStr = "";
		String subjects = "";
		String objects = "";
		
		for (String desc : descs){
			
			if(desc.startsWith("id:")){
				id = desc.split(":")[1];
				continue;
			}
			
			if(desc.startsWith("synSet:")){
				synSetStr = desc.split(":")[1];
				continue;
			}
			
			if(desc.startsWith("tense:")){
				tenseStr = desc.split(":")[1];
				continue;
			}
			
			if(desc.startsWith("subjects:")){
				subjects = desc.split(":")[1];
				continue;
			}
			
			if(desc.startsWith("objects:")){
				objects = desc.split(":")[1];
				continue;
			}
		}
		
		if(id.length() < 1) return false;
		
		int synSet = Integer.parseInt(synSetStr);
		action = RAction.create(id, synSet);
		//TODO add other components of the action
		int tense = 0;
		if(tenseStr.length()>0)
			tense = Integer.parseInt(tenseStr);
		int aspect = 0;
		if(aspectStr.length()>0)
			tense = Integer.parseInt(tenseStr);
		action.addVerbSpecif(tense, aspect);
		
		if(subjects.length() > 2){
			if (!(subjects.startsWith("[") && subjects.endsWith("]"))){
				System.out.println("subjects=" + subjects);
				return false;
			}
				
			subjects = subjects.substring(1, subjects.length()-1);
			for (String subject: subjects.split(",")){
				action.addSubject(subject.trim());
			}
		}
		
		if(objects.length() > 2){
			if (!(objects.startsWith("[") && objects.endsWith("]")))
				return false;
			objects = objects.substring(1, objects.length()-1);
			for (String object: objects.split(",")){
				action.addObject(object.trim());
			}
		}
		
		actions.put(id, action);
		
		return true;
	}

	private boolean Role(String description){
		
		RRolePlayer role;
		
		Matcher m = Pattern.compile("id\\:([^;]+)(;|$)").matcher(description);
		
		if (! m.find()) return false;
		
		String id = m.group(1);
		
		m = Pattern.compile("synSet\\:([^;]+)(;|$)").matcher(description);
		
		if (! m.find()) return false;
		
		String synSetStr = m.group(1);
		
		int synSet = Integer.parseInt(synSetStr);
		
		role = RRolePlayer.create(id, synSet);
		
		m = Pattern.compile("adjectives\\:\\[(.+adj\\:\\})\\]").matcher(description);
		
		if (m.find()){
			String adjectives = m.group(1);
			if (! Adjectives(role, adjectives)) return false;
		}

		players.put(id, role);
		
		return true;
		
	}

	private boolean Adjectives(RRolePlayer role, String description){
		
		int idx;
		while ((idx = description.indexOf("adj:}")) >= 0) {
			String adjective =  description.substring(5, idx);
			description = description.substring(idx+5);
			if (description.startsWith(","))
				description = description.substring(1);
			
			String[] descs = adjective.split(";");
			
			int synSet = 0;
			HashSet<Integer> advSynSets = new HashSet<Integer>();
			for (String desc: descs){
				if(desc.startsWith("synSet:")){
					String synSetStr = desc.split(":")[1];
					synSet = Integer.parseInt(synSetStr);
					continue;
				}
				
				if(desc.startsWith("adverbs:")){
					String synSetStrs = desc.split(":")[1];
					synSetStrs = synSetStrs.substring(1, synSetStrs.length()-1);
					
					for (String AdvsynSetStr: synSetStrs.split(",")){
						
						int AdvsynSet = Integer.parseInt(AdvsynSetStr);
						advSynSets.add(AdvsynSet);
					}
				}
				
			}
			
			if (synSet < 1){
				//System.out.println("No synSet for the adjective");
				return false;
			}
			
			role.addAdjective(synSet, advSynSets);
			
        }
		return true;
	}

}
