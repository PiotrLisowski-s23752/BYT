package b_Money;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BankTest {
    Currency SEK, DKK;
    Bank SweBank, Santander, DanskeBank;

    @Before
    public void setUp() throws Exception {
        DKK = new Currency("DKK", 0.20);
        SEK = new Currency("SEK", 0.15);
        SweBank = new Bank("SweBank", SEK);
        Santander = new Bank("Santander", SEK);
        DanskeBank = new Bank("DanskeBank", DKK);
        SweBank.openAccount("Beatrice");
        SweBank.openAccount("Jolene");
        Santander.openAccount("Jolene");
        DanskeBank.openAccount("Gertrud");
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("Test: Bank names as expected", "SweBank", SweBank.getName());
    }

    @Test
    public void testGetCurrency() {
        Assert.assertEquals("Test: Currency of bank as expected", "SEK", SweBank.getCurrency().getName());
    }

    @Test
    public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
        DanskeBank.openAccount("Dominik");
        Assert.assertEquals("Test: Account exist after opening", "Dominik", DanskeBank.getAccountFromAccountlist("Dominik").getAccountName());


    }

    @Test
    public void testDeposit() throws AccountDoesNotExistException {
        SweBank.deposit("Jolene", new Money(10000, SEK));
        SweBank.deposit("Jolene", new Money(10000, SEK));
        Assert.assertEquals("Test: Deposits are adding to account", Integer.valueOf(20000), SweBank.getBalance("Jolene"));
    }

    @Test
    public void testWithdraw() throws AccountDoesNotExistException {
        SweBank.deposit("Jolene", new Money(10000, SEK));
        SweBank.withdraw("Jolene", new Money(10000, SEK));
        Assert.assertEquals("TODO", Integer.valueOf(0), SweBank.getBalance("Jolene"));
        //error here(not test error, but printed message in red color), Jolene should have 0
        SweBank.withdraw("Jolene", new Money(20000, SEK));
    }

    @Test
    public void testGetBalance() throws AccountDoesNotExistException {
        Assert.assertEquals("Test: Get balance of Jolene", Integer.valueOf(0), SweBank.getBalance("Jolene"));
    }

    @Test
    public void testTransfer() throws AccountDoesNotExistException {

        SweBank.deposit("Beatrice", new Money(10000, SEK));
        SweBank.transfer("Beatrice", "Jolene", new Money(10000, SEK));
        Assert.assertEquals("Test: transfer fromAccount balance", Integer.valueOf(0), SweBank.getBalance("Beatrice"));
        Assert.assertEquals("Test: transfer toAccount balance", Integer.valueOf(10000), SweBank.getBalance("Jolene"));
        //error here(not test error, but printed message in red color), Beatrice should have 0
        SweBank.transfer("Beatrice", "Jolene", new Money(10000, SEK));
        Assert.assertEquals("Test: transfer fromAccount balance", Integer.valueOf(0), SweBank.getBalance("Beatrice"));
        Assert.assertEquals("Test: transfer toAccount balance", Integer.valueOf(10000), SweBank.getBalance("Jolene"));
    }

    @Test
    public void testTimedPayment() throws AccountDoesNotExistException {
        SweBank.addTimedPayment("Beatrice", "1", 10, 5, new Money(100, SEK), SweBank, "Jolene");
        assertTrue("Test: timed payment added", SweBank.getAccountFromAccountlist("Beatrice").timedPaymentExists("1"));
        SweBank.removeTimedPayment("Beatrice", "1");
        assertFalse("Test: timed payment remove", SweBank.getAccountFromAccountlist("Beatrice").timedPaymentExists("1"));
        SweBank.deposit("Beatrice", new Money(1000, SEK));
        SweBank.addTimedPayment("Beatrice", "1", 1, 0, new Money(100, SEK), SweBank, "Jolene");
        SweBank.tick();
        SweBank.tick();
        Assert.assertEquals("Test: check balance after timedPayment", Integer.valueOf(900), SweBank.getBalance("Beatrice"));
        Assert.assertEquals("Test: check balance after timedPayment", Integer.valueOf(100), SweBank.getBalance("Jolene"));

    }
}
