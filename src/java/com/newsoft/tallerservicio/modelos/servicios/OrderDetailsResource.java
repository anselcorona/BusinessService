/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.servicios;

import com.newsoft.tallerservicio.entidades.OrderDetails;
import com.newsoft.tallerservicio.modelos.beans.ModOrderDetails;
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
@Path("orderdetails")
@Consumes(MediaType.APPLICATION_JSON) 
@Produces(MediaType.APPLICATION_JSON) 
public class OrderDetailsResource {
    
    ModOrderDetails modOrderDetails = lookupModOrderDetailsBean();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of OrderDetailsResource
     */
    public OrderDetailsResource() {
    }

    @GET
    public OrderDetails getJson() {
        return modOrderDetails.getDaoOrderDetails().buscarPorID(1);
    }
    
    public void persist(Object object) {
        /* Add this to the deployment descriptor of this module (e.g. web.xml, ejb-jar.xml):
         * <persistence-context-ref>
         * <persistence-context-ref-name>persistence/LogicalName</persistence-context-ref-name>
         * <persistence-unit-name>tallerservicioPU</persistence-unit-name>
         * </persistence-context-ref>
         * <resource-ref>
         * <res-ref-name>UserTransaction</res-ref-name>
         * <res-type>javax.transaction.UserTransaction</res-type>
         * <res-auth>Container</res-auth>
         * </resource-ref> */
        try {
            javax.naming.Context ctx = new InitialContext();
            UserTransaction utx = (UserTransaction) ctx.lookup("java:comp/env/UserTransaction");
            utx.begin();
            EntityManager em = (EntityManager) ctx.lookup("java:comp/env/persistence/LogicalName");
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
    @PUT
    public void putJson(OrderDetails content) {
    }

    private ModOrderDetails lookupModOrderDetailsBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ModOrderDetails) c.lookup("java:global/BusinessServicio/ModOrderDetails!com.newsoft.tallerservicio.modelos.beans.ModOrderDetails");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
