package lv.javaguru.travel.insurance.services.impl;

import com.google.gson.Gson;
import lv.javaguru.travel.insurance.domain.Crypto;
import lv.javaguru.travel.insurance.domain.CryptoInfo;
import lv.javaguru.travel.insurance.services.CryptoInfoService;
import lv.javaguru.travel.insurance.client.Client;

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
        String json = jsonOptional.orElseThrow(
                () -> new Exception("Couldn't get JSON")
        );

        return json;
    }

    private List<Crypto> getObjectsFromJson(String jsonText) {

        jsonText = "{ currencies: " + jsonText + "}";

        CryptoInfo cryptoInfo = new Gson().fromJson(jsonText, CryptoInfo.class);
        List<Crypto> cryptoList = cryptoInfo.getCurrencies();

        /*for (Crypto crypto : cryptoList) {
            System.out.println(crypto);
        }*/

        return cryptoList;
    }

    private List<String> getAllRates(String jsonText) throws Exception {

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