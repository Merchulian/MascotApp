package com.mascotas.Entidades;

import com.mascotas.Enumeraciones.Rol;
import static com.mascotas.Enumeraciones.Rol.USER;
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
public class Usuario {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid" , strategy ="uuid2")    
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String clave;
    private Rol rol;

    @Temporal(TemporalType.TIMESTAMP)
    private Date alta;
    @Temporal(TemporalType.TIMESTAMP)
    private Date baja;
    
    @OneToOne  //cambiar
    private Zona zonaUsuario;
    
    @OneToOne
    private Foto fotoUsuario;

    public Usuario() {
        this.rol = USER;
    }
  
    public Usuario(String id, String nombre, String apellido, String email, String clave, Rol rol, Date alta, Date baja, Zona zonaUsuario, Foto fotoUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.clave = clave;
        this.rol = USER;
        this.alta = alta;
        this.baja = baja;
        this.zonaUsuario = zonaUsuario;
        this.fotoUsuario = fotoUsuario;
    }
   
// Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }


    public String getApellido() {
        return apellido;
    }


    public String getEmail() {
        return email;
    }


    public String getClave() {
        return clave;
    }

    public Date getAlta() {
        return alta;
    }


    public Date getBaja() {
        return baja;
    }
    
    public Zona getZonaUsuario() {
        return zonaUsuario;
    }
    
    public Rol getRol() {
        return rol;
    }
    
    public Foto getFotoUsuario() {
        return fotoUsuario;
    }

//  Setters
    public void setId(String id) {
        this.id = id;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public void setClave(String clave) {
        this.clave = clave;
    }


    public void setAlta(Date alta) {
        this.alta = alta;
    }


    public void setBaja(Date baja) {
        this.baja = baja;
    }

    public void setZonaUsuario(Zona zonaUsuario) {
        this.zonaUsuario = zonaUsuario;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    public void setFotoUsuario(Foto fotoUsuario) {
        this.fotoUsuario = fotoUsuario ;
    }    

}
