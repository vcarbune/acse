import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.PublicCloneable;


public class ROCGraph {

	private ArrayList<Double> fpRate = new ArrayList<Double>();
	private ArrayList<Double> tpRate = new ArrayList<Double>();

	public void addPoint(double tp, double fp) {
		tpRate.add(tp);
		fpRate.add(fp);
	}

	public void createROCGraph(String fileName){
		XYSeries series = new XYSeries("ROC");
		StringBuilder builder = new StringBuilder();
		int poz = 0;
		for(double fp : fpRate){
			double tp = tpRate.get(poz++);
			series.add(fp, tp);
			builder.append(poz).append(" ");
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

		chart.setBackgroundPaint(Color.WHITE);

		XYLineAndShapeRenderer renderer =
			new XYLineAndShapeRenderer(true, true);
		renderer.setBaseShapesVisible(true);
		renderer.setBaseShapesFilled(true);
		((XYPlot)chart.getPlot()).setRenderer(renderer);
		// label the points
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		XYItemLabelGenerator generator = new MyGenerator();
		renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelsVisible(true);


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

class MyGenerator implements XYItemLabelGenerator {

	private static int poz = 0;
    @Override
    public String generateLabel(XYDataset dataset, int series, int item) {
         poz++;
         return "" + poz;
    }
}
