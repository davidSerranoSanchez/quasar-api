package co.com.davidserrano.apps.quasar.service;

import co.com.davidserrano.apps.quasar.exception.APIServiceException;

import java.awt.geom.Point2D;

public interface IGetLocationService {

    public Point2D.Float getLocation(Float... distances) throws APIServiceException;

}
