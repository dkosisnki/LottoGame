package pl.lotto.domain.resultchecker;

class ResultCheckerNotFoundException extends RuntimeException{

    public ResultCheckerNotFoundException(String message) {
        super(message);
    }
}
