/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.servicios;

import com.newsoft.tallerservicio.entidades.Products;
import com.newsoft.tallerservicio.modelos.beans.ModProducts;
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
@Path("products")
@Consumes(MediaType.APPLICATION_JSON) 
@Produces(MediaType.APPLICATION_JSON) 
public class ProductsResource {
    
    ModProducts modProducts = lookupModProductsBean();
    
    @Context
    private UriInfo context;

    public ProductsResource() {
    }
    
    @GET
    public Products getJson() {
        return modProducts.getDaoProducts().buscarPorID("24 hour");
    }
    
    @PUT
    public void putJson(Products content) {
    }

    private ModProducts lookupModProductsBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ModProducts) c.lookup("java:global/BusinessServicio/ModProducts!com.newsoft.tallerservicio.modelos.beans.ModProducts");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
