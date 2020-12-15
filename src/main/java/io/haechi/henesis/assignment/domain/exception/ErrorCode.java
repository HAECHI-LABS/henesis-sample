package io.haechi.henesis.assignment.domain.exception;

public enum ErrorCode {
    BAD_REQUEST(4000),
    UNAUTHORIZED(4001),
    EMAIL_DOES_NOT_EXIST(4002),
    INVALID_PASSWORD(4003),
    OTP_AUTHENTICATION_FAILED(4004),
    INVALID_GAS_PRICE(4005),
    DUPLICATED_WALLET_NAME(4006),
    INSUFFICIENT_BALANCE(4007),
    NOT_VERIFIED_IP(4008),
    TIMEOUT_IP(4009),
    ALREADY_VERIFIED_IP(4010),
    INVALID_IP_VERIFY_REQUEST(4011),
    DUPLICATED_WITHDRAWAL_POLICY(4012),
    ALREADY_WHITELISTED_ADDRESS(4013),
    DUPLICATED_LABEL(4014),
    ALREADY_WHITELISTED_IP_ADDRESS(4015),
    FAIL_TO_REPLACE_TRANSACTION(4016),
    ALREADY_PROCESSED_TRANSACTION(4017),
    ALREADY_REPLACED_TRANSACTION(4018),
    SAME_GAS_PRICE(4019),
    INTERNAL_SERVER(5000),
    INVALID_MINIMUM_BALANCE(5001),
    INACTIVE_WALLET(5002),
    FAIL_TO_SEND_EMAIL(5006);

    private final int value;
    private final String message;

    private ErrorCode(int value, String message) {
        this.value = value;
        this.message = message;
    }

    private ErrorCode(int value) {
        this.value = value;
        this.message = "";
    }

    public int getValue() {
        return this.value;
    }

    public String getMessage() {
        return this.message;
    }
}