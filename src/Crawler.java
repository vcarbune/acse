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
        stopWords = new HashSet<String>();
    }

    public Crawler(String folderName, String stopWordsFile) {
        this.folderName = folderName;
        this.stopWordsFile = stopWordsFile;

        dataSet = new DataSet();
        documents = new TreeSet<String>();
    }

    /**
     * Reads the stop words from the file given by the stopWordsFile field. The
     * stop words are upperCased and after that added into a hashSet.
     * 
     */
    public void readStopWords() {
        this.stopWordsFlag = true;

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
     * Handles reading of the terms from the corpus files in case the
     * stopWordsFlag is on. It removes the stop words from the corpus and counts
     * the position of the filtered words into the file after the stop words
     * were deleted. It adds the term plus the document Id and the position of
     * the word in the file into the dataSet.
     * 
     * @param reader The BufferedReader used to read from file.
     * @param docID The name of the file.
     * @throws IOException Exception thrown in case the file can not be read
     */
    private void processFile(BufferedReader reader, String docID)
            throws IOException {
        String line;
        int countPos = 0;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("-", " ");
            line = line.replaceAll("[^a-zA-Z0-9]", " ");

            StringTokenizer tokens = new StringTokenizer(line);

            while (tokens.hasMoreElements()) {
                String token = tokens.nextToken();
                if (stopWords.contains(token) == false) {
                    countPos++;

                    if (token.isEmpty() == false) {
                        dataSet.addPair(token.toUpperCase(), docID,
                                countPos);
                    }
                }
            }
        }
    }

    /**
     * Handles reading from a corpus file in case no flag is set.
     * 
     * @param reader The BufferedReader object that is connected to a corpus file
     * @param docID  The ID of the document currently reading from.
     * @throws IOException Exception thrown in case the file can not be read.
     */
    @Deprecated
    private void processLine(BufferedReader reader, String docID)
            throws IOException {
        String line;
        int count = 0;
        
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("-", " ");
            StringTokenizer tokens = new StringTokenizer(line);

            while (tokens.hasMoreElements()) {
                String token = tokens.nextToken();
                String formatted = token.replaceAll("[^a-zA-Z0-9]", "");
                if (formatted.isEmpty() == false) {
                    dataSet.addPair(formatted.toUpperCase(), docID, count);
                }
                count++;
            }
        }
    }
    
    // TODO(anyone?): This should be integrated in processFile and method removed.
    private void processFileStemmingOn(BufferedReader reader,
            String docID) throws IOException{
        String line;
       
        int count = 0;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("-", " ");
            line = line.replaceAll("[^a-zA-Z0-9]", " ");

            StringTokenizer tokens = new StringTokenizer(line);
            while(tokens.hasMoreElements()){
                String token = tokens.nextToken();
                if (token.isEmpty() == false) {
                    count++;
                    //System.out.println("init: " + token );
                    Stemmer stemmer = new Stemmer();
                    stemmer.add(token.toLowerCase().toCharArray(), token.length());
                    stemmer.stem();
                    
                    String stemmed = stemmer.toString().toUpperCase();
                    //System.out.println("Stemmed: " + stemmed);
                    
                    dataSet.addPair(stemmed, docID, count);
                }
            }
        }
        
    }

    /**
     * Reads the corpus documents line by line and adds the terms together with 
     * the document ID and the word position into a DataSet. The terms are changed 
     * accordingly to which flags are on. If the stopWords flag is set then the stop 
     * words are removed. If the stemming is on then each term is stemmed and then 
     * added to the DataSet.
     *  
     * @return  A DataSet object containing terms from corpus.
     * @throws IOException Exception thrown in case the corpus file can not be found.
     */
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

            if(Config.enableStemming == true){
                processFileStemmingOn(reader, nameWithoutType);
            } else {
                processFile(reader, nameWithoutType);
            }


            inputStream.close();
        }

        dataSet.setDocSet(documents);

        return dataSet;
    }

    /**
     * Retrieves all the names of documents from corpus.
     * @return A treeSet object containing all documents name. 
     */
    public TreeSet<String> getDocumentIDs() {
        return documents;
    }

    /**
     * Retrieves the stop words read from the stopWordsFile
     * but upper cased.
     * @return A HashSet containing all stop words.
     */
    public HashSet<String> getStopWords() {
        return stopWords;
    }


    /**
     * Sets the file where the stop words are stored. 
     * @param stopWordsFile The name of the file.
     */
    public void setStopWordsFile(String stopWordsFile) {
        this.stopWordsFile = stopWordsFile;
    }



}
