/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.dao.daohibernate;

import com.newsoft.tallerservicio.entidades.ProductLines;
import com.newsoft.tallerservicio.modelos.dao.genericos.GenericHibernateDAO;
import com.newsoft.tallerservicio.modelos.dao.interfaces.DAOProductLines;

/**
 *
 * @author acorona
 */
public class DAOHibProductLines extends GenericHibernateDAO<ProductLines, String> 
    implements DAOProductLines{
    
}