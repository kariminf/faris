package dz.aak.faris.ston;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RRolePlayer {

	private int nounSynSet;
	private String id;
	private List<RAdjective> adjectives = new ArrayList<RAdjective>();
	private String quantity = null;
	private List<String> possessives = new ArrayList<String>();
	
	/**
	 * @return the nounSynSet
	 */
	public int getNounSynSet() {
		return nounSynSet;
	}


	/**
	 * @return the adjectives
	 */
	public List<RAdjective> getAdjectives() {
		return adjectives;
	}


	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}


	/**
	 * @return the possessives
	 */
	public List<String> getPossessives() {
		return possessives;
	}


	private static Set<String> ids = new HashSet<String>();
	
	private RRolePlayer(String id, int nounSynSet) {
		this.nounSynSet = nounSynSet;
		this.id = id;
	}
	
	
	public static RRolePlayer create(String id, int nounSynSet){
		//protection for same ids
		if(ids.contains(id)) return null;
		ids.add(id);
		return new RRolePlayer(id, nounSynSet);
	}
	
	public void addAdjective(int adjSynSet, Set<Integer> advSynSets){
		
		RAdjective adjective = new RAdjective(adjSynSet);
		if ((advSynSets != null) && ! advSynSets.isEmpty()){
			adjective.setAdvSynSets(advSynSets);
		}
		
		adjectives.add(adjective);
	}
	
	
	public void setQuantity(String quantity){
		this.quantity = quantity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "r:{";
		
		result += "id:" + id;
		result += ";synSet:" + nounSynSet;
		
		if (quantity != null)
			result += ";quantity:" + quantity;
		
		if(! adjectives.isEmpty()) {
			result += ";adjectives:[";

			Iterator<RAdjective> it = adjectives.iterator();
			while(it.hasNext()){
				result += it.next();
				if(it.hasNext())
					result += ",";
			}
			
			result += "]";
		}
		
		
		result += "r:}";
		
		return result;
	}
	
	
	public String structuredString() {
		String result = "\tr:{";
		
		result += "\n\t\tid: " + id;
		result += ";\n\t\tsynSet: " + nounSynSet;
		
		if (quantity != null)
			result += ";\n\t\tquantity: " + quantity;
		
		if(! adjectives.isEmpty()) {
			result += ";\n\t\tadjectives: [\n";
			
			Iterator<RAdjective> it = adjectives.iterator();
			while(it.hasNext()){
				result += it.next().structuredString();
				if(it.hasNext())
					result += ",";
				result += "\n";
			}
			
			result += "\t\t]";
		}
		
		
		result += "\n\tr:}";
		
		return result;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
