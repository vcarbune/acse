import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    
    public static void main(String args[]) throws IOException, FileNotFoundException {

        if (args.length < 1) {
            System.out.println("Usage: Main <document_folder>");
            return;
        }
        
        initializeLogging();

        Crawler crawler = new Crawler(args[0]);
        
        try {
            DataSet dataSet = crawler.readDocuments();
        }
        catch (IOException e) {
            System.out.println("Could not read the documents. Exiting...");
            return;
        }

        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter new query (or \"quit\" to quit.): ");
            String query = in.nextLine();

            if (query.equals("quit")) {
                break;
            } else {
                // TODO: Process query
            }
        }
    }
}