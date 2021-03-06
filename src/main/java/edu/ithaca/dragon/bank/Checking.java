package edu.ithaca.dragon.bank;

import java.util.ArrayList;
import java.util.List;

public class Checking implements Account {
    private String acctId;
    private String name;
    private String password;
    private double balance;
    private boolean frozen;
    private List<String> history;

    public Checking(String acctIdIn, String nameIn, String passwordIn, double startingBalance) throws IllegalArgumentException {
        if (acctIdIn.length() == 10) {
            this.acctId = acctIdIn;
        } else throw new IllegalArgumentException("Account ID must be 10 digits");

        if (isNameValid(nameIn)) {
            this.name = nameIn;
        } else throw new IllegalArgumentException("Name is not valid");

        if (isAmountValid(startingBalance)) {
            this.balance = startingBalance;
        } else throw new IllegalArgumentException("Starting balance is invalid");

        this.password = passwordIn;
        frozen = false;
        history = new ArrayList<String>();
    }

    public String getAcctId(){
        return acctId;
    }

    public String getName(){
        return name;
    }

    public String getAcctPassword(){
        return password;
    }

    public double checkBalance(String acctId) throws AcctFrozenException{
        return this.balance;
    }

    public boolean getFrozenStatus(){
        return frozen;
    }

    public void withdraw(String acctId, double amount) throws InsufficientFundsException, AcctFrozenException{
        if(frozen) {
            throw new AcctFrozenException("Cannot deposit into a forzen account");
        }
        else {
            if (amount > balance) {
                throw new InsufficientFundsException("Cannot withdraw amount greater than balance");
            }
            if (!isAmountValid(amount)) {
                throw new IllegalArgumentException("Amount is not valid");
            } else {
                balance -= amount;
                history.add("withdrawal of " + String.valueOf(amount));
            }
        }
    }

    public void deposit(String acctId, double amount) throws AcctFrozenException{
        if(frozen){
            throw new AcctFrozenException("Cannot deposit into a frozen account");
        }
        else {
            if (isAmountValid(amount)) {
                balance += amount;
                history.add("deposit of " + String.valueOf(amount));
            } else {
                throw new IllegalArgumentException("Amount is not valid");
            }
        }
    }

    public String transactionHistory(String acctId) throws AcctFrozenException{
        if(frozen){
            throw new AcctFrozenException("Cannot return from a frozen account");
        }
        else {
            String historyStr = "";
            for (int i = 0; i < history.size(); i++) {
                historyStr += history.get(i);
                if (i < history.size() - 1) historyStr += "; ";
            }
            return historyStr;
        }
    }

    public static boolean isNameValid(String name){
        if (name.length() < 3) return false;

        int spaceCount = 0;
        //check there's at least one space
        for (int i = 0; i < name.length()-1; i++){
            if(name.charAt(i) == ' '){
                spaceCount++;
            }
        }
        if(spaceCount < 1) return false;

        for (int i = 0; i < name.length(); i++){
            if(Character.isLetter(name.charAt(i)) || name.charAt(i) == ' '){
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean isAmountValid(double amount){
        if (amount < 0){
            return false;
        } else if (amount == 0) {
            return true;
        }

        // check number of decimal places
        String checkDouble = Double.toString(amount);
        int indexDecimal = checkDouble.indexOf('.');
        return checkDouble.length() - indexDecimal <= 3;
    }

    public void setFrozen(boolean value){
        this.frozen = value;
    }
}
