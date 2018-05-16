package com.newsoft.tallerservicio.modelos.beans;

/**
 *
 * @author acorona
 */
import com.newsoft.tallerservicio.entidades.Products;
import com.newsoft.tallerservicio.modelos.dao.DAOHibernateFactory;
import com.newsoft.tallerservicio.modelos.dao.daohibernate.DAOHibProducts;
import com.newsoft.tallerservicio.modelos.dao.interfaces.DAOProducts;
import javax.ejb.Stateless;
import lombok.Getter;

@Stateless
public class ModProducts extends DAOAbstractModel<Products> {
    
    @Getter
    DAOProducts daoProducts = new DAOHibProducts();


    public ModProducts() {
         this.daoEntityClass = new DAOHibernateFactory().instanciarDAOGenerico(DAOHibProducts.class);
         this.daoProducts = (DAOProducts) this.daoEntityClass;
    }
    
         
}