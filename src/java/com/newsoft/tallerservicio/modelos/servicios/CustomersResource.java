/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.servicios;

import com.newsoft.tallerservicio.modelos.beans.ModCustomers;
import com.newsoft.tallerservicio.entidades.Customers;
import java.util.List;
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
@Path("customers")
@Consumes(MediaType.APPLICATION_JSON) 
@Produces(MediaType.APPLICATION_JSON) 
public class CustomersResource {
    ModCustomers modCustomers = lookupModCustomersBean();

    @Context
    private UriInfo context;


    public CustomersResource() {
    }

    @GET
    public List<Customers> getJson() {
        
        
       return modCustomers.getDaoCustomers().buscarRango(1, 5);
    }

  
    @PUT
    public void putJson(Customers content) {
    }

  

    private ModCustomers lookupModCustomersBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ModCustomers) c.lookup("java:global/BusinessServicio/ModCustomers!com.newsoft.tallerservicio.modelos.beans.ModCustomers");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
