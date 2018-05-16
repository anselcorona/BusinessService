package com.newsoft.tallerservicio.modelos.beans;


import com.newsoft.tallerservicio.modelos.dao.DAOHibernateFactory;
import com.newsoft.tallerservicio.modelos.dao.daohibernate.DAOHibCustomers;
import com.newsoft.tallerservicio.modelos.dao.interfaces.DAOCustomers;
import com.newsoft.tallerservicio.entidades.Customers;
import javax.ejb.Stateless;
import lombok.Getter;

/**
 *
 * @author Jmartinez
 */
@Stateless
public class ModCustomers extends DAOAbstractModel<Customers> {
    
    @Getter
    DAOCustomers daoCustomers = new DAOHibCustomers();


    public ModCustomers() {
         this.daoEntityClass = new DAOHibernateFactory().instanciarDAOGenerico(DAOHibCustomers.class);
         this.daoCustomers = (DAOCustomers) this.daoEntityClass;
    }
    
         
}
