
package com.newsoft.tallerservicio.modelos.servicios;

import com.newsoft.tallerservicio.entidades.Orders;
import com.newsoft.tallerservicio.modelos.beans.ModOrders;
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
@Path("orders")
@Consumes(MediaType.APPLICATION_JSON) 
@Produces(MediaType.APPLICATION_JSON) 
public class OrdersResource {
    
    ModOrders modOrders = lookupModOrdersBean();
    
    @Context
    private UriInfo context;

    public OrdersResource() {
    }
    
    @GET
    public Orders getJson() {
        return modOrders.getDaoOrders().buscarPorID(1);
    }
    
    @PUT
    public void putJson(Orders content) {
    }

    private ModOrders lookupModOrdersBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ModOrders) c.lookup("java:global/BusinessServicio/ModOrders!com.newsoft.tallerservicio.modelos.beans.ModOrders");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
