package lv.javaguru.travel.insurance.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Crypto {
    @SerializedName("symbol")
    private String symbol;

    @SerializedName("price")
    private String price;

    @Override
    public String toString() {
        return symbol + ' ' + price;
    }
}

