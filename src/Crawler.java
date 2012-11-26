import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;

public class Crawler {

	private String folderName;
	private String stopWordsFile;
	private HashSet<String> stopWords;

	public Crawler(String folderName) {
		this.folderName = folderName;
		stopWords = new HashSet<String>();
	}

	public Crawler(String folderName, String stopWordsFile) {
		this.folderName = folderName;
		this.stopWordsFile = stopWordsFile;
	}

	/**
	 * Reads the stop words from the file given by the stopWordsFile field. The stop words are
	 * upperCased and after that added into a hashSet.
	 *
	 */
	public void readStopWords() {
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(stopWordsFile);
			DataInputStream dataInput = new DataInputStream(inputStream);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					dataInput));

			String line;
			while ((line = reader.readLine()) != null) {
				stopWords.add(line.toUpperCase());
			}

			inputStream.close();
		} catch (IOException e) {
			System.out.println("The stop word file can not be found!");
			System.exit(2);
		}
	}


	private void processFile(BufferedReader reader, DocEntry docEntry)
	throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			line = line.replaceAll("[^a-zA-Z]", " ");

			StringTokenizer tokens = new StringTokenizer(line.toUpperCase());

			while (tokens.hasMoreElements()) {
				String token = tokens.nextToken();
				if (Config.enableStopwordElimination && stopWords.contains(token) == true) {
					continue;
				}
				if (Config.enableStemming) {
					Stemmer stemmer = new Stemmer();
					stemmer.add(token.toLowerCase().toCharArray(), token.length());
					stemmer.stem();

					token = stemmer.toString().toUpperCase();
				}

				if (token.isEmpty() == false) {
					docEntry.incCount(token);
				}
			}
		}
	}

	public void readDocEntries(DocSet docSet, String docSetName) throws IOException {
		File folder = new File(docSetName);
		if(folder.isDirectory() == false)
			throw new IOException("The input should be a folder!"); 

		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			String fileName = listOfFiles[i].getName();
			
			if (fileName.startsWith(".")) { // check for Mac
			    continue;
			}

			boolean spam = false; 
			if(fileName.startsWith("spmsg"))
				spam = true; 
			//	System.out.println(fileName + " " + spam);
			DocEntry docEntry = new DocEntry(fileName, spam);

			FileInputStream inputStream = new FileInputStream(listOfFiles[i]);
			DataInputStream dataInput = new DataInputStream(inputStream);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					dataInput));

			processFile(reader, docEntry);
			docSet.addDoc(docEntry);

			inputStream.close();
		}
	}

	public ArrayList<DocSet> readDocSet() throws IOException{
		File folder = new File(folderName);
		if(folder.isDirectory() == false)
			throw new IOException("The input should be a folder!");

		ArrayList<DocSet> listDocSets = new ArrayList<DocSet>();

		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if(listOfFiles[i].isDirectory()){
				DocSet docSet = new DocSet();

				readDocEntries(docSet, listOfFiles[i].getAbsolutePath()); 
				docSet.precomputeWordWeights();

				listDocSets.add(docSet);
			}
		}

		return listDocSets;
	}
	
	/**
	 * For phase 2.
	 * 
	 * Read the doc files and compute the vectors for all of them.
	 * 
	 * TODO: test this
	 * 
	 * @return
	 * @throws IOException
	 */
	public ArrayList<DocEntry> readDocEntriesAndComputeVectors(String folderName) throws IOException {
	    File folder = new File(folderName);
        if (folder.isDirectory() == false) 
            throw new IOException("The input should be a folder!"); 
        
        File[] listOfFiles = folder.listFiles();
        
        ArrayList<DocEntry> docList = new ArrayList<DocEntry>();
        int numDocs = listOfFiles.length;
        HashMap<String, Integer> dfMap = new HashMap<String, Integer>();
        
        for (int i = 0; i < listOfFiles.length; i++) {
            String fileName = listOfFiles[i].getName();
            
            if (fileName.startsWith(".")) { // check for Mac
                continue;
            }

            boolean spam = false; 
            if (fileName.startsWith("spmsg"))
                spam = true; 
            DocEntry docEntry = new DocEntry(fileName, spam);

            FileInputStream inputStream = new FileInputStream(listOfFiles[i]);
            DataInputStream dataInput = new DataInputStream(inputStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    dataInput));

            processFile(reader, docEntry);
            inputStream.close();
            
            docList.add(docEntry);
            
            // Update the doc frequency map
            for (Map.Entry<String, Integer> entry: docEntry.getWordCounts()) {
                Integer df = dfMap.get(entry.getKey());
                if (df == null) {
                    df = 0;
                }
                dfMap.put(entry.getKey(), df + 1);
            }
        }
        
        // Precompute the vector for each document
        for (DocEntry doc: docList) {
            doc.computeWordWeights(dfMap, numDocs);
        }
        
        return docList;
	}

	/**
	 * Retrieves the stop words read from the stopWordsFile but upper cased.
	 *
	 * @return A HashSet containing all stop words.
	 */
	public HashSet<String> getStopWords() {
		return stopWords;
	}

	/**
	 * Sets the file where the stop words are stored.
	 *
	 * @param stopWordsFile The name of the file.
	 */
	public void setStopWordsFile(String stopWordsFile) {
		this.stopWordsFile = stopWordsFile;
	}
}
