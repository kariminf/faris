package dz.aak.faris.ston;

import java.util.HashMap;
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
		
		System.out.println(description);
		
		Matcher m = CONT.matcher(description);
		if (m.find()) {
			String roles =  m.group(1);
			String actions =  m.group(2);
			Roles(roles);
			Actions(actions);
        }
	}
	
	
	private void Roles(String description){
		
		int idx;
		while ((idx = description.indexOf("r:}")) >= 0) {
			String role =  description.substring(3, idx);
			description = description.substring(idx+3);
			//System.out.println(role);
			Role(role);
        }
	}
	
	private void Actions (String description){
		
		int idx;
		while ((idx = description.indexOf("act:}")) >= 0) {
			String action =  description.substring(5, idx);
			description = description.substring(idx+5);
			//System.out.println(role);
			Action(action);
        }
	}
	
	
	//TODO complete the action
	private void Action(String description){
		
		RAction action;
		
		String[] descs = description.split(",");
		
		String id = "";
		String synSetStr = "";
		String tenseStr = "0";
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
		
		if(id.length() < 1) return;
		
		int synSet = Integer.parseInt(synSetStr);
		action = RAction.create(id, synSet);
		
			
	}

	private void Role(String description){
		
		RRolePlayer role;
		
		int idx = description.indexOf(",");
		String id = description.substring(0,idx);
		if(! id.matches("id:.*")) return;
		id = id.substring(3);
		
		System.out.println(id);
		
		description = description.substring(idx +1);
		
		idx = description.indexOf(",");
		if (idx < 0) idx = description.length();
		
		{
			String synSetStr = description.substring(0,idx);
			if(! synSetStr.matches("synSet:.*")) return;
			
			synSetStr = synSetStr.substring(7);
			System.out.println(synSetStr);
			int synSet = Integer.parseInt(synSetStr);
			role = RRolePlayer.create(id, synSet);
			
		}
		
		
	}
	
	
	
	public static void main(String[] args) {
		
		
		Pattern desc = Pattern.compile("r:\\{(.*)r:\\}$");
		
		String ss = "r:{kkkkkkkk1rr:},r:{kkkkkkkk2rr:}";
		//Matcher m = desc.matcher();
		
		for (String s : ss.split("r:\\},r:\\{|r:\\}|r:\\{")){
			System.out.println(s);
		}
		/*while (m.find()) {
			String  description =  m.group(1);
			System.out.println(description);
        }*/

	}
	

}
