package kariminf.faris.process;

import kariminf.faris.knowledge.Conditional.ConditionalWrapper;
import kariminf.faris.knowledge.Faris.FarisWrapper;
import kariminf.faris.knowledge.Mind.MindWrapper;
import kariminf.faris.knowledge.Opinion.OpinionWrapper;
import kariminf.faris.knowledge.Thought.ThoughtWrapper;
import kariminf.faris.philosophical.Action.ActionWrapper;
import kariminf.faris.philosophical.Place.PlaceWrapper;
import kariminf.faris.philosophical.Quality.QualityWrapper;
import kariminf.faris.philosophical.QuantSubstance.QSubstanceWrapper;
import kariminf.faris.philosophical.Quantity.QuantityWrapper;
import kariminf.faris.philosophical.Relative.RelativeWrapper;
import kariminf.faris.philosophical.State.StateWrapper;
import kariminf.faris.philosophical.Substance.SubstanceWrapper;
import kariminf.faris.philosophical.Time.TimeWrapper;

public interface Processor {
	
	//Knowledge
	public void processFaris(FarisWrapper wrapper);
	public void processMind(MindWrapper wrapper);
	public void processIdea(ThoughtWrapper wrapper);
	public void processIdea(OpinionWrapper wrapper);
	public void processIdea(ConditionalWrapper wrapper);
	
	//Philosophical
	public void processRelative(RelativeWrapper wrapper);
	public void processPlace(PlaceWrapper wrapper);
	public void processTime(TimeWrapper wrapper);
	public void processAction(ActionWrapper wrapper);
	public void processState(StateWrapper wrapper);
	public void processQuality(QualityWrapper wrapper);
	public void processQuantity(QuantityWrapper wrapper);
	public void processSubstance(QSubstanceWrapper wrapper);
	public void processSubstance(SubstanceWrapper wrapper);
	
	
}
