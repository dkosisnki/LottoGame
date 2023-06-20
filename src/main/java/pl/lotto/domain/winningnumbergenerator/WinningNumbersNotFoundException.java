package pl.lotto.domain.winningnumbergenerator;

class WinningNumbersNotFoundException extends RuntimeException{
    public WinningNumbersNotFoundException(String message) {
        super(message);
    }
}
