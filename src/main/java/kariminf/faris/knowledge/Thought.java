package kariminf.faris.knowledge;

import kariminf.faris.philosophical.Action;
import kariminf.faris.process.Processor;

public class Thought extends Idea {
	
	public static final class ThoughtWrapper {
		public Thought thought;
		public Action action;
		
		public ThoughtWrapper(Thought thought){
			this.thought = thought;
		}
		
		public void unsafeAddAll(){
			action = thought.action;
		}
	}

	private Action action;
	
	/**
	 * 
	 * @param action
	 */
	public Thought(Action action) {
		this.action = action;
	}
	
	/**
	 * 
	 * @return
	 */
	public Action getAction(){
		return action;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Thought: " + action + "\n";
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return action.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Thought)) return false;
		Thought other = (Thought) obj;
		return (action.equals(other.action));
	}

	public boolean update(Thought thought){
		
		if (! equals(thought)) return false;
		
		action.update(thought.action);
		
		return true;
	}

	@Override
	public void process(Processor pr) {
		ThoughtWrapper wrapper = new ThoughtWrapper(this);
		wrapper.unsafeAddAll();
		pr.processIdea(wrapper);
		
	}

}
