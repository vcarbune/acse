import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class Crawler {
    private DataSet dataSet;
    private String folderName;
    private TreeSet<String> documents;
    private boolean stopWordsFlag = false;
    private String stopWordsFile;
    private HashSet<String> stopWords;

    public Crawler(String folderName) {
        this.folderName = folderName;
        dataSet = new DataSet();
        documents = new TreeSet<String>();
    }

    public Crawler(String folderName, String stopWordsFile) {
        this.folderName = folderName;
        this.stopWordsFile = stopWordsFile;

        dataSet = new DataSet();
        documents = new TreeSet<String>();
        stopWords = new HashSet<String>();
    }

    /**
     * Reads the stop words from the file given by the stopWordsFile field. The
     * stop words are upperCased and after that added into a hashSet.
     * 
     */
    public void readStopWords() {
        this.stopWordsFlag = true;
        stopWords = new HashSet<String>();
        
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

    /**
     * Handles the reading of the terms from the corpus files in case the
     * stopWordsFlag is on. It removes the stop words from the corpus and counts
     * the position of the filtered words into the file after the stop words
     * were deleted. It adds the term plus the document Id and the position of
     * the word in the file into the dataSet.
     * 
     * @param reader
     *            The BufferedReader used to read from file.
     * @param docID
     *            The name of the file.
     * @throws IOException
     *             Exception thrown in case the file can not be read
     */
    private void processFileStopWordOn(BufferedReader reader, String docID)
    throws IOException {
        String line;
        int countPos = 0;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("-", " ");
            StringTokenizer tokens = new StringTokenizer(line);

            while (tokens.hasMoreElements()) {
                String token = tokens.nextToken();
                if (stopWords.contains(token) == false) {
                    countPos++;
                    String formatted = token.replaceAll("[^a-zA-Z0-9]", "");
                  //  System.out.println(formatted + " " + docID);
                    if (formatted.isEmpty() == false) {
                        dataSet.addPair(formatted.toUpperCase(), docID,
                                countPos);
                    }
                }
            }
        }
    }

    private void processLine(BufferedReader reader, String docID)
    throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("-", " ");
            StringTokenizer tokens = new StringTokenizer(line);

            while (tokens.hasMoreElements()) {
                String token = tokens.nextToken();
                String formatted = token.replaceAll("[^a-zA-Z0-9]", "");
                if (formatted.isEmpty() == false) {
                    dataSet.addPair(formatted.toUpperCase(), docID, 0);
                }
            }
        }
    }

    public DataSet readDocuments() throws IOException {
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            String fileName = listOfFiles[i].getName();
            int dotIndex = fileName.indexOf(".");
            String nameWithoutType = fileName.substring(0, dotIndex);
            documents.add(nameWithoutType);

            FileInputStream inputStream = new FileInputStream(listOfFiles[i]);
            DataInputStream dataInput = new DataInputStream(inputStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    dataInput));

            if (stopWordsFlag == true) {
                processFileStopWordOn(reader, nameWithoutType);
            } else {
                processLine(reader, nameWithoutType);
            }

            inputStream.close();
        }

        dataSet.setDocSet(documents);

        return dataSet;
    }

    public TreeSet<String> getDocumentIDs() {
        return documents;
    }

    public HashSet<String> getStopWords() {
        return stopWords;
    }

    public void setStopWordsFile(String stopWordsFile) {
        this.stopWordsFile = stopWordsFile;
    }
    
    

}
