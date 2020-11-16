package converter;

import org.jdom2.JDOMException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface IConverter {
    public double euroToOtherCurrency(double amount, String currencyCode) throws IOException, JDOMException;
    public List<String> getAvailableCurrencies();
    public List<String> euroToOtherCurrencies(double amount);
}
