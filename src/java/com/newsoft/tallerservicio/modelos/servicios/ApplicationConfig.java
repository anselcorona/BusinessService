/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.servicios;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author acorona
 */
@javax.ws.rs.ApplicationPath("/")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.newsoft.tallerservicio.modelos.servicios.CustomersResource.class);
        resources.add(com.newsoft.tallerservicio.modelos.servicios.EmployeesResource.class);
        resources.add(com.newsoft.tallerservicio.modelos.servicios.OfficesResource.class);
        resources.add(com.newsoft.tallerservicio.modelos.servicios.OrderDetailsResource.class);
        resources.add(com.newsoft.tallerservicio.modelos.servicios.OrdersResource.class);
        resources.add(com.newsoft.tallerservicio.modelos.servicios.PaymentsResource.class);
        resources.add(com.newsoft.tallerservicio.modelos.servicios.ProductlinesResource.class);
        resources.add(com.newsoft.tallerservicio.modelos.servicios.ProductsResource.class);
    }
    
}
