package lv.javaguru.travel.insurance.services.impl;

import lv.javaguru.travel.insurance.services.CryptoInfoService;
import lv.javaguru.travel.insurance.client.Client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.json.*;

@Service
public class CryptoInfoServiceImpl implements CryptoInfoService {

    @Autowired
    private Client client;

    private JsonArray getJsonContent(String jsonText) {
        InputStream stream = new ByteArrayInputStream(jsonText.getBytes(StandardCharsets.UTF_8));
        JsonReader jsonReader = Json.createReader(stream);

        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close();

        return jsonArray;
    }

    private List<String> getAllRates(String jsonText) throws Exception {

        JsonArray jsonArray = getJsonContent(jsonText);

        StringBuilder currentMessage = new StringBuilder();
        List<String> messages = new ArrayList<>();

        for (JsonValue jsonValue : jsonArray) {
            JsonObject jsonObject = (JsonObject) jsonValue;

            if (currentMessage.length() > 1000)
            {
                messages.add(currentMessage.toString());
                currentMessage.setLength(0);
            }

            currentMessage
                    .append(jsonObject.getString("symbol"))
                    .append(" ")
                    .append(jsonObject.getString("price"))
                    .append('\n');

            //System.out.println(jsonObject.getString("symbol") + " " + jsonObject.getString("price"));
        }

        messages.add(currentMessage.toString());

        return messages;
    }

    private String getSpecificRate(String cryptoName) {
        return "";
    }

    @Override
    public List<String> getCryptoRate() throws Exception {
        var jsonOptional = client.getCryptoInfoJson();
        String json = jsonOptional.orElseThrow(
                () -> new Exception("Couldn't get JSON")
        );

        return getAllRates(json);
    }

    @Override
    public String getCryptoRate(String cryptoName) throws Exception {
        return null;
    }
}