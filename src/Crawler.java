import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Crawler {
    private DataSet dataSet;
    private String folderName;
    private HashSet<String> documents;

    public Crawler(String folderName) {
        this.folderName = folderName;
        dataSet = new DataSet();
        documents = new HashSet<String>();
    }

    public DataSet readDocuments() throws IOException {
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            documents.add(listOfFiles[i].getName());

            FileInputStream inputStream = new FileInputStream(listOfFiles[i]);
            DataInputStream dataInput = new DataInputStream(inputStream);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(dataInput));

            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(line);

                while (tokens.hasMoreElements()) {
                    String s = tokens.nextToken().replaceAll("[^a-zA-Z0-9]","");
                    if(s.isEmpty() == false){
                    //    System.out.println(s  + " " + listOfFiles[i].getName());
                        dataSet.addPair(s,
                                listOfFiles[i].getName());
                    }
                }
            }
        }

        return dataSet;
    }

    public HashSet<String> getDocumentIDs(){
        return documents;
    }

}
