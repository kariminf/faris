package dz.aak.faris.knowledge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FarisTest {
	
	static String testFile = "ston/test2.ston";

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
		String specif = readFile(testFile);
		Faris faris = new Faris();
		faris.addStonDescription(specif);
		System.out.println(faris.info());
		//System.out.print(faris.getNoAdjectives("Default"));
		System.out.print(faris.getSynSetText("Default", 9917593));
	}

}
