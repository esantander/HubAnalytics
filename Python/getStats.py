#!/usr/bin/env python
import os
import mmap
import re
import sys
import string

#this list will hold all files and their info as tuples
masterList = []

##
## ----------------------------------------------------------------
##Gets files from directory and calls getInfo on them in a loop.
def main(argv):
    sawArgument = 0

    for arg in argv[1:]:
        if arg[0] == '-':
            if len(arg) > 1 and (arg[1] == 'h' or arg[1] == 'H'):
                printHelp(argv[0])
                sys.exit(0)
        else:
            pa = arg
            sawArgument = 1

    if sawArgument == 0:
        printHelp(argv[0])

    files = [f for f in os.listdir(pa) if f.startswith("STAT")]
    # walks through every file in current directory
    for fi in files:
        fi = "%s%s" %(pa,fi)
        getInfo(fi)

    #prints as comma seperated values
    for ls in masterList:
        for value in ls:
            for thing in value:
                print "%s," %thing,
        print
        print

    sys.exit(0)

##
## ----------------------------------------------------------------
## prints help
##
def printHelp(progname):

    sepIndex = string.rfind(progname, "/")
    if sepIndex < 0:
        printProgname = progname
    else:
        printProgname = progname[sepIndex + 1:]

    sys.stderr.write("\ngetStats.py [OPTION]... [Path to STATS files folder]\n\n")
    sys.stderr.write("For example: \"./getStats.py  ../STATfiles/\"\n\n")
    sys.stderr.write("Outputs stats for all STAT files in directroy designated by path into comma seperated values.\n\n")
    sys.stderr.write("FILE, AGE, DAYS ACTIVE, #FILES, #LINES ADDED, #LINES REMOVED, #CURRENT LINES, AUTHORS, COMMITS BY TIME ZONE (FROM -1100 TO 1300), COMMITS BY DAY AND HOUR (24*7), COMMITS BY HOUR (24), COMMITS BY DAY OF WEEK (MON-SUN), COMMITS BY MONTH IN YEAR (2008-01 -> 2013-12),LINES ADDED BY MONTH IN YEAR (2008-01 -> 2013-12), LINES REMOVED BY MONTH IN YEAR (2008-01 -> 2013-12), COMMITS BY MONTH (OPTIONAL(COMMENTED OUT)) \n\n")

    sys.exit(0)

##
## ----------------------------------------------------------------
##Puts info into masterList[] from each file as a tuple

def getInfo(fi):
    fileList = []
    size = os.stat(fi).st_size
    f = open(fi)
    data = mmap.mmap(f.fileno(), size, access=mmap.ACCESS_READ)

    #commits year and month, lines added and removed (soon)
    temp = re.search("(?<=Month , Commits , Lines added , Lines removed )(?s).*(?=Commits by Year)", data)
    temp = temp.group()
    ladded = []
    lremoved = []
    combymonth = []
    tlinesadded = 0
    tlinesremoved = 0

    for year in range(2008,2014):
        for month in range(1,13):
            sstr = "(%d-%02d , )([0-9]+) , ([0-9]+) , ([0-9]+)" %(year, month)
            t = re.search(sstr, temp)

            if t:
                la = t.group(3)
                lr = t.group(4)
                t = t.group(2)
            else:
                t = "0"
                la = "0"
                lr = "0"

            ladded.append(la)
            lremoved.append(lr)
            tlinesadded = tlinesadded + int(la)
            tlinesremoved = tlinesremoved + int(lr)
            combymonth.append(t)
    #commits by year and month  lines added and removed

    #get age and days active files lines and authors
    age = re.search("(?<=Age )[0-9]+(?= days)", data)
    active = re.search("[0-9]+(?= Active Days)", data)
    files = re.search("(?<=Total Files )[0-9]+", data)
    curLines = re.search("(?<=Total Lines of Code )[0-9]+", data)
    authors = re.search("[0-9]+(?= Authors)", data)
    fileList.append(fi)
    fileList.append(age.group())
    fileList.append(active.group())
    fileList.append(files.group())
    fileList.append(tlinesadded)
    fileList.append(tlinesremoved)
    fileList.append(curLines.group())
    fileList.append(authors.group())
    #end age and days active files lines and authors

    #commits by time zone
    temp = re.search("(?<=Timezone , Commits)(?s).*", data)
    temp = temp.group()

    for zone in range(11,0,-1):
        sstr = "( -%02d00 , )([0-9]+)" %zone
        t = re.search(sstr, temp)

        if t:
            t = t.group(2)
        else:
            t = "0"

        fileList.append(t)

    for posZone in range(0,14):
        sstr = "( \+%02d00 , )([0-9]+)" %posZone
        t = re.search(sstr, temp)

        if t:
            t = t.group(2)
        else:
            t = "0"
        fileList.append(t)

    #commits by time zone

    #commits by hour of particular day
    temp = re.search("(?<=Weekday / Commits BY hour)(?s).*(?=Commits by Month of Year)", data)
    temp = temp.group()

    for day in ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]:
        sstr = "( %s) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+)" %day

        for hour in range(24):
            t = re.search(sstr, temp)
            t = t.group(hour+2)
            fileList.append(t)

    #commits by hour of particular day

    #commits by hour of day
    temp = re.search("(?<=Commits by HOUR OF THE DAY \(0-23\))(?s).*(?=Commits by Day)", data)
    temp = temp.group()

    for x in range(24):
        sstr = "(%s) ([0-9]+)" %x
        t = re.search(sstr, temp)
        t = t.group(2)
        fileList.append(t)

    #commits by hour of day

    #commits by day
    temp = re.search("(?<=Commits by Day)(?s).*(?=Weekday / Commits BY hour)", data)
    temp = temp.group()

    for x in ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]:
        sstr = "(%s) ([0-9]+)" %x
        t = re.search(sstr, temp)
        t = t.group(2)
        fileList.append(t)

    #commits by day

    #add remaining variables
    for th in combymonth:
        fileList.append(th)

    for thing in ladded:
        fileList.append(thing)

    for thi in lremoved:
        fileList.append(thi)

    #commits by month EXTRA

    #temp = re.search("(?<=Commits by Month of Year)(?s).*(?=Commits by year/month)", data)
    #temp = temp.group()

    #for x in range(1,13):
        #sstr = "(%s) ([0-9]+)" %x
        #t = re.search(sstr, temp)
        #t = t.group(2)
        #fileList.append(t)

    #commits by month EXTRA


    f.close()
    #Populates masterList with all the atributes
    masterList.append([fileList])

##
## ----------------------------------------------------------------
## This causes the main() function to be called if run from
## the command line; otherwise we can be loaded as an
## importable module
if __name__ == "__main__": main(sys.argv)

