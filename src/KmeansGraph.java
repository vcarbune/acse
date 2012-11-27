import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class KmeansGraph {
    
    public void createGraph(ArrayList<Double> purity, ArrayList<Double> randIndex){
        XYSeries seriesPur = new XYSeries("Purity");
        int index = 0; 
        for(Double p: purity){
            int x = Config.infK + index; 
            seriesPur.add(x, p);
            index ++;
        }
        
        XYSeries seriesRand = new XYSeries("RandIndex"); 
        int rIndex = 0; 
        for(Double r: randIndex){
            int x = Config.infK + rIndex;
            seriesRand.add(x, r);
            rIndex ++; 
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesPur); 
        dataset.addSeries(seriesRand);
        
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Kmeans: Purity And RandIndex", // chart title
                "Number of clusters",           // x axis label
                "Rate",                         // y axis label
                dataset,                        // data
                PlotOrientation.VERTICAL,
                true,                           // include legend
                true,                           // tooltips
                false                           // urls
        );

        chart.setBackgroundPaint(Color.WHITE);
        
        XYLineAndShapeRenderer renderer =
            new XYLineAndShapeRenderer(true, true);
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
        ((XYPlot)chart.getPlot()).setRenderer(renderer);
        
        // Integer values for X-axis
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        // label the points
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        renderer.setBaseItemLabelsVisible(true);


        // Creating image file for the chart
        try {
            FileOutputStream out = new FileOutputStream(new File("KmeansGraph.png"));
            ChartUtilities.writeChartAsPNG(out, chart, 1200, 800);

            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}



