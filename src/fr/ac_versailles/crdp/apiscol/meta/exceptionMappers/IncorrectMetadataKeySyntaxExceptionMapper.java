package fr.ac_versailles.crdp.apiscol.meta.exceptionMappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import fr.ac_versailles.crdp.apiscol.meta.IncorrectMetadataKeySyntaxException;

@Provider
public class IncorrectMetadataKeySyntaxExceptionMapper implements
		ExceptionMapper<IncorrectMetadataKeySyntaxException> {

	@Override
	public Response toResponse(IncorrectMetadataKeySyntaxException e) {
		return Response.status(Status.BAD_REQUEST)
				.type(MediaType.APPLICATION_XML).entity(e.getXMLMessage())
				.build();
	}
}