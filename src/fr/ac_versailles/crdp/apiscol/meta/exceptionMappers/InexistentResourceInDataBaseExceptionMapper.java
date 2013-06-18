package fr.ac_versailles.crdp.apiscol.meta.exceptionMappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import fr.ac_versailles.crdp.apiscol.database.InexistentResourceInDatabaseException;

@Provider
public class InexistentResourceInDataBaseExceptionMapper implements
		ExceptionMapper<InexistentResourceInDatabaseException> {

	@Override
	public Response toResponse(InexistentResourceInDatabaseException e) {
		return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML)
				.entity(e.getXMLMessage()).build();
	}
}