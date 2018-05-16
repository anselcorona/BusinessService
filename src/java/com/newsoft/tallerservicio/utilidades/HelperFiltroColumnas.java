/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.utilidades;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author avasquez
 */
public class HelperFiltroColumnas<T> {

    public HelperFiltroColumnas() {
    }

     public void aplicarCondicionNombre(Criteria parCriteria, Map<String,Object> parColumnasAnalizar)
    {
        if (parColumnasAnalizar == null || parColumnasAnalizar.isEmpty()) {
            return;
        }
        // Cada Key del mapa Corresponde al Nombre de la Columna en la Tabla y la parte del Valor de cada Key
        // es el valor que será utilizado para la condición del Select.
        Set<String> columnas = parColumnasAnalizar.keySet();
        for (String columna : columnas) {
            if (parColumnasAnalizar.get(columna) != null && !(parColumnasAnalizar.get(columna) + "").trim().isEmpty()) {
                parCriteria.add(Restrictions.ilike(columna, parColumnasAnalizar.get(columna).toString(), MatchMode.EXACT));
            }else
            {
                parCriteria.add(Restrictions.isNull(columna));
            }
        }
        
    }
    public void aplicarFiltroAliasColumnas(Class<T> entityClass, Map<String, String> condicion, Criteria criteria) {

        // Verificar si la columna por la cual se aplicará el filtro es una entidad. Si es una entidad, crear un alias
        // para evitar el error de que hibernate no puede encontrar la propiedad.
        int indice = 0;
        String nuevaClave;
        Map<String, String> mapAliasUtilizados = new HashMap<>();
        Map<String, String> condicionModificada = new HashMap<>(condicion);
        Map<String, String> tipoDatosCondicion = new HashMap<>();

        for (String clave : condicion.keySet()) {
            List<Map<String, String>> tipoDatoAliasColumna = obtenerTipoDatoAliasColumna(entityClass, clave);
            System.out.println("resultado tipodatosalias : " + tipoDatoAliasColumna);

            if (tipoDatoAliasColumna.get(1) != null && tipoDatoAliasColumna.get(1).size() > 0) {

                Map<String, String> aliasCreados = tipoDatoAliasColumna.get(1);

                String[] columnaDesglosada = clave.split("\\.");
                String columnaFiltrarPor = columnaDesglosada[columnaDesglosada.length - 1];

                String valor = condicion.get(clave);
                nuevaClave = aliasCreados.get(columnaFiltrarPor);

                aliasCreados.remove(columnaFiltrarPor);

                condicionModificada.remove(clave);
                condicionModificada.put(nuevaClave, valor);

                // Esto es para los casos en los cuales existen Columnas a filtrar con el mismo nombre de una entidad. 
                // Ejemplo usuario.id.usuario, donde usuario es de tipo Usuarios y el id.usuario es string.
                if (!clave.trim().toLowerCase().equals(nuevaClave.trim().toLowerCase() )
                        && aliasCreados.isEmpty()) {
                    System.out.println(" clave : " + clave + " nuevaClave : " + nuevaClave);
                    String[] columnaAliasDesglosada = nuevaClave.split("\\.");
                    for (int indiceC = 0; indiceC < columnaDesglosada.length; indiceC++) {
                        if (!columnaDesglosada[indiceC].trim().toLowerCase().equals(columnaAliasDesglosada[indiceC].trim().toLowerCase())) {
                            aliasCreados.put(columnaDesglosada[indiceC], columnaAliasDesglosada[indiceC]);
                        }
                    }
                }
                
                // salvar todos los alias creados
                for (String claveAlia : aliasCreados.keySet()) {
                    if (!mapAliasUtilizados.containsKey(claveAlia)) {
                        mapAliasUtilizados.put(claveAlia, aliasCreados.get(claveAlia));
                    }
                }

            } else {
                nuevaClave = clave;
            }
            if (tipoDatoAliasColumna.get(0) != null && tipoDatoAliasColumna.get(0).get("tipoDato") != null) {
                tipoDatosCondicion.put(nuevaClave, tipoDatoAliasColumna.get(0).get("tipoDato"));
            }
        }

        // Crear los alias que fueron generados en la parte anterior.
        System.out.println("SE CREARAN LOS ALIAS");
        for (String clave : mapAliasUtilizados.keySet()) {
            System.out.println("Objeto : " + clave + " Alias : " + mapAliasUtilizados.get(clave));
            criteria.createAlias(clave, mapAliasUtilizados.get(clave));
        }

        System.out.println("---------> NUEVA MAP DE FILTRO <--------- : " + condicionModificada);

        for (Map.Entry mapEntry : condicionModificada.entrySet()) {
            Criterion obtenerCriterioTipoDato = obtenerCriterioTipoDato(mapEntry.getKey() + "", mapEntry.getValue().toString(), tipoDatosCondicion.get(mapEntry.getKey() + ""));
            if ( obtenerCriterioTipoDato != null)
             criteria.add(obtenerCriterioTipoDato);
        }
                
    }

    public List<Map<String, String>> obtenerTipoDatoAliasColumna(Class<T> entityClass, String parColumnaFiltro) {
        List<Map<String, String>> tipoDatoAliasRetornar = new ArrayList<>();
        String[] columnas = parColumnaFiltro.split("\\.");
        Map<String, String> tipoDato = new HashMap<>();
        Map<String, String> aliasColumna = new HashMap<>();

        // La primera Posicion es para el tipo de datos y la segunda para el alias.
        if (columnas.length <= 1) {
            try {
                tipoDato.put("tipoDato", entityClass.getDeclaredField(parColumnaFiltro).getType().getName());

            } catch (NoSuchFieldException | SecurityException ex) {
                System.out.println("NO EXISTE LA PROPIEDAD : " + parColumnaFiltro + " en la clase  : " + entityClass.getName());
            }
        } else {

            // Si se pasa una columna con la siguiente composicion : segTipoTransCajaChicaId.cajaChicaId.usuario, se creará un arreglo con
            // 3 casillas y se necesitará un alia para la primera casilla ( segTipoTransCajaChicaId ), si la misma es un mapping, y determinar el tipo de datos para la
            // la última columna ( usuario ), la cual pertenece a la clase "cajaChicaId".

            tipoDato.put("tipoDato", obtenerTipoDatoColumna(entityClass, columnas, 0));
            aliasColumna.putAll(obtenerAliasColumna(entityClass, columnas, 0, ""));
        }

        tipoDatoAliasRetornar.add(tipoDato);
        tipoDatoAliasRetornar.add(aliasColumna);
        return tipoDatoAliasRetornar;
    }

    public Criterion obtenerCriterioTipoDato(String parColumna, String parValor, String parTipoDato) {
        Object valor = null;
        boolean usarLike = false;
        Criterion condicionAgregar=null;
        
        if ( (parValor + "").toLowerCase().contains(" or ") || (parValor + "").toLowerCase().contains(" and ") )
            return obtenerCriterioConectoresLogicos(parColumna, parValor, parTipoDato);
        
        if (parTipoDato.toLowerCase().contains("long")) {
            valor = Long.parseLong(removerOperadoresLogicoCondicion(parValor));
        } else if (parTipoDato.toLowerCase().contains("int")) {
            valor = Integer.parseInt(removerOperadoresLogicoCondicion(parValor));
        } else if (parTipoDato.toLowerCase().contains("double")) {
            valor = Double.parseDouble(removerOperadoresLogicoCondicion(parValor));
        } else if (parTipoDato.toLowerCase().contains("short")) {
            valor = Short.parseShort(removerOperadoresLogicoCondicion(parValor));
        } else if (parTipoDato.toLowerCase().contains("bigdecimal")) {
            valor = new BigDecimal(removerOperadoresLogicoCondicion(parValor));
        } else if (parTipoDato.toLowerCase().contains("date") && parValor.length() >= 8) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                String valorString = removerOperadoresLogicoCondicion(parValor);
                valor = sdf.parse(valorString);
                               
            } catch (ParseException ex) {
                Logger.getLogger(HelperFiltroColumnas.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            usarLike = true;
            valor = removerOperadoresLogicoCondicion(parValor);
        }
         
        if (valor != null) {
            if ( parValor.contains(">="))
                condicionAgregar = Restrictions.ge(parColumna, valor);
            else if ( parValor.contains("<="))
                condicionAgregar = Restrictions.le(parColumna, valor);
            else if ( parValor.contains("="))
                condicionAgregar = Restrictions.eq(parColumna, valor);
            else if ( parValor.contains(">"))
                condicionAgregar = Restrictions.gt(parColumna, valor);
            else if ( parValor.contains("<"))
                condicionAgregar = Restrictions.lt(parColumna, valor);
            else if ( parValor.contains("<>"))
                condicionAgregar = Restrictions.ne(parColumna, valor);
            else if ( usarLike )
                condicionAgregar = Restrictions.ilike(parColumna, valor+"", MatchMode.ANYWHERE);
            else 
                condicionAgregar = Restrictions.eq(parColumna, valor);
        }
        
        return condicionAgregar;
    }

    public Criterion obtenerCriterioConectoresLogicos(String parColumna, String parValor, String parTipoDato){
        String[] condicionAplicar = parValor.split(" AND | OR | and | or");
    
        Criterion condicionConectorLogico = null;
        
        for (   int indice=0; indice < condicionAplicar.length;indice++) {
            String valorUno;
            String valorDos;
            if ( indice == 1)
            {
                valorUno = condicionAplicar[0];
                valorDos = condicionAplicar[1];
                if ( parValor.toLowerCase().contains(" or ")) 
                    condicionConectorLogico = Restrictions.or(obtenerCriterioTipoDato(parColumna, valorUno, parTipoDato), obtenerCriterioTipoDato(parColumna, valorDos, parTipoDato));
                else
                    condicionConectorLogico = Restrictions.and(obtenerCriterioTipoDato(parColumna, valorUno, parTipoDato), obtenerCriterioTipoDato(parColumna, valorDos, parTipoDato));
            }else if ( indice > 1 )
            {
                valorUno = condicionAplicar[indice];
                if ( parValor.toLowerCase().contains(" or ")) 
                    condicionConectorLogico = Restrictions.or(condicionConectorLogico, obtenerCriterioTipoDato(parColumna, valorUno, parTipoDato));
                else
                    condicionConectorLogico = Restrictions.and(condicionConectorLogico, obtenerCriterioTipoDato(parColumna, valorUno, parTipoDato));
            }
        }
        // esto es lo que se debe utilizar para agregar la condicion OR : "criteria.add(condicionLogica2);"
        
        return condicionConectorLogico;
    }
    public String obtenerTipoDatoColumna(Class<T> entityClass, String[] parColumnas, int parIndiceInicial) {
        if (parIndiceInicial < 0 || parIndiceInicial > (parColumnas.length - 1)) {
            return "";
        }
        try {
            if (parIndiceInicial == (parColumnas.length - 1)) {
                return entityClass.getDeclaredField(parColumnas[parIndiceInicial]).getType().getName();
            }

            Class<T> entidad = (Class<T>) Class.forName(entityClass.getDeclaredField(parColumnas[parIndiceInicial]).getType().getName());
            return obtenerTipoDatoColumna(entidad, parColumnas, parIndiceInicial + 1);

        } catch (Exception ex) {
            System.out.println("OCURRIO UNA CONDICION EXCEPCIONAL TRANTA DE OBTENER EL TIPO DATO PARA LA PROPIEDAD : " + parColumnas[parIndiceInicial]
                    + " EN LA CLASE : " + entityClass.getName());
        }
        return "";
    }

    public Map<String, String> obtenerAliasColumna(Class<T> entityClass, String[] parColumnas, int parIndiceInicial, String parAliaPrevio) {
        Map<String, String> aliasCreados = new HashMap<>();

        try {
            // Se pregunta si es mayor o igual que la última casilla, ya que en esta se encuentra la columna por la cual
            // se ejecutara el filtro. Ejemplo : Si se tiene la condicion = "segTipoTransCajaChicaId.cajaChicaId.usuario"
            // Esta resulta en un arreglo de 3 casillas con los siguientes valores : "segTipoTransCajaChicaId", "cajaChicaId", "usuario".
            // Entonces la ultima casilla contiene a "usuario", el cual corresponde a la columna por la que se filtrará y no debe crearse
            // alia para la misma.

            if (parIndiceInicial < 0 || parIndiceInicial >= (parColumnas.length - 1)) {

                if (parIndiceInicial == (parColumnas.length - 1)) {

                    if (parAliaPrevio.trim().length() > 0) {
                        aliasCreados.put(parColumnas[parIndiceInicial], parAliaPrevio + "." + parColumnas[parIndiceInicial]);
                    } else {
                        aliasCreados.put(parColumnas[parIndiceInicial], parColumnas[parIndiceInicial]);
                    }
                }
                return aliasCreados;
            }

            String clase = entityClass.getDeclaredField(parColumnas[parIndiceInicial]).getType().getName();

            // Determinar si es un mapping para crear el alias.
            if (HibernateUtil.getSessionFactory().getClassMetadata(clase) != null) {
                if (parAliaPrevio.trim().length() > 0) {
                    aliasCreados.put(parAliaPrevio.trim() + "." + parColumnas[parIndiceInicial], parColumnas[parIndiceInicial] + "1");
                } else {
                    aliasCreados.put(parColumnas[parIndiceInicial], parColumnas[parIndiceInicial] + "1");
                }

                parAliaPrevio = parColumnas[parIndiceInicial] + "1";

                Class<T> entidad = (Class<T>) Class.forName(clase);
                aliasCreados.putAll(obtenerAliasColumna(entidad, parColumnas, parIndiceInicial + 1, parAliaPrevio));
            } else {
                // Esto es para columna de tipo clase que no son Mapping, sino Embeddabled ID de una tabla

                String columnaFiltrarPor = parColumnas[parColumnas.length - 1];
                String aliaColumnaFiltrarPor = parAliaPrevio;
                for (int indice = parIndiceInicial; indice < parColumnas.length; indice++) {
                    if (aliaColumnaFiltrarPor.trim().length() > 0) {
                        aliaColumnaFiltrarPor = aliaColumnaFiltrarPor + "." + parColumnas[indice];
                    } else {
                        aliaColumnaFiltrarPor = parColumnas[indice];
                    }
                }
                aliasCreados.put(columnaFiltrarPor, aliaColumnaFiltrarPor);
            }

        } catch (Exception ex) {
            System.out.println("CONDICION EXCEPCIONAL OBTENIENDO TIPO DATO Y ALIAS COLUMNA FILTRO : " + ex.getMessage());
        }

        return aliasCreados;
    }

    public String obtenerTipoDatoPropiedad(Field parPropiedad, String parColumnaFiltrar) {

        try {
            // Si la propiedad que se pasó es una clase, entonces se debe crear una entidad con la misma y recorrer sus propiedades
            // para determinar el tipo de datos de la misma. Ejemplo : si tenemos una propiedad "gestion.descripcionCorta",
            // entonces la clase es Gestion y descripcionCorta es la propiedad a la cual se le debe determinar el tipo de datos.

            Class<?> entidad;
            entidad = Class.forName(parPropiedad.getType().getName());
            if (parPropiedad.getType().getName().contains("String")) {
                return parPropiedad.getType().getName();
            }

            for (Field propiedadSubClase : entidad.getDeclaredFields()) {

                System.out.println(" getName() : " + propiedadSubClase.getName() + " getType : " + propiedadSubClase.getType().getName());

                if (parColumnaFiltrar.contains(propiedadSubClase.getName())) {
                    return obtenerTipoDatoPropiedad(propiedadSubClase, parColumnaFiltrar);
                }

            }

        } catch (ClassNotFoundException ex) {
            System.out.println("NO ES UNA CLASE SE RETORNA : " + parPropiedad.getType().getName());
            return parPropiedad.getType().getName();
        }

        return parPropiedad.getType().getName();
    }

    public boolean exiteColumnaEnCondicion(String parCondicion, String parColumna) {

        String[] columnas = parCondicion.split(".");

        for (String columna : columnas) {
            if (columna.trim().equals(parColumna.trim())) {
                return true;
            }
        }
        if (parCondicion.trim().equals(parColumna.trim())) {
            return true;
        }
        return false;
    }

    public String removerOperadoresLogicoCondicion(String parCondicion) {

        return parCondicion.replaceAll(">", "").replaceAll("<", "").replaceAll("=", "").trim();
    }
}
