/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.servicios;

import com.newsoft.tallerservicio.entidades.ProductLines;
import com.newsoft.tallerservicio.modelos.beans.ModProductLines;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
/**
 * REST Web Service
 *
 * @author acorona
 */
@Path("productlines")
@Consumes(MediaType.APPLICATION_JSON) 
@Produces(MediaType.APPLICATION_JSON) 
public class ProductlinesResource {
    
    ModProductLines modProductLines = lookupModProductLinesBean();
    
    @Context
    private UriInfo context;
    
    /**
     * Creates a new instance of ProductlinesResource
     */
    public ProductlinesResource() {
    }

    /**
     * Retrieves representation of an instance of com.newsoft.tallerservicio.modelos.servicios.ProductlinesResource
     * @param ID
     * @return an instance of com.newsoft.tallerservicio.entidades.ProductLines
     */
    @GET
    @Produces("application/json")
    @Path("/query")
    public ProductLines getJson(@QueryParam("id") String ID) {
       return modProductLines.getDaoProductLines().buscarPorID(ID);
    }

    /**
     * PUT method for updating or creating an instance of ProductlinesResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(ProductLines content) {
    }

    private ModProductLines lookupModProductLinesBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ModProductLines) c.lookup("java:global/BusinessServicio/ModProductLines!com.newsoft.tallerservicio.modelos.beans.ModProductLines");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
