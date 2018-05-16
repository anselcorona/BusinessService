/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.dao.genericos;

import com.newsoft.tallerservicio.utilidades.HelperFiltroColumnas;
import com.newsoft.tallerservicio.utilidades.HibernateUtil;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

/**
 *
 * @author jrozon
 * @param <T>
 * @param <ID>
 */
public abstract class GenericHibernateDAO<T, ID extends Serializable>
        implements GenericDAO<T, ID> {

    private Class<T> clasePersistente;
    private Session sessionProceso;
    @Getter
    private Map<String, String> condicionAgregarFiltros = new HashMap<>();
    private List<Map<String, String>> filtroMultiplesValoresColumnas = new ArrayList<>();
    private Map<String, String> columnasOrderPor = new HashMap<>();
   
    @Override
    public Session getSessionProceso (){
        if ( sessionProceso == null)
        {   System.out.println("Nueva SessionProceso");
            sessionProceso = HibernateUtil.getSessionFactory().openSession();
        }
        if(!sessionProceso.isOpen() || !sessionProceso.isConnected()){
            System.out.println("sessionProceso was closed");
            sessionProceso = HibernateUtil.getSessionFactory().openSession();
        }
        return sessionProceso;
    }
    public GenericHibernateDAO() {
        this.clasePersistente = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void setColumnasOdenarPor(Map<String, String> parColumnasOrdenarPor)
    {
        this.columnasOrderPor.putAll(parColumnasOrdenarPor);
    }
    @SuppressWarnings("unchecked")
    public void setSession(Session session) {
        System.out.println("set session from: " + clasePersistente.getName());
        this.sessionProceso = session;
    }
    @SuppressWarnings("unchecked")
    @Override
    public void setSessionProceso(Session session) {
        System.out.println("set session from: " + clasePersistente.getName());
        this.sessionProceso = session;
    }
    public Session getSession() {
        System.out.println("Get session from: " + clasePersistente.getName());
//        if (HibernateUtil.getCurrentSession() == null || !HibernateUtil.getCurrentSession().isOpen()  ) {
//            HibernateUtil.getSessionFactory().openSession();
//        }
        return HibernateUtil.getCurrentSession();
    }

    @Override
    public Class<T> getClasePersistente() {
        return clasePersistente;
    }
    
    @Override
    public void refresh(Object objeto) {
        getSession().refresh(objeto);    
    }
    @Override
    public void refreshProceso(Object objeto) {
        getSessionProceso().refresh(objeto);    
    }
    @Override
    public void asignarFiltroAdicionales(Map<String, String> parFiltroAdicionar) {
        this.condicionAgregarFiltros = parFiltroAdicionar;
    }

    public void flush() {
        getSession().flush();
    }

    public void clear() {
        getSession().clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T buscarPorID(ID id, boolean lock) {
        T entity;
        if (lock) {
            entity = (T) getSession().get(getClasePersistente(), id, LockOptions.UPGRADE);
        } else {
            entity = (T) getSession().get(getClasePersistente(), id);
        }

        return entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T buscarPorID(ID id) {
        return buscarPorID(id, false);
    }
    @SuppressWarnings("unchecked")
    @Override
    public T buscarPorIDProceso ( ID id){
        T entity;
        entity = (T) getSessionProceso().get(getClasePersistente(), id);
        
        return entity;
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<T> buscarTodos() {
        return buscarPorCriteria();
    }

    public List<T> buscarPorExample(T exampleInstance, String[] excluir) {
        Criteria crit = getSession().createCriteria(getClasePersistente());
        Example example = Example.create(exampleInstance);
        for (String exclude : excluir) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> buscarPorExample(T exampleInstance) {
        Criteria crit = getSession().createCriteria(getClasePersistente());
        crit.add(Example.create(exampleInstance));
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void saveOrUpdate(T entity) throws HibernateException {
        getSession().saveOrUpdate(entity);
    }

    @Override
    public void saveOrUpdateProceso(T entity) throws HibernateException {
        getSessionProceso().saveOrUpdate(entity);
    }
    @Override
    public void merge(T objetosalvar) throws HibernateException {
        getSession().merge(objetosalvar);
    }
    
    @Override
    public T mergeProceso(T objetosalvar) throws HibernateException {
        return (T) getSessionProceso().merge(objetosalvar);
    }
    @Override
    public void persistProceso( T entity){
        getSessionProceso().persist(entity);
    }
    @SuppressWarnings("unchecked")
    protected List<T> buscarPorCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getClasePersistente());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }

    @Override
    public List<T> buscarTodosOrdenado(String order) {
        List<T> listaElementos = null;
        try {
            Criteria createCriteria = getSession().createCriteria(getClasePersistente());
            this.aplicarCondicionesAdicionales(createCriteria);
            createCriteria.addOrder(Order.asc(order));
            listaElementos = createCriteria.list();
        } catch (HibernateException he) {
            throw new HibernateException("Error buscando todo ordenado para " + getClasePersistente() + he);
        } finally {
            return listaElementos;
        }
    }

    @Override
    public List<T> buscarTodosOrdenado(Map<String, String> order) {
        List<T> listaElementos = null;
        try {
            Criteria createCriteria = getSession().createCriteria(getClasePersistente());
            this.aplicarCondicionesAdicionales(createCriteria);
            for (String ordenAsignar : order.keySet()) {
                if (order.get(ordenAsignar).toUpperCase().equals("DESC")) {
                    createCriteria.addOrder(Order.desc(ordenAsignar));
                } else {
                    createCriteria.addOrder(Order.asc(ordenAsignar));
                }
            }
            listaElementos = createCriteria.list();
        } catch (HibernateException he) {
            System.out.println(he);
        } finally {
            return listaElementos;
        }
    }

    @Override
    public List<T> buscarRango(int desde, int hasta) {
        List<T> listaElementos = null;
        try {
            Criteria createCriteria = getSession().createCriteria(getClasePersistente());
            this.aplicarCondicionesAdicionales(createCriteria);
            // Aplicar el orden de las columnas especificado
            this.aplicarOrdenColumnas(createCriteria);
            createCriteria.setMaxResults(hasta - desde);
            createCriteria.setFirstResult(desde);
            listaElementos = createCriteria.list();
        } catch (HibernateException e) {
            throw new HibernateException("Error buscando buscarRango para " + getClasePersistente() + e);
        }
        return listaElementos;
    }

    @Override
    public int count() {
        int count = 0;
        try {
            Criteria createCriteria = getSession().createCriteria(getClasePersistente());
            this.aplicarCondicionesAdicionales(createCriteria);
            createCriteria.setProjection(Projections.rowCount());
            Object uniqueResult = createCriteria.uniqueResult();
            count = uniqueResult == null?0:Integer.valueOf(uniqueResult + "");
        } catch (HibernateException e) {
            throw new HibernateException("Error buscando count para " + getClasePersistente() + e);
        }
        return count;
    }

    @Override
    public void aplicarCondicionesAdicionales(Criteria parCriterioAplicarCondicion) {
        System.out.println("APLICAR CONDICIONES ADICIONALES: " + this.condicionAgregarFiltros);
        if (condicionAgregarFiltros == null || condicionAgregarFiltros.size() == 0) {
            return;
        }

        // Instanciar la Clase que se encargará de aplicar los filtros y el alias a las columnas que serán filtradas
        HelperFiltroColumnas helperFiltroColumnas = new HelperFiltroColumnas();
        helperFiltroColumnas.aplicarFiltroAliasColumnas(getClasePersistente(), condicionAgregarFiltros, parCriterioAplicarCondicion);

    }

    @Override
    public List<T> filtrarPorRango(Map<String, String> condicion, int desde, int hasta) {
        List<T> listaElementos = null;
        Criteria add;

        try {
            System.out.println("ENTIDAD EN EL FILTRO ES : " + getClasePersistente());
            System.out.println(" mapa : " + condicion);
            add = getSession().createCriteria(getClasePersistente());
            // Instanciar la Clase que se encargará de aplicar los filtros y el alias a las columnas que serán filtradas
            HelperFiltroColumnas helperFiltroColumnas = new HelperFiltroColumnas();
            helperFiltroColumnas.aplicarFiltroAliasColumnas(getClasePersistente(), condicion, add);

            // Incorporar las condiciones adicionales, si las mismas fueron especificadas.
            aplicarCondicionesAdicionales(add);

            // 26/10/2015 Incorporar Filtro varias columnas antes de aplicar el Select.[AV]
            this.aplicarOrdenColumnas(add);
            
            add.setMaxResults(hasta - desde);
            add.setFirstResult(desde);
            listaElementos = add.list();
            System.out.println("RESULTADO : " + listaElementos);
        } catch (HibernateException e) {
            System.out.println("ERROR APLICANDO FILTRO EN filtrarPorRango : " + e.getMessage());
        }
        return listaElementos;
    }

    @Override
    public List<T> filtrarPorColumnas(Map<String, String> condicion) {
        List<T> listaElementos = null;
        Criteria add;

        try {
            add = getSession().createCriteria(getClasePersistente());

            // Instanciar la Clase que se encargará de aplicar los filtros y el alias a las columnas que serán filtradas
            HelperFiltroColumnas helperFiltroColumnas = new HelperFiltroColumnas();
            helperFiltroColumnas.aplicarFiltroAliasColumnas(getClasePersistente(), condicion, add);

            // Incorporar las condiciones adicionales, si las mismas fueron especificadas.
            aplicarCondicionesAdicionales(add);

            // 26/10/2015 Incorporar Filtro varias columnas antes de aplicar el Select.[AV]
            this.aplicarOrdenColumnas(add);
            
            listaElementos = add.list();
        } catch (HibernateException e) {
            System.out.println("ERROR APLICANDO FILTRO EN filtrarPorColumna : " + e.getMessage());
        }
        return listaElementos;
    }

    @Override
    public int countByQuery(Map<String, String> condicion, int desde, int hasta) {
        int count = 0;
        Criteria add;

        try {
            add = getSession().createCriteria(getClasePersistente());

            // Instanciar la Clase que se encargará de aplicar los filtros y el alias a las columnas que serán filtradas
            HelperFiltroColumnas helperFiltroColumnas = new HelperFiltroColumnas();
            helperFiltroColumnas.aplicarFiltroAliasColumnas(getClasePersistente(), condicion, add);

            // Incorporar las condiciones adicionales, si las mismas fueron especificadas.
            aplicarCondicionesAdicionales(add);

            add.setProjection(Projections.rowCount());
            count = ((Long) add.uniqueResult()).intValue();
        } catch (HibernateException e) {
            System.out.println("ERROR APLICANDO FILTRO EN COUNTQUERY : " + e.getMessage());
        }

        return count;
    }

    @Override
    public List<DetachedCriteria> obtenerCondicionesExists(List<Class<T>> entidades, String parCondicionGlobal, Map<String, Object> parColumnas) {
        Map<String, String> columnasJoinSubQuery = null;
        List<DetachedCriteria> subConsultas = new ArrayList<>();
        String columnaAlplicarFiltroGlobal = null;

        // El parámetro parColumnas es un Map que contiene con clave el nombre de la entidad y como valor un objeto MAp<String,String> con las columnas
        // de la tabla padre que será enlazadas en el SubQuery con la tabla hijo.
        // Instanciar la Clase que se encargará de aplicar los filtros y el alias a las columnas que serán filtradas
        HelperFiltroColumnas helperFiltroColumnas = new HelperFiltroColumnas();
        for (Class<T> entidad : entidades) {
            columnasJoinSubQuery = (Map<String, String>) parColumnas.get(entidad.getSimpleName());
            if (columnasJoinSubQuery == null) {
                continue;
            }

            // Obtener la columna que sera utilizada para filtrar con el valor especificado en el filtro global.
            // Enlazar la condicion en el Where condition del Subquery con la tabla del query principal.
            Map<String, String> columnasAlplicarFiltroGlobal = new HashMap<>(columnasJoinSubQuery);
            Map<String, String> condicionAplicar = new HashMap<>();
            for (String columnaQuery : columnasAlplicarFiltroGlobal.keySet()) {
                if (columnaQuery.contains("columnaFiltroGlobal")) {
                    condicionAplicar.put(columnasJoinSubQuery.get(columnaQuery), parCondicionGlobal);
                    columnasJoinSubQuery.remove(columnaQuery);
                }
            }
            DetachedCriteria subCons = DetachedCriteria.forClass(entidad, entidad.getSimpleName());
            helperFiltroColumnas.aplicarFiltroAliasColumnas(entidad, condicionAplicar, subCons.getExecutableCriteria(getSession()));

            // Enlazar la condicion en el Where condition del Subquery con la tabla del query principal.
            for (String columnaQuery : columnasJoinSubQuery.keySet()) {

                subCons.add(Property.forName(entidad.getSimpleName() + "." + columnasJoinSubQuery.get(columnaQuery)).eqProperty(columnaQuery));
            }
        }
        return subConsultas;
    }

    public void aplicarOrdenColumnas(Criteria parCriteria)
    {
        System.out.println("COLUMNAS ORDENAR POR: " + this.columnasOrderPor);
        for (String ordenAsignar : this.columnasOrderPor.keySet()) {
                if (((columnasOrderPor.get(ordenAsignar))+"").toUpperCase().equals("DESC")) {
                    parCriteria.addOrder(Order.desc(ordenAsignar));
                } else {
                    parCriteria.addOrder(Order.asc(ordenAsignar));
                }
            }
    }
    @Override
    public void save(T entity) {
        getSession().save(entity);
    }
    @Override
    public void saveProceso(T entity) {
        getSessionProceso().save(entity);
    }
    @Override
    public void update(T entity) {
        getSession().update(entity);
    }

    @Override
     public void updateProceso(T entity) {
         try{
        getSessionProceso().update(entity);
         }catch(Exception ex){throw ex;}
    }
    @Override
    public void delete(T entity) {
        getSession().delete(entity);
    }
    @Override
    public void deleteProceso(T entity)
    {
        getSessionProceso().delete(entity);
    }
}
