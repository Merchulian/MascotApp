/* PONER modelo.put() para los datos que faltan (zona y foto.
ver porque desaparecen las zona cuando se recarga la pagina en caso de error*/
package com.mascotas.Controladores;

import com.mascotas.Entidades.Zona;
import com.mascotas.Excepciones.ExcepcionServicio;
import com.mascotas.Repositorio.ZonaRepositorio;
import com.mascotas.Servicios.ServicioUsuario;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PortalControlador {
    @Autowired
    private ServicioUsuario sUsuario;
    @Autowired
    private ZonaRepositorio zRep;
       
    @GetMapping("/")
    public String inicio(){
    return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false)  String error, @RequestParam(required = false)  String logout, ModelMap modelo){
        if(error !=null){
        modelo.put("error", "Usuario o clave incorrectos.");
        
        }
        /* En vez de generar una accion del controlador en caso de logour, genero aca
        mismo el comportamiento esperado al realizar la accion de desloguearse*/
        if(logout != null){
        modelo.put("logout" , "Se ha deslogueado correctamente");
        }
    return "login.html";
    }
        /*Esta anotacion impide el acceso libre a la url /login. para póder acceder es necesario
    que se asigne el ROLE_USUARIO_REGISTRADO. esto lo configuramos en el metodo
    de Springboot loadUserByUsername() que sebreescribimos en ServicioUsuario.
    En general,  todo aquel que posea este rol podra acceder a los metodos pre autorizados con esta anotacion*/
    
    @GetMapping("/inicio")
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    public String pantallaInicio(ModelMap modelo, @RequestParam(required = false) String msg){
        modelo.put("msg",msg);
        
        return "inicio.html";
    }
    
    
//                   ====    PAGINA DE REGISTRO DE NUEVOS USUARIOS    ====
    @GetMapping("/registro")
    public String registro(ModelMap modelo){
        /* para rellenar el select con todas las zonas posibles buscamos todas
        las zonas guardades en el repositorio  y hacemos una lista, luego con el ModelMap
        mandamos esa informacion que sera mostrada al usuario en el menu desplegablede zonas
        dentro del formulario de registro*/
        List<Zona> lista = zRep.findAll();
        modelo.put("zonas", lista);
    return "registro.html";
    }
    
//                   ====    procesamiento de datops de registro   ==== 
    @PostMapping("/registrar")
    public String registrar(ModelMap modelo ,MultipartFile archivo, @RequestParam String nombre,@RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2,@RequestParam String idZona) throws ExcepcionServicio, IOException{
        
        try{
        sUsuario.registrarUsuario(archivo , nombre, apellido, mail , clave1 , clave2 , idZona);
        return "exito.html";    //en caso de exito vuelvo al index
        }catch(ExcepcionServicio ex){
            // la siguiente linea imprime en consola el error que ocurrio
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            /* a continuacion vamos a reenviar los datos ingresados por el usuario
             para rellenar los campos que estaban bin y que solo se modifiquen los
            que quedaron nuos o vacios.*/
            modelo.put("nombre", nombre);
            modelo.put("apellido" , apellido);
            modelo.put("mail" , mail);
            modelo.put("clave1" , clave1);
            modelo.put("clave2" , clave2);
            //vuelvo a poner que aparezcan las zonas
            List<Zona> lista = zRep.findAll();
            modelo.put("zonas", lista);
            /*En el registro debo incorporar las anotaciones th para que esto que mando
            se muestre en el campo correspondiente*/
        return "registro.html";}
        }

    

}
