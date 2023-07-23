package pl.lotto.domain.winningnumbergenerator;

public class WinningNumbersNotFoundException extends RuntimeException{
    public WinningNumbersNotFoundException(String message) {
        super(message);
    }
}
