package ua.lsi.media_tracker.creators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.enums.MessageCode;

import java.io.File;

/**
 * Created by LSI on 28.03.2016.
 *
 * @author LSI
 */
@Component
public class MessageCreator {

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

    public String getMessageRelatedToCode(MessageCode code) {
        return Messages.getInstance().getMessage(code);
    }

}
