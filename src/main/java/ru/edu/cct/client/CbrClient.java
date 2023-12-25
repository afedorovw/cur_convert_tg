package ru.edu.cct.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;
import ru.edu.cct.exception.ServiceException;

import java.io.IOException;
import java.util.Optional;

@Component
public class CbrClient {

    @Autowired
    private OkHttpClient client;

    @Value("${cbr.currency.rates.xml.url}")
    private String cbrCurrencyRatesXmlUrl;

    public Optional<String> getCurrencyRatesXML() throws ServiceException {
        Request request = new Request.Builder()
                .url(cbrCurrencyRatesXmlUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            return body == null ? Optional.empty() : Optional.of(body.string());
        } catch (IOException e) {
            throw new ServiceException("Ошибка получения курсов валют от ЦБ РФ", e);
        }
    }
}
