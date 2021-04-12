package co.com.davidserrano.apps.quasar.controller;

import co.com.davidserrano.apps.quasar.controller.converter.SatelliteConverter;
import co.com.davidserrano.apps.quasar.dto.CommandShipResponse;
import co.com.davidserrano.apps.quasar.dto.SatelliteDTO;
import co.com.davidserrano.apps.quasar.dto.SatelliteFleet;
import co.com.davidserrano.apps.quasar.entity.Satellite;
import co.com.davidserrano.apps.quasar.exception.APIServiceException;
import co.com.davidserrano.apps.quasar.repository.ISatelliteRepository;
import co.com.davidserrano.apps.quasar.service.IGetLocationService;
import co.com.davidserrano.apps.quasar.service.IGetMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CommandShipController {

    private final IGetMessageService getMessageService;
    private final IGetLocationService getPositionService;
    private final ISatelliteRepository satelliteRepository;

    private SatelliteConverter satelliteConverter;

    @Autowired
    public CommandShipController(final IGetMessageService getMessageService, final IGetLocationService getPositionService,
                                 final ISatelliteRepository satelliteRepository, final SatelliteConverter satelliteConverter) {
        this.getMessageService = getMessageService;
        this.getPositionService = getPositionService;
        this.satelliteRepository = satelliteRepository;
        this.satelliteConverter = satelliteConverter;
    }

    /**
     *
     * @param satellites
     * @return
     * @throws APIServiceException
     */
    @RequestMapping(value = "/topsecret",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<CommandShipResponse> topSecret(@RequestBody final SatelliteFleet satellites, @RequestHeader(value = "locale", required = false) final String locale) throws APIServiceException {
        ArrayList<String[]> messages = getFleetMessages(satellites.getSatellites());
        List<Float> distances = getFleetLocations(satellites.getSatellites());

        String decodeMessage = getMessageService.getMessage(messages.toArray(new String[messages.size()][]));
        Point2D.Float position = getPositionService.getLocation(distances.toArray(new Float[distances.size()]));

        return new ResponseEntity<>(new CommandShipResponse(position, decodeMessage), HttpStatus.OK);
    }

    /**
     *
     * @param satellite_name
     * @param satelliteDTO
     * @return
     * @throws APIServiceException
     */
    @RequestMapping(value = "/topsecret_split/{satellite_name}", produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
    public ResponseEntity<CommandShipResponse> postTopSecretSplit(@PathVariable String satellite_name, @RequestBody SatelliteDTO satelliteDTO, @RequestHeader(value = "locale", required = false) final String locale) throws APIServiceException {
        satelliteDTO.setName(satellite_name);

        final Optional<Satellite> optionalSatellite = satelliteRepository.findById(satellite_name);
        if (optionalSatellite.isPresent()) {
            satelliteRepository.delete(optionalSatellite.get());
        }
        satelliteRepository.save(satelliteConverter.toEntity(satelliteDTO));
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     *
     * @param satellite_name
     * @param satelliteDTO
     * @return
     * @throws APIServiceException
     */
    @RequestMapping(value = "/topsecret_split/{satellite_name}", produces = "application/json", consumes = "application/json", method = RequestMethod.GET)
    public ResponseEntity<CommandShipResponse> getTopSecretSplit(@PathVariable String satellite_name, @RequestBody SatelliteDTO satelliteDTO, @RequestHeader(value = "locale", required = false) final String locale) throws APIServiceException {
        final List<Satellite> satellites = satelliteRepository.findAll();
        final List<SatelliteDTO> satellitesDTO = satellites.stream().map(satellite -> satelliteConverter.toDTO(satellite)).collect(Collectors.toList());

        ArrayList<String[]> messages = getFleetMessages(satellitesDTO);
        List<Float> distances = getFleetLocations(satellitesDTO);

        String decodeMessage = getMessageService.getMessage(messages.toArray(new String[messages.size()][]));
        Point2D.Float position = getPositionService.getLocation(distances.toArray(new Float[distances.size()]));

        return new ResponseEntity<>(new CommandShipResponse(position, decodeMessage), HttpStatus.OK);
    }

    private List<Float> getFleetLocations(List<SatelliteDTO> satellites){
        return satellites.stream().map(SatelliteDTO::getDistance).collect(Collectors.toList());
    }

    private ArrayList<String[]> getFleetMessages(List<SatelliteDTO> satellites){
        ArrayList<String[]> messages = new ArrayList<>();
        satellites.stream().map(SatelliteDTO::getMessage).forEach(messages::add);
        return messages;
    }
}
