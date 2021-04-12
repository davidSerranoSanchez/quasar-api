package co.com.davidserrano.apps.quasar.controller.converter;

import co.com.davidserrano.apps.quasar.dto.SatelliteDTO;
import co.com.davidserrano.apps.quasar.entity.Satellite;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class SatelliteConverter {

    public SatelliteDTO toDTO(final Satellite entity) {
        final ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(entity, SatelliteDTO.class);
    }

    public Satellite toEntity(final SatelliteDTO dto) {
        final ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Satellite.class);
    }

}

