package com.newsoft.tallerservicio.modelos.beans;

/**
 *
 * @author acorona
 */
import com.newsoft.tallerservicio.entidades.ProductLines;
import com.newsoft.tallerservicio.modelos.dao.DAOHibernateFactory;
import com.newsoft.tallerservicio.modelos.dao.daohibernate.DAOHibCustomers;
import com.newsoft.tallerservicio.modelos.dao.daohibernate.DAOHibProductLines;
import com.newsoft.tallerservicio.modelos.dao.interfaces.DAOProductLines;
import javax.ejb.Stateless;
import lombok.Getter;

@Stateless
public class ModProductLines extends DAOAbstractModel<ProductLines> {
    
    @Getter
    DAOProductLines daoProductLines = new DAOHibProductLines();


    public ModProductLines() {
         this.daoEntityClass = new DAOHibernateFactory().instanciarDAOGenerico(DAOHibProductLines.class);
         this.daoProductLines = (DAOProductLines) this.daoEntityClass;
    }
    
         
}