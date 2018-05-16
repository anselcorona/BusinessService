package com.newsoft.tallerservicio.modelos.beans;

/**
 *
 * @author acorona
 */
import com.newsoft.tallerservicio.entidades.Payments;
import com.newsoft.tallerservicio.modelos.dao.DAOHibernateFactory;
import com.newsoft.tallerservicio.modelos.dao.daohibernate.DAOHibPayments;
import com.newsoft.tallerservicio.modelos.dao.interfaces.DAOPayments;
import javax.ejb.Stateless;
import lombok.Getter;

@Stateless
public class ModPayments extends DAOAbstractModel<Payments> {
    
    @Getter
    DAOPayments daoPayments = new DAOHibPayments();


    public ModPayments() {
         this.daoEntityClass = new DAOHibernateFactory().instanciarDAOGenerico(DAOHibPayments.class);
         this.daoPayments = (DAOPayments) this.daoEntityClass;
    }
    
         
}