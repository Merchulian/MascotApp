package com.mascotas.Controladores;
import com.mascotas.Entidades.Mascota;
import com.mascotas.Entidades.Usuario;
import com.mascotas.Enumeraciones.Sexo;
import com.mascotas.Enumeraciones.Tipo;
import com.mascotas.Excepciones.ExcepcionServicio;
import com.mascotas.Repositorio.MascotaRepositorio;
import com.mascotas.Repositorio.UsuarioRepositorio;
import com.mascotas.Servicios.ServicioMascota;
import java.io.IOException;
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

@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@Controller
@RequestMapping("/mascota")
public class MascotaControlador {
    @Autowired
    private UsuarioRepositorio uRep;

    @Autowired
    private ServicioMascota sMascota;
    
    @Autowired
    private MascotaRepositorio mRep;
    
    
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session , @RequestParam(required = false) String id, ModelMap modelo) throws ExcepcionServicio{
        
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuariosession");
        if(usuarioLogueado == null){
        return "redirect:/inicio";
        }
        Mascota mascota = new Mascota();
        if(id !=null && !id.isEmpty()){
            mascota = sMascota.buscarPorId(id);
        }
          
        modelo.addAttribute("perfil" , mascota);
        modelo.put("Sexo", Sexo.values());
        modelo.put("Tipo", Tipo.values());
        return "mascota.html";
        
    }
    
    
    @PostMapping("/actualizar-perfil")
    public String actualizar(HttpSession session, ModelMap modelo, @RequestParam(required = false) MultipartFile archivo ,@RequestParam String id /*id de la mascota*/, @RequestParam String nombre, @RequestParam Sexo sexo, @RequestParam Tipo tipo) throws ExcepcionServicio, IOException{
        String msg = null;
        Mascota mascota =new Mascota();        
        try{
            sMascota.validar(nombre);
            Usuario usuarioLogueado = (Usuario) session.getAttribute("usuariosession");
            if(usuarioLogueado == null){
                msg ="sesion caducada!";
                modelo.put("msg" , msg);
                return "redirect:/inicio?msg='fracaso'";
            }else{
                String idUsuario = usuarioLogueado.getId();
                if(id == null || id.isEmpty() ){
                    System.out.println("nombre: " + nombre);
                    sMascota.agregarMascota(idUsuario, nombre, archivo, sexo, tipo);
                    msg ="Tu mascota ha sido agregada!!";
                } else {
                sMascota.modificarMascota(id , idUsuario, nombre, archivo, sexo, tipo);
                msg ="'Tu mascota ha sido modificada!!'";
                }
                modelo.put("msg" , msg);
                return "redirect:/inicio?msg="+msg;
            }
            
            }catch(ExcepcionServicio ex){
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);   
            mascota.setSexo(sexo);
            mascota.setTipo(tipo);
            modelo.put("error", ex.getMessage());
            modelo.put("Sexo", Sexo.values());
            modelo.put("Tipo" , Tipo.values());
            modelo.put("nombre" , nombre);
            modelo.put("archivo" , archivo);
            modelo.put("perfil", mascota);
            System.out.println(session.getAttribute("usuariosession"));
            return "mascota.html";
        }
    
    }
}
