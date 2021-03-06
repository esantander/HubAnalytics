import java.io.*;
import java.util.*;

/*
*This method reads STATxx.txt files in a directory and determines if they contain negative lines in the "Total Lines" field.
*It is critical to detect and delete these files, as they interefere fatally with the performance of getStats.py.
**/
public class main {
	
	public static void main (String[] args) throws Exception{
		
		File folder = new File(//STATFILEdirectorypath);
		File[] listofFiles = folder.listFiles();
		int progresscounter=0;
		
		for (File f: listofFiles)
		{
			if(!f.getPath().contains(".DS_Store") && !f.getPath().contains("getStats.py")){
			Scanner scan = new Scanner(f);
			scan.nextLine();
			scan.nextLine();
			scan.nextLine();
			scan.nextLine();
			scan.nextLine();
			scan.nextLine();
			String s = scan.nextLine();
			
				if (s.contains("-")){
					System.out.println("The file " + f.getName() + " contains negative lines.");
				}
			}
			
			
			if (progresscounter %1000==0){
				int total = listofFiles.length;
				float perc;
				perc = (progresscounter / total) * 100;
				
				System.out.println("We are " + perc + " percent done.");
			}
			progresscounter++;
			
		}
		System.out.println("ANALYSIS COMPLETE");
	}

}
