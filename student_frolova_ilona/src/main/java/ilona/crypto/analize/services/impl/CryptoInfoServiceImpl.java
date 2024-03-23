package ilona.crypto.analize.services.impl;

import com.google.gson.Gson;
import ilona.crypto.analize.services.CryptoInfoService;
import ilona.crypto.analize.domain.Crypto;
import ilona.crypto.analize.domain.CryptoInfo;
import ilona.crypto.analize.client.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CryptoInfoServiceImpl implements CryptoInfoService {

    @Autowired
    private Client client;

    private String getJsonContent() throws Exception {
        var jsonOptional = client.getCryptoInfoJson();

        return jsonOptional.orElseThrow(
                () -> new Exception("Couldn't get JSON")
        );
    }

    private List<Crypto> getObjectsFromJson(String jsonText) {
        jsonText = "{ currencies: " + jsonText + "}";
        CryptoInfo cryptoInfo = new Gson().fromJson(jsonText, CryptoInfo.class);
        return cryptoInfo.getCurrencies();
    }

    private List<String> getAllRates(String jsonText) {

        List<Crypto> cryptoList = getObjectsFromJson(jsonText);

        StringBuilder currentMessage = new StringBuilder();
        List<String> messages = new ArrayList<>();

        for (Crypto crypto : cryptoList) {

            if (currentMessage.length() > 1000) {
                messages.add(currentMessage.toString());
                currentMessage.setLength(0);
            }

            currentMessage
                    .append(crypto.toString())
                    .append('\n');
        }

        messages.add(currentMessage.toString());

        return messages;
    }

    @Override
    public List<String> getCryptoRate() throws Exception {
        String json = getJsonContent();
        return getAllRates(json);
    }

    @Override
    public String getCryptoRate(String cryptoName) throws Exception {
        String jsonText = getJsonContent();
        List<Crypto> cryptoList = getObjectsFromJson(jsonText);
        List<Crypto> resultList = cryptoList.stream().filter(it -> Objects.equals(it.getSymbol(), cryptoName)).toList();

        if (resultList.isEmpty())
            return "Unknown crypto";
        else
            return resultList.get(0).toString();
    }
}