package pl.lotto.domain.numberreceiver;

import java.util.UUID;

public class HashGenerator implements HashGenerable{

    @Override
    public String getHash() {
        return UUID.randomUUID().toString();
    }

}
