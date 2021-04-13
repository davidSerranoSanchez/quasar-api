package co.com.davidserrano.apps.quasar.service;

import co.com.davidserrano.apps.quasar.exception.APIServiceErrorCodes;
import co.com.davidserrano.apps.quasar.exception.APIServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class GetMessageServiceImpl implements IGetMessageService {

    /**
     * @param messages
     * @return
     * @throws APIServiceException
     */
    @Override
    public String getMessage(String[]... messages) throws APIServiceException {

        if (messages == null || messages.length < 3) {
            throw new APIServiceException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    APIServiceErrorCodes.NOT_ENOUGH_INFORMATION);
        }
        Set messageDecoded = new LinkedHashSet();
        int position = 0;
        boolean decodeFinished = false;
        //move vertically
        while (!decodeFinished) {
            int countMessageFinished = 0;
            //move horizontally
            for (String[] message : messages) {
                try {
                    if (message != null && position < message.length) {
                        if (message[position].trim().length() != 0) {
                            messageDecoded.add(message[position]);
                        }
                    } else {
                        countMessageFinished++;
                        //validate that I have finished all the words in all messages
                        if (countMessageFinished == messages.length) {
                            decodeFinished = true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new APIServiceException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            APIServiceErrorCodes.MESSAGE_NOT_DEFINABLE);
                }
            }
            position++;
        }
        String decodeMessage = String.join(" ", messageDecoded);
        if (decodeMessage.isEmpty()) {
            throw new APIServiceException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    APIServiceErrorCodes.MESSAGE_NOT_DEFINABLE);
        } else {
            return String.join(" ", messageDecoded);
        }

    }
}
