package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.AccountService;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

public class MakeDepositHandler implements HttpHandler, ResponseSender {
    private final AccountService accountService;

    public MakeDepositHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        byte[] response;

        ObjectNode node = new ObjectMapper().readValue(exchange.getRequestBody(), ObjectNode.class);
        String accountNumber = node.get("number").asText();
        BigDecimal sum = node.get("sum").decimalValue();

        accountService.topUpAccountBalance(accountNumber, sum);

        // If all works correctly
        response = "Balance replenished.".getBytes(StandardCharsets.UTF_8);
        sendResponse(exchange, 200, response);
    }
}
