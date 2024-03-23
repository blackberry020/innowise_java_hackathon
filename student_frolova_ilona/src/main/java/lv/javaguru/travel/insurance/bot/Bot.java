package lv.javaguru.travel.insurance.bot;

import lombok.Getter;
import lv.javaguru.travel.insurance.services.CryptoInfoService;
import lv.javaguru.travel.insurance.services.impl.CryptoInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Getter
@Component
public class Bot extends TelegramLongPollingBot {

    //private static final Logger LOG = LoggerFactory.getLogger(Bot.class);

    private static final String START = "/start";
    private static final String GET_ALL = "/getAllRates";

    @Autowired
    private CryptoInfoServiceImpl service;

    public Bot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        switch (message) {
            case GET_ALL -> {

                String userName = update.getMessage().getChat().getUserName();

                List<String> messages;

                try {
                    messages = service.getCryptoRate();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                for (String curMessage : messages) {
                    var formattedText = String.format(curMessage, userName);
                    sendMessage(chatId, formattedText);
                }
            }
            /*case USD -> usdCommand(chatId);
            case EUR -> eurCommand(chatId);
            case HELP -> helpCommand(chatId);*/
            default -> unknownCommand(chatId);
        }
    }

    private void unknownCommand(Long chatId) {
        var text = "Unknown command!";
        sendMessage(chatId, text);
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            //LOG.error("Ошибка отправки сообщения", e);
            System.out.println("error sending message: " + e);
        }
    }

    @Override
    public String getBotUsername() {
        return "crypto020_bot";
    }
}
