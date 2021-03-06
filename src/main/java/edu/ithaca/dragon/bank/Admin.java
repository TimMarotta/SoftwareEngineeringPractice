package edu.ithaca.dragon.bank;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Admin implements AdminAPI {
    private HashMap<String, Account> accounts;

    public ATM atm;
    public Teller teller;

    public Admin(){
        this.accounts = new HashMap<>();
        this.atm = new ATM(this);
        this.teller = new Teller(this);
    }

    public Account getAccount (String acctId) throws IllegalArgumentException {
        Account toReturn = accounts.get(acctId);

        if (toReturn != null){
            return toReturn;
        } else {
            throw new IllegalArgumentException("Account not found in bank. Invalid Account ID");
        }
    }

    public void createCheckingForTeller(String acctIdIn, String nameIn, String passwordIn, double startingBalance) throws IllegalArgumentException {
        if(accounts.containsKey(acctIdIn)){
            throw new IllegalArgumentException("Cannot create account; account ID already exists.");
        } else {
            accounts.put(acctIdIn, new Checking(acctIdIn, nameIn, passwordIn, startingBalance));
        }
    }

    public void createSavingsForTeller(String acctIdIn, String nameIn, String passwordIn, double startingBalance, double interestRateIn, double maxWithdrawalIn) throws IllegalArgumentException {
        if(accounts.containsKey(acctIdIn)){
            throw new IllegalArgumentException("Cannot create account; account ID already exists.");
        } else {
            accounts.put(acctIdIn, new Savings(acctIdIn, nameIn, passwordIn, startingBalance, interestRateIn, maxWithdrawalIn));
        }
    }

    public void closeAccount(String acctId) throws IllegalArgumentException, AcctFrozenException {
        if (getAccount(acctId) == null){
            throw new IllegalArgumentException("Account does not exist");
        }
        if (getAccount(acctId).getFrozenStatus()){
            throw new AcctFrozenException("Cannot close a frozen account");
        }
        accounts.remove(acctId);
    }

    /**
     * Calculates total balance of all accounts held by accounts HashMap
     * @return double of balance in the bank
     */
    @Override
    public double calcTotalAssets() throws AcctFrozenException {
        if (accounts.size() == 0){
            return 0;
        } else {
            double totalAssets = 0;
            for (Account toCheck : accounts.values()) {
                if (toCheck.getFrozenStatus()) {
                    toCheck.setFrozen(false);
                    totalAssets += toCheck.checkBalance(toCheck.getAcctId());
                    toCheck.setFrozen(true);
                } else {
                    totalAssets += toCheck.checkBalance(toCheck.getAcctId());
                }
            }
            return totalAssets;
        }
    }

    /**
     * Finds accounts with suspicious activity
     * @return list of account IDs with suspicious activity
     */
    @Override
    public Collection<String> findAcctIdsWithSuspiciousActivity() {
        return null;
    }

    /**
     * Freezes account so its methods cannot be used
     * @param acctId string ID of account that needs to be frozen
     * @throws IllegalArgumentException if acctId is not in the list
     */
    @Override
    public void freezeAccount(String acctId) throws IllegalArgumentException {
        if(accounts.containsKey(acctId)){
            accounts.get(acctId).setFrozen(true);
        } else {
            throw new IllegalArgumentException("Account ID invalid; cannot freeze an account that does not exist");
        }
    }

    /**
     * Unfreezes account so its methods can be used
     * @param acctId string ID of account that needs to be unfrozen
     * @throws IllegalArgumentException if acctId is not in the list
     */
    @Override
    public void unfreezeAcct(String acctId) throws IllegalArgumentException {
        if (accounts.containsKey(acctId)){
            accounts.get(acctId).setFrozen(false);
        } else {
            throw new IllegalArgumentException("Account ID invalid; cannot unfreeze an account that does not exist");
        }
    }
}
