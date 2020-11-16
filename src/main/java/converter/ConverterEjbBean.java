package converter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Remote
@Stateful(name = "ConverterEjbEJB")
public class ConverterEjbBean implements IConverter{
    private List<Monnaie> listeMonnaieDispo;

    public ConverterEjbBean() {
        listeMonnaieDispo = new ArrayList<>();
    }

    public List<Element> getCubeElements(){
        SAXBuilder sxb = new SAXBuilder();
        URL url = null;
        try {
            url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = sxb.build(con.getInputStream());
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element racine = doc.getRootElement();
        Namespace ns = Namespace.getNamespace("http://www.ecb.int/vocabulary/2002-08-01/eurofxref");
        Element elm = racine.getChild("Cube",ns);
        Element elm2 = elm.getChild("Cube",ns);

        List<Element> listCube = new ArrayList<Element>(elm2.getChildren());
        return listCube;
    }

    @Override
    public double euroToOtherCurrency(double amount, String currencyCode){
        List<Element> listCube = getCubeElements();
        for (Element e : listCube) {
            if(e.getAttributeValue("currency").equals(currencyCode)){
                return amount * Double.parseDouble(e.getAttributeValue("rate"));
            }
        }
        System.out.println("Pas de code currency correspondant");
        return -1;
    }

    public List<Monnaie> getAvailableCurrencies(){
        listeMonnaieDispo.clear();
        List<Element> listCube = getCubeElements();
        for(Element e : listCube){
            listeMonnaieDispo.add(new Monnaie(e.getAttributeValue("currency"),Double.parseDouble(e.getAttributeValue("rate"))));
        }
        remplirListeMonnaie();
        return listeMonnaieDispo;
    }

    @Override
    public Map<Monnaie,Double> euroToOtherCurrencies(double amount){
        getAvailableCurrencies(); // fonctionne pas sans rappeler cette méthode
        Map<Monnaie,Double> resMap = new HashMap<>();
        for(Monnaie m : listeMonnaieDispo){
            resMap.put(m, amount * m.getTauxChange());
        }
        return resMap;
    }

    public void remplirListeMonnaie(){
        List<Element> listeElmPays = getListElmPays();
            for (Element e : listeElmPays) {
                try{
                    String code = e.getChild("Ccy").getValue();
                    for (Monnaie m : listeMonnaieDispo) {
                        if (m.getCodeMonnaie().equals(code)) {
                            m.getListePays().add(e.getChild("CtryNm").getValue());
                            m.setNomCompletMonnaie(e.getChild("CcyNm").getValue());
                        }
                    }
                } catch (NullPointerException nulle){
                    System.out.println("pas d'info ici");
                }
            }
    }



    public List<Element> getListElmPays(){
        SAXBuilder sxb = new SAXBuilder();
        URL url = null;
        try {
            url = new URL("https://www.currency-iso.org/dam/downloads/lists/list_one.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = sxb.build(con.getInputStream());
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element racine = doc.getRootElement();
        Element elm = racine.getChild("CcyTbl");
        List<Element> listeElmPays = elm.getChildren();

        return listeElmPays;
    }
}
