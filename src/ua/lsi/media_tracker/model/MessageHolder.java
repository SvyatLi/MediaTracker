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
    private Map<MessageCode,String> messages;

    private static class Holder {
        public static final MessageHolder INSTANCE = new MessageHolder();
    }

    public static MessageHolder getInstance(){
        return Holder.INSTANCE;
    }

    public MessageHolder() {
        messages = new HashMap<>();
        messages.put(MessageCode.AUTO_LOAD_NOT_SUCCESSFUL,"Automatic load not successful");
        messages.put(MessageCode.AUTO_LOAD_SUCCESSFUL,"Automatic load successful, file : ");
        messages.put(MessageCode.LOAD_SUCCESSFUL,"File : ");
        messages.put(MessageCode.LOAD_NOT_SUCCESSFUL,"File not selected");
        messages.put(MessageCode.SAVE_SUCCESSFUL,"Data saved to file : ");
        messages.put(MessageCode.SAVE_NOT_SUCCESSFUL,"File to save not selected");
    }

    public String getMessage(MessageCode code){
        return messages.get(code);
    }
}
