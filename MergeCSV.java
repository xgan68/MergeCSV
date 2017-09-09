import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MergeCSV{
	public static final int HOSTSIZE = 3;
	public static final int EVENTSIZE = 4;

	public static void main(String[] args) {

		String hostsFile = "hosts.csv";
		String eventsFile = "events.csv";
		String outputFile = "output.csv";

		if (args.length == 3) {
			hostsFile = args[0];
			eventsFile = args[1];
			outputFile = args[2];
		} else if (args.length == 1) {
			outputFile = args[0];
		} else if (args.length == 0) {
			hostsFile = hostsFile;
		} else {
			System.out.println("Please enter: \n java MergeCSV hosts_file_name.csv events_file_name.csv output_file_name.csv \n to start merging");
			return;
		}

		System.out.println("Start Merging...\n");

		ArrayList<String> hostsList = new ArrayList<>();
		readCSV(hostsFile, hostsList);
		ArrayList<String> eventsList = new ArrayList<>();
		readCSV(eventsFile, eventsList);

		HashMap<String, ArrayList<String>> hostsMap = new HashMap<>();
		parseItems(hostsList, hostsMap);

		writeCSV(hostsMap, eventsList, outputFile);
		
		System.out.println("\nFinished Merging " + hostsFile + " and " + eventsFile + ", output to: " + outputFile + "\n");

		return;
	}

	private static void writeCSV(HashMap<String, ArrayList<String>> hostsMap, 
								 ArrayList<String> eventsList, String outputFileName) {

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter(outputFileName);
			bw = new BufferedWriter(fw);
			
			int index = 0;
			for (String eventItems : eventsList) {
				String line = mergeStrings(eventItems, hostsMap, ++index);
				if (line.length() != 0) {
					bw.write(line);
				}
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static String mergeStrings(String eventItems, HashMap<String, ArrayList<String>> hostsMap, int index) {
		List<String> items = Arrays.asList(eventItems.split(","));
		String line = "";

		try{
			if (!hostsMap.containsKey(items.get(0))) {
				throw new HostNotFoundException(items.get(0), index);
			}

			if (items.size() != EVENTSIZE) {
				throw new EntrySizeException(items.get(0), "hosts.csv");
			}
		} catch (EntrySizeException ex) {
			System.out.println(ex.toString());
			return line;
		} catch (HostNotFoundException ex) {
			System.out.println(ex.toString());
			return line;
		}

		if (!hostsMap.containsKey(items.get(0))) {
			System.out.println("Error merging hosts and events: Cannot find hosts ID: " + items.get(0));
			return line;
		}

		String hostID = items.get(0);
		line = hostID;
		for (String hostItem : hostsMap.get(hostID)) {
			line = line + "," + hostItem;
		}
		for (int i = 1; i < items.size(); i++) {
			line = line + "," + items.get(i);
		}
		line += "\n";
		return line;
	}


	private static void parseItems(ArrayList<String> itemList, 
								   HashMap<String, ArrayList<String>> itemMap) {
		for (String item : itemList) {
			List<String> items = Arrays.asList(item.split(","));

			try{
				if (items.size() != HOSTSIZE) {
					throw new EntrySizeException(items.get(0), "hosts.csv");
				}
			} catch (EntrySizeException ex) {
				System.out.println(ex.toString());
				continue;
			}
	
			String hostID = items.get(0);
			ArrayList<String> hostItems = new ArrayList<String>(items.subList(1, items.size()));

			try {
				if (itemMap.containsKey(hostID)) {
					throw new DuplicatedHostsException(hostID);
				}
				itemMap.put(hostID, hostItems);
			} catch (DuplicatedHostsException ex) {
				System.out.println(ex.toString());
			}
		}
	}

	private static void readCSV(String hostsFile, ArrayList<String> hostsList) {

		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(hostsFile);
			br = new BufferedReader(fr);

		    String line = br.readLine();
		    while (line != null) {
		    	if (line.length() > 0) {
		    		hostsList.add(line);
		    	}
		       
		        line = br.readLine();
		    }
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		 finally {
		 	try{
		 		if (br != null) {
		 			br.close();
		 		}

		 		if (fr != null){
					fr.close();
		 		}
		 	} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}


class EntrySizeException extends Exception{
 	public EntrySizeException(String id, String type)
 	{
   		super("\n\tInvailed " + type + " entry found on host_ID: " + id);
  	}
}

class HostNotFoundException extends Exception{
 	public HostNotFoundException(String id, int index)
 	{
   		super("Cannot find hosts \n\tID: " + id + ", events.csv entry index: " + index + ".");
  	}
}

class DuplicatedHostsException extends Exception{
 	public DuplicatedHostsException(String id)
 	{
   		super("\n\tDuplicated hosts found on host_id: " + id + ".");
  	}
}







