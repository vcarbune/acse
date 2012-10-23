import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class PrecisionRecall {
    private HashMap<Integer, ArrayList<Integer>> testResults;
    private ArrayList<TablePerQuery> tablesInterpolated;


    public PrecisionRecall(String fileName) {
        testResults = new HashMap<Integer, ArrayList<Integer>>();
        readTestResults(fileName);
        tablesInterpolated = new ArrayList<TablePerQuery>();
    }

    private void readTestResults(String file){
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            DataInputStream dataInput = new DataInputStream(inputStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    dataInput));

            String line;
            int queryID;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(line);
                if(tokens.hasMoreTokens() == false)
                    break;
                queryID = Integer.parseInt(tokens.nextToken());

                ArrayList<Integer> docIds = new ArrayList<Integer>();
                while(tokens.hasMoreTokens()){
                    docIds.add(Integer.parseInt(tokens.nextToken()));
                }
                testResults.put(queryID, docIds);
            }
            inputStream.close();
        } catch (IOException e) {
            System.out.println("The file with the test results can not be opened!");
            System.exit(2);
        }
    }

    public void computePrecisionAndRecall(int queryId, TreeSet<QueryResult> result){
        TablePerQuery table = new TablePerQuery();
        ArrayList<Integer> docs = testResults.get(queryId);
        int expectedRes = docs.size();
        if(expectedRes == 0){
            System.out.println("The query does not return any result!");
            return;
        }

        int correctResults = 0;
        int retrievedRes = 0;
        ArrayList<QueryResult> listResults = new ArrayList<QueryResult>();
        listResults.addAll(result);
        int docId;
        for(QueryResult res: listResults){
            if(correctResults >= expectedRes)
                break;
            retrievedRes ++;
            docId = Integer.parseInt(res.getDocId().replaceAll("[a-zA-Z]", ""));
            if(docs.contains(docId)){
                correctResults ++;
            }

            table.addRecall(((double) correctResults)/expectedRes);
            table.addPrecision(((double) correctResults) /retrievedRes);

            //         System.out.println(docId +  "correct "+ correctResults+ " " +((double) correctResults)/expectedRes 
            //                + "   " + (double) correctResults /retrievedRes);
        }

        System.out.println("###Query: " + queryId + " #####");
        table.interpolate();
        tablesInterpolated.add(table);

    }

    public double[] computeAverageOverAllQueries(){
        System.out.println("### Average over all queries ###");
        double avg[] = new double[11];
        int nrQueries = tablesInterpolated.size(); 
        for(TablePerQuery t:tablesInterpolated){
            ArrayList<Double> precision = t.getPrecision();
            int index = 0;
            for(Double d: precision){
                avg[index] += d/nrQueries;
                index++;
            }
        }

        for(int i = 0; i <11; i++){
            double recall = i * 0.1;
            System.out.println(recall + "  " + avg[i]);
        }

        return avg;

    }
    
    public void generateGraph() throws IOException{
        XYSeriesCollection dataset = new XYSeriesCollection();  
        XYSeries xySeries = new XYSeries( "Precision-Recall Graph");  
        xySeries.add( 1, 10 );  
        xySeries.add( 2, 4 );  
        xySeries.add( 3, 6 );  
        xySeries.add( 4, 12 );  
        xySeries.add( 5, 11 );  
        xySeries.add( 6, 29 );  
        xySeries.add( 7, 33 );  
       
   
       
        dataset.addSeries( xySeries );  
    
       
        JFreeChart chart = ChartFactory.createXYLineChart( "Park Chart",  
                                                           "Day",  
                                                           "# of Visitors",  
                                                           dataset,  
                                                           PlotOrientation.VERTICAL,  
                                                           true,  
                                                           false,  
                                                           false );  
       
        FileOutputStream out = new FileOutputStream( new File( "parkchart.png" ) );  
        ChartUtilities.writeChartAsPNG( out, chart, 400, 200 );    
    }
}
