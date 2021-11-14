package com.mascotas.Servicios;

import com.mascotas.Excepciones.ExcepcionServicio;
import com.mascotas.Entidades.Foto;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mascotas.Repositorio.FotoRepositorio;
import com.mascotas.Repositorio.UsuarioRepositorio;

@Service
public class ServicioFoto {
    @Autowired
    private FotoRepositorio fRep;
    
    @Autowired
    private UsuarioRepositorio uRep;
    
      
    public Foto convertirArchivoAFoto(MultipartFile archivo) throws ExcepcionServicio, IOException{
        /*    Este metodo se encarga de subir un archivo nuevo desde el formulario front
        id = "subir-foto de la vista user-info.html"          */
        
        
        if(archivo != null && !archivo.isEmpty()){   //archivo hace referencia al archivo que estoy subiendo
            try{
            Foto foto = new Foto();
            foto.setMime(archivo.getContentType());   // determino el tipo de archivo de la foto
            foto.setNombre(archivo.getName());
            foto.setContenido(archivo.getBytes());
            return fRep.save(foto);
            }catch(IOException e){throw new ExcepcionServicio("No se ha podido cargar el archivo");} 
        }else{return null;}
    }
}
