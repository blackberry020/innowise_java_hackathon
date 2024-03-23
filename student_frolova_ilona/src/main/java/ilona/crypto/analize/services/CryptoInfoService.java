package ilona.crypto.analize.services;

import java.util.List;

public interface CryptoInfoService {

    List<String> getCryptoRate() throws Exception;
    String getCryptoRate(String cryptoName) throws Exception;
}
