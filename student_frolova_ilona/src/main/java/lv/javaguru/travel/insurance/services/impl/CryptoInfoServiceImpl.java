package lv.javaguru.travel.insurance.services.impl;

import lv.javaguru.travel.insurance.services.CryptoInfoService;
import lv.javaguru.travel.insurance.client.Client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

import javax.json.*;

@Service
public class CryptoInfoServiceImpl implements CryptoInfoService {

    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";

    @Autowired
    private Client client;

    public void printAll(String jsonText) throws Exception {

        System.out.println("STARTED PRINTING ALL");

        InputStream stream = new ByteArrayInputStream(jsonText.getBytes(StandardCharsets.UTF_8));

        JsonReader jsonReader = Json.createReader(stream);
        JsonArray jsonArray = jsonReader.readArray();

        for (JsonValue jsonValue : jsonArray) {
            JsonObject jsonObject = (JsonObject) jsonValue;
            System.out.println(jsonObject.getString("symbol") + " " + jsonObject.getString("price"));
        }

        jsonReader.close();
    }

    @Override
    public String getCryptoRate() throws Exception {
        var jsonOptional = client.getCryptoInfoJson();
        String json = jsonOptional.orElseThrow(
                () -> new Exception("Couldn't get JSON")
        );

        printAll(json);

        return "";
    }
}