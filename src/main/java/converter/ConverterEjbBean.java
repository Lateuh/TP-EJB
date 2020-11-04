package converter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import javax.ejb.Stateless;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "ConverterEjbEJB")
public class ConverterEjbBean implements IConverter{
    public ConverterEjbBean() {
    }

    @Override
    public double euroToOtherCurrency(double amount, String currencyCode) throws IOException, JDOMException {
        SAXBuilder sxb = new SAXBuilder();
        URL url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        Document doc = sxb.build(con.getInputStream());
        Element racine = doc.getRootElement();
        Namespace ns = Namespace.getNamespace("http://www.ecb.int/vocabulary/2002-08-01/eurofxref");
        Element elm = racine.getChild("Cube",ns);
        Element elm2 = elm.getChild("Cube",ns);



        List<Element> listCube = new ArrayList<Element>(elm2.getChildren());
        for (Element e : listCube) {
            System.out.println(e.toString());
            if(e.getAttributeValue("currency").equals(currencyCode)){
                System.out.println("Code currency found");
                return amount * Double.parseDouble(e.getAttributeValue("rate"));
            }
        }
        System.out.println("Pas de code currency correspondant");
        return 0;
    }
}
