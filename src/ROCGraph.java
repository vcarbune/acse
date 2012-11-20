import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class ROCGraph {

	private ArrayList<Double> fpRate = new ArrayList<Double>();
	private ArrayList<Double> tpRate = new ArrayList<Double>();

	public void addPoint(double tp, double fp) {
	    tpRate.add(tp);
	    fpRate.add(fp);
	}
	
	public void createROCGraph(String fileName){
		XYSeries series = new XYSeries("ROC");
		int poz = 0;
		for(double fp : fpRate){
			double tp = tpRate.get(poz++);
			series.add(fp, tp);
		}

		XYDataset xyDataset = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"ROC Curve",      // chart title
				"False positive rate",                      // x axis label
				"True positive rate",                      // y axis label
				xyDataset,                  // data
				PlotOrientation.VERTICAL,
				true,                     // include legend
				true,                     // tooltips
				false                     // urls
		);

		chart.setBackgroundPaint(Color.white);
		
		
		// Creating image file for the chart
		try {
		    FileOutputStream out = new FileOutputStream(new File(fileName));
		    ChartUtilities.writeChartAsPNG(out, chart, 800, 600);
 
            out.close();
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
