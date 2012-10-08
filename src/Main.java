import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.LogManager;

public class Main {

    void printStaticStats(DataSet ds) {
        // TODO: Static statistics for the whole dataset.
    }

    void printDynamicStats(String query) {
        // TODO: Dynamic statistics for each query.
    }

    public static void initializeLogging() {
        try {
            InputStream inputStream = new FileInputStream("logging.properties");
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (Exception e) {
            // Just ignore configuration if file does not exist.
        }
    }
    
    public static void initializeFlags(String args[]) {
        for (int i = 1; i < args.length; ++i) {
            if (args[i].equals(Config.PARAM_STOPWORD)) {
                Config.enableStopwordElimination = true;
            } else if (args[i].equals(Config.PARAM_STEMMING)) {
                Config.enableStemming = true;
            }
        }
    }
    
    public static void main(String args[]) throws IOException, FileNotFoundException {

        if (args.length < 1) {
            System.out.println("Usage: Main <document_folder>" +
            		"[" + Config.PARAM_STOPWORD + "]" +
            		"[" + Config.PARAM_STEMMING + "]");
            
            return;
        }
        
        initializeLogging();
        
        if (args.length >= 2)
            initializeFlags(args);

        Crawler crawler = new Crawler(args[0]);
        DataSet dataSet;

        try {
            dataSet = crawler.readDocuments();
        }
        catch (IOException e) {
            System.out.println("Could not read the documents. Exiting...");
            return;
        }

        dataSet.logStaticStats();

        QueryHandler handler = new QueryHandler(dataSet);

        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter new query (or \"quit\" to quit): ");
            String queryString = in.nextLine();

            if (queryString.equals("quit")) {
                break;
            } else {
                //TODO: log dynamic stats

                long startTime = System.currentTimeMillis();

                Query query = new Query(queryString);
                ArrayList<String> docs = handler.retrieveDocumentsForQuery(query);

                long endTime = System.currentTimeMillis();

                System.out.println("The query was processed in " + (endTime-startTime)
                        + " milliseconds.");
                
                System.out.println("Number of documents: " + docs.size());

                System.out.println("Results:");

                for (String s: docs) {
                    System.out.println(s);
                }

                System.out.println();

            }
        }
    }
}
