package com.barack.securebanksystem.controller;

import com.barack.securebanksystem.dto.*;
import com.barack.securebanksystem.entity.User;
import com.barack.securebanksystem.service.Impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Account Management APIs",
    description = "This API is to manage the CRUD operations of a user and take care of the transfer")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(
            summary = "Create New User Account",
            description = "Creating a new user and assigning an account ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED"
    )
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }
    @GetMapping
    @Operation(
            summary = "List of all Users Account",
            description = "Getting the list of all users account with all their details"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    @GetMapping("/balanceEnquiry")
    @Operation(
            summary = "Balance Enquiry",
            description = "Given the account number and check how much the user has"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    public BankResponse banlanceEnquiry(@RequestBody EnquiryRequest request){
        return userService.balanceEnquiry(request);
    }
    @GetMapping("/nameEnquiry")
    @Operation(
            summary = "Name Enquiry",
            description = "Given the account number and see all the details of an user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    public String nameEnquiry(@RequestBody EnquiryRequest request){
        return  userService.nameEnquiry(request);
    }
    @PostMapping("/credit")
    @Operation(
            summary = "Credit an account",
            description = "Sending money to a specific user after providing an account number and an amount of money"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request){
        return userService.creditAccount(request);
    }
    @PostMapping("/debit")
    @Operation(
            summary = "Debit an account",
            description = "Withdrawing money from a specific user after providing an account number and an amount of money"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request){
        return userService.debitAccount(request);
    }
    @PostMapping("transfer")
    @Operation(
            summary = "P2P Transaction",
            description = "Making a peer to peer transaction between 2 account after getting their account number and the amount of money to transfer"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    public BankResponse transfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }
    @GetMapping("/{id}")
    @Operation(
            summary = "Find an user by ID",
            description = "Getting a specific user detail after providing his unique ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    public BankResponse getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }
}
