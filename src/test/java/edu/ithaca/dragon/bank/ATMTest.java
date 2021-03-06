package edu.ithaca.dragon.bank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ATMTest {
    @Test
    void confirmCredentialsTest() throws IllegalArgumentException, AcctFrozenException{
        Admin admin = new Admin();

        //valid checking account
        admin.createCheckingForTeller("1234567890", "Bob Job", "dog123", 100);
        assertTrue(admin.atm.confirmCredentials("1234567890", "dog123"));

        //checking account valid ID invalid password
        assertFalse(admin.atm.confirmCredentials("1234567890", "frog"));

        //valid savings account
        admin.createSavingsForTeller("1111111111", "Bob Lob", "frog123", 100, .1, 800);
        assertTrue(admin.atm.confirmCredentials("1111111111", "frog123"));

        //savings account valid ID invalid password
        assertFalse(admin.atm.confirmCredentials("1111111111", "bob"));

        //account invalid ID valid password
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.confirmCredentials("24235354", "dog123"));

        //account invalid ID and password
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.confirmCredentials("24353535", "dog"));
    }

    @Test
    void checkBalanceTest() throws IllegalArgumentException, AcctFrozenException{

        Admin admin = new Admin();

        //check checkings balance
        admin.createCheckingForTeller("1234567890", "Bob Job", "dog123", 225.56);
        assertEquals(225.56, admin.atm.checkBalance("1234567890","dog123"));

        //check savings balance
        admin.createSavingsForTeller("1111111111", "Bob Lob", "frog123", 45.32, .1, 800);
        assertEquals(45.32, admin.atm.checkBalance("1111111111", "frog123"));

        //check invalid account balance throws exception
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.checkBalance("245422244", "frog123"));

        //check frozen checking account
        admin.freezeAccount("1234567890");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.checkBalance("1234567890", "dog123"));

        //check checking balance after unfrozen
        admin.unfreezeAcct("1234567890");
        assertEquals(225.56, admin.atm.checkBalance("1234567890", "dog123"));

        //check frozen savings account
        admin.atm.admin.freezeAccount("1111111111");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.checkBalance("1111111111", "frog123"));

        //check savings account balance after unfrozen
        admin.unfreezeAcct("1111111111");
        assertEquals(45.32, admin.atm.checkBalance("1111111111", "frog123"));
    }

    @Test
    void withdrawTest() throws IllegalArgumentException, InsufficientFundsException, AcctFrozenException{

        Admin admin = new Admin();

        //valid checking acct positive withdrawal
        admin.createCheckingForTeller("1111111111", "Bob Jo", "dog223", 500);
        admin.atm.withdraw("1111111111", "dog223",200.75);
        assertEquals(299.25, admin.atm.checkBalance("1111111111","dog223"));

        //valid checking acct left with 0
        admin.atm.withdraw("1111111111", "dog223",299.25);
        assertEquals(0, admin.atm.checkBalance("1111111111","dog223"));

        //valid checking acct overdraw
        admin.createCheckingForTeller("2222222222", "Lob E", "dog456", 300);
        assertThrows(InsufficientFundsException.class, ()-> admin.atm.withdraw("2222222222", "dog456",301));

        //valid checking acct negative withdrawal
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.withdraw("2222222222", "dog456",-35));

        //valid checking acct three decimal points
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.withdraw("2222222222", "dog456", 211.532));

        //frozen checking acct
        admin.freezeAccount("2222222222");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.withdraw("2222222222", "dog456",35));

        //checking acct after unfrozen
        admin.unfreezeAcct("2222222222");
        admin.atm.withdraw("2222222222", "dog456",40);
        assertEquals(260, admin.atm.checkBalance("2222222222", "dog456"));

        //valid savings acct positive withdrawal
        admin.createSavingsForTeller("3333333333", "Rob Jo", "dog443", 300, .1, 600);
        admin.atm.withdraw("3333333333", "dog443",175);
        assertEquals(125, admin.atm.checkBalance("3333333333","dog443"));

        //valid savings acct left with 0
        admin.atm.withdraw("3333333333", "dog443",125);
        assertEquals(0, admin.atm.checkBalance("3333333333", "dog443"));

        //valid savings acct overdraw
        admin.createSavingsForTeller("4444444444", "Goo Boo", "dog64", 1000, .2, 600);
        assertThrows(InsufficientFundsException.class, ()-> admin.atm.withdraw("4444444444", "dog64",1001));

        //valid savings acct negative withdrawal
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.withdraw("4444444444", "dog64",-35));

        //valid savings acct three decimal points
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.withdraw("4444444444","dog64", 35.632));

        //frozen savings acct
        admin.freezeAccount("4444444444");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.withdraw("4444444444", "dog64",50));

        //savings acct after unfrozen
        admin.unfreezeAcct("4444444444");
        admin.atm.withdraw("4444444444", "dog64",400);
        assertEquals(600, admin.atm.checkBalance("4444444444","dog64"));


        //invalid acct
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.withdraw("23535353", "dog443", 250));
    }

    @Test
    void depositTest() throws IllegalArgumentException, AcctFrozenException{

        Admin admin = new Admin();

        //valid checking acct positive deposit
        admin.createCheckingForTeller("1111111111", "Bob Jo", "dog223", 500);
        admin.createCheckingForTeller("2222222222", "Bill Ji", "dog456", 200);
        admin.atm.deposit("1111111111", "dog223",200.75);
        admin.atm.deposit("2222222222", "dog456",20);
        assertEquals(700.75, admin.atm.checkBalance("1111111111","dog223"));
        assertEquals(220, admin.atm.checkBalance("2222222222","dog456"));

        //valid checking acct negative deposit
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.deposit("2222222222", "dog456",-35));

        //valid checking acct three decimal points
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.deposit("2222222222", "dog456",211.532));

        //frozen checking acct
        admin.freezeAccount("2222222222");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.deposit("2222222222", "dog456",35));

        //checking acct after unfrozen
        admin.unfreezeAcct("2222222222");
        admin.atm.deposit("2222222222", "dog456",40);
        assertEquals(260, admin.atm.checkBalance("2222222222", "dog456"));

        //valid savings acct positive deposit
        admin.createSavingsForTeller("3333333333", "Rob Jo", "dog443", 300, .1, 600);
        admin.atm.deposit("3333333333", "dog443", 175);
        assertEquals(475, admin.atm.checkBalance("3333333333","dog443"));
        admin.createSavingsForTeller("4444444444", "Rob Oj", "dog64", 25, .1, 600);
        admin.atm.deposit("4444444444", "dog64", 175);
        assertEquals(200, admin.atm.checkBalance("4444444444","dog64"));

        //valid savings acct negative deposit
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.deposit("4444444444", "dog64",-35));

        //valid savings acct three decimal points
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.deposit("4444444444", "dog64",35.632));

        //frozen savings acct
        admin.freezeAccount("4444444444");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.deposit("4444444444", "dog64",50));

        //savings acct after unfrozen
        admin.unfreezeAcct("4444444444");
        admin.atm.deposit("4444444444", "dog64",400);
        assertEquals(600, admin.atm.checkBalance("4444444444","dog64"));

        //invalid acct
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.deposit("23535353", "dog64",250));
    }

    @Test
    void transferTest() throws IllegalArgumentException, InsufficientFundsException, AcctFrozenException{

        Admin admin = new Admin();

        //valid checking acct to savings acct
        admin.createCheckingForTeller("1111111111", "Joe Bob", "dog244", 400);
        admin.createSavingsForTeller("2222222222", "Billy B", "3effe", 200, .2, 500);
        admin.atm.transfer("1111111111", "2222222222", "dog244", "3effe",200);
        assertEquals(200, admin.atm.checkBalance("1111111111", "dog244"));
        assertEquals(400, admin.atm.checkBalance("2222222222", "3effe"));

        //valid checking acct to savings acct left with 0
        admin.atm.transfer("1111111111", "2222222222", "dog244", "3effe",200);
        assertEquals(0, admin.atm.checkBalance("1111111111", "dog244"));
        assertEquals(600, admin.atm.checkBalance("2222222222", "3effe"));

        //valid checking acct to savings acct overdraw
        admin.createCheckingForTeller("3333333333", "Bo J", "3rf2d", 225);
        admin.createSavingsForTeller("4444444444", "Robby D", "tefer3r", 300, .1, 600);
        assertThrows(InsufficientFundsException.class, ()-> admin.atm.transfer("3333333333", "4444444444", "3rf2d", "tefer3r",226));

        //valid checking acct to savings acct negative value
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.transfer("3333333333", "4444444444", "3rf2d", "tefer3r",-100));

        //valid checking acct to savings acct three decimal points
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.transfer("3333333333", "4444444444", "3rf2d","tefer3r",50.353));

        //frozen checking acct to savings acct
        admin.freezeAccount("3333333333");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.transfer("3333333333", "4444444444", "3rf2d","tefer3r",200));

        //frozen checking acct to frozen savings acct
        admin.freezeAccount("4444444444");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.transfer("3333333333", "4444444444", "3rf2d","tefer3r",100.53));

        //checking acct to frozen savings acct
        admin.unfreezeAcct("3333333333");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.transfer("3333333333", "4444444444", "3rf2d","tefer3r",100.53));

        //checking acct after unfrozen to savings acct after unfrozen
        admin.unfreezeAcct("4444444444");
        admin.atm.transfer("3333333333", "4444444444", "3rf2d","tefer3r",50);
        assertEquals(175, admin.atm.checkBalance("3333333333","3rf2d"));
        assertEquals(350, admin.atm.checkBalance("4444444444", "tefer3r"));

        //valid checking acct to invalid acct
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.transfer("3333333333", "353535", "3rf2d","3rf2d",15));

        //valid savings acct to checking acct positive value
        admin.createSavingsForTeller("5555555555", "Jon Locke", "3effe", 500, .2, 800);
        admin.createCheckingForTeller("6666666666", "Bo Li" , "dog244", 400);
        admin.atm.transfer("5555555555", "6666666666", "3effe","dog244",100.25);
        assertEquals(399.75, admin.atm.checkBalance("5555555555","3effe"));
        assertEquals(500.25, admin.atm.checkBalance("6666666666","dog244"));

        //valid savings acct to checking acct left with 0
        admin.atm.transfer("5555555555", "6666666666", "3effe","dog244",399.75);
        assertEquals(0, admin.atm.checkBalance("5555555555","3effe"));
        assertEquals(900, admin.atm.checkBalance("6666666666","dog244"));

        //valid savings acct to checking acct overdraw
        admin.createSavingsForTeller("7777777777", "Gob Holly", "4tef3r", 800, .2, 500);
        admin.createCheckingForTeller("8888888888", "Bob Dono", "44gg4g4", 300);
        assertThrows(InsufficientFundsException.class, ()-> admin.atm.transfer("7777777777", "8888888888", "4tef3r","44gg4g4",801));

        //valid savings acct to checking acct negative value
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.transfer("7777777777", "8888888888", "4tef3r","44gg4g4",-25));

        //valid savings acct to checking acct three decimal points
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.transfer("7777777777", "8888888888", "4tef3r","44gg4g4",78.321));

        //frozen savings acct to checking acct
        admin.freezeAccount("7777777777");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.transfer("7777777777", "8888888888", "4tef3r","44gg4g4",100));

        //frozen savings acct to frozen checking acct
        admin.freezeAccount("8888888888");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.transfer("7777777777", "8888888888", "4tef3r","44gg4g4",50));

        //savings acct to frozen checking acct
        admin.unfreezeAcct("7777777777");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.transfer("7777777777", "8888888888", "4tef3r","44gg4g4",100));

        //savings acct after unfrozen to checking acct after unfrozen
        admin.unfreezeAcct("8888888888");
        admin.atm.transfer("7777777777", "8888888888", "4tef3r","44gg4g4",400);
        assertEquals(400, admin.atm.checkBalance("7777777777","4tef3r"));
        assertEquals(700, admin.atm.checkBalance("8888888888","44gg4g4"));

        //valid savings acct to invalid acct
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.transfer("7777777777", "35353535", "4tef3r","44gg4g4",50));

        //invalid acct to invalid acct
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.transfer("355363636", "35333636", "4tef3r","4tef3r",50));
    }

    @Test
    void transactionHistoryTest() throws InsufficientFundsException, IllegalArgumentException, AcctFrozenException{

        Admin admin = new Admin();

        //valid checking acct after 1 withdraw
        admin.createCheckingForTeller("1111111111", "Greg Leg", "3r3f3f", 400);
        admin.atm.withdraw("1111111111", "3r3f3f",100.25);
        assertEquals("withdrawal of 100.25", admin.atm.transactionHistory("1111111111","3r3f3f"));

        //valid checking acct after 1 deposit
        admin.createCheckingForTeller("2222222222", "Sam Bammo", "rgeg322", 100);
        admin.atm.deposit("2222222222", "rgeg322",300);
        assertEquals("deposit of 300.0", admin.atm.transactionHistory("2222222222","rgeg322"));

        //valid checking acct after 1 deposit then 1 withdraw
        admin.atm.withdraw("2222222222", "rgeg322",100.52);
        assertEquals("deposit of 300.0; withdrawal of 100.52", admin.atm.transactionHistory("2222222222","rgeg322"));

        //valid checking acct after 1 withdraw then 1 deposit
        admin.createCheckingForTeller("3333333333", "Sally Skebab", "kfjell2k", 300);
        admin.atm.withdraw("3333333333", "kfjell2k",100);
        admin.atm.deposit("3333333333", "kfjell2k",200);
        assertEquals("withdrawal of 100.0; deposit of 200.0", admin.atm.transactionHistory("3333333333","kfjell2k"));

        //valid checking acct after 2 withdraws then 2 deposits
        admin.createCheckingForTeller("4444444444", "Arthur Lang", "ljfn3lk", 500);
        admin.atm.withdraw("4444444444","ljfn3lk", 100);
        admin.atm.withdraw("4444444444", "ljfn3lk",300.25);
        admin.atm.deposit("4444444444", "ljfn3lk",235.61);
        admin.atm.deposit("4444444444", "ljfn3lk",56.29);
        assertEquals("withdrawal of 100.0; withdrawal of 300.25; deposit of 235.61; deposit of 56.29", admin.atm.transactionHistory("4444444444","ljfn3lk"));

        //frozen checking acct
        admin.freezeAccount("4444444444");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.transactionHistory("4444444444","ljfn3lk"));

        //checking acct after unfrozen
        admin.unfreezeAcct("4444444444");
        admin.atm.deposit("4444444444", "ljfn3lk",225);
        assertEquals("withdrawal of 100.0; withdrawal of 300.25; deposit of 235.61; deposit of 56.29; deposit of 225.0", admin.atm.transactionHistory("4444444444","ljfn3lk"));

        //check transfer shows up in checking acct out and checking acct in
        admin.createCheckingForTeller("5555555555", "Lucy Trolleyman", "3t3tfef", 200);
        admin.createCheckingForTeller("6666666666", "Sandra Qualdrick", "r3foihf2", 400);
        admin.atm.transfer("5555555555", "6666666666", "3t3tfef","r3foihf2",150);
        assertEquals("withdrawal of 150.0", admin.atm.transactionHistory("5555555555","3t3tfef"));
        assertEquals("deposit of 150.0", admin.atm.transactionHistory("6666666666","r3foihf2"));

        //valid checking acct after a transfer out then deposit then transfer in
        admin.atm.deposit("5555555555", "3t3tfef",50);
        admin.atm.transfer("6666666666", "5555555555",  "r3foihf2","3t3tfef",25.43);
        assertEquals("withdrawal of 150.0; deposit of 50.0; deposit of 25.43", admin.atm.transactionHistory("5555555555","3t3tfef"));

        //valid savings acct after 1 withdraw
        admin.createSavingsForTeller("7777777777", "Paul Giamatti", "3f3f33f", 400, 0.1, 800);
        admin.atm.withdraw("7777777777", "3f3f33f",50.64);
        assertEquals("withdrawal of 50.64", admin.atm.transactionHistory("7777777777","3f3f33f"));

        //valid savings acct after 1 deposit
        admin.createSavingsForTeller("8888888888", "Sara Palooza", "eflkn33", 2000, 0.1, 500);
        admin.atm.deposit("8888888888", "eflkn33",125);
        assertEquals("deposit of 125.0", admin.atm.transactionHistory("8888888888","eflkn33"));

        //valid savings acct after 1 deposit then 1 withdraw
        admin.atm.withdraw("8888888888", "eflkn33",56.32);
        assertEquals("deposit of 125.0; withdrawal of 56.32", admin.atm.transactionHistory("8888888888","eflkn33"));

        //valid savings acct after 1 withdraw then 1 deposit
        admin.atm.deposit("7777777777", "3f3f33f",145.13);
        assertEquals("withdrawal of 50.64; deposit of 145.13", admin.atm.transactionHistory("7777777777","3f3f33f"));

        //frozen savings acct
        admin.freezeAccount("8888888888");
        assertThrows(AcctFrozenException.class, ()-> admin.atm.transactionHistory("8888888888","eflkn33"));

        //savings acct after unfrozen
        admin.unfreezeAcct("8888888888");
        admin.atm.deposit("8888888888","eflkn33", 90.11);
        assertEquals("deposit of 125.0; withdrawal of 56.32; deposit of 90.11", admin.atm.transactionHistory("8888888888","eflkn33"));

        //check transfer shows up after savings acct out and savings acct in
        admin.createSavingsForTeller("9999999999", "Natalie Price", "feij302", 600, .1, 900);
        admin.createSavingsForTeller("0000000000", "Riley Ranger", "0t3082", 400, .2, 1000);
        admin.atm.transfer("9999999999", "0000000000", "feij302","0t3082",120.65);
        assertEquals("withdrawal of 120.65", admin.atm.transactionHistory("9999999999","feij302"));
        assertEquals("deposit of 120.65", admin.atm.transactionHistory("0000000000","0t3082"));

        //valid savings acct after transfer out then deposit then transfer in
        admin.atm.deposit("9999999999","feij302", 50.65);
        admin.atm.transfer("0000000000", "9999999999", "0t3082","feij302",20);
        assertEquals("withdrawal of 120.65; deposit of 50.65; deposit of 20.0", admin.atm.transactionHistory("9999999999","feij302"));

        //invalid acct
        assertThrows(IllegalArgumentException.class, ()-> admin.atm.transactionHistory("336366336","feij302"));
    }
}
