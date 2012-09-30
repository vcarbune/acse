
import java.io.IOException;
import java.util.Scanner;

public class Main {

    void printStaticStats(DataSet ds) {
        // TODO: Static statistics for the whole dataset.
    }

    void printDynamicStats(String query) {
        // TODO: Dynamic statistics for each query.
    }


    public static void main(String args[]) throws IOException {

        if (args.length < 1) {
            System.out.println("Usage: Main <document_folder>");
            return;
        }

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