package ua.lsi.media_tracker.model;

import ua.lsi.media_tracker.enums.MessageCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LSI on 28.03.2016.
 *
 * @author LSI
 */
public class MessageHolder {
    private Map<MessageCode, String> messages;

    public MessageHolder() {
        messages = new HashMap<>();
        messages.put(MessageCode.AUTO_LOAD_UNSUCCESSFUL, "Automatic load not successful");
        messages.put(MessageCode.AUTO_LOAD_SUCCESSFUL, "Automatic load successful, file : ");
        messages.put(MessageCode.LOAD_SUCCESSFUL, "File : ");
        messages.put(MessageCode.LOAD_UNSUCCESSFUL, "File not selected");
        messages.put(MessageCode.SAVE_SUCCESSFUL, "Data saved to file : ");
        messages.put(MessageCode.SAVE_UNSUCCESSFUL, "File to save not selected");
        messages.put(MessageCode.TEMP_FILENAME, "MediaTracker.txt");
        messages.put(MessageCode.DEFAULT_SECTION_NAME, "Default");
    }

    public static MessageHolder getInstance() {
        return Holder.INSTANCE;
    }

    public String getMessage(MessageCode code) {
        return messages.get(code);
    }

    private static class Holder {
        public static final MessageHolder INSTANCE = new MessageHolder();
    }
}
