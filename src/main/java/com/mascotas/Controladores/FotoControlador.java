/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mascotas.Controladores;

import com.mascotas.Entidades.Usuario;
import com.mascotas.Excepciones.ExcepcionServicio;
import com.mascotas.Servicios.ServicioUsuario;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.servlet.function.RequestPredicates.headers;

@Controller
@RequestMapping("/Foto")
public class FotoControlador {
    @Autowired
    private ServicioUsuario sUsuario;
    
    @GetMapping("/usuario")
    public ResponseEntity<byte[]> fotousuario(String id) throws ExcepcionServicio{
    try{
        Usuario usuario = sUsuario.buscarPorId(id);
        byte[] foto = usuario.getFotoUsuario().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(foto , headers , HttpStatus.OK);
    
    
    }catch(ExcepcionServicio ex){
    Logger.getLogger( FotoControlador.class.getName() ).log(Level.SEVERE, null , ex);}
    
    }
    






}
