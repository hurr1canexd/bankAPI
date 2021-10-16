package org.misha.bankapi.db.DAO;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.misha.bankapi.model.Card;

import java.sql.SQLException;
import java.util.List;


public interface CardDAO {
    void create(Card card) throws SQLException;
    List<Card> getCards() throws SQLException;
}
