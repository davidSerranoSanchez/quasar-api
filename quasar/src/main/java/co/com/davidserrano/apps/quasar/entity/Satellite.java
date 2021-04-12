package co.com.davidserrano.apps.quasar.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "SATELLITE_MESSAGES")
public class Satellite {

    @Id
    @Column(name = "SATELLITE_NAME")
    private String name;
    @Column(name = "DISTANCE")
    private Float distance;
    @Column(name = "MESSAGE")
    private String[] message;

    public Satellite() {
    }

    public Satellite(String name, Float distance, String[] message) {
        this.name = name;
        this.distance = distance;
        this.message = message;
    }
}

