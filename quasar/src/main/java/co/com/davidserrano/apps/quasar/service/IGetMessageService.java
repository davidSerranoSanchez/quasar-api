package co.com.davidserrano.apps.quasar.service;

import co.com.davidserrano.apps.quasar.exception.APIServiceException;

public interface IGetMessageService {

    public String getMessage(String[]... messages) throws APIServiceException;

}
