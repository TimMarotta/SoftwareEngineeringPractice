package edu.ithaca.dragon.bank;

import java.util.List;

public class Checking implements Account {
    private int acctId;
    private String name;
    private String password;
    private double balance;
    private boolean frozen;
    private List<String []> history;

    public Checking(int acctIdIn, String nameIn, String passwordIn, double startingBalance){

    }

    public double checkBalance(String acctId){
        return 0;
    }

    public void withdraw(String acctId, double amount) throws InsufficientFundsException{

    }

    public void deposit(String acctId, double amount){

    }

    public void transfer(String acctIdToWithdrawFrom, String acctIdToDepositTo, double amount) throws InsufficientFundsException{

    }

    public String transactionHistory(String acctId){
        return "";
    }
}