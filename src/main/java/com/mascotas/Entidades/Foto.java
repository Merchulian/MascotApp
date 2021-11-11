package com.mascotas.Entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Foto {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid" , strategy ="uuid2")  
    private String id;
    private String nombre;
    private String mime;            // guarda el tipo de archivo de la foto
    
    // fetch marca como debera ser la carga de la foto (carga temprana FetchType.EAGER
    // o carga peresoza (FetchType.LAZY) si no queremos que la imagen pesada se 
    //termine de cargar antes de mostrar el contenido
    @Lob     @Basic(fetch = FetchType.LAZY)  // tipo que permite persistir un archivo grande
    private byte[] contenido;       // gfuarda el contenido de la fotografia
    
    // Constructors

    public Foto() {
    }

    public Foto(String id, String nombre, String mime, byte[] contenido) {
        this.id = id;
        this.nombre = nombre;
        this.mime = mime;
        this.contenido = contenido;
    }
    
    // Getters

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMime() {
        return mime;
    }

    public byte[] getContenido() {
        return contenido;
    }

    // Setters
    
    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }
    
    
    
    
    
}
