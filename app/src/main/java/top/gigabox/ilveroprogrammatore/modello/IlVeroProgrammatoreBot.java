package top.gigabox.ilveroprogrammatore.modello;

import com.vdurmont.emoji.EmojiParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.gigabox.ilveroprogrammatore.Bot;
import top.gigabox.ilveroprogrammatore.Constanti;
import top.gigabox.ilveroprogrammatore.persistenza.DAOFrasi;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.input.KeyCode.F;

/**
 * @author https://github.com/vincenzopalazzo
 */

public class IlVeroProgrammatoreBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(IlVeroProgrammatoreBot.class);

    @Override
    public String getBotToken() {
        return "Il tuo token";
    }

    @Override
    public String getBotUsername() {
        return "il tuo nome per il bot";
    }

    @Override
    public void onUpdateReceived(Update update) {

        //Creazione tastiera
        InlineKeyboardMarkup tastiera = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> bottonis = new ArrayList<>();
        List<InlineKeyboardButton> bottoni = new ArrayList<>();
        InlineKeyboardButton bottone = new InlineKeyboardButton("INFO");
        bottone.setCallbackData("messaggio_info_sviluppo");
        bottoni.add(bottone);
        bottone = new InlineKeyboardButton("Dimmi una bella frase");
        bottone.setCallbackData("cosa_so_fare");
        bottoni.add(bottone);
        bottonis.add(bottoni);
        tastiera.setKeyboard(bottonis);

        //TODO Se riceve una vide risposta breve, solleva nullpointer
        if (update.hasCallbackQuery()) {
            LOGGER.debug("e' un messaggio da tastiera inline");


            if (update.getCallbackQuery().getData().equals("messaggio_info_sviluppo")) {
                //Verifico la presenza dell username e in caso contrario la salvo per notificare aggiornamenti in futuro.
                Utenti utenti = (Utenti) Bot.getIstance().getModelloPersistente().getPersistentBean(Constanti.UTENTI, Utenti.class);
                if (!utenti.isConteins(update.getCallbackQuery().getMessage().getChat().getId())) {
                    LOGGER.debug("Username non conosciuto: " + update.getMessage().getChat().getUserName());
                    utenti.getUtenti().add(update.getMessage().getChat().getId());
                    Bot.getIstance().getModelloPersistente().saveBean(Constanti.UTENTI, utenti);
                }

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                String messaggio = "Ciao sto riscrivendo il mio bot in java, Ho bisogno solo di un po di tempo!" +
                        " Per maggiori info puoi contattarmi qui @crazyjoker96";
                LOGGER.debug("Sto inviando il seguente messaggio: " + messaggio);
                sendMessage.setText(messaggio);

                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    LOGGER.debug("Si e' verificato il seguente errore: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
            if (update.getCallbackQuery().getData().equals("cosa_so_fare")) {
                LOGGER.debug("Ho riceviuto cosa_so_fare");
                //Verifico la presenza dell username e in caso contrario la salvo per notificare aggiornamenti in futuro.
                Utenti utenti = (Utenti) Bot.getIstance().getModelloPersistente().getPersistentBean(Constanti.UTENTI, Utenti.class);
                if (!utenti.isConteins(update.getCallbackQuery().getMessage().getChat().getId())) {
                    LOGGER.debug("Username non conosciuto: " + update.getMessage().getChat().getUserName());
                    utenti.getUtenti().add(update.getMessage().getChat().getId());
                    Bot.getIstance().getModelloPersistente().saveBean(Constanti.UTENTI, utenti);
                }
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                generaFrase(sendMessage);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    LOGGER.debug("Si e' verificato il seguente errore: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
            if(update.getCallbackQuery().getData().equals("add_voto_minus") || update.getCallbackQuery().getData().equals("add_voto_plus") ){
                generaVotazione(update);
            }
        }

        if (update.hasMessage()) {
            //Verifico la presenza dell username e in caso contrario la salvo per notificare aggiornamenti in futuro.
            Utenti utenti = (Utenti) Bot.getIstance().getModelloPersistente().getPersistentBean(Constanti.UTENTI, Utenti.class);
            if (!utenti.isConteins(update.getMessage().getChat().getId())) {
                LOGGER.debug("Username non conosciuto: " + update.getMessage().getChat().getUserName());
                utenti.getUtenti().add(update.getMessage().getChat().getId());
                Bot.getIstance().getModelloPersistente().saveBean(Constanti.UTENTI, utenti);
            }
            //TODO debbuggare if-else
            if (update.getMessage().getChat().isUserChat()) {
                LOGGER.debug("Sto comunicando con un utente");
                SendMessage message = new SendMessage();
                String messaggio = new String();
                if (update.getMessage().hasAnimation()) {
                    LOGGER.debug("Il bot ha ricevuto un animazione");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasContact()) {
                    LOGGER.debug("Il bot ha ricevuto un contatto");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasDocument()) {
                    LOGGER.debug("Il bot ha ricevuto un documento");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasLocation()) {
                    LOGGER.debug("Il bot ha ricevuto una localita");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasPhoto()) {
                    LOGGER.debug("Il bot ha ricevuto una foto");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasSticker()) {
                    LOGGER.debug("Il bot ha ricevuto uno stikers");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasVideo()) {
                    LOGGER.debug("Il bot ha ricevuto un video");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasVideoNote()) {
                    LOGGER.debug("Il bot ha ricevuto una nota video");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasText()) {
                    //TODO Fattorizzare questo codice
                    if (messaggioPresentazione(update)) {
                        return;
                    }
                    messaggio = "Ciao " + update.getMessage().getChat().getFirstName() + ", mi piace la tua foto, come stai?";
                }
                //TODO posizione temporanea.
                message.setReplyMarkup(tastiera);
                message.setChatId(update.getMessage().getChatId());
                message.setText(messaggio);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                /*try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }*/
            } else if (update.getMessage().getChat().isGroupChat()) {
                SendMessage message = new SendMessage();
                String messaggio = new String();
                if (update.getMessage().hasAnimation()) {
                    LOGGER.debug("Il bot ha ricevuto un animazione");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasContact()) {
                    LOGGER.debug("Il bot ha ricevuto un contatto");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasDocument()) {
                    LOGGER.debug("Il bot ha ricevuto un documento");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasLocation()) {
                    LOGGER.debug("Il bot ha ricevuto una localita");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasPhoto()) {
                    LOGGER.debug("Il bot ha ricevuto una foto");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasSticker()) {
                    LOGGER.debug("Il bot ha ricevuto uno stikers");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasVideo()) {
                    LOGGER.debug("Il bot ha ricevuto un video");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasVideoNote()) {
                    LOGGER.debug("Il bot ha ricevuto una nota video");
                    messaggio = "Non so rispondere a questo tipo di formato file";
                } else if (update.getMessage().hasText()) {
                    //TODO Fattorizzare questo codice
                    LOGGER.debug("Sto comunicando con un gruppo");
                    if (update.getMessage().getText().contains("@ilVeroProgrammatore_bot")) {
                        message.setReplyMarkup(tastiera);

                        message.setChatId(update.getMessage().getChat().getId());
                        messaggio = "Ehy ciao Ragazzi, sono felice di stare in questo nuovo gruppo";
                        message.setText(messaggio);
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                    //messaggio = "Come va? ti riassumo molto brevemente cosa so fare cosi puoi decidere se ti sono utile";
                    // generaMessaggioPresentazioneCompetenze(message, bottonis, bottoni, tastiera, bottone, messaggio, update);

                    //Messaggio su cosa so fare
                    message = new SendMessage();

                    //Creazione tastiera secondaria
                    bottonis.clear();
                    bottoni = new ArrayList<>();
                    //bottoni.add(bottone);
                    bottone = new InlineKeyboardButton("Dimmi una bella frase");
                    bottone.setCallbackData("cosa_so_fare");
                    bottoni.add(bottone);
                    bottonis.add(bottoni);
                    tastiera.setKeyboard(bottonis);

                    message.setReplyMarkup(tastiera);

                    message.setChatId(update.getMessage().getChat().getId());
                    messaggio = "Come va? ti riassumo molto brevemente cosa so fare cosi puoi decidere se ti sono utile";
                    message.setText(messaggio);
                    try {
                        execute(message);
                        return;
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void generaMessaggioPresentazioneCompetenze(SendMessage message, List<List<InlineKeyboardButton>> bottonis,
                                                        List<InlineKeyboardButton> bottoni, InlineKeyboardMarkup tastiera,
                                                        InlineKeyboardButton bottone, String messaggio, Update update) {
        //Messaggio su cosa so fare
        message = new SendMessage();

        //Creazione tastiera secondaria
        bottonis.clear();
        bottoni = new ArrayList<>();
        //bottoni.add(bottone);
        bottone = new InlineKeyboardButton("Dimmi una bella frase");
        bottone.setCallbackData("cosa_so_fare");
        bottoni.add(bottone);
        bottonis.add(bottoni);
        tastiera.setKeyboard(bottonis);

        message.setReplyMarkup(tastiera);

        message.setChatId(update.getMessage().getChat().getId());
        message.setText(messaggio);
    }

    private void generaFrase(SendMessage message) {
        //Generazione frasi
        Frase fraseGenerata = Bot.getIstance().getOperatore().generaFrase();

        message.setText(fraseGenerata.getFrase());
        StatusGenerazione statusGenerazione = (StatusGenerazione) Bot.getIstance().getModello().getBean(Constanti.STATO);
        statusGenerazione.putFrase(message.getChatId(), fraseGenerata);
        //generazione tastiera feed
        InlineKeyboardMarkup tastieraFeed = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> bottonis = new ArrayList<>();
        List<InlineKeyboardButton> bottoni = new ArrayList<>();
        InlineKeyboardButton bottone = new InlineKeyboardButton(EmojiParser.parseToUnicode(":heart:") + " " + fraseGenerata.getVotoPos());
        bottone.setCallbackData("add_voto_plus");
        bottoni.add(bottone);
        bottone = new InlineKeyboardButton(EmojiParser.parseToUnicode(":poop:") + " " + fraseGenerata.getVotoNeg());
        bottone.setCallbackData("add_voto_minus");
        bottoni.add(bottone);
        bottonis.add(bottoni);
        tastieraFeed.setKeyboard(bottonis);
        message.setReplyMarkup(tastieraFeed);
    }

    private void generaVotazione(Update update){
        Long id = update.getCallbackQuery().getMessage().getChatId();
        String chatId = String.valueOf(id);
        StatusGenerazione statusGenerazione = (StatusGenerazione) Bot.getIstance().getModello().getBean(Constanti.STATO);
        if(statusGenerazione != null){
            if(statusGenerazione.getFrasiUtenti().containsKey(chatId)){
                LOGGER.debug("Contiene l'id chat");
                Frase frase = statusGenerazione.getFresi(chatId);
                if (update.getCallbackQuery().getData().equals("add_voto_plus")){
                    LOGGER.debug("Registro un voto positivo");
                    frase.setVotoPos(frase.getVotoPos() + 1);
                    Bot.getIstance().getModelloPersistente().saveBean(Constanti.ARCHIVIO, Bot.getIstance().getArchivio());
                }
                if (update.getCallbackQuery().getData().equals("add_voto_minus")){
                    LOGGER.debug("Registro un voto negativo");
                    frase.setVotoNeg(frase.getVotoNeg() + 1);
                    Bot.getIstance().getModelloPersistente().saveBean(Constanti.ARCHIVIO, Bot.getIstance().getArchivio());
                }
                SendMessage message = new SendMessage();
                message.setText("Grazie per aver lasciato il feed sulla frase");
                message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    LOGGER.error("Si e' verificato un errore del tipo: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
            Archivio archivio = (Archivio) Bot.getIstance().getModelloPersistente().getPersistentBean(Constanti.ARCHIVIO, Archivio.class);
            LOGGER.debug("Nella lista delle frasi i voto sono, +: " + archivio.getFrasi().get(0).getVotoPos()
                    + " -: " + archivio.getFrasi().get(0).getVotoNeg());
            //TODO quando carico devo sempre caricare i dati piu recenti dall'archivio nel getArchvio di bot. (Da debuggare)
            Bot.getIstance().getModelloPersistente().saveBean(Constanti.ARCHIVIO, Bot.getIstance().getArchivio());
        }
    }

    private boolean messaggioPresentazione(Update update) {
        LOGGER.debug("e' un messagio normale, messaggioPresentazione");
        if (update.getMessage().getText().equals("/hey") || update.getMessage().getText().equals("/hey@ilVeroProgrammatore_bot")) {
            LOGGER.debug("Ha comuniocato con un comando /");
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChat().getId());
            String messaggio;
            if (update.getMessage().getChat().isUserChat()) {
                messaggio = "Ciao, per chidermi quali sono i miei comandi all'interno di un gruppo chiedimi " +
                        "Ciao @IlVeroProgrammatore_bot cosa sai fare?, oppure basta semplicemente un Ciao @IlVeroProgrammatore_bot." +
                        "\nInvece qui in privato con 'Ciao', se non so rispondere a qualcosa mio padre ha detto che riusciro' ad imparare";
            } else if (update.getMessage().getChat().isGroupChat()) {
                messaggio = "Ciao, per chidermi quali sono i miei comandi all'interno di un gruppo chiedimi " +
                        "Ciao @IlVeroProgrammatore_bot cosa sai fare?, oppure basta semplicemente un Ciao @IlVeroProgrammatore_bot.";
            } else {
                messaggio = "Non so rispondere a questo ripo di contenuto";
            }
            //TODO cercare un modo per far imparare le cose...
            message.setText(messaggio);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            } finally {
                return true;
            }
        }
        return false;
    }

    //TODO risolvere errore di quando la chat non e' piu' disponibile
    public void messaggioBrodcast(String messaggio) {
        Utenti utenti = (Utenti) Bot.getIstance().getModelloPersistente().getPersistentBean(Constanti.UTENTI, Utenti.class);
        if (utenti != null) {
            for (Long chatId : utenti.getUtenti()) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText(messaggio);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    LOGGER.error("Si e' verificato un errore del tipo: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
            System.out.println("Ok messaggi mandati");
        }
    }
}
