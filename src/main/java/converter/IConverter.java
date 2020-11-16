package converter;

import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public interface IConverter {
    public double euroToOtherCurrency(double amount, String currencyCode);
    public Map<Monnaie,Double> euroToOtherCurrencies(double amount);
}
