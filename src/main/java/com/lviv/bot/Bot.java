package com.lviv.bot;

import com.lviv.service.ExchangeRateService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class Bot extends TelegramLongPollingBot {

    private static final String TOKEN = "957859761:AAHcpwQ8_3b6iyRsatF63zPVbtD2VRdChMM";
    private static final String BOT_NAME = "money_exchange_rate_bot";


    @SneakyThrows
    public void onUpdateReceived(Update update) {
        var message = update.getMessage().getText();
        var exchangeRateService = new ExchangeRateService();
        var response = StringUtils.EMPTY;

        if (StringUtils.isNumeric(message)) {
            response = exchangeRateService.exchangeToUAH(Double.parseDouble(message));
        } else {
            response = "Invalid input";
        }

        sendMsg(update.getMessage().getChatId().toString(), response);

    }


    @SneakyThrows
    public synchronized void sendMsg(String chatId, String uah) {
        var sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(uah);
        sendMessage(sendMessage);
    }

    public String getBotToken() {
        return TOKEN;
    }

    public String getBotUsername() {
        return BOT_NAME;
    }

}
