/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.beans;

import com.newsoft.tallerservicio.utilidades.HelperFiltroColumnas;
import com.newsoft.tallerservicio.utilidades.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

public abstract class AbstractModel<T> {

    private Class<T> entityClass;
    @Getter
    private List<T> datasource;
    @Getter
    private Map<String, Object> condicionAgregarFiltros = new HashMap<>();
    @Getter
    private Map<String, Object> columnasOrdenarPor = new HashMap<>();
    protected static Session sessionProceso;

    public AbstractModel(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void setColumnasOrdenarPor(Map<String, Object> parColumnasOrdenarPor) {
        if (parColumnasOrdenarPor == null) {
            columnasOrdenarPor = new HashMap<>();
        } else {
            columnasOrdenarPor = parColumnasOrdenarPor;
        }
    }

    protected abstract Session getSession();

    public Session getSessionProceso() {
        if (sessionProceso == null) {
            sessionProceso = HibernateUtil.getCurrentSession();
        }
        return sessionProceso;
    }
    
     public void saveProceso(T objetoAsalvar) {
        try {
            getSessionProceso().saveOrUpdate(objetoAsalvar);

        } catch (HibernateException e) {
            getSession().clear();
            getSession().flush();
            System.out.println(e);
            throw e;
        }
    }
     
     public void updateProceso(T objetoAActualizar) {
        try {

            getSessionProceso().merge(objetoAActualizar);

        } catch (HibernateException e) {
            getSession().clear();
            getSession().flush();
            System.out.println(e);
            throw e;
        }
    }

    public void save(T objetoAsalvar) {
        try {
            getSession().beginTransaction();
            getSession().saveOrUpdate(objetoAsalvar);
            getSession().getTransaction().commit();
            getSession().flush();
        } catch (HibernateException e) {
            getSession().clear();
            System.out.println(e);
        }
    }

   

    public void update(T objetoAActualizar) {
        try {
            getSession().clear();
            /*
             getSession().merge(objetoAActualizar);
             getSession().beginTransaction();
             getSession().saveOrUpdate(objetoAActualizar);
             */
            getSession().beginTransaction();
            getSession().merge(objetoAActualizar);
            getSession().getTransaction().commit();
            getSession().flush();
        } catch (HibernateException e) {
            getSession().clear();
            System.out.println(e);
        }
    }

    

   

    public void saveOrUpdate(T objetoAActualizar) {
        try {
            getSession().beginTransaction();
            getSession().saveOrUpdate(objetoAActualizar);
            getSession().getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

    }

    public void delete(T objetoAEliminar) {
        try {
            getSession().clear();
            //getSession().merge(objetoAEliminar);
            getSession().beginTransaction();
            getSession().delete(objetoAEliminar);
            getSession().getTransaction().commit();
            getSession().flush();
        } catch (HibernateException e) {
            getSession().clear();
            System.out.println(e);
        }
    }

    public void deleteProceso(T objetoAEliminar) {
        try {
            getSessionProceso().delete(objetoAEliminar);
        } catch (HibernateException e) {
            getSession().clear();
            getSession().flush();
            System.out.println(e);
            throw e;
        }
    }

    public void aplicarCondicionesAdicionales(Criteria parCriterioAplicarCondicion) {

        if (condicionAgregarFiltros == null || condicionAgregarFiltros.size() == 0) {
            return;
        }

        // Instanciar la Clase que se encargará de aplicar los filtros y el alias a las columnas que serán filtradas
        HelperFiltroColumnas helperFiltroColumnas = new HelperFiltroColumnas();
        helperFiltroColumnas.aplicarFiltroAliasColumnas(entityClass, condicionAgregarFiltros, parCriterioAplicarCondicion);

    }
    
     public void updateSinClear(T objetoAActualizar) {
        try {
            getSession().beginTransaction();
            System.out.println("OBJETO RECIBIDO " + objetoAActualizar);
            getSession().merge(objetoAActualizar);
            getSession().getTransaction().commit();
            getSession().flush();
        } catch (HibernateException e) {
            getSession().clear();
            e.printStackTrace();
        }

    }

    public T buscarPorID(String id) {
        T elementos = null;
        try {
            elementos = (T) getSession().get(entityClass, id);
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return elementos;
    }

    public T buscarPorID(String id, Boolean parUsarSessionProceso) {
        T elementos = null;
        Session sessionUtilizar;
        if (parUsarSessionProceso) {
            sessionUtilizar = getSessionProceso();
        } else {
            sessionUtilizar = getSession();
        }
        try {
            elementos = (T) sessionUtilizar.get(entityClass, id);
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return elementos;
    }

    public T buscarPorID(Integer id) {
        T elementos = null;
        try {
            elementos = (T) getSession().get(entityClass, id);
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return elementos;
    }

    public T buscarPorID(Integer id, Boolean parUsarSessionProceso) {
        T elementos = null;
        Session sessionUtilizar;
        if (parUsarSessionProceso) {
            sessionUtilizar = getSessionProceso();
        } else {
            sessionUtilizar = getSession();
        }
        try {
            elementos = (T) sessionUtilizar.get(entityClass, id);
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return elementos;
    }

    public T buscarPorID(Short id) {
        T elementos = null;
        try {
            elementos = (T) getSession().get(entityClass, id);
        } catch (HibernateException e) {
            System.out.println(e);
        } finally {
        }
        return elementos;
    }

    public T buscarPorID(Short id, Boolean parUsarSessionProceso) {
        T elementos = null;
        Session sessionUtilizar;
        if (parUsarSessionProceso) {
            sessionUtilizar = getSessionProceso();
        } else {
            sessionUtilizar = getSession();
        }
        try {
            elementos = (T) sessionUtilizar.get(entityClass, id);
        } catch (HibernateException e) {
            System.out.println(e);
        } finally {
        }
        return elementos;
    }

    public T buscarPorID(Long id) {
        T elementos = null;
        try {
            elementos = (T) getSession().get(entityClass, id);
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return elementos;
    }

    public T buscarPorID(Long id, Boolean parUsarSessionProceso) {
        T elementos = null;
        Session sessionUtilizar;
        if (parUsarSessionProceso) {
            sessionUtilizar = getSessionProceso();
        } else {
            sessionUtilizar = getSession();
        }
        try {
            elementos = (T) sessionUtilizar.get(entityClass, id);
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return elementos;
    }

    public List<T> buscarTodos() {
        List<T> listaElementos = null;
        try {
            Criteria createCriteria = getSession().createCriteria(entityClass);

            listaElementos = createCriteria.list();
        } catch (HibernateException e) {
        } finally {
            return listaElementos;
        }
    }

    public List<T> buscarPorFecha(Integer codigoCajaChica, Date fechaInicial, Date fechaFinal) {
        List<T> listaElementos = null;
        try {
            Criteria createCriteria = getSession().createCriteria(entityClass);
            createCriteria.add(Restrictions.between("fechaEfectiva", fechaInicial, fechaFinal));
            createCriteria.add(Restrictions.eq("cajaChica.codigoCajaChica", codigoCajaChica));

            listaElementos = createCriteria.list();
        } catch (HibernateException e) {
            System.out.println("ERROR OBTENIENDO LOS DATOS DE LA ENTIDAD : " + e.getMessage());
        } finally {
            return listaElementos;
        }
    }

    public List<T> buscarPorFecha(Integer codigoCajaChica, long codigoHeader, Date fechaInicial, Date fechaFinal) {
        List<T> listaElementos = null;
        try {
            Criteria createCriteria = getSession().createCriteria(entityClass);
            createCriteria.add(Restrictions.between("fechaEfectiva", fechaInicial, fechaFinal));
            createCriteria.add(Restrictions.eq("cajaChica.codigoCajaChica", codigoCajaChica));
            createCriteria.add(Restrictions.eq("codigoHeader", codigoHeader));

            listaElementos = createCriteria.list();
        } catch (HibernateException e) {
        } finally {
            return listaElementos;
        }
    }

    public List<T> buscarPorSistema(Integer codigoSistema) {
        List<T> listaElementos = null;
        try {
            Criteria createCriteria = getSession().createCriteria(entityClass);
            createCriteria.add(Restrictions.eq("usuarios.id.sistema.codigoSistema", codigoSistema));

            listaElementos = createCriteria.list();
        } catch (HibernateException e) {
            System.out.println("ERROR OBTENIENDO LOS DATOS DE LA ENTIDAD : " + e.getMessage());
        } finally {
            return listaElementos;
        }
    }

    public List<T> buscarTodosOrdenado(String order) {
        List<T> listaElementos = null;
        try {
            Criteria createCriteria = getSession().createCriteria(entityClass);
            createCriteria.addOrder(Order.asc(order));
            listaElementos = createCriteria.list();
        } catch (HibernateException he) {
            System.out.println(he);
        } finally {
            return listaElementos;
        }
    }

    public List<T> buscarTodosOrdenado(String order, Boolean parUsarSessionProceso) {
        List<T> listaElementos = null;
        Session sessionUtilizar;
        if (parUsarSessionProceso) {
            sessionUtilizar = getSessionProceso();
        } else {
            sessionUtilizar = getSession();
        }
        try {
            Criteria createCriteria = sessionUtilizar.createCriteria(entityClass);
            createCriteria.addOrder(Order.asc(order));
            listaElementos = createCriteria.list();
        } catch (HibernateException he) {
            System.out.println(he);
        } finally {
            return listaElementos;
        }
    }

    public List<T> buscarTodosOrdenado(Map<String, Object> order) {
        List<T> listaElementos = null;
        try {
            Criteria createCriteria = getSession().createCriteria(entityClass);
            for (String ordenAsignar : order.keySet()) {
                if (((order.get(ordenAsignar))+"").toUpperCase().equals("DESC")) {
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

    public List<T> buscarRango(int desde, int hasta) {
        List<T> listaElementos = null;
        try {
            Criteria createCriteria = getSession().createCriteria(entityClass);

            // Incorporar las condiciones adicionales, si las mismas fueron especificadas.
            aplicarCondicionesAdicionales(createCriteria);

            createCriteria.setMaxResults(hasta - desde);
            createCriteria.setFirstResult(desde);
            listaElementos = createCriteria.list();
        } catch (HibernateException e) {
        }
        return listaElementos;
    }

    public int count() {
        int count = 0;
        try {
            Criteria createCriteria = getSession().createCriteria(entityClass);

            // Incorporar las condiciones adicionales, si las mismas fueron especificadas.
            aplicarCondicionesAdicionales(createCriteria);

            createCriteria.setProjection(Projections.rowCount());
            count = ((Long) createCriteria.uniqueResult()).intValue();
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return count;

    }

    private void setMergeObject(List<T> listaElementos) throws HibernateException {
        for (T t : listaElementos) {
            getSession().merge(t);
        }
    }

    public List<T> filtrarPorRango(Map<String, Object> condicion, int desde, int hasta) {
        List<T> listaElementos = null;
        Criteria add;

        try {
            System.out.println("ENTIDAD EN EL FILTRO ES : " + entityClass);
            System.out.println(" mapa : " + condicion);
            add = getSession().createCriteria(entityClass);
            // Instanciar la Clase que se encargará de aplicar los filtros y el alias a las columnas que serán filtradas
            HelperFiltroColumnas helperFiltroColumnas = new HelperFiltroColumnas();
            helperFiltroColumnas.aplicarFiltroAliasColumnas(entityClass, condicion, add);

            // Incorporar las condiciones adicionales, si las mismas fueron especificadas.
            aplicarCondicionesAdicionales(add);

            add.setMaxResults(hasta - desde);
            add.setFirstResult(desde);
            listaElementos = add.list();
            System.out.println("RESULTADO : " + listaElementos);
        } catch (HibernateException e) {
            System.out.println("ERROR APLICANDO FILTRO EN filtrarPorRango : " + e.getMessage());
        }
        return listaElementos;
    }

    public List<T> filtrarPorColumnas(Map<String, Object> condicion) {
        List<T> listaElementos = null;
        Criteria add;

        try {
            add = getSession().createCriteria(entityClass);

            // Instanciar la Clase que se encargará de aplicar los filtros y el alias a las columnas que serán filtradas
            HelperFiltroColumnas helperFiltroColumnas = new HelperFiltroColumnas();
            helperFiltroColumnas.aplicarFiltroAliasColumnas(entityClass, condicion, add);

            // Incorporar las condiciones adicionales, si las mismas fueron especificadas.
            aplicarCondicionesAdicionales(add);

            listaElementos = add.list();
        } catch (HibernateException e) {
            System.out.println("ERROR APLICANDO FILTRO EN filtrarPorColumna : " + e.getMessage());
        }
        return listaElementos;
    }

    public int countByQuery(Map<String, Object> condicion, int desde, int hasta) {
        int count = 0;
        Criteria add;

        try {
            add = getSession().createCriteria(entityClass);

            // Instanciar la Clase que se encargará de aplicar los filtros y el alias a las columnas que serán filtradas
            HelperFiltroColumnas helperFiltroColumnas = new HelperFiltroColumnas();
            helperFiltroColumnas.aplicarFiltroAliasColumnas(entityClass, condicion, add);

            // Incorporar las condiciones adicionales, si las mismas fueron especificadas.
            aplicarCondicionesAdicionales(add);

            add.setProjection(Projections.rowCount());
            count = ((Long) add.uniqueResult()).intValue();
        } catch (HibernateException e) {
            System.out.println("ERROR APLICANDO FILTRO EN COUNTQUERY : " + e.getMessage());
        }

        return count;
    }

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

    public void limpiarSesion() {
        try {
            getSession().clear();
        } catch (HibernateException he) {
            System.out.println(he);
        }
    }
    
    public void refreshObject ( Object objeto) throws HibernateException{
        getSession().refresh(objeto);
    }
//    public String nombreTabla(Annotation[] annotations) {
//        String nombre = null;
//        for (Annotation annotation : annotations) {
//            if (annotation.toString().contains("Table")) {
//                String[] split = annotation.toString().split(",");
//                 for (String string : split) {
//                    string = string.replaceAll(" ", "");
//                    String[] split1 = string.split("=");
//                    if (split1[0].equals("name")) {
//                        nombre = split1[1].replace(")", "");
//                    }
//                }
//            } 
//        }
//        return nombre;
//    }
}