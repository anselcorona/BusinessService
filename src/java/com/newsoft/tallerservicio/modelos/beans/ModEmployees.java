package com.newsoft.tallerservicio.modelos.beans;

/**
 *
 * @author acorona
 */
import com.newsoft.tallerservicio.entidades.Employees;
import com.newsoft.tallerservicio.modelos.dao.DAOHibernateFactory;
import com.newsoft.tallerservicio.modelos.dao.daohibernate.DAOHibCustomers;
import com.newsoft.tallerservicio.modelos.dao.daohibernate.DAOHibEmployees;
import com.newsoft.tallerservicio.modelos.dao.interfaces.DAOEmployees;
import javax.ejb.Stateless;
import lombok.Getter;

@Stateless
public class ModEmployees extends DAOAbstractModel<Employees> {
    
    @Getter
    DAOEmployees daoEmployees = new DAOHibEmployees();


    public ModEmployees() {
         this.daoEntityClass = new DAOHibernateFactory().instanciarDAOGenerico(DAOHibEmployees.class);
         this.daoEmployees = (DAOEmployees) this.daoEntityClass;
    }
    
         
}