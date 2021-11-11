
package com.mascotas.Entidades;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Voto {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid" , strategy ="uuid2")
    private String id;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date voto;
    @Temporal(TemporalType.TIMESTAMP)
    private Date respuesta;
    /* que recibe el voto, una mascota puede tener varios votos*/
    
    @ManyToOne                   
    private Mascota mascota1;
    
    /* respuesta al voto, una mascota puede tener varios respuesta*/
    @ManyToOne
    private Mascota mascota2;
    
    private Boolean esValido;    // indica si aun el voto es valido, por si algun despechado
                                // retira su voto     
    //constructores

    public Voto() {
        this.esValido = true;
    }

    public Voto(String id, Date voto, Date respuesta, Mascota mascota1, Mascota mascota2) {
        this.id = id;
        this.voto = voto;
        this.respuesta = respuesta;
        this.mascota1 = mascota1;
        this.mascota2 = mascota2;
        this.esValido =true;
    }
    
    // Getters

    public String getId() {
        return id;
    }

    public Date getVoto() {
        return voto;
    }

    public Date getRespuesta() {
        return respuesta;
    }

    public Mascota getMascota1() {
        return mascota1;
    }

    public Mascota getMascota2() {
        return mascota2;
    }
    public Boolean getEsValido() {
        return esValido;
    }
    
    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setVoto(Date voto) {
        this.voto = voto;
    }

    public void setRespuesta(Date respuesta) {
        this.respuesta = respuesta;
    }

    /**
     * @param mascota1 the mascota1 to set
     */
    public void setMascota1(Mascota mascota1) {
        this.mascota1 = mascota1;
    }

    public void setMascota2(Mascota mascota2) {
        this.mascota2 = mascota2;
    }

    public void setEsValido(Boolean esValido) {
        this.esValido = esValido;
    }
 
}
