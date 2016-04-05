package ua.lsi.media_tracker.creators;

import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.enums.MessageCode;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LSI on 28.03.2016.
 *
 * @author LSI
 */
@Component
public class Messages {
    private static Messages messages;
    private Messages messagesToSaveInStatic;
    private Map<MessageCode, String> messagesMap;

    public Messages() {
        messagesMap = new HashMap<>();
        messagesMap.put(MessageCode.AUTO_LOAD_DISABLED, "Automatic load disabled");
        messagesMap.put(MessageCode.AUTO_LOAD_UNSUCCESSFUL, "Automatic load not successful");
        messagesMap.put(MessageCode.AUTO_LOAD_SUCCESSFUL, "Automatic load successful, file : ");
        messagesMap.put(MessageCode.LOAD_SUCCESSFUL, "File : ");
        messagesMap.put(MessageCode.LOAD_UNSUCCESSFUL, "File not selected");
        messagesMap.put(MessageCode.SAVE_SUCCESSFUL, "Data saved to file : ");
        messagesMap.put(MessageCode.SAVE_UNSUCCESSFUL, "File to save not selected");
        messagesMap.put(MessageCode.PROPERTIES_FILE, "MediaTracker.properties");
        messagesMap.put(MessageCode.SETTINGS_SAVED, "Settings saved");
        messagesMap.put(MessageCode.SETTINGS_NOT_SAVED, "Settings not saved");
        messagesToSaveInStatic = this;
    }

    //Used in javaFX and builder
    public static Messages getInstance() {
        return messages;
    }

    public String getMessage(MessageCode code) {
        return messagesMap.get(code);
    }

    @PostConstruct
    public void saveInstance() {
        Messages.messages = messagesToSaveInStatic;
    }

}
