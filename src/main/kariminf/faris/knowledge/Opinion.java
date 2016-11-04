package kariminf.faris.knowledge;

public class Opinion extends Idea {

	Mind otherMind;
	
	public Opinion(Mind otherMind) {
		this.otherMind = otherMind;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Opinion: " + otherMind + "\n";
	}
	
	
	

}
