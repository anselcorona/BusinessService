/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.modelos.dao.daohibernate;

import com.newsoft.tallerservicio.modelos.dao.interfaces.DAOPayments;
import com.newsoft.tallerservicio.modelos.dao.genericos.GenericHibernateDAO;
import com.newsoft.tallerservicio.entidades.Payments;

/**
 *
 * @author acorona
 */
public class DAOHibPayments extends GenericHibernateDAO<Payments, String> 
    implements DAOPayments{
    
}
