package com.semicolon.safebank;

import com.semicolon.safebank.console.InputReader;
import com.semicolon.safebank.console.SafeBankMenu;
import com.semicolon.safebank.service.AccountService;

import java.util.Scanner;

public class SafeBankApplication {

    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        InputReader inputReader = new InputReader(new Scanner(System.in));
        SafeBankMenu menu = new SafeBankMenu(accountService, inputReader);
        menu.start();
    }
}