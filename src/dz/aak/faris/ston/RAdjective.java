package dz.aak.faris.ston;

import java.util.HashSet;
import java.util.Set;

public class RAdjective {
	
	private int adjSynSet = 0;
	private Set<Integer> advSynSets = new HashSet<Integer>();
	
	
	public RAdjective(int adjSynSet) {
		this.adjSynSet = adjSynSet;
	}
	
	/**
	 * @return the adjSynSet
	 */
	public int getAdjSynSet() {
		return adjSynSet;
	}

	/**
	 * @return the advSynSets
	 */
	public Set<Integer> getAdvSynSets() {
		//Alert: Security problem
		return advSynSets;
	}
	
	/**
	 * @param advSynSets the advSynSets to set
	 */
	public void setAdvSynSets(Set<Integer> advSynSets) {
		this.advSynSets.addAll(advSynSets);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "adj:{";
		result += "synSet:" + adjSynSet;
		
		if ( ! advSynSets.isEmpty()){
			result += ";adverbs: " + advSynSets;
		}
		result += "adj:}";
		return result;
	}
	
	
	public String structuredString(){
		
		String result = "\t\t\tadj:{\n";
		result += "\t\t\t\tsynSet:" + adjSynSet;
		
		if ( ! advSynSets.isEmpty()){
			result += ";\n\t\t\t\tadverbs: " + advSynSets;
		}
		result += "\n\t\t\tadj:}";
		return result;
		
	}
	
	

}
