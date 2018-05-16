/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.beans;


import com.newsoft.tallerservicio.modelos.dao.genericos.GenericDAO;
import com.newsoft.tallerservicio.utilidades.HibernateUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;

/**
 *
 * @author esantiago
 */
public abstract class DAOAbstractModel<T>{
    @Getter
    protected GenericDAO daoEntityClass;
    @Getter
    private List<T> datasource;
    @Getter
    private Map<String, String> columnasOrdenarPor = new HashMap<>();
    
    public void setColumnasOrdenarPor(Map<String, String> parColumnasOrdenarPor) {
        if (parColumnasOrdenarPor == null) {
            columnasOrdenarPor = new HashMap<>();
        } else {
            columnasOrdenarPor = parColumnasOrdenarPor;
        }
        this.daoEntityClass.setColumnasOdenarPor(parColumnasOrdenarPor);
    }
    
    public void save(T objeto) throws HibernateException {
        try {
            HibernateUtil.getCurrentSession().beginTransaction();     
            daoEntityClass.save(objeto);
            HibernateUtil.getCurrentSession().getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
             HibernateUtil.getCurrentSession().getTransaction().rollback();
        }
        
    }
    public void saveProceso(T objeto) throws HibernateException {
        try {
                 
            daoEntityClass.saveProceso(objeto);
            
        } catch (Exception e) {
             throw e;
        }
        
    }
    public void update(T objeto) {
       try {
            HibernateUtil.getCurrentSession().beginTransaction();     
            daoEntityClass.update(objeto);
            HibernateUtil.getCurrentSession().getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
             HibernateUtil.getCurrentSession().getTransaction().rollback();
        }
    }
    
    public void delete(T objeto) {
        try {
            HibernateUtil.getCurrentSession().beginTransaction();     
            daoEntityClass.delete(objeto);
            HibernateUtil.getCurrentSession().getTransaction().commit();

        } catch (Exception e) {
             HibernateUtil.getCurrentSession().getTransaction().rollback();
        }
    }
    public void deleteProceso (T objeto ){
         try {
             
            daoEntityClass.deleteProceso(objeto);
            

        } catch (Exception e) {
             throw e;
        }
    }
    public void saveOrUpdate(T objeto){
        try {
            HibernateUtil.getCurrentSession().beginTransaction();     
            daoEntityClass.saveOrUpdate(objeto);
            HibernateUtil.getCurrentSession().getTransaction().commit();

        } catch (Exception e) {
             HibernateUtil.getCurrentSession().getTransaction().rollback();
        }
    }
    public T mergeProceso(T objeto){
        return (T) daoEntityClass.mergeProceso(objeto);
    }
    public void saveOrUpdateProceso(T objeto){
        try {
            daoEntityClass.saveOrUpdateProceso(objeto);
            
        } catch (Exception e) {
             throw e;
        }
    }
    public void updateProceso(T objeto){
        try {
            daoEntityClass.updateProceso(objeto);
            
        } catch (Exception e) {
             throw e;
        }
    }
    public void persistProceso( T objeto){
        try {
            daoEntityClass.persistProceso(objeto);
            
        } catch (Exception e) {
             throw e;
        }
    }
    public void aplicarCondicionesAdicionales(Criteria parCriterioAplicarCondicion ) {
        daoEntityClass.aplicarCondicionesAdicionales(parCriterioAplicarCondicion);
    }
    
    public void modificarResultado(List<T> parRegistrosModificar){
    }
}
