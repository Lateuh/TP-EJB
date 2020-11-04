package converter;

import org.jdom2.JDOMException;

import java.io.IOException;
import java.net.MalformedURLException;

public interface IConverter {
    public double euroToOtherCurrency(double amount, String currencyCode) throws IOException, JDOMException;
}
