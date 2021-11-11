package com.mascotas.Controladores;
import com.mascotas.Entidades.Usuario;
import com.mascotas.Entidades.Zona;
import com.mascotas.Excepciones.ExcepcionServicio;
import com.mascotas.Repositorio.UsuarioRepositorio;
import com.mascotas.Repositorio.ZonaRepositorio;
import com.mascotas.Servicios.ServicioUsuario;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {
    @Autowired
    private UsuarioRepositorio uRep;
    @Autowired
    private ZonaRepositorio zRep;
    @Autowired
    private ServicioUsuario sUsuario;
    
    
    @GetMapping("/editar-perfil")
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    public String editarPerfil(HttpSession session , @RequestParam String id, ModelMap modelo) throws ExcepcionServicio{
        List<Zona> lista = zRep.findAll();
        modelo.put("zonas", lista);
      Usuario usuarioLogueado = (Usuario) session.getAttribute("usuariosession");
      if(usuarioLogueado == null || !usuarioLogueado.getId().equals(id) ){
        return "redirect:/inicio";
      }else{
            Usuario usuario = uRep.buscarPorId(id);
            modelo.addAttribute("perfil" , usuario);
        return "perfil.html";
    }
    }
    @PostMapping("/actualizar-perfil")
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    public String actualizar(ModelMap modelo, HttpSession session ,@RequestParam String id, MultipartFile archivo, @RequestParam String clave1, @RequestParam String clave2,@RequestParam String idZona) throws ExcepcionServicio, IOException{
       System.out.println("Id: " + id);
       System.out.println("clave ingresada: " + clave1);
       System.out.println("clave ingresada: " + clave2);

        try{
            Usuario usuarioLogueado = (Usuario) session.getAttribute("usuariosession");
            if(usuarioLogueado == null || !usuarioLogueado.getId().equals(id) ){
            return "redirect:/inicio";
            }else{
           
           
           Optional<Usuario> rta = uRep.findById(id);
            if(rta.isPresent()){
                Usuario usuarioModificar = rta.get();
                sUsuario.modificarUsuario(usuarioModificar.getId() , archivo , clave1 , clave2 , idZona);
                /*session.setAttribute("usuariosession" , usuarioModificar);*/
                return "redirect:/inicio";}//en caso de exito vuelvo  la edicion de perfil
           }
        }catch(ExcepcionServicio ex){
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
        }
        return "perfil.html";
}
}