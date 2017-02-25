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

import kariminf.faris.process.Generator;

/**
 * Quantity (ποσόν, poson, how much). This is the extension of an object, and may be 
 * either discrete or continuous. Further, its parts may or may not have relative 
 * positions to each other. All medieval discussions about the nature of the continuum, 
 * of the infinite and the infinitely divisible, are a long footnote to this text. 
 * It is of great importance in the development of mathematical ideas in the medieval 
 * and late Scholastic period. Examples: two cubits long, number, space, (length of) time.
 * 
 * @author Abdelkrime Aries (kariminfo0@gmail.com)
 *         <br>
 *         Copyright (c) 2015-2017 Abdelkrime Aries
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
public class Quantity extends Being{
	
	public static final class QuantityWrapper {
		public Quantity quantity;
		public double nbr;
		public Substance unit; //mesure unit: kilogram, etc.
		public boolean cardinal = true;
		public boolean plural = false;
		
		public QuantityWrapper (Quantity quantity){
			this.quantity = quantity;
		}
		
		public void unsafeAddAll(){
			nbr = quantity.nbr;
			unit = quantity.unit;
			cardinal = quantity.cardinal;
			plural = quantity.plural;
		}
	}
	
	private double nbr;
	private Substance unit; //mesure unit: kilogram, etc.
	private boolean cardinal = true;
	private boolean plural = false;

	public Quantity(double nbr) {
		this.nbr = nbr;
	}
	
	public Quantity copy(){
		Quantity result;
		if(plural){
			result = new Quantity();
		} else {
			result = new Quantity(nbr);
		}
		
		result.unit = unit;
		result.cardinal = cardinal;
		
		return result;
	}
	
	/**
	 * This means there are a lot (Plural): undefined plural quantity
	 */
	public Quantity() {
		this.plural = true;
	}
	
	/**
	 * We can set ordinal for number quantities
	 */
	public void setOrdinal(){
		if (! plural) cardinal = false;
	}
	
	public boolean isPlural(){
		return plural;
	}
	
	public boolean isCardinal(){
		return cardinal;
	}
	
	public void addUnit(Substance unit){
		this.unit = unit;
	}
	
	public double getNumber(){
		return nbr;
	}
	
	public Substance getUnit(){
		return unit;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		if (plural) result += "PL";
		else {
			result += nbr;
			result += (cardinal)? "": "O";
		}
		result += (unit != null)? ":" + unit: "";
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void generate(Generator gr) {
		QuantityWrapper wrapper = new QuantityWrapper(this);
		wrapper.unsafeAddAll();
		gr.processQuantity(wrapper);
		
	}
	
	

}
