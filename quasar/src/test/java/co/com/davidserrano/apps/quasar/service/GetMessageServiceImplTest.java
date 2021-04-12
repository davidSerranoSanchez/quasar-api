package co.com.davidserrano.apps.quasar.service;

import co.com.davidserrano.apps.quasar.exception.APIServiceErrorCodes;
import co.com.davidserrano.apps.quasar.exception.APIServiceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;


public class GetMessageServiceImplTest {

    @InjectMocks
    GetMessageServiceImpl getMessageService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void getMessageTestDecodingMessageSuccessfully() throws APIServiceException {
        String[][] messages = {{"este", "", "", "mensaje", ""},
                {"", "es", "", "", "secreto"},
                {"este", "", "un", "", ""}};

        Assert.assertEquals("este es un mensaje secreto", getMessageService.getMessage(messages));
    }

    @Test
    public void getMessageTestNullMessage() throws APIServiceException {
        try {
            getMessageService.getMessage(null);
        } catch (final APIServiceException apiException) {
            Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), apiException.getMessage());
            Assert.assertEquals(APIServiceErrorCodes.NOT_ENOUGH_INFORMATION, apiException.getCode());
        }
    }
}



