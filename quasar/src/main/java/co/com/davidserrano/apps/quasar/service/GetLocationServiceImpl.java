package co.com.davidserrano.apps.quasar.service;

import co.com.davidserrano.apps.quasar.dto.KenobiSatelliteDTO;
import co.com.davidserrano.apps.quasar.dto.SatelliteDTO;
import co.com.davidserrano.apps.quasar.dto.SatoSatelliteDTO;
import co.com.davidserrano.apps.quasar.dto.SkywalkerSatelliteDTO;
import co.com.davidserrano.apps.quasar.exception.APIServiceErrorCodes;
import co.com.davidserrano.apps.quasar.exception.APIServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;

@Service
public class GetLocationServiceImpl implements IGetLocationService {

    /**
     *
     * @param distances
     * @return
     * @throws APIServiceException
     */
    @Override
    public Point2D.Float getLocation(Float... distances) throws APIServiceException {

        //I need minimum 3 satellites To identify Position
        if (distances == null || distances.length < 3) {
            throw new APIServiceException(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    APIServiceErrorCodes.NOT_ENOUGH_INFORMATION);
        }
        SatelliteDTO kenobiSatelliteDTO = new KenobiSatelliteDTO();
        SatelliteDTO skywalkerSatelliteDTO = new SkywalkerSatelliteDTO();
        SatelliteDTO satoSatelliteDTO = new SatoSatelliteDTO();

        kenobiSatelliteDTO.setDistance(distances[0]);
        skywalkerSatelliteDTO.setDistance(distances[1]);
        satoSatelliteDTO.setDistance(distances[2]);

        SatelliteDTO[] satelliteDTOS = {kenobiSatelliteDTO, skywalkerSatelliteDTO, satoSatelliteDTO};

        //Getting Source of Help message
        Point2D.Float source = getIntersectionPoint(satelliteDTOS);

        return source;
    }

    /**
     *
     * @param satelliteDTOS
     * @return
     * @throws APIServiceException
     */
    private Point2D.Float getIntersectionPoint(SatelliteDTO... satelliteDTOS) throws APIServiceException {

        Float ERROR_MARGIN = 0.1F;

        //Getting satelites Positions
        Float kenobiX = satelliteDTOS[0].getPosition().x;
        Float kenobiY = satelliteDTOS[0].getPosition().y;
        Float distanceKenobi = satelliteDTOS[0].getDistance();
        Float skywalkerX = satelliteDTOS[1].getPosition().x;
        Float skywalkerY = satelliteDTOS[1].getPosition().y;
        Float distanceSkywalker = satelliteDTOS[1].getDistance();
        Float satoX = satelliteDTOS[2].getPosition().x;
        Float satoY = satelliteDTOS[2].getPosition().y;
        Float distanceSato = satelliteDTOS[2].getDistance();

        Point2D.Float intersectionCoordinates = new Point2D.Float();

        Double distanceBetweenTwoCircles = Math.sqrt(Math.pow((skywalkerX - kenobiX), 2) + Math.pow((skywalkerY - kenobiY), 2));

        if (distanceBetweenTwoCircles > distanceKenobi + distanceSkywalker) {
            throw new APIServiceException(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    APIServiceErrorCodes.LOCATION_NOT_DEFINABLE);
        }
        //One circle within other
        if (distanceBetweenTwoCircles < Math.abs(distanceKenobi - distanceSkywalker)) {
            throw new APIServiceException(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    APIServiceErrorCodes.LOCATION_NOT_DEFINABLE);
        }
        //coincident circles
        if (distanceBetweenTwoCircles == 0 && distanceKenobi.equals(distanceSkywalker)) {
            throw new APIServiceException(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    APIServiceErrorCodes.LOCATION_NOT_DEFINABLE);
        }

        Double variation = 0.25 * (Math.sqrt((distanceBetweenTwoCircles + distanceKenobi + distanceSkywalker) *
                (distanceBetweenTwoCircles + distanceKenobi - distanceSkywalker) *
                (distanceBetweenTwoCircles - distanceKenobi + distanceSkywalker) *
                (-distanceBetweenTwoCircles + distanceKenobi + distanceSkywalker)
        )
        );

        Double commonPartEquation = Math.pow(distanceKenobi, 2) - Math.pow(distanceSkywalker, 2);

        Double equationXPart1 = (kenobiX + skywalkerX) / 2d;
        double equationXPart2 = ((skywalkerX - kenobiX) * commonPartEquation) / (2 * Math.pow(distanceBetweenTwoCircles, 2));
        double equationXPart3 = (2 * (kenobiY - skywalkerY) / Math.pow(distanceBetweenTwoCircles, 2)) * variation;

        Double intersectionPointX1 = (equationXPart1 +
                equationXPart2 +
                equationXPart3
        );

        Double intersectionPointX2 = (equationXPart1 +
                equationXPart2 -
                equationXPart3
        );

        Double equationYPart1 = (kenobiY + skywalkerY) / 2d;
        double equationYPart2 = ((skywalkerY - kenobiY) * commonPartEquation) / (2 * Math.pow(distanceBetweenTwoCircles, 2));
        double equationYPart3 = (2 * (kenobiX - skywalkerX) / (Math.pow(distanceBetweenTwoCircles, 2))) * variation;

        Double intersectionPointY1 = (equationYPart1 +
                equationYPart2 +
                equationYPart3
        );

        Double intersectionPointY2 = (equationYPart1 +
                equationYPart2 -
                equationYPart3
        );

        /* Lets determine if circle 3 intersects at either of the above intersection points. */
        Double dx = intersectionPointX1 - satoX;
        Double dy = intersectionPointY1 - satoY;
        Double d1 = Math.sqrt((dy * dy) + (dx * dx));

        dx = intersectionPointX2 - satoX;
        dy = intersectionPointY2 - satoY;
        Double d2 = Math.sqrt((dy * dy) + (dx * dx));

        if (Math.abs(d1 - distanceSato) < ERROR_MARGIN) {
            intersectionCoordinates.setLocation(intersectionPointX1, intersectionPointY1);
        } else if (Math.abs(d2 - distanceSato) < ERROR_MARGIN) {
            intersectionCoordinates.setLocation(intersectionPointX2, intersectionPointY2);
        } else {
            throw new APIServiceException(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    APIServiceErrorCodes.LOCATION_NOT_DEFINABLE);
        }
        return intersectionCoordinates;
    }

}
