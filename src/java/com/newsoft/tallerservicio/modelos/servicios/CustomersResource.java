/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.servicios;

import com.newsoft.tallerservicio.modelos.beans.ModCustomers;
import com.newsoft.tallerservicio.entidades.Customers;
import com.newsoft.tallerservicio.entidades.MensajeServicio;
import com.newsoft.tallerservicio.utilidades.HibernateUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.hibernate.Session;

/**
 * REST Web Service
 *
 * @author acorona
 */
@Path("customers")
//@Consumes(MediaType.APPLICATION_JSON) 
@Produces(MediaType.APPLICATION_JSON) 
public class CustomersResource {
    ModCustomers modCustomers = lookupModCustomersBean();

    @Context
    private UriInfo context;


    public CustomersResource() {
    }

    @GET
    public List<Customers> getJson() {
       return modCustomers.getDaoCustomers().buscarTodos();
    }

  
    @POST
    @Path("/customer/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response insertCustomer(@FormParam("cusName") String cusName, @FormParam("cusPhone") String cusPhone, @FormParam("credLim") double credLim) {
        int cusNum = modCustomers.getDaoCustomers().count()+1;
        if(cusNum!=0){
            
            Session sProceso = HibernateUtil.getSessionFactory().openSession();
            sProceso.beginTransaction();
            
            try{
                this.modCustomers.getDaoCustomers().setSessionProceso(sProceso);
                
                HibernateUtil.getCurrentSession().clear();
                Customers c = new Customers(cusNum, cusName, cusPhone, credLim);
                c = this.modCustomers.getDaoCustomers().mergeProceso(c);
                sProceso.getTransaction().commit();
                sProceso.flush();
                sProceso.close();
                sProceso = null;
                GenericEntity<Customers> entidad = new GenericEntity<Customers>(c){
                };
                return Response.status(Status.CREATED).entity(entidad).build();
                
            }catch(Exception ex){
                sProceso.getTransaction().rollback();
                sProceso.disconnect();

                sProceso.close();
                sProceso = null;

                HibernateUtil.getCurrentSession().clear();

                MensajeServicio error = new MensajeServicio(Status.INTERNAL_SERVER_ERROR.getStatusCode(), ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
            }
            
        }
        return null;    
    }
    @DELETE
    @Path("/delete/{cusNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void delCustomer(@PathParam("cusNumber") int cusNumber) {
        
        System.out.print(cusNumber);
        Session sProceso = HibernateUtil.getSessionFactory().openSession();
        sProceso.beginTransaction();
        
  
            try{
                modCustomers.getDaoCustomers().setSessionProceso(sProceso);
                Customers customerBorrar = modCustomers.getDaoCustomers().buscarPorID(cusNumber);
                    
                System.out.println(customerBorrar.getCustomerNumber().toString());
                if(customerBorrar == null){                    
                    sProceso.getTransaction().rollback();
                    sProceso.disconnect();
                    sProceso.close();
                    
                    sProceso = null;
                    
                    HibernateUtil.getCurrentSession().clear();
                    System.out.println("ERROR");
                    return;
                }            
                
                HibernateUtil.getCurrentSession().clear();

                
                this.modCustomers.getDaoCustomers().deleteProceso(customerBorrar);
                
                sProceso.getTransaction().commit();
                
                sProceso.flush();
                sProceso.close();
                sProceso = null;
                

            }catch(Exception ex){
               ex.printStackTrace();
                sProceso.getTransaction().rollback();
                sProceso.disconnect();

                sProceso.close();
                sProceso = null;

                HibernateUtil.getCurrentSession().clear();

                MensajeServicio error = new MensajeServicio(Status.INTERNAL_SERVER_ERROR.getStatusCode(), ex.getMessage());
            }
    } 
    
    
    @PUT
    @Path("/customer/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateOrInsertCustomer(@FormParam("cusNumber") int cusNumber, @FormParam("cusName") String cusName, @FormParam("cusPhone") String cusPhone, @FormParam("credLim") double credLim) {
        
        Customers actual = modCustomers.getDaoCustomers().buscarPorID(cusNumber);
        
        Session sProceso = HibernateUtil.getSessionFactory().openSession();
        sProceso.beginTransaction();
        if(actual!=null){
            try{
                this.modCustomers.getDaoCustomers().setSessionProceso(sProceso);

                HibernateUtil.getCurrentSession().clear();
                actual.setCustomerName(cusName);
                actual.setPhone(cusPhone);
                actual.setCreditLimit(credLim);
                
                this.modCustomers.getDaoCustomers().saveOrUpdateProceso(actual);
                sProceso.getTransaction().commit();
                sProceso.flush();
                sProceso.close();
                sProceso = null;
                GenericEntity<Customers> entidad = new GenericEntity<Customers>(actual){
                };
                return Response.status(Status.CREATED).entity(entidad).build();

            }catch(Exception ex){
                sProceso.getTransaction().rollback();
                sProceso.disconnect();

                sProceso.close();
                sProceso = null;

                HibernateUtil.getCurrentSession().clear();

                MensajeServicio error = new MensajeServicio(Status.INTERNAL_SERVER_ERROR.getStatusCode(), ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
            }
        }
        return null;            
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
