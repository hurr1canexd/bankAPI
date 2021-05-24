package org.misha.bankapi;

import org.junit.Assert;
import org.junit.Test;
import org.misha.bankapi.db.DAO.AccountDAOImpl;
import org.misha.bankapi.db.DAO.CardDAOImpl;
import org.misha.bankapi.db.DBInitializer;
import org.misha.bankapi.db.H2JDBCUtils;
import org.misha.bankapi.model.Card;
import org.misha.bankapi.service.AccountService;
import org.misha.bankapi.service.AccountServiceImpl;
import org.misha.bankapi.service.CardService;
import org.misha.bankapi.service.CardServiceImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UnitTest {
    @Test
    public void shouldInsertCardTest() throws IOException {
        DBInitializer.init();

        Card card = new Card(
                "42024305346286324586", "07", "2028", "242", BigDecimal.ZERO, 1);
        CardService cardService = new CardServiceImpl(new CardDAOImpl());
        try {
            cardService.insertCardInDatabase(card);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        int actual = 0;
        String getCardNumberQuery = "SELECT COUNT(*) AS total FROM Card";
        try (Connection connection = H2JDBCUtils.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(getCardNumberQuery);
            while (rs.next()) {
                actual = rs.getInt("total");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        int expected = 2;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldViewCardsTest() throws IOException {
        DBInitializer.init();

        String actual = "";
        CardService cardService = new CardServiceImpl(new CardDAOImpl());
        try {
            actual = cardService.getCards().toString();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        String expected = "[{\"id\":1,\"number\":\"42024305346286324576\"}]";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldMakeDepositTest() throws IOException {
        DBInitializer.init();

        AccountService accountService = new AccountServiceImpl(new AccountDAOImpl());
        accountService.topUpAccountBalance("40804810200003497183", BigDecimal.valueOf(250));

        BigDecimal actual = BigDecimal.ZERO;
        String getBalanceQuery = "SELECT balance FROM Account WHERE number = '40804810200003497183';";
        try (Connection connection = H2JDBCUtils.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(getBalanceQuery);
            while (rs.next()) {
                actual = rs.getBigDecimal("balance");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        BigDecimal expected = new BigDecimal(250);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldCheckBalanceTest() throws IOException {
        DBInitializer.init();

        AccountService accountService = new AccountServiceImpl(new AccountDAOImpl());
        BigDecimal actual = null;
        try {
            actual = accountService.getAccountBalance("40804810200003497183");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        BigDecimal expected = new BigDecimal(0);
        Assert.assertEquals(expected, actual);
    }

}
