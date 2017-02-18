/* FARIS : Factual Arrangement and Representation of Ideas in Sentences
 * FAris : Farabi & Aristotle
 * Faris : A knight (in Arabic)
 * --------------------------------------------------------------------
 * Copyright (C) 2015 Abdelkrime Aries (kariminfo0@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package kariminf.faris.philosophical;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import kariminf.faris.linguistic.Adverb;
import kariminf.faris.process.Generator;
import kariminf.sentrep.univ.types.Relation.Adpositional;

/**
 * When or time (πότε, pote, when). 
 * Position in relation to the course of events. Examples: yesterday, last year.
 * 
 * @author Abdelkrime Aries (kariminfo0@gmail.com)
 *         <br>
 *         Copyright (c) 2015 Abdelkrime Aries
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
public class Time extends Being{

	//TODO complete the time
	
	LocalDateTime datetime ;
	private Adverb adv;
	
	private Adpositional relation;
	private ArrayList<QuantSubstance> times = new ArrayList<>();
	
	public Time(Adverb adv) {
		this.adv = adv;
	}
	
	public Time(Adpositional prep) {
		this.relation = prep;
	}
	
	public void addTimeSubstance(QuantSubstance quantSubstance){
		if (relation != null)
			times.add(quantSubstance);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String result = "T:";
		
		if (adv != null){
			result += adv;
		} else {
			result += relation;
			result += (times.size() > 0)? times: "";
		}
		
		return result;
	}

	@Override
	public void generate(Generator gr) {
		gr.processTime(relation, adv, datetime, times);
	}
}
