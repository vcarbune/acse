
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Crawler {

    private String rootLink;
    private DocumentBuilder builder;
    
    public Crawler(String rootLink) {
        this.rootLink = rootLink;
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private InputStream getDocument(String link) {
        try {
            URL url = new URL(link);
            InputStream page = url.openStream();
            
            return page;
        } catch (Exception ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private void parseDocument(InputStream page) {
        Document document = null;
        try {
            document = builder.parse(page);
        } catch (Exception ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
