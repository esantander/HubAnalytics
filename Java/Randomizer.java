import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * This class loads a list of pre-selected words in to an ArrayList.
 * A method is available to then select a word randomly
 * with resampling (once a word is selected, it can be selected again).
 * 
 * @author E Santander
 *
 */
public class Randomizer {
	static int sizeOfList = 0;
	ArrayList <String> words = new ArrayList<String>();
	Random generator = new Random();
	File f = new File ("/Users/Mo/Desktop/GithubResearch/words.txt");
	
	public Randomizer () 
	{
		try{
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		int counter2 = 0;
		while ((line = br.readLine()) != null)
		{
			words.add(line);
			counter2++;
		}
	
		sizeOfList = words.size();
		}
		catch (Exception e)
		{
			System.out.println("A problem has occured in Randomizer/loading words");
		}
	}
	/**
	 * This method returns a randomly word from a word list. 
	 * The word list is not modified.
	 * 
	 * @return String A randomly selected word.
	 */
	public String fetchRandomWord()
	{
		int randomIndex = generator.nextInt(sizeOfList);
		String answer = words.get(randomIndex);
		return answer;
		
	}
	
}
