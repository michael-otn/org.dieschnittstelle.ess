package org.dieschnittstelle.ess.jrs;

import org.dieschnittstelle.ess.entities.crm.StationaryTouchpoint;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/touchpoints")
@Consumes({MediaType.APPLICATION_JSON}) // nimmt JSON an
@Produces({MediaType.APPLICATION_JSON}) // gibt Json zurück
public interface ITouchpointCRUDService {

	@GET
	List<StationaryTouchpoint> readAllTouchpoints();

	@GET
	@Path("/{touchpointId}")
	StationaryTouchpoint readTouchpoint(@PathParam("touchpointId") long id);

	//ohne Id weil diese erst erstellt werden  muss
	@POST
	StationaryTouchpoint createTouchpoint(StationaryTouchpoint touchpoint);

	@DELETE
	@Path("/{touchpointId}")
	boolean deleteTouchpoint(@PathParam("touchpointId") long id);
		
	/*
	 * TODO JRS1: add a new annotated method for using the updateTouchpoint functionality of TouchpointCRUDExecutor and implement it
	 */
	/*
	@PUT
	@Path("/{touchpointId}")
	boolean updateTouchpoint(@PathParam("touchpointId") long id);
*/
}
