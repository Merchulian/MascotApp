package com.mascotas.Servicios;
import com.mascotas.Excepciones.ExcepcionServicio;
import com.mascotas.Entidades.Foto;
import com.mascotas.Entidades.Mascota;
import com.mascotas.Entidades.Usuario;
import com.mascotas.Enumeraciones.Sexo;
import com.mascotas.Enumeraciones.Tipo;
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


// ================================================================================================      
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
 
//                                     ====     VALIDAR     =====
    
    public void validar(String nombre) throws ExcepcionServicio{
    if(nombre.isEmpty()){
    throw new ExcepcionServicio("Nombre mascota nulo o vacio.");   
    }else{
        System.out.println("El nombre de la mascota es: " + nombre);
    }
}
    
//                                   ====    AGREGAR MASCOTA    ====
    @Transactional
    public void agregarMascota(String idUsuario, String nombre, MultipartFile archivo,Sexo sexo,Tipo tipo ) throws ExcepcionServicio, IOException{
       Mascota mascota = new Mascota();
        try{          
            Usuario usuario = uRep.findById(idUsuario).get();
            System.out.println(usuario.toString());
            mascota.setNombre(nombre);
            mascota.setSexo(sexo);
            mascota.setTipo(tipo);
            mascota.setUsuario(usuario);
            if(archivo != null || !archivo.isEmpty()){
                Foto foto = sFoto.convertirArchivoAFoto(archivo);
                mascota.setFotoMascota(foto);
            }
            mRep.save(mascota);
        }catch(ExcepcionServicio e){
            e.getMessage();    
        }
    }
//                                   ====     MODIFICAR MASCOTA     =====    
    @Transactional
    public void modificarMascota( String id ,String idUsuario,String nombre,MultipartFile archivo,Sexo sexo, Tipo tipo) throws ExcepcionServicio{
        Mascota mascota = new Mascota();
        // busco si la mascota esta en la db, si es asi la modifico, sino la creo
        Optional<Mascota> response = mRep.findById(id);
            if(response.isPresent()){
                mascota = response.get();    
            }else{
            Usuario usuario = uRep.findById(idUsuario).get();
            mascota.setUsuario(usuario);
            }
            if(mascota.getUsuario().getId().equals(idUsuario)){
                mascota.setNombre(nombre);
                mascota.setSexo(sexo);
                mascota.setTipo(tipo);
                mRep.save(mascota);
            }else{
            throw new ExcepcionServicio("no tiene permisos para hacer esa modificación");
        }
        
    
    }

//                                     ====     BUSCAR POR ID    =====

    @Transactional
    public Mascota buscarPorId(String id){
        Mascota mascota = null;
        Optional<Mascota> consulta = mRep.findById(id);
        if(consulta.isPresent()){
        mascota = consulta.get();
        }
    return mascota;
    }

}