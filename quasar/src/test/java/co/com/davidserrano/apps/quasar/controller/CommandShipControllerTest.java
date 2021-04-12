package co.com.davidserrano.apps.quasar.controller;

import co.com.davidserrano.apps.quasar.controller.converter.SatelliteConverter;
import co.com.davidserrano.apps.quasar.dto.*;
import co.com.davidserrano.apps.quasar.entity.Satellite;
import co.com.davidserrano.apps.quasar.exception.APIServiceErrorCodes;
import co.com.davidserrano.apps.quasar.exception.APIServiceException;
import co.com.davidserrano.apps.quasar.repository.ISatelliteRepository;
import co.com.davidserrano.apps.quasar.service.GetLocationServiceImpl;
import co.com.davidserrano.apps.quasar.service.GetMessageServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommandShipControllerTest {

    @InjectMocks
    private CommandShipController commandShipController;

    @Mock
    private ISatelliteRepository satelliteRepository;

    @Mock
    private GetLocationServiceImpl getLocationService;

    @Mock
    private GetMessageServiceImpl getMessageService;

    @Mock
    private SatelliteConverter satelliteConverter;

    List<SatelliteDTO> satelliteDTOS = new ArrayList<>();
    List<Satellite> satellites = new ArrayList<Satellite>();

    private SatelliteFleet satelliteFleet = new SatelliteFleet();

    Satellite satellite = new Satellite();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        SatelliteConverter satelliteConverter = new SatelliteConverter();
        SatelliteDTO kenobiSatellite = new KenobiSatelliteDTO();
        SatelliteDTO skywalkerSatellite = new SkywalkerSatelliteDTO();
        SatelliteDTO satoSatellite = new SatoSatelliteDTO();

        //Kenobi
        kenobiSatellite.setMessage(new String[]{"este", "", "", "mensaje", ""});
        kenobiSatellite.setDistance(400F);
        satelliteDTOS.add(kenobiSatellite);
        satellite = satelliteConverter.toEntity(kenobiSatellite);
        satellites.add(satellite);

        //Skywalker
        skywalkerSatellite.setMessage(new String[]{"", "es", "", "", "secreto"});
        skywalkerSatellite.setDistance(400.5F);
        satelliteDTOS.add(skywalkerSatellite);
        satellite = satelliteConverter.toEntity(skywalkerSatellite);
        satellites.add(satellite);

        //Kenobi
        satoSatellite.setMessage(new String[]{"este", "", "un", "", ""});
        satoSatellite.setDistance(899.4F);
        satelliteDTOS.add(satoSatellite);
        satellite = satelliteConverter.toEntity(satoSatellite);
        satellites.add(satellite);

        satelliteFleet.setSatellites(satelliteDTOS);
    }

    @Test
    public void topSecretOneCallLocationAndMessageServicesTest() throws APIServiceException {
        commandShipController.topSecret(satelliteFleet, "EN");
        verify(getMessageService, times(1)).getMessage(any());
        verify(getLocationService, times(1)).getLocation(any());
    }

    @Test
    public void getTopSecretSplitOneCallLocationAndMessageServicesTest() throws APIServiceException {

        when(satelliteRepository.findAll()).thenReturn(satellites);
        when(satelliteConverter.toDTO(any())).thenReturn(satelliteFleet.getSatellites().get(0));

        commandShipController.getTopSecretSplit("kenobi", satelliteFleet.getSatellites().get(0), "EN");
        verify(getMessageService, times(1)).getMessage(any());
        verify(getLocationService, times(1)).getLocation(any());
    }

    @Test
    public void postTopSecretSplitTest() throws APIServiceException {

        Optional<Satellite> satelliteOptional = Optional.of(satellites.get(0));
        when(satelliteRepository.findById(any())).thenReturn(satelliteOptional);
        Mockito.doNothing().when(satelliteRepository).delete(any());
        when(satelliteRepository.save(satellites.get(0))).thenReturn(satellites.get(0));

        ResponseEntity<CommandShipResponse> response = commandShipController.postTopSecretSplit("kenobi", satelliteFleet.getSatellites().get(0), "EN");
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = APIServiceException.class)
    public void topSecretTestThrowsApiServiceException() throws APIServiceException {
        when(getMessageService.getMessage(any())).thenThrow(new APIServiceException(
                HttpStatus.NOT_FOUND.getReasonPhrase(), APIServiceErrorCodes.MESSAGE_NOT_DEFINABLE));
        commandShipController.topSecret(satelliteFleet, "EN");
    }

    @Test(expected = Exception.class)
    public void topSecretTestThrowsException() throws APIServiceException {
        when(getMessageService.getMessage(any())).thenThrow(new Exception("test Error"));
        commandShipController.topSecret(satelliteFleet, "EN");
    }
}