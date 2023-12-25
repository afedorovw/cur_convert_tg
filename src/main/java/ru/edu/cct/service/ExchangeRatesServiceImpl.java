package ru.edu.cct.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import ru.edu.cct.client.CbrClient;
import ru.edu.cct.exception.ServiceException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.Optional;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService, CurPath {

    @Autowired
    private CbrClient client;

    public String getExchangeRate() throws ServiceException {
        Optional<String> xmlOptional = client.getCurrencyRatesXML();
        return xmlOptional.orElseThrow(() -> new ServiceException("Не удалось получить XML"));
    }

    @Cacheable(value = "usd", unless = "#result == null or #result.isEmpty()")
    @Override
    public String getUSDExchangeRate() throws ServiceException {
        return extractCurrencyValueFromXML(getExchangeRate(), USD_XPATH);
    }

    @Cacheable(value = "eur", unless = "#result == null or #result.isEmpty()")
    @Override
    public String getEURExchangeRate() throws ServiceException {
        return extractCurrencyValueFromXML(getExchangeRate(), EUR_XPATH);
    }

    private static String extractCurrencyValueFromXML(String xml, String xpathExpression)
            throws ServiceException {
        InputSource source = new InputSource(new StringReader(xml));
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            Document document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

            String result = xpath.evaluate(xpathExpression, document);
            return result.substring(0, result.length() - 2);
        } catch (XPathExpressionException e) {
            throw new ServiceException("Не удалось распарсить XML", e);
        }
    }
}
