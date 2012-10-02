import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class Crawler {
    private DataSet dataSet;
    private String folderName;
    private TreeSet<String> documents;

    public Crawler(String folderName) {
        this.folderName = folderName;
        dataSet = new DataSet();
        documents = new TreeSet<String>();
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
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(dataInput));

            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(line);

                while (tokens.hasMoreElements()) {
                    String s = tokens.nextToken().replaceAll("[^a-zA-Z0-9]","");
                    if(s.isEmpty() == false){
                        dataSet.addPair(s, nameWithoutType);
                    }
                }
            }
        }

        return dataSet;
    }

    public TreeSet<String> getDocumentIDs(){
        return documents;
    }

}
