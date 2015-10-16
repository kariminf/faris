package dz.aak.faris.ston;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import dz.aak.faris.knowledge.Idea;
import dz.aak.faris.ston.RequestCreator;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;


public class StonCreateTest {
	
	
	
	public static String readFile (String f) {
		try {
			String contents = "";

			BufferedReader input = new BufferedReader(new FileReader(f));

			
			for(String line = input.readLine(); line != null; line = input.readLine()) {
				contents += line + "\n";
			}
			input.close();

			return contents;

		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		} 
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// mother and two noizy little children ate food. food was extremely good. this is truth.
		
		//Search nouns
		int child = getSynSet("child", POS.NOUN);
		int food = getSynSet("food", POS.NOUN);
		int mother = getSynSet("mother", POS.NOUN);
		int truth = getSynSet("truth", POS.NOUN);
		
		//Search verbs
		int eat = getSynSet("eat", POS.VERB);
		int be = getSynSet("be", POS.VERB);
		
		//Search adverbs
		int extremely = getSynSet("extremely", POS.ADVERB);
		
		//Search adjectives
		int good = getSynSet("good", POS.ADJECTIVE);
		int noisy = getSynSet("noisy", POS.ADJECTIVE);
		int little = getSynSet("little", POS.ADJECTIVE);
		int happy = getSynSet("happy", POS.ADJECTIVE);
		
		System.out.println(happy);
		RequestCreator rq = new RequestCreator();
		
		
		//===============================
		//Mother and two noizy little children ate food
		rq.addRolePlayer("2children", child);
		rq.addRolePlayer("mother", mother);
		rq.addRolePlayer("food", food);
		
		rq.setQuantity("2children", "2");
		rq.addAdjective("2children", noisy, null);
		rq.addAdjective("2children", little, null);
		
		rq.addAction("eat", eat);
		
		rq.addSubject("eat", "2children");
		
		rq.addSubject("eat", "mother");
		rq.addObject("eat", "food");
		
		//===============================
		
		//food was extremly good (food)
		rq.addAction("was", be);
		rq.addRolePlayer("+goodfood", food);
		{
			HashSet<Integer> adv = new HashSet<Integer>();
			adv.add(extremely);
			rq.addAdjective("+goodfood", good, adv);
		}
		
		rq.addSubject("was", "food");
		rq.addObject("was", "+goodfood");
		//===============================
		
		//===============================
		//This is truth (st1 and st2)
		rq.addAction("is", be);
		rq.addVerbSpecif("is", 1, 0);
		rq.addSubject("is", "was");
		rq.addSubject("is", "eat");
		rq.addRolePlayer("truth", truth);
		rq.addObject("is", "truth");
		//===============================
		
		System.out.println(rq.getRequest());
		System.out.println("----------------------------");
		
		String str = rq.getStructuredRequest();
		System.out.println(str);
		
		String str2 = readFile("ston/test.ston");

		if (str.equals(str2))
			System.out.print("equal");

	}
	
	public static int getSynSet(String word, POS pos){
		
		File file =new File("dict/");
		// construct the dictionary object and open it
		IDictionary dict = new Dictionary(file) ;
		try {
			dict.open();
			// look up first sense of the word " dog "
			IIndexWord idxWord = dict.getIndexWord (word , pos) ;
			dict.close();
			if(idxWord == null) return -1;
			
			return idxWord.getWordIDs().get(0).getSynsetID().getOffset();
			
		} catch (IOException e) {
			e.printStackTrace();
			dict.close();
			return -1;
		}
		
		
	}

}
