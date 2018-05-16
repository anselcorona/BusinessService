/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.servicios;

import com.newsoft.tallerservicio.entidades.Employees;
import com.newsoft.tallerservicio.modelos.beans.ModEmployees;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
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
@Path("employees")
@Consumes(MediaType.APPLICATION_JSON) 
@Produces(MediaType.APPLICATION_JSON) 
public class EmployeesResource {
    
    ModEmployees modEmployees = lookupModEmployeesBean();
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of EmployeesResources
     */
    public EmployeesResource() {
    }

    
    @GET
    public Employees getJson() {
       return modEmployees.getDaoEmployees().buscarPorID(1);
    }

    /**
     * PUT method for updating or creating an instance of EmployeesResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    public void putJson(Employees content) {
    }
    
    

    private ModEmployees lookupModEmployeesBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ModEmployees) c.lookup("java:global/BusinessServicio/ModEmployees!com.newsoft.tallerservicio.modelos.beans.ModEmployees");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
