package co.com.davidserrano.apps.quasar.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.awt.geom.Point2D;

@Getter
@Setter
@ToString
public class CommandShipResponse {

    public CommandShipResponse(){}
    public CommandShipResponse(Point2D.Float position, String message){
        this.position = position;
        this.message = message;
    }

    private Point2D.Float position;
    private String message;
}
