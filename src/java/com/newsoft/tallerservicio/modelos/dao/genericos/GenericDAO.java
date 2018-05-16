/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.dao.genericos;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

/**
 *
 * @author jrozon
 * @param <T>
 * @param <ID>
 */
public interface GenericDAO<T, ID extends Serializable> {

    Session getSessionProceso();
    void setSessionProceso(Session parSessionProceso);
    void save(T entity);
    void saveProceso(T entity);
    void update(T entity);
    void updateProceso(T entity);
    void delete(T entity);
    void deleteProceso(T entity);
    void refresh(Object objetoRefrescar);
    void refreshProceso(Object objetoRefrescar);
    void merge(T objetosalvar);
    T mergeProceso(T objetosalvar);
    void saveOrUpdate(T entity);
    void saveOrUpdateProceso(T entity);
    void persistProceso( T entity);
    
    
    void aplicarCondicionesAdicionales(Criteria parCriterioAplicarCondicion);
    void asignarFiltroAdicionales(Map<String, String> parFiltroAdicionar);
    
    
    
    T buscarPorID(ID id, boolean lock);
    T buscarPorID(ID id);
    T buscarPorIDProceso(ID id);
    
    Class<T> getClasePersistente();
    
    List<T> buscarTodos();
    List<T> buscarTodosOrdenado(String order);
    List<T> buscarTodosOrdenado(Map<String, String> order);
    List<T> buscarRango(int desde, int hasta);
    
    int count();
    int countByQuery(Map<String, String> condicion, int desde, int hasta);
    
    List<T> filtrarPorRango(Map<String, String> condicion, int desde, int hasta);
    List<T> filtrarPorColumnas(Map<String, String> condicion);
    List<DetachedCriteria> obtenerCondicionesExists(List<Class<T>> entidades, String parCondicionGlobal, Map<String, Object> parColumnas);
    List<T> buscarPorExample(T exampleInstance);
    
    public void setColumnasOdenarPor ( Map<String,String> parColumnas);
    

}
