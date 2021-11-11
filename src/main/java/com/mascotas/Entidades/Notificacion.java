/*
Cada notificacion tiene un unico destinatario, pero un destinatario puede poseer muchas notificaciones
 */

package com.mascotas.Entidades;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Notificacion {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid" , strategy ="uuid2")
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @OneToOne     //cambiar
    private Usuario destinatario;
    private String contenido;
    
    // comnstructores

    public Notificacion() {
    }

    public Notificacion(String id, Date fecha, Usuario destinatario, String contenido) {
        this.id = id;
        this.fecha = fecha;
        this.destinatario = destinatario;
        this.contenido = contenido;
    }
    
    // Getters

    public String getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public String getContenido() {
        return contenido;
    }

    
 //   Setters:
    
    public void setId(String id) {
        this.id = id;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
