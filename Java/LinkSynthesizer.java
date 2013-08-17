import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import java.awt.*;
import java.util.*;

/**
 * This class is not named well. What it actually does is build
 * what eventually becomes 61kaddresses.txt . It should be called
 * "randomAddressSynthesizer". It is put in to a .txt file in the Main method.
 * this is bad design, I know.
 * <p>
 * This class utilizes a random word to query GitHub's search function for the
 * existence repositories containing the random word. This is done by a RepositoryService
 * class. A SearchRepository object is returned, which must be converted to
 * a string for further processing. 
 * <p>
 * The class detects if and/or how many responses are received. In the case of
 * multiple responses, the class randomly selects a repository and generates a
 * URL for it.
 * <p>
 * If there are no search results for a word, a new random word is selected.
 * <p>
 * 
 * @author E Santander
 * 
 */
public class LinkSynthesizer extends Main{
	GitHubClient client = new GitHubClient("api.github.com");
	String currentWord = "";
	RepositoryService repo = new RepositoryService(client);
	java.util.List<SearchRepository> results = new ArrayList<SearchRepository>();
	String finalURL = "";
	/**
	 * The Randomizer class simply loads a pre-made list of english words and
	 * can produce a random word from that list.
	 */
	Randomizer randomBuddy = new Randomizer();
	Random generator = new Random();
	SearchRepository chosenResult;
	String URLCOMPONENT2 = "";
	String output;
	
	public LinkSynthesizer() {
		
		currentWord = randomBuddy.fetchRandomWord();
		
		/**
		 * these are my github account credentials. I don't mind you having
		 * my username and password, but let's be mindful that this is here
		 * in case this code ever gets sent/seen by somebody else.
		 */
		client.setCredentials("//username", "//password");
	}

	public String searchGitHub() {
		try {
			results = repo.searchRepositories(currentWord);
		}
		catch (IOException e) {
			System.out.println("An error has occured in searching github.");
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		/**
		 * there may be more than one search result for a word. In case there is 
		 * more than one result, we randomly pick one from that array.
		 */

		int possibilities = results.size();

		if (possibilities == 0) {
			System.out.println("0 possibilities found for word / " + currentWord + " /. Now sleeping for 13ms.");
			currentWord = randomBuddy.fetchRandomWord();
			try {
				System.out.println("Recursive call with / " + currentWord + " /. Will initiate after 13ms.");
				// 13ms is the amount of time the system must wait in order to not
				// reach the 5,000 requests-per-minute rate limit we must follow.
				Thread.sleep(13);
				
			}
			catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			searchGitHub();
		}
		else {
			int randomIndex = generator.nextInt(possibilities);
			chosenResult = results.get(randomIndex);
			URLCOMPONENT2 = chosenResult.toString();
			output = "git@github.com:" + URLCOMPONENT2 + ".git";
		}
			return output;
	
		}
	}


