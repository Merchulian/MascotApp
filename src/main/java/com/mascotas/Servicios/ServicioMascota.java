package com.mascotas.Servicios;

import com.mascotas.Excepciones.ExcepcionServicio;
import com.mascotas.Entidades.Foto;
import com.mascotas.Entidades.Mascota;
import com.mascotas.Entidades.Usuario;
import com.mascotas.Enumeraciones.Sexo;
import com.mascotas.Repositorio.FotoRepositorio;
import com.mascotas.Repositorio.MascotaRepositorio;
import com.mascotas.Repositorio.UsuarioRepositorio;
import java.io.IOException;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ServicioMascota {
    @Autowired
    private UsuarioRepositorio uRep;
    @Autowired
    private FotoRepositorio fRep;
     @Autowired
    private MascotaRepositorio mRep;
    @Autowired
    private ServicioFoto sFoto;
    
    @Transactional        
    public void agregarMascota(String idUsuario, String nombreMascota, Sexo sexoMascota) throws ExcepcionServicio{
        //busco usuario por id
        Optional<Usuario> response = uRep.findById(idUsuario);
        // valido los datos de la mascota y el sexo (la parte de no nulos y que el sexo sea
        // valor enumerado lo trabajo desde el front
        if(response.isPresent()){
            Usuario usuario = response.get();
            Mascota nuevaMascota = new Mascota(nombreMascota, usuario , sexoMascota);
            mRep.save(nuevaMascota);
        }
        else{ throw new ExcepcionServicio("No se ha encontrado el usuario");}    
    }
    
    /*@Transactional
    public void eliminarMascota(String idMascota){}*/
    
    @Transactional
    public void actualizarFotoMascota(MultipartFile archivo, String idDestino) throws ExcepcionServicio, IOException{
    /* este metodo se encarga de asignar un objeto Foto a un usuario 
    a traves de un id de destino(que rescato segun el objeto Sesion abierta).
    Este metodo llama al servicio subirFoto() de ServicioFoto para subir un archivo
    y cargar sus datos como objeto JAVA. Este metodo se encarga de  persistir 
    la foto*/
        Foto nuevaFoto = sFoto.convertirArchivoAFoto(archivo);
        if(nuevaFoto != null){
            Optional<Mascota> consulta = mRep.findById(idDestino);  //busco mascota
            if(consulta.isPresent()){
                Mascota mascota = consulta.get();
                mascota.setFotoMascota(nuevaFoto);
                mRep.save(mascota);
            }else{             // si subirFoto() retorno null
            throw new ExcepcionServicio("La accion no se pudo completar!");
            }
        }
    }
    
}
