import java.io.*;
import javax.swing.*;
import java.util.*;

	
/**
 * <p>
 * This java program reads in a list of random github addresses.
 * <p>
 * It proceeds to clone the git to a local directory using the GitbubAPI and
 * the System.runtime
 * <p>
 * After cloning, this program calls modifiedgitstats.py to yield a xxxSTAT.txt
 * file from the downloaded clone.
 * <p>
 * After the xxSTAT.txt file has been generated, the program cleans up and
 * deletes the cloned git, as it is no longer needed.
 * <p>
 * The program also removes the completed address from the list of addresses. 
 * This is to ensure gits are not reanalyzed in case the program fails before
 * completing all 61,000 addresses (and xxxSTAT.txt files).
 * <p>
 * The program should, after a while, yield a folder with a large amount of 
 * xxxSTAT.txt files ready to be aggregated in a .csv file by getStats.py
 * 
 * 
 * @author Santander & Mora
 */
public class FetchDownloadAnalyze {
	
	/**
	 * -------------------INSTANCE VARS---------------------------------------
	 */

	
	//This is the txt file that holds all the randomly-acquired git addresses we
	//have. All 61,000 of them.
	
	String fileLocation = JOptionPane.showInputDialog("Please enter the full path of the text file containing Github Addresses.");
	File f = new File(fileLocation);
	
	//this is the data structure that will hold the addresses imported from
	//the .txt file, just for a little easier access.
	List<String> addresses = new ArrayList<String>();
	
	//the current address chosen from addresses.arrayList
	String currentAddress = "";
	
	//size of addresses.arrayList
	int MAX_SIZE=0;
	
	//a Set to hold addresses that were done already. This can be printed
	//in case of error.
	Set <String> alreadycomplete = new HashSet<String>();
	
	//a random number generator that is completely unnecessary
	Random rng = new Random();
	
	//a random number list that is completely unnecessary
	List<Integer> generated = new ArrayList<Integer>();
	
	//unknown var
	int generation_counter=0;
	
	String user = JOptionPane.showInputDialog("Please enter your github username, surrounded with double quotes: \"example\" " );
	String STATlocation = JOptionPane.showInputDialog("Please enter the full path of the directory where you would like your STAT files created. Please surround with double quotes \"example/path/\"");
	String PythonLoc = JOptionPane.showInputDialog("Please enter the location of gitstats.py, surrounded with double quotes \"example/location/gitstats.py\"");
	
	/**
	 * -------------------CONSTRUCTOR---------------------------------------
	 */

public FetchDownloadAnalyze() {
	
	//load the addresses from 61kaddresses.txt and count them on the way in
	try {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		
		int counter2 = 0;
		while ((line = br.readLine()) != null) {
			if (!addresses.contains(line)) {
				addresses.add(line);
				counter2++;
			}
			
		}
		MAX_SIZE = counter2;
	}// end try
	catch (Exception e) {
		System.out.println("An error has occured in loading addresses");
		System.out.println(e.getStackTrace());
	}
	
	/**
	 * This loop creates a list of random numbers. This list is useful when
	 * selecting an index of addresses[] to randomly evaluate for stats. This
	 * further reduces the likelihood of pattern effects emerging in the final 
	 * data.
	 */
	for (int i = 0; i < MAX_SIZE; i++)  				
	{
	    while(true)
	    {
	        Integer next = rng.nextInt(MAX_SIZE);
	        if (!generated.contains(next))
	        {
	            // Done for this iteration
	            generated.add(next);
	            break;
	        }//endif
	    }//endwhile
	}//endfor
	
	
	
	
}


/**
 * 
 * @throws Exception
 */
public void executeStats () throws Exception
{
	
	long counter= 0;
	
	/**
	 * ProcessBuilder is a class used to access the 'shell' and pass the
	 * system commands. To use the Github API, we have to use the
	 * shell/system/CLI.
	 * 
	 * After every command is submitted, there must be a waitFor() to allow
	 * the command to finish. 
	 * 
	 * InheritIO makes errors print out to the Eclipse console, but it also
	 * makes the progress print out to the Eclipse console. That is why
	 * InheritIO is only applied to 'critical' events that we should know
	 * about, because applying InheritIO to every command would clutter the
	 * console.
	 */
	ProcessBuilder pb = new ProcessBuilder ();
	pb.inheritIO();

		for (int i=0; i< addresses.size();i++)
		{
			 pb.command("curl", "-i", "-u", user, "https://api.github.com/").start().waitFor();
			 System.out.println("A login has occurred.");
			 
			 
			 /**
			  * I don't know exactly what these six strings do, but I remember I made
			  * them in order to appropriately name,call, and move the files that were
			  * being produced by modifiedgitstats.py .
			  * 
			  * I do feel bad about this.
			  */
		String two = Cloner();
		System.out.println("A clone has occurred.");
		String three = randomDirectory();
		String four = "" + three;
		String five = "" + three;
		five = five.replace("git@github.com:", "");
		five = five.replace("/", "");
		five = five.replace("/", "");
		String six = "STATS" + five + ".txt";
		
		//if we haven't already analyzed the currentAddress, execute modifiedgitstats.py
				if (!alreadycomplete.contains(six))
				{
				
					System.out.println("Gitstats is about to execute...");
					
		pb.command(PythonLoc,""+ two, ""+ three).start().waitFor();

		System.out.println("gitstats.py has executed.");
	
	
	//Delete accessory files: .cache and .dat
		
		/**
		 * 
		 * CLEAN UP:
		 * Below is where we do "clean-up" of all the useless files that 
		 * modifiedgitstats.py outputs. I know, we can prevent these files from
		 * even being made if we changed modifiedgitstats.py, but I have never
		 * done python and I already hacked apart that python script as it is.
		 * If I messed around with it more, I would break it. So if we change up
		 * modifiedgitstats.py to ONLY output a text file, we could get rid of the
		 * stuff below.
		 */
	
	ProcessBuilder pd = new ProcessBuilder();
	pd.inheritIO();
	pd.command("rm", three + "/commits_by_year_month.dat").start().waitFor();
	pd.command("rm", three + "/gitstats.cache").start().waitFor();
	pd.command("rm", three + "/commits_by_year.dat").start().waitFor();
	pd.command("rm", three + "/day_of_week.dat").start().waitFor();
	pd.command("rm", three + "/hour_of_day.dat").start().waitFor();
	pd.command("rm", three + "/month_of_year.dat").start().waitFor();
	three = currentAddress;
	three = three.replace("git@github.com:", "");
	three = three.replace("/", "");
	three = three.replace("/", "");
	pd.command("mv", four + "/STATS" + three + ".txt", STATlocation).start().waitFor();  //!! This line contains a string that must be modified
	alreadycomplete.add(four + "/STATS" + three + ".txt");
	counter++;
				}
	ProcessBuilder px = new ProcessBuilder();
	px.inheritIO();
	px.command("rm", "-rf", "" + four).start().waitFor();
	
	System.out.println("Successfully processed " + counter );
	
	
	/**
	 * This loop gives a progress report every 100 analyzed projects.
	 */
	if (counter%100 ==0)
		{
		System.out.println("Processing git " + counter + " of" + addresses.size());
		System.out.println(((counter/addresses.size())*100) + "% complete");
		}
	
	BufferedWriter out = new BufferedWriter(new FileWriter(STATlocation + "/doneaddresses.txt"));
	Iterator it = alreadycomplete.iterator();
	while(it.hasNext()) {
	    out.write(it.next()+"\n");
	}
	out.close();

	
	}
		
		System.out.println("The program has completed processing " + addresses.size() + " github repositories.");
		
		
	
}


/**
 * this method isn't named correctly. 
 * 
 * What it actually does is make a folder for modifiedgitstats.py to put
 * all of its files. This folder is eventually deleted during clean-up.
 * <p>
 * @return String -Name of a temporary folder
 * @throws Exception -processbuilder Exception
 */
public String randomDirectory()throws Exception{
	
	String dest = currentAddress.replace("git@github.com:", "");
	dest = dest.replace("/", "");
	Process pc = new ProcessBuilder("mkdir", "/home/MTA/esantander/desktop/GithubResearch/" + dest +"Data").start();
	pc.waitFor();
	String out = "/home/MTA/esantander/desktop/GithubResearch/" + dest + "Data";
	return out;
	
	
}


/**
 * 
 * This method takes the current address and downloads ("clones") the target
 * project from github. 
 * 
 * Before doing this, it is supposed to delete any existing downloads in order
 * to keep used space to a minimum. It doesn't do this well. I don't think
 * that part works. I accomplished that instead in CLEAN UP.
 * 
 *  TODO: optimize cleanup
 * 
 * @return
 * @throws Exception
 */
	public String Cloner() throws Exception {
		String gotoo = randomAddress();
		ProcessBuilder p = new ProcessBuilder();
		p.inheritIO();
		p.command("rm", "-rf", "/home/MTA/esantander/desktop/GithubResearch/gits/").start().waitFor();
		p.command("mkdir", "/home/MTA/esantander/desktop/GithubResearch/gits/").start().waitFor();	
		ProcessBuilder pc = new ProcessBuilder();
		//make new string
		String c = "" + gotoo;
		c = c.replace("git@github.com:", "");
		c = c.replace("/", "");
		pc.command("/home/MTA/esantander/git/bin-wrappers/git", "clone", "" + gotoo, "/home/MTA/esantander/desktop/GithubResearch/gits/" + c).start().waitFor();
		return "/home/MTA/esantander/desktop/GithubResearch/gits/" + c ;

	}

	/**
	 * This method sets the currentAddress by generating a random number
	 * and then getting that index from 61kaddresses[]. This is actually the 
	 * third layer of randomization that the addresses experience, and looking
	 * at it now I realize it is completely not necessary. All the 61kaddresses[]
	 * are going to be processed eventually, so it doesn't matter WHEN they 
	 * are processed. We should remove this and simply iterate through the 
	 * 61kaddresses[] ArrayList.
	 * 
	 * TODO: remove this brutally unnecessary randomization.
	 * 
	 * @return currentAddress
	 */

	public String randomAddress()

	{
	
		int randomIndex = generated.get(generation_counter);
		String output = addresses.get(randomIndex);
		generation_counter++;
		currentAddress = output;
		return output;
	}
	




public static void main(String[] args) throws Exception {
	
	FetchDownloadAnalyze fetcher = new FetchDownloadAnalyze();
	fetcher.executeStats();
	
	
}

}