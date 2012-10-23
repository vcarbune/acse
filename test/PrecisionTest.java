import static org.junit.Assert.*;

import java.util.TreeSet;

import org.junit.Test;


public class PrecisionTest {
    
    
    @Test
    public void testQueryResult(){
        QueryResult q = new QueryResult("doc1", 0);
        System.out.println(q.equals(new QueryResult("doc8", 0)));
    }
    
    @Test
    public void testPrecision(){
        PrecisionRecall p = new PrecisionRecall("/home/loredana/ETH/Sem3/InformationRetrieval/test");
        TreeSet<QueryResult> t = new TreeSet<QueryResult>();
        t.add(new QueryResult("doc2", 0.8));
        t.add(new QueryResult("doc3", 0.7));
        t.add(new QueryResult("doc8", 0.6)); 
        t.add(new QueryResult("doc7", 0.5));
        t.add(new QueryResult("doc9", 0.4));
        t.add(new QueryResult("doc1", 0.3));
        p.computePrecisionAndRecall(1, t);
        
        System.out.println("BAU!!!!!" + t.size());
        t = new TreeSet<QueryResult>();
        t.add(new QueryResult("doc3", 0.8));
        t.add(new QueryResult("doc2", 0.7));
        t.add(new QueryResult("doc5", 0.6)); 
        t.add(new QueryResult("doc7", 0.5));
        t.add(new QueryResult("doc4", 0.4));
        p.computePrecisionAndRecall(2, t);
        
        p.computeAverageOverAllQueries();
        
    }

}
