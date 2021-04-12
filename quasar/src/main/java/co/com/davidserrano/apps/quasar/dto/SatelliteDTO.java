package co.com.davidserrano.apps.quasar.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.awt.geom.Point2D;

@Getter
@Setter
public class SatelliteDTO {

    public SatelliteDTO() {
    }

    public SatelliteDTO(String name, Float positionX, Float positionY) {
        this.name = name;
        this.position =  new Point2D.Float(positionX, positionY);
    }

    private String name;
    private Point2D.Float position;
    private Float distance;
    private String[] message;

}
