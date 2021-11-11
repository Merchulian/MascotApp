package com.mascotas.Servicios;

import com.mascotas.Excepciones.ExcepcionServicio;
import com.mascotas.Entidades.Foto;
import com.mascotas.Entidades.Usuario;
import com.mascotas.Entidades.Zona;
import com.mascotas.Repositorio.FotoRepositorio;
import com.mascotas.Repositorio.UsuarioRepositorio;
import com.mascotas.Repositorio.ZonaRepositorio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ServicioUsuario implements UserDetailsService{
    @Autowired   //indica que el servidor de aplicaciones inicialice esta variable
    private UsuarioRepositorio uRep;
    @Autowired
    private FotoRepositorio fRep;
    @Autowired
    private ZonaRepositorio zRep;
    @Autowired
    private ServicioFoto sFoto;
    
    //                    ====    REGISTRO DE NUEVOS USUARIOS    ====
    @Transactional
    public void registrarUsuario(MultipartFile archivo, String nombre, String apellido, String mail , String clave1 , String clave2, String idZona) throws ExcepcionServicio, IOException{
    /* Metodo que recibe la info ingresada desde el formulario id ="nuevo-usuario"
    estos datos ya deben venir validados desde el front:
        campos no nulos
        clave de 8 caracteres o mas
        clave con al menos una mayuscula, un numero y un caracter especial    */   
    try{
        /* No hace falta bloque try - catch si validar() falla todo el metodo falla */
        validar(nombre, apellido, mail, clave1, clave2, idZona);  
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellido(apellido);
        nuevoUsuario.setEmail(mail);
        // ingreso de la foto de perfil:
        if(archivo !=null){
        Foto foto = sFoto.convertirArchivoAFoto(archivo);
        nuevoUsuario.setFotoUsuario(foto);}
        else{nuevoUsuario.setFotoUsuario(null);}
        //Compruebo que el id de la Zona sea valido
        System.out.println("buscando zona");
        Optional<Zona> rta = zRep.findById(idZona);    // buscar la zona a la que corresponde el id
        if(rta.isPresent()){
            
            Zona zona = rta.get();
            nuevoUsuario.setZonaUsuario(zona);}
        else{
            nuevoUsuario.setZonaUsuario(null);
            throw new ExcepcionServicio("Error interno, la zona no esta disponible.");}
        // habiendo indicado en el main el encriptador y el servicio que lo utiliza
        // aqui persisto la clave encriptada
            String claveEncrypt = new BCryptPasswordEncoder().encode(clave1);
            nuevoUsuario.setClave(claveEncrypt);    
            nuevoUsuario.setAlta(new Date());
    // utilizo este metodo .save para mandarlo a la base de datos
        uRep.save(nuevoUsuario);
    }catch(ExcepcionServicio ex){throw new ExcepcionServicio("No se han cargado los datos. Vuelva a reintentar");}
    
    }
   // ====================     MODIFICACION DE DATOS    ========================
    @Transactional
    public void modificarClave(String id, String nuevaClave) throws ExcepcionServicio{
        //busco el usuario por id en la DB:
        if(nuevaClave == null || nuevaClave.isEmpty()){
            throw new ExcepcionServicio("Debe ingresar una clave nueva");
        }
        else{
            // esto es para testear si el usuario existe en la DB
            Optional<Usuario> respuesta = uRep.findById(id);   
            
            // si al consultar la DB hay un output para el id consultado:
            if(respuesta.isPresent()){   
                Usuario usuario = respuesta.get();
                
                String nuevaClaveEncrypt = new BCryptPasswordEncoder().encode(nuevaClave);
                usuario.setClave(nuevaClaveEncrypt);
                
                uRep.save(usuario);}
            else{ throw new ExcepcionServicio("No existe el usuario.");}
        }
    }   
    //                ====    deshabilitar un usuario    ====
    @Transactional
    public void deshabilitarUsuario(String id) throws ExcepcionServicio{
        if(id == null || id.isEmpty()){
            throw new ExcepcionServicio("El id ingresado es nulo.");
        }
        else{
            Optional<Usuario> respuesta = uRep.findById(id);   
            
            if(respuesta.isPresent()){   
                Usuario usuario = respuesta.get();
                usuario.setBaja(new Date());
                uRep.save(usuario);}
            else{ throw new ExcepcionServicio("No existe un usuario con esa id");}
        }    
    }    
//                   ====    Rehabilitar un usuario    ====
    
    @Transactional
    public void rehabilitarUsuario(String id) throws ExcepcionServicio{
    /* Este servicio se lanza desde el log in. En el metodo inicioSesion() de la clase 
    ServicioSesion, si la Baja != null, entonces inicia sesion y dispara este metodo */   
        if(id == null || id.isEmpty()){
            throw new ExcepcionServicio("El id ingresado es nulo.");
        }
        else{ 
            Optional<Usuario> respuesta = uRep.findById(id);   
            
            if(respuesta.isPresent()){
                Usuario usuario = respuesta.get();
                if(usuario.getBaja() == null){
                    throw new ExcepcionServicio(" El usuario seleccionado esta activo!");
                }
                else{
                    /* Convertir esto en un alert()*/
                    System.out.println("Bienvenido nuevamente. >La cuenta se habia dado de baja desde : " 
                            + usuario.getBaja() + "Ya se encuentra habilitada nuevamente." );
                    usuario.setBaja(null);
                    uRep.save(usuario);}  //no modifico el alta del usuario
                                            // solamente mando el baja de nuevo a valor null
                }
            else{ throw new ExcepcionServicio("No existe un usuario con esa id");}
        }    
    }
        
//                   ====    SUBIR/ MODIFICAR FOTO DE PERFIL    ====
    @Transactional
    public void actualizarFotoPerfil(MultipartFile archivo, String idDestino) throws ExcepcionServicio, IOException{
        /* este metodo se encarga de asignar un objeto Foto a un usuario 
        a traves de un id de destino(que rescato segun el objeto Sesion abierta).
        Este metodo llama al servicio subirFoto() de ServicioFoto para subir un archivo
        y cargar sus datos como objeto JAVA. Este metodo se encarga de  persistir 
        la foto*/
        Foto nuevaFoto = sFoto.convertirArchivoAFoto(archivo);
        if(nuevaFoto != null){
            Optional<Usuario> consulta = uRep.findById(idDestino);  //busco usuario
            if(consulta.isPresent()){
                Usuario usuario = consulta.get();
                usuario.setFotoUsuario(nuevaFoto);
                uRep.save(usuario);
            }else{             // si subirFoto() retorno null
            throw new ExcepcionServicio("La accion no se pudo completar!");
            }
        }
    }
    
//                    ====    REGISTRO DE NUEVOS USUARIOS    ====
    @Transactional
    public void modificarUsuario(String id , MultipartFile archivo, String clave1 , String clave2, String idZona) throws ExcepcionServicio, IOException{
    /* MSolo se puede modificar la zona y la contraseña. nombre de usuario, apellido y correo son
        inamovibles*/   

        try{
        Optional<Usuario> query = uRep.findById(id);
        Usuario usuarioModificar = query.get();
            System.out.println("mime foto actual: " + usuarioModificar.getFotoUsuario().getMime());
        // ingreso de la foto de perfil:
        if(archivo !=null || archivo.isEmpty()){
        Foto foto = sFoto.convertirArchivoAFoto(archivo);
            System.out.println(foto.getMime());
            if( !foto.getMime().equals("application/octet-stream") ){
                usuarioModificar.setFotoUsuario(foto);
                System.out.println("foto modificada");
            }else{System.out.println("foto NO modificada");}
        }
        // modifico zona:
        Optional<Zona> rta = zRep.findById(idZona);    // buscar la zona a la que corresponde el id
        if(rta.isPresent()){
            Zona zona = rta.get();
            usuarioModificar.setZonaUsuario(zona);
        }
        // cambio de clave:
            System.out.println("clave actual: " + clave1);
            System.out.println(clave2);
        if(clave1.length()>=8){
            if( clave1.equals(clave2) ){
            String claveEncrypt = new BCryptPasswordEncoder().encode(clave1);
            usuarioModificar.setClave(claveEncrypt);
                System.out.println("clave actualizada!");
            }
        }
        uRep.save(usuarioModificar);
    // utilizo este metodo .save para mandarlo a la base de datos
    }catch(ExcepcionServicio ex){
        throw new ExcepcionServicio("No se han cargado los datos. Vuelva a reintentar")
            ;}
    
    }    
    
    
 //                          ====    VALIDAR DATOS    ====
    
    public void validar(String nombre, String apellido, String mail, String clave1, String clave2 , String idZona) throws ExcepcionServicio{
        if(nombre == null || nombre.isEmpty()){
            throw new ExcepcionServicio("El nombre de usuario no puede ser nulo.");
        }
        
        if(apellido == null || apellido.isEmpty()){
            throw new ExcepcionServicio("El apellido de usuario no puede ser nulo.");
        }
        // doble validacion por mail: No se pueden anotar dos o mas usuarios con un correo
        Usuario query = uRep.buscarPorMail(mail);
        if(query != null){ throw new ExcepcionServicio("Ese correo ya se encuentra en uso!"); }
        
        if(mail == null || mail.isEmpty()){throw new ExcepcionServicio("El correo no puede ser nulo.");}
        // Validacion de las claves, por longitud y ver si son iguales
        if(clave1 == null || clave1.isEmpty() || clave1.length() <8 ){throw new ExcepcionServicio("La clave debe tener 8 o mas caracteres.");}
        if(!clave1.equals(clave2)){throw new ExcepcionServicio("Las claves no coinciden");}
        // validacion de la zona:
        if(idZona == null){throw new ExcepcionServicio("No se ha seleccionado ninguna Zona");}
    }


//      ======================== METODOS DE UserDetailsService ========================

    @Override
    public UserDetails loadUserByUsername(String usuarioEmail) throws UsernameNotFoundException {
        Usuario consulta = uRep.buscarPorMail(usuarioEmail); // busco si existe el usuario
        if(consulta != null){
            
        // defino una lista de los permisos que tiene este usuario:     
        
        List<GrantedAuthority> permisos = new ArrayList<>();
        
        GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
        permisos.add(p1);
        // con esto guardo los atributos del usuario, guardando los datos del request http
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        // guardo los datos del inicio de session 
        session.setAttribute("usuariosession", consulta);
        
        
        
        
        /*GrantedAuthority p2 = new SimpleGrantedAuthority("MODULO_MASCOTAS");
        permisos.add(p2);
        GrantedAuthority p3 = new SimpleGrantedAuthority("MODULO_VOTOS");
        permisos.add(p3);*/
        // fin lista permisos
        // La clase User es clase reservada de Spring Security:
            User user = new User(consulta.getEmail(), consulta.getClave(), permisos);
            return user;
        }else{
            return null;}   
    }

    
}
