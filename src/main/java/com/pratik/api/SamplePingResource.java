package com.pratik.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("ping")
public class SamplePingResource
{
    @GET
    public Response samplePing()
    {
        return Response.ok("pong by server").build();
    }
}
