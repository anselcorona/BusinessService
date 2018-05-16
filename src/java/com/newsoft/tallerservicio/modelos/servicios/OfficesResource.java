
package com.newsoft.tallerservicio.modelos.servicios;

import com.newsoft.tallerservicio.entidades.Offices;
import com.newsoft.tallerservicio.modelos.beans.ModOffices;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author acorona
 */
@Path("offices")
@Consumes(MediaType.APPLICATION_JSON) 
@Produces(MediaType.APPLICATION_JSON) 
public class OfficesResource {
    
    ModOffices modOffices = lookupModOfficesBean();
    
    @Context
    private UriInfo context;

    public OfficesResource() {
    }

    @GET
    public Offices getJson() {
        return modOffices.getDaoOffices().buscarPorID("AAPC");
    }

    @PUT
    public void putJson(Offices content) {
    }

    private ModOffices lookupModOfficesBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ModOffices) c.lookup("java:global/BusinessServicio/ModOffices!com.newsoft.tallerservicio.modelos.beans.ModOffices");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
