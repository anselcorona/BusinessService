/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.servicios;

import com.newsoft.tallerservicio.entidades.Payments;
import com.newsoft.tallerservicio.modelos.beans.ModPayments;
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
@Path("payments")
@Consumes(MediaType.APPLICATION_JSON) 
@Produces(MediaType.APPLICATION_JSON) 
public class PaymentsResource {
    
    ModPayments modPayments = lookupModPaymentsBean();
    
    @Context
    private UriInfo context;

    public PaymentsResource() {
    }
    
    @GET
    public Payments getJson() {
        return modPayments.getDaoPayments().buscarPorID("0604204436402620180");
    }
    
    @PUT
    public void putJson(Payments content) {
    }

    private ModPayments lookupModPaymentsBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ModPayments) c.lookup("java:global/BusinessServicio/ModPayments!com.newsoft.tallerservicio.modelos.beans.ModPayments");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
