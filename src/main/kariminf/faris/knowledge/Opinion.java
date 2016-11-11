package kariminf.faris.knowledge;

import kariminf.faris.philosophical.QuantSubstance;

public class Opinion extends Idea {

	private Mind otherMind;
	
	/**
	 * 
	 * @param superMindName
	 * @param otherMindOwner
	 */
	public Opinion(String superMindName, QuantSubstance otherMindOwner) {
		String name = superMindName + "." + otherMindOwner.getSubstance().getNounSynSet();
		this.otherMind = new Mind(name, otherMindOwner);
	}
	
	/**
	 * 
	 * @return
	 */
	public Mind getMind(){
		return otherMind;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Opinion: " + otherMind + "\n";
	}
	
	
	

}
