package com.mascotas.Servicios;

import com.mascotas.Excepciones.ExcepcionServicio;
import com.mascotas.Entidades.Mascota;
import com.mascotas.Entidades.Voto;
import com.mascotas.Repositorio.MascotaRepositorio;
import com.mascotas.Repositorio.VotoRepositorio;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioVoto {
    @Autowired
    private MascotaRepositorio mRep;
    @Autowired
    private VotoRepositorio vRep;
    @Transactional
    public void darVoto(String usuarioId, String idMascotaPropia, String idMascotaVotada) throws ExcepcionServicio{
        Voto voto = new Voto();
        voto.setVoto(new Date());
        // si existe la mascota a votar entonces doy el voto:
        Optional<Mascota> respuesta = mRep.findById("idMascotaPropia");
        if(respuesta.isPresent()){
            Mascota mascota1 = respuesta.get();
            if(mascota1.getBaja() != null){   // si la mascota no esta dada de baja!!
            // si el usuario es dueño de la mascota, se realiza el cambio...
                if(mascota1.getUsuario().getId().equals(usuarioId)){
                voto.setMascota1(mascota1);             
                }else{ // si no soy el dueño
                 throw new ExcepcionServicio("No tienes los permisos necesarios!");   
                }
            }else{ // si la mascota esta dada de baja
                throw new ExcepcionServicio("La mascota esta dada de baja");
                
            }
        }else{ // si el id no valido
            throw new ExcepcionServicio("no existe una masctoa con ese id!!");}
    // segunda parte: registro la mascota votada:
        Optional<Mascota> mascotaVotada = mRep.findById("idMascotaVotada");
        if(mascotaVotada.isPresent()){
        Mascota mascota2 = mascotaVotada.get();
            if(mascota2.getBaja() != null){
                voto.setMascota2(mascota2);
            }else{  // mascota2 dada de baja
                throw new ExcepcionServicio(" La mascota no puede ser votada");
            }
        }else{   // id invalido
            throw new ExcepcionServicio("El id no pertenece a ninguna mascota");
        }
        if(idMascotaPropia.equals(idMascotaVotada)){
            throw new ExcepcionServicio("voto no valido (autovoto)");
        }else{
            vRep.save(voto);
        }

    } //fin darVoto()
    //                ====    RESPONDER VOTO    ====
    @Transactional
    public void recibirVoto(String idVoto){
        
        Optional<Voto> respuesta = vRep.findById("idVoto");
        if(respuesta.isPresent()){
            Voto aResponder = respuesta.get();
            if(aResponder.getRespuesta() !=null){
                aResponder.setRespuesta(new Date());
                vRep.save(aResponder);
            }
        }
    }
    /*@Transactional
    public void anularVoto(String idVoto){}
    */
}
