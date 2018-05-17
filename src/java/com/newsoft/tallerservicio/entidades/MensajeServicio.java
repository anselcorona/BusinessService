/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newsoft.tallerservicio.entidades;

import java.util.Objects;

/**
 *
 * @author avasquez
 */
public class MensajeServicio {
    private int codigoError;
    private int codigoRetorno;
    private String mensaje;

    public MensajeServicio(int codigoRetorno, String mensaje) {
        this.codigoError = codigoRetorno;
        this.codigoRetorno = codigoRetorno;
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getCodigoRetorno() {
        return codigoRetorno;
    }

    public void setCodigoRetorno(int codigoRetorno) {
        this.codigoRetorno = codigoRetorno;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.codigoError);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MensajeServicio other = (MensajeServicio) obj;
        if (!Objects.equals(this.codigoError, other.codigoError)) {
            return false;
        }
        return true;
    }

    
}