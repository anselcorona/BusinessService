package com.newsoft.tallerservicio.modelos.beans;

/**
 *
 * @author acorona
 */
import com.newsoft.tallerservicio.entidades.OrderDetails;
import com.newsoft.tallerservicio.modelos.dao.DAOHibernateFactory;
import com.newsoft.tallerservicio.modelos.dao.daohibernate.DAOHibOrderDetails;
import com.newsoft.tallerservicio.modelos.dao.interfaces.DAOOrderDetails;
import javax.ejb.Stateless;
import lombok.Getter;

@Stateless
public class ModOrderDetails extends DAOAbstractModel<OrderDetails> {
    
    @Getter
    DAOOrderDetails daoOrderDetails = new DAOHibOrderDetails();


    public ModOrderDetails() {
         this.daoEntityClass = new DAOHibernateFactory().instanciarDAOGenerico(DAOHibOrderDetails.class);
         this.daoOrderDetails = (DAOOrderDetails) this.daoEntityClass;
    }
    
         
}