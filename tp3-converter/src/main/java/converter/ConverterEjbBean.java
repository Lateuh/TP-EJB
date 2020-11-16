package converter;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Remote
@Stateless(name = "ConverterEjbEJB")
public class ConverterEjbBean implements IConverter{
    public ConverterEjbBean() {
    }

    @Override
    public double euroToOtherCurrency(double amount, String currencyCode) throws IOException{
        URL url = new URL("http://currencies.apps.grandtrunk.net/getlatest/"
                                +"EUR"+"/"+currencyCode);
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String taux = in.readLine();
            in.close();
            return amount*Double.parseDouble(taux);
        }catch (IOException e){
            System.out.println("PAS DE TAUX ENREGISTRE POUR LA MONNAIE : "+ currencyCode);
        }
        return -1;
    }


    @Override
    public List<String> euroToOtherCurrencies(double amount) {
        List<String> listCurrencies = getAvailableCurrencies();
        List<String> listResultats = new ArrayList<>();

        for(String s : listCurrencies){
            try {
                listResultats.add(amount+" EUR is : "+euroToOtherCurrency(amount,s)+" "+s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return listResultats;
    }

    @Override
    public List<String> getAvailableCurrencies(){
        List<String> listCurrencies = new ArrayList<>();

        URL url = null;
        try {
            url = new URL("http://currencies.apps.grandtrunk.net/currencies");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String code;
            while((code = in.readLine()) != null){
                listCurrencies.add(code);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listCurrencies;
    }


}
