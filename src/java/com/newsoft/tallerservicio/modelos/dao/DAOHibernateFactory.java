/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.dao;

import com.newsoft.tallerservicio.modelos.dao.genericos.DAOFactory;
import com.newsoft.tallerservicio.modelos.dao.genericos.GenericHibernateDAO;
import com.newsoft.tallerservicio.utilidades.HibernateUtil;
import org.hibernate.Session;

/**
 *
 * @author jrozon
 */
public class DAOHibernateFactory extends DAOFactory {
        
    private GenericHibernateDAO instanciarDAO(Class daoClass) {
        try {
            System.out.println("instanciarDAO para la clase: "+daoClass.getName());
            GenericHibernateDAO dao = (GenericHibernateDAO) daoClass.newInstance();
            //dao.setSession(getCurrentSession());
            return dao;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException("No se pudo instanciar DAO: " + daoClass, ex);
        }
    }

    // You could override this if you don't want HibernateUtil for lookup
    public Session getCurrentSession() {
        return HibernateUtil.getCurrentSession();
    }

    //Con el siguiente metodo ya no es necesario hacer un metodo por cada entidad DAO
    public GenericHibernateDAO instanciarDAOGenerico(Class daoClass){
        return instanciarDAO(daoClass);
    }
 
    //Modelos que no tienen ninguna logica de negocio pueden ser declarados aqui
  
}
