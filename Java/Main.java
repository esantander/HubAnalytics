import org.eclipse.egit.*;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import java.io.*;
import java.util.*;

public class Main {

	/**
	 * The primary function of the Main class is to ensure requests are occuring
	 * at appropriate intervals.
	 * 
	 * I've been a very bad programmer, and instead of doing this right,
	 * whenever I was done with a STEP, I just commented it out and wrote in
	 * the next STEP. Bad design.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
	/**
	 * STEP:???
	 * This chunk counts the unique lines in a text file and copies the unique lines
	 * to a new location.
	 * 
	 * I actually have no idea what I did in here. But keep in mind that I had to do
	 * STEP 1 in multiple attempts, since I didn't just get it right the first time.
	 * This resulted in me having multiple .txt files with 10,000-15,000 address strings,
	 * so this method probably just merged all these smaller .txt files in to a larger one.
	 * 
	    BufferedReader reader = new BufferedReader(new FileReader("/Users/Mo/Desktop/GithubResearch/54kadd.txt"));
	    Set<String> lines = new HashSet<String>(100000); // maybe should be bigger
	    String line;
	    while ((line = reader.readLine()) != null) {
	        lines.add(line);
	    }
	    reader.close();
	    BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/Mo/Desktop/GithubResearch/54kadd2.txt"));
	    int counter = 0;
	    for (String unique : lines) {
	        writer.write(unique);
	        counter++;
	        writer.newLine();
	    }
	    writer.close();
	    System.out.println("There were " + counter + " unique lines.");
	}
*/
		
		
		/**
		 * STEP 2
		 * this bit of code is what should be executed after you already have
		 * your addresses.txt file. So...STEP 2
		 */
		
		Terminalizer term = new Terminalizer();
		term.executeStats();
		
	}
		
		
			
		
		

		// TODO Auto-generated method stub
	
	/**
	 * 
	 * STEP 1
	 * 
	 * Below is how we make 61kaddresses.txt. The main method shouldn't be doing
	 * all this heavy lifting. I know. Bad design.
	 */
/**
		// List<SearchRepository> searchresults = new list<SearchRepository>();
		GitHubClient client = new GitHubClient("api.github.com");
		//!!Credentials
		client.setCredentials("//username", "//password");
		File fn = new File("/Users/Mo/Desktop/GithubResearch/61kaddresses.txt");
		PrintWriter pw = new PrintWriter(fn);
		int counter1 = 0;

		while (counter1 != 61000) {
			String s;
			LinkSynthesizer lulu = new LinkSynthesizer();
			pw.write(s = lulu.searchGitHub());
			pw.write("\n");
			counter1++;
			Thread.sleep(13);
			pw.flush();
			System.out.println("Just added: " + s);
		}
		System.out.println("COMPLETE!");

		pw.close();
	}
		*/
}

		
		
