INTRODUCTION
------------

This program is used to merge the contents of hosts.csv and events.csv, then output the merged file.


REQUIREMENTS
------------

This program has only been test on the following environment:

	* System: OS X El Capitan(Version 10.11.6)
	* java version: 1.8.0_101


INSTALLATION AND RUN
--------------------
 
 1. Unzip the .zip file
 2. Put the hosts.csv and events.csv file into the unzipped directory
 3. Open terminal and go to the unzipped directory
 4. Compile MergeCSV.java: javac MergeCSV.java
 5. Run the program: java MergeCSV

 * If you want to rename the output file, please run as: java MergeCSV <OUTPUT_FILE_NAME.csv>
 	Default output file name is "output.csv"

 * If you want to merge files other than hosts.csv and events.csv, please run as: 
 	java MergeCSV <HOSTS_FILE_NAME.csv> <EVENTS_FILE_NAME.csv> <OUTPUT_FILE_NAME.csv>

 * This program only support hosts.csv and events.csv format as following:
 	hosts.csv
		Host-Id , Host-Name , Host-IP-Address
	events.csv
		Host-Id , Timestamp , Event-Code , Event-Description

	If you have csv files with other formats, please change the HOSTSIZE and EVENTSIZE in MergeCSV.java on line 9 and 10.

 * Program will not stop if detect invalid entries, all other valid entries will be merged and output. 
 	Invalid entries will be print on terminal as exceptions.
