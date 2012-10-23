import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;


public class TablePerQuery {
    private ArrayList<Double> recall;
    private ArrayList<Double> precision;

    public TablePerQuery(){
        recall = new ArrayList<Double>();
        precision = new ArrayList<Double>();
    }

    public void addRecall(Double d){
        recall.add(d);
    }

    public void addPrecision(Double p){
        precision.add(p);
    }

    public void interpolate(){
        ArrayList<Double> newR = new ArrayList<Double>();
        ArrayList<Double> newP = new ArrayList<Double>();

        double recallInit = 0.0;
        double recallStep = 0.1;
        Iterator<Double> recallIt = recall.iterator();
        Iterator<Double> precIt = precision.iterator();

        double realRec=1.0, realPrec = 0.0, nextPrec = 0.0, nextRec=0.0;
        if(recallIt.hasNext()){
             realRec = recallIt.next();
             realPrec = precIt.next();
            if(recallIt.hasNext()){
                nextPrec = precIt.next();
                nextRec = recallIt.next();
            }
        }
        realPrec = Math.max(realPrec, nextPrec);

        for(double currRec = recallInit; currRec <= 1.0; currRec += recallStep){
            newR.add(currRec);
            DecimalFormat f = new DecimalFormat("##.00");
            System.out.print("Recall: " + f.format(currRec));
            if(currRec <= realRec){
                newP.add(realPrec);
                System.out.println(" Prec: " + realPrec);
            }else{
                if(recallIt.hasNext() == false){
                    realPrec = nextPrec;
                    realRec = nextRec;
                    newP.add(realPrec);
                    System.out.println(" Prec: " + realPrec);
                }else{
                    while(((currRec >  nextRec) || (nextRec <= currRec + 1)) && recallIt.hasNext()){
                        realRec = nextRec;
                        realPrec = Math.max(nextPrec, realPrec);
                        nextRec = recallIt.next();
                        nextPrec = precIt.next();
                    }
                    newP.add(realPrec);
                    System.out.println(" Prec: " + realPrec);
                }               
            }
        }


        recall = newR;
        precision = newP;
    }

    public ArrayList<Double> getRecall() {
        return recall;
    }
    public ArrayList<Double> getPrecision() {
        return precision;
    }

    public Double getPrecisionForPos(int index){
        return precision.get(index);
    }

}
