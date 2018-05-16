/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.utilidades;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static Session hibernateSession;
    
    static {
        try {
            Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
            StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
            sb.applySettings(cfg.getProperties());
            StandardServiceRegistry standardServiceRegistry = sb.build();
            sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);
            hibernateSession = sessionFactory.openSession();

        } catch (Throwable th) {
            th.printStackTrace();
            System.err.println("Enitial SessionFactory creation failed" + th);
            throw new ExceptionInInitializerError(th);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static Session getCurrentSession(){
        //System.out.println("estado de la session (connected): "+hibernateSession.isConnected());
        //System.out.println("estado de la session (openned): "+hibernateSession.isOpen());
        if(!hibernateSession.isOpen() || !hibernateSession.isConnected()){
            System.out.println("session was closed");
            hibernateSession = sessionFactory.openSession();
            
        }
        return hibernateSession;
    }
    
    public static void generarNuevaSession ( ){
        hibernateSession = sessionFactory.openSession();
    }

}
