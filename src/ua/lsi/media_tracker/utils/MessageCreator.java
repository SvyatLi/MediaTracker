package ua.lsi.media_tracker.utils;

import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.model.Messages;

import java.io.File;

/**
 * Created by LSI on 28.03.2016.
 *
 * @author LSI
 */
public class MessageCreator {
    public static MessageCreator getInstance() {
        return CreatorHolder.INSTANCE;
    }

    public String getMessageRelatedToCodeAndFile(MessageCode code, File file) {
        StringBuilder sb = new StringBuilder();
        sb.append(Messages.getInstance().getMessage(code));
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

    private static class CreatorHolder {
        public static final MessageCreator INSTANCE = new MessageCreator();
    }
}
