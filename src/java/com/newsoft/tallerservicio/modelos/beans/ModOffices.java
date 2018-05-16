package com.newsoft.tallerservicio.modelos.beans;

/**
 *
 * @author acorona
 */
import com.newsoft.tallerservicio.entidades.Offices;
import com.newsoft.tallerservicio.modelos.dao.DAOHibernateFactory;
import com.newsoft.tallerservicio.modelos.dao.daohibernate.DAOHibOffices;
import com.newsoft.tallerservicio.modelos.dao.interfaces.DAOOffices;
import javax.ejb.Stateless;
import lombok.Getter;

@Stateless
public class ModOffices extends DAOAbstractModel<Offices> {
    
    @Getter
    DAOOffices daoOffices = new DAOHibOffices();


    public ModOffices() {
         this.daoEntityClass = new DAOHibernateFactory().instanciarDAOGenerico(DAOHibOffices.class);
         this.daoOffices = (DAOOffices) this.daoEntityClass;
    }
    
         
}