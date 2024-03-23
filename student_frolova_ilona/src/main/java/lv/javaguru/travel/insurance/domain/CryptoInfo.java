package lv.javaguru.travel.insurance.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CryptoInfo {
    @SerializedName("currencies")
    private List<Crypto> currencies;
}
