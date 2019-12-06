package com.lviv.service;

import com.google.gson.Gson;
import com.lviv.model.CurrencyResponse;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class ExchangeRateService {

    private final String URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=&json";
    private final String DOLLAR_CODE = "USD";
    private final String EURO_CODE = "EUR";
    private final String GBP = "GBP";
    private final String POLAND_CODE = "PLN";

    private final List<String> currencyCodes = Arrays.asList(DOLLAR_CODE, EURO_CODE, GBP, POLAND_CODE);


    @SneakyThrows
    public String exchangeToUAH(double money) {

        var gson = new Gson();

        var httpClient = HttpClient.newBuilder()
                .build();

        var request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        var mostPopularCurrencies = getMostPopularCurrencies(gson.fromJson(response.body(), CurrencyResponse[].class));

        return getResponse(mostPopularCurrencies, money);

    }

    private List<CurrencyResponse> getMostPopularCurrencies(CurrencyResponse[] listOfCurrencies) {

        return Arrays.asList(listOfCurrencies).stream()
                .filter(cc -> currencyCodes.contains(cc.getCc()))
                .collect(Collectors.toList());
    }

    private String getResponse(List<CurrencyResponse> currencyCodes, double money) {

        return currencyCodes.stream()
                .map(cc -> String.format("\n %.0f %s : %.2f UAH", money, cc.getCc(), cc.getRate() * money))
                .collect(Collectors.joining(" "));
    }
}
