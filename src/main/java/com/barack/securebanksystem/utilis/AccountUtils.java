package com.barack.securebanksystem.utilis;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account created";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account successfully created";
    public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "This user does not have an account";
    public static final String ACCOUNT_FOUND_EXISTS_CODE = "004";
    public static final String ACCOUNT_FOUND_EXISTS_MESSAGE = "User Account found";
    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User Account credited successfully";
    public static final String ACCOUNT_DEBITED_FAILURE_CODE = "006";
    public static final String ACCOUNT_DEBITED_FAILURE_MESSAGE = "Not sufficient amount in the account";
    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "User Account debited successfully";
    public static final String RECEIVER_ACCOUNT_NOT_EXISTS_CODE = "008";
    public static final String RECEIVER_ACCOUNT_NOT_EXISTS_MESSAGE = "The receiver's account does not exist";
    public static final String TRANSFER_SUCCESS_CODE = "009";
    public static final String TRANSFER_SUCCESS_MESSAGE = "Transaction successfully done...";
    public static final String RECURSION_TRANSFER_CODE = "010";
    public static final String RECURSION_TRANSFER_MESSAGE = "You cannot send money to yourself...";
    public static String generateAccountNumber(){
        /**
         * 2023 + random 6 digits
         * */
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;
        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);

        return  year + randomNumber;
    }
}
