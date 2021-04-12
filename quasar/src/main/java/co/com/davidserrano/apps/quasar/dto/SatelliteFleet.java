package co.com.davidserrano.apps.quasar.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class SatelliteFleet {

    private List<SatelliteDTO> satellites;

}
