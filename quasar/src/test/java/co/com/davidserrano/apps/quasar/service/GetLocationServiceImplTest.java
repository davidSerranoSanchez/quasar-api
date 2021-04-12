package co.com.davidserrano.apps.quasar.service;

import co.com.davidserrano.apps.quasar.exception.APIServiceErrorCodes;
import co.com.davidserrano.apps.quasar.exception.APIServiceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.awt.geom.Point2D;

public class GetLocationServiceImplTest {

    @InjectMocks
    GetLocationServiceImpl getLocationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void getLocationSuccessfullyPoint1() throws APIServiceException {
        Float[] distances = {400.0F, 400.5F, 899.4F};
        Point2D.Float location = getLocationService.getLocation(distances);
        Assert.assertEquals(-243.09987,location.x, 0.1);
        Assert.assertEquals(-406.70615,location.y, 0.1);
    }

    @Test
    public void getLocationSuccessfullyPoint2() throws APIServiceException {
        Float[] distances = {400.0F, 400.5F, 657.5F};
        Point2D.Float location = getLocationService.getLocation(distances);
        Assert.assertEquals(-157.5,location.x, 0.1);
        Assert.assertEquals(106.59,location.y, 0.1);
    }

    @Test
    public void getLocationErrorNotEnoughInfo() throws APIServiceException {
        Float[] distances = {400.0F, 400.5F};
        try {
            getLocationService.getLocation(distances);
        }catch(APIServiceException apiException){
            Assert.assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), apiException.getMessage());
            Assert.assertEquals(APIServiceErrorCodes.NOT_ENOUGH_INFORMATION, apiException.getCode());
        }
    }

    @Test
    public void getLocationErrorNotTangentCircles() throws APIServiceException {
        Float[] distances = {400.0F, 100F, 899.4F};
        try {
            getLocationService.getLocation(distances);
        }catch(APIServiceException apiException){
            Assert.assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), apiException.getMessage());
            Assert.assertEquals(APIServiceErrorCodes.LOCATION_NOT_DEFINABLE, apiException.getCode());
        }
    }

    @Test
    public void getLocationErrorOneCircleWithinAnother() throws APIServiceException {
        Float[] distances = {400.0F, 1400.5F, 899.4F};
        try {
            getLocationService.getLocation(distances);
        }catch(APIServiceException apiException){
            Assert.assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), apiException.getMessage());
            Assert.assertEquals(APIServiceErrorCodes.LOCATION_NOT_DEFINABLE, apiException.getCode());
        }
    }

    @Test
    public void getLocationErrorThirdCircleDoesNotHaveIntersection() throws APIServiceException {
        Float[] distances = {400.0F, 400.5F, 657F};
        try {
        getLocationService.getLocation(distances);
        }catch(APIServiceException apiException){
            Assert.assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), apiException.getMessage());
            Assert.assertEquals(APIServiceErrorCodes.LOCATION_NOT_DEFINABLE, apiException.getCode());
        }
    }

}
