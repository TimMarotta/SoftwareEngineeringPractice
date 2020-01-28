package edu.ithaca.dragon.bank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void getBalanceTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        assertEquals(200, bankAccount.getBalance());

        bankAccount = new BankAccount("a@b.com", 0);
        assertEquals(0, bankAccount.getBalance());


    }

    @Test
    void withdrawTest() throws InsufficientFundsException {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        // check zero balance
        bankAccount.withdraw(100);
        assertEquals(100, bankAccount.getBalance());

        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(200));
        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(-20));

        bankAccount.withdraw(100);
        assertEquals(0, bankAccount.getBalance());

        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(1));
        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(-20000));
    }

    @Test
    void isEmailValidTest(){
        //valid equivalence class
        //border case
        assertTrue(BankAccount.isEmailValid( "a@b.com"));

        //invalid equivalence class email length
        //border case-less
        assertFalse( BankAccount.isEmailValid(""));

        //invalid equivalence class special characters
        //not border case
        // prefix tests
        assertFalse(BankAccount.isEmailValid("a-@b.com"));
        //invalid equivalence class email with dots
        //not border case
        assertFalse(BankAccount.isEmailValid("a..@b.com"));
        //invalid equivalence class email with dots
        //border case
        assertFalse(BankAccount.isEmailValid(".a@b.com"));
        //invalid equivalence class special characters
        //not border case
        assertFalse(BankAccount.isEmailValid("a#b@b.com"));

        // domain tests
        //invalid equivalence class email domain
        //border case
        assertFalse(BankAccount.isEmailValid("a@b."));
        //invalid equivalence class email domain
        //not a border case
        assertFalse(BankAccount.isEmailValid("a@b.."));
        assertFalse(BankAccount.isEmailValid("a@b#.com"));
        //test length of domain
        assertFalse(BankAccount.isEmailValid("a@b"));

    }

    @Test
    void constructorTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance());
        //check for exception thrown correctly
        assertThrows(IllegalArgumentException.class, ()-> new BankAccount("", 100));
    }

    @Test
    void isAmountValidTest() {
        // check negative
        assertFalse(BankAccount.isAmountValid(-23.45));

        // check zero
        assertTrue(BankAccount.isAmountValid(0));

        // check positive
        assertTrue(BankAccount.isAmountValid(1.00));
        // check one decimal place
        assertTrue(BankAccount.isAmountValid(2.0));
        // check two decimal places
        assertTrue(BankAccount.isAmountValid(3.45));
        // check three decimal places
        assertFalse(BankAccount.isAmountValid(4.567));

        // check large positive
        assertTrue(BankAccount.isAmountValid(12345.67));
        // check one decimal place
        assertTrue(BankAccount.isAmountValid(23456.7));
        // check two decimal places
        assertTrue(BankAccount.isAmountValid(34567.89));
        // check three decimal places
        assertFalse(BankAccount.isAmountValid(45678.901));
    }

}