package ua.lsi.media_tracker.creators;

import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.enums.MessageCode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LSI on 28.03.2016.
 *
 * @author LSI
 */
@Component
public class Messages {
    private Map<MessageCode, String> messagesMap;

    public Messages() {
        messagesMap = new HashMap<>();
        messagesMap.put(MessageCode.AUTO_LOAD_DISABLED, "Automatic load disabled");
        messagesMap.put(MessageCode.AUTO_LOAD_UNSUCCESSFUL, "Automatic load not successful");
        messagesMap.put(MessageCode.AUTO_LOAD_SUCCESSFUL, "Automatic load successful");
        messagesMap.put(MessageCode.LOAD_SUCCESSFUL, "File : ");
        messagesMap.put(MessageCode.LOAD_UNSUCCESSFUL, "File not selected");
        messagesMap.put(MessageCode.SAVE_SUCCESSFUL, "Data saved to file : ");
        messagesMap.put(MessageCode.SAVE_UNSUCCESSFUL, "File to save not selected");
        messagesMap.put(MessageCode.SAVE_NOT_SAVED, "Data not saved");
        messagesMap.put(MessageCode.SAVE_ERROR, "Error occurred during saving, see log for more information");
        messagesMap.put(MessageCode.SETTINGS_SAVED, "Settings saved");
        messagesMap.put(MessageCode.SETTINGS_NOT_SAVED, "Settings not saved");
        messagesMap.put(MessageCode.DEFAULT_SECTION, "Default");
        messagesMap.put(MessageCode.SAVE_SQLITE_SUCCESSFUL, "Save to SQLite successful");
        messagesMap.put(MessageCode.SAVE_SQLITE_UNSUCCESSFUL, "Error while saving to SQLite");
    }

    public String getMessage(MessageCode code) {
        return messagesMap.getOrDefault(code, code.name());
    }

    public String getMessageRelatedToFile(MessageCode code, File file) {
        StringBuilder sb = new StringBuilder();
        sb.append(getMessage(code));
        switch (code) {
            case AUTO_LOAD_SUCCESSFUL:
            case LOAD_SUCCESSFUL:
            case SAVE_SUCCESSFUL:
                sb.append(file.getAbsolutePath());
                break;
            default:
        }
        return sb.toString();
    }

}
