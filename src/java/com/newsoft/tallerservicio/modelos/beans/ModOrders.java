package com.newsoft.tallerservicio.modelos.beans;

/**
 *
 * @author acorona
 */
import com.newsoft.tallerservicio.entidades.Orders;
import com.newsoft.tallerservicio.modelos.dao.DAOHibernateFactory;
import com.newsoft.tallerservicio.modelos.dao.daohibernate.DAOHibOrders;
import com.newsoft.tallerservicio.modelos.dao.interfaces.DAOOrders;
import javax.ejb.Stateless;
import lombok.Getter;

@Stateless
public class ModOrders extends DAOAbstractModel<Orders> {
    
    @Getter
    DAOOrders daoOrders = new DAOHibOrders();

    public ModOrders() {
         this.daoEntityClass = new DAOHibernateFactory().instanciarDAOGenerico(DAOHibOrders.class);
         this.daoOrders = (DAOOrders) this.daoEntityClass;
    }
    
         
}