package dk.vandborgandersen.exposure;

import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import dk.vandborgandersen.exposure.model.ErrorExposure;

/**
 * Application representation helper.
 *
 * @author mortena@gmail.com
 */

public class RepresentationHelper {
    @Inject
    RepresentationFactory representationFactory;

    public Representation createError(int errorCode, String msg, UriInfo uriInfo) {
        ErrorExposure entity = new ErrorExposure(errorCode, msg);
        Representation representation =
            representationFactory.newRepresentation(uriInfo.getBaseUriBuilder().path("errors").build()).withBean(entity);
        return representation;
    }
}
