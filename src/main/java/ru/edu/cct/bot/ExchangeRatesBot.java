package ru.edu.cct.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.edu.cct.configuration.DataBot;
import ru.edu.cct.exception.ServiceException;
import ru.edu.cct.service.ExchangeRatesService;

import java.time.LocalDate;

@Component
public class ExchangeRatesBot extends TelegramLongPollingBot
        implements CommandAndInfo, DataBot {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesBot.class);
    private static final String date = new MyDate().myToString(LocalDate.now());

    @Autowired
    private ExchangeRatesService exchangeRatesService;

    public ExchangeRatesBot(@Value(botToken) String botTkn) {
        super(botTkn);
    }

    @Override
    public void onUpdateReceived(Update update) {

        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        switch (message) {
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
            case USD -> usdCommand(chatId);
            case EUR -> eurCommand(chatId);
            case LIST -> listCommand(chatId);
            case HELP -> helpCommand(chatId);
            default -> unknownCommand(chatId, message);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    private void startCommand(Long chatId, String userName) {
        sendMessage(chatId, String.format(startBot, userName));
    }

    private void usdCommand(Long chatId) {
        String usd;
        try {
            usd = String.format(curr, date, exchangeRatesService.getUSDExchangeRate());
        } catch (ServiceException e) {
            LOG.error(errorCurLog, e);
            usd = errorCurLogInfo;
        }
        sendMessage(chatId, usd);
    }

    private void eurCommand(Long chatId) {
        String eur;
        try {
            eur = String.format(curr, date, exchangeRatesService.getEURExchangeRate());
        } catch (ServiceException e) {
            LOG.error(errorCurLog, e);
            eur = errorCurLogInfo;
        }
        sendMessage(chatId, eur);
    }

    private void listCommand(Long chatId) {
        String text;
        try {
            text = "На " + date + "\n" +
                    "курс евро " + exchangeRatesService.getEURExchangeRate() + "\n" +
                    "курс доллара " + exchangeRatesService.getUSDExchangeRate();
        } catch (ServiceException e) {
            LOG.error(errorCurLog, e);
            text = errorCurLogInfo;
        }
        sendMessage(chatId, text);
    }

    private void helpCommand(Long chatId) {
        sendMessage(chatId, helpComm);
    }

    private void unknownCommand(Long chatId, String txt) {
        sendMessage(chatId, "Не удалось распознать команду!\n" + txt + "\nИспользуйте /info");
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщения", e);
        }
    }
}
