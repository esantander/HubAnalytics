HubAnalytics
============

Toolset for massive analysis of Github projects

The final product is a .csv file containing the commit statistics on an arbitrary amount of Github Projects.

##Essential Elements

### 1. Java Core
A Java program interfaces with Github API v3 for the:
* Creation of a randomized list of Github project addresses
  * A corpus of english words and non-words is used with Github API's search functionality to create a list of addresses for cloning.
* Local cloning of previously-obtained random-addresses
  * Java method takes a .txt file of random addresses and locally clones them

The Java program further interfaces with Sysem.runtime to:

* Execute gitstats.py on locally cloned repositories
* Remove auxiliary files created in the process of executing gitstats.py

### 2. Allied Python Processes

Two Python scripts assist in the compilation of data in to, eventually, a single .csv file.

* gitstats.py
  * Compiles data from the .git tree structure in a respository and outputs a .txt file
* getStats.py
  * Parses data from each .txt file output by gitstats.py
  * Produces a .csv wherein each project constitutes a line


## Workflow

##### 1. Create list of random addresses
Starting product: None

Ending product: list of addresses (.txt)

* main.java works with LinkSynthesizer.java to query the Github API search function and output a randomly generated git address.
  * The amount of time this occurs is dictated by the amount of times the loop in Main.java is allowed to run.
  * LinkSynthesizer.java contains features that rate-limit the queries to the 5k/hr limit imposed by Github API (GAPI).

##### 2. Clone and Analyze
Starting Product: List of addresses (.txt)

Ending Product: Directory full of STAT files (.txt)

* FetchDownloadAnalyze.java reads a list of addresses and utilizes System.runtime to execute the auxiliary python process "gitstats.py"
  * FetchDownloadAnalyze also:
    * performs cleanup necessary after the execution of gitstats.py
    * intelligently interfaces with gitstats.py so gitstats.py output is appropriately named

##### 3. Parse and Collate

Starting product: Directory full of STATxxxx.txt files

Final product: .csv file with aggregated data from all STAT files in directory

* getStats.py takes the address of a directory with STATxxx.txt files and outputs a .csv file


##Additional Notes

#### Known Issues
* Gitstats.py can produce STATxxx.txt files with negative total lines of code. mainCleaner.java in Extras can detect these, and they should be removed from the directory.


