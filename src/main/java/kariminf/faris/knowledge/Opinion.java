package kariminf.faris.knowledge;

import kariminf.faris.philosophical.QuantSubstance;
import kariminf.faris.process.Processor;

public class Opinion extends Idea {
	
	public static final class OpinionWrapper {
		
		public Opinion opinion;
		public Mind otherMind;
		
		public OpinionWrapper(Opinion opinion){
			this.opinion = opinion;
		}
		
		public void unsafeAddAll(){
			otherMind = opinion.otherMind;
		}
	}

	private Mind otherMind;
	
	/**
	 * 
	 * @param superMindName
	 * @param otherMindOwner
	 */
	protected Opinion(String superMindName, QuantSubstance otherMindOwner) {
		String name = superMindName + "." + otherMindOwner.getSubstance().getNounSynSet();
		this.otherMind = new Mind(name, otherMindOwner);
	}
	
	protected Opinion(String superMindName, Mind otherMind) {
		String name = superMindName + "." + otherMind.getName();
		otherMind.name = name;
		this.otherMind = otherMind;
	}
	
	/**
	 * 
	 * @return
	 */
	protected Mind getMind(){
		return otherMind;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Opinion: " + otherMind + "\n";
	}

	@Override
	public void process(Processor pr) {
		OpinionWrapper wrapper = new OpinionWrapper(this);
		wrapper.unsafeAddAll();
		pr.processIdea(wrapper);
	}

	
	
	

}
