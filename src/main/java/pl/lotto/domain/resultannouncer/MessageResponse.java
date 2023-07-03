package pl.lotto.domain.resultannouncer;

enum MessageResponse {

    HASH_DOES_NOT_EXIST_MESSAGE("Given hash doesn't exist"),
    WAIT_MESSAGE("Result is not calculated yet. Please try later!"),
    WIN_MESSAGE("Congratulations, you won!"),
    LOSE_MESSAGE("Unfortunately you lose, try again!"),
    ALREADY_CHECKED_MESSAGE("You have already checked you ticket, please come back later!");
    final String message;

    MessageResponse(String message) {
        this.message = message;
    }
}
