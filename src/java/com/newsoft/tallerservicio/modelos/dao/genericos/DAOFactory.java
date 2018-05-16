/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.dao.genericos;

import com.newsoft.tallerservicio.modelos.dao.DAOHibernateFactory;


/**
 *
 * @author jrozon
 */
public abstract class DAOFactory {

     /**
     * Creates a standalone DAOFactory that returns unmanaged DAO
     * beans for use in any environment Hibernate has been configured
     * for. Uses HibernateUtil/SessionFactory and Hibernate context
     * propagation (CurrentSessionContext), thread-bound or transaction-bound,
     * and transaction scoped.
     */
    public static final Class HIBERNATE = DAOHibernateFactory.class;
 
    /**
     * Factory method for instantiation of concrete factories.
     */
    public static DAOFactory instance(Class factory) {
        try {
            return (DAOFactory)factory.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException("Couldn't create DAOFactory: " + factory);
        }
    }
 
    // Add your DAO interfaces here
}
