package com.barack.securebanksystem.service.Impl;

import com.barack.securebanksystem.dto.*;
import com.barack.securebanksystem.entity.Transaction;
import com.barack.securebanksystem.entity.User;
import com.barack.securebanksystem.repository.UserRepository;
import com.barack.securebanksystem.utilis.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Component
public class UserServiceImpl implements  UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private EmailService emailService;
    @Autowired
    PasswordEncoder passwordEncoder;
    private  boolean flag;
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .email(userRequest.getEmail())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();
        User savedUser = userRepository.save(newUser);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulation your account has been created \nYour account Details:\nAccount Name: "
                        +savedUser.getFirstName() + " "+savedUser.getLastName()+ " "
                        +savedUser.getOtherName()+ "\n Account Number: "+savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() +" "
                                + savedUser.getLastName() + " "
                                +savedUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userDB = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_EXISTS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_EXISTS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userDB.getFirstName() + " "
                                    + userDB.getLastName() + " " + userDB.getOtherName())
                        .accountBalance(userDB.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }
        User userDB = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return "Account Details: "+userDB.getFirstName() +" "
                                  +userDB.getLastName()+ " "
                                  +userDB.getOtherName();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userRepository.findAll();
    }

    @Override
    public BankResponse getUserById(Long id) {
        if(!userRepository.existsById(id)){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User user = userRepository.getById(id);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_EXISTS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_EXISTS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(user.getFirstName() +" "+
                                     user.getLastName() +" "+
                                     user.getOtherName())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        if(!userRepository.existsByAccountNumber(request.getAccountNumber())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userDB = userRepository.findByAccountNumber(request.getAccountNumber());
        userDB.setAccountBalance(userDB.getAccountBalance().add(request.getAmount()));
        userRepository.save(userDB);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userDB.getEmail())
                .subject("CREDIT ALERT")
                .messageBody("Your account has been credited with an amount of: "+request.getAmount()+" \nYour account Details:\nAccount Name: "
                        +userDB.getFirstName() + " "+userDB.getLastName()+ " "
                        +userDB.getOtherName()+ "\nAccount Number: "+userDB.getAccountNumber() +"\n"
                        +"Your new balance is: "+userDB.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailDetails);
        TransactionRequest transaction = TransactionRequest.builder()
                .transactionType("CREDIT")
                .accountNumber(userDB.getAccountNumber())
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transaction);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userDB.getFirstName() +" "+
                                     userDB.getLastName() + " "+
                                     userDB.getOtherName())
                        .accountNumber(userDB.getAccountNumber())
                        .accountBalance(userDB.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        if(!userRepository.existsByAccountNumber(request.getAccountNumber())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        BankResponse debitAccountResponse = deductionAccount(request.getAccountNumber(), request.getAmount());
        User userDB = userRepository.findByAccountNumber(request.getAccountNumber());
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userDB.getEmail())
                .subject("DEBIT ALERT")
                .messageBody("Your account has been debited with an amount of: "+request.getAmount()+" \nYour account Details:\nAccount Name: "
                        +userDB.getFirstName() + " "+userDB.getLastName()+ " "
                        +userDB.getOtherName()+ "\nAccount Number: "+userDB.getAccountNumber() +"\n"
                        +"Your new balance is: "+userDB.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailDetails);
        TransactionRequest transaction = TransactionRequest.builder()
                .transactionType("DEBIT")
                .accountNumber(userDB.getAccountNumber())
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transaction);
        return debitAccountResponse;
    }
    @Override
    public BankResponse transfer(TransferRequest request) {
        this.flag = false;
        if(!userRepository.existsByAccountNumber(request.getReceiverAccountNumber())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.RECEIVER_ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.RECEIVER_ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        BankResponse bankResponse = deductionAccount(request.getSenderAccountNumber(), request.getAmount());
        if(this.flag) return bankResponse;
        User sender = userRepository.findByAccountNumber(request.getSenderAccountNumber());
        User receiver = userRepository.findByAccountNumber(request.getReceiverAccountNumber());
        if(request.getSenderAccountNumber().equals(request.getReceiverAccountNumber())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.RECURSION_TRANSFER_CODE)
                    .responseMessage(AccountUtils.RECURSION_TRANSFER_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(sender.getFirstName() +" "+
                                    sender.getLastName() + " "+
                                    sender.getOtherName())
                            .accountNumber(sender.getAccountNumber())
                            .accountBalance(sender.getAccountBalance())
                            .build())
                    .build();
        }
        receiver.setAccountBalance(receiver.getAccountBalance().add(request.getAmount()));
        userRepository.save(receiver);
        EmailDetails emailDetailsSender = EmailDetails.builder()
                .recipient(sender.getEmail())
                .subject("ACCOUNT DEBITED")
                .messageBody("Your account has been debited with an amount of: "+request.getAmount()+" \nYour account Details:\nAccount Name: "
                        +sender.getFirstName() + " "+sender.getLastName()+ " "
                        +sender.getOtherName()+ "\nAccount Number: "+sender.getAccountNumber() +"\n"
                        +"Your new balance is: "+sender.getAccountBalance())
                .build();
        EmailDetails emailDetailsReceiver = EmailDetails.builder()
                .recipient(sender.getEmail())
                .subject("ACCOUNT CREDITED")
                .messageBody("Your account has been credited with an amount of: "+request.getAmount()+" \nYour account Details:\nAccount Name: "
                        +sender.getFirstName() + " "+sender.getLastName()+ " "
                        +sender.getOtherName()+ "\nAccount Number: "+sender.getAccountNumber() +"\n"
                        +"Your new balance is: "+sender.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailDetailsSender);
        emailService.sendEmailAlert(emailDetailsReceiver);
        TransactionRequest trans1 = TransactionRequest.builder()
                .transactionType("DEBIT")
                .accountNumber(request.getSenderAccountNumber())
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(trans1);
        TransactionRequest trans2 = TransactionRequest.builder()
                .transactionType("CREDIT")
                .accountNumber(request.getReceiverAccountNumber())
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(trans2);
        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(sender.getFirstName() +" "+
                                sender.getLastName() + " "+
                                sender.getOtherName())
                        .accountNumber(sender.getAccountNumber())
                        .accountBalance(sender.getAccountBalance())
                        .build())
                .build();
    }
    @Override
    public BankResponse deductionAccount(String accountNumber, BigDecimal amount) {
        User userDB = userRepository.findByAccountNumber(accountNumber);
        if(userDB.getAccountBalance().compareTo(amount) < 0){
            this.flag = true;
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_FAILURE_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_FAILURE_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userDB.getFirstName() +" "+
                                    userDB.getLastName() + " "+
                                    userDB.getOtherName())
                            .accountNumber(userDB.getAccountNumber())
                            .accountBalance(userDB.getAccountBalance())
                            .build())
                    .build();
        }
        userDB.setAccountBalance(userDB.getAccountBalance().subtract(amount));
        userRepository.save(userDB);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userDB.getFirstName() +" "+
                                userDB.getLastName() + " "+
                                userDB.getOtherName())
                        .accountNumber(userDB.getAccountNumber())
                        .accountBalance(userDB.getAccountBalance())
                        .build())
                .build();
    }
}
