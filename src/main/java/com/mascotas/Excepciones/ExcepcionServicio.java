/*
generamos nuestras nuevas excepciones
 */

package com.mascotas.Excepciones;


public class ExcepcionServicio extends Exception{
    
/* declaro un constructor comun y silvestre que tire el mensaje que nosotros le 
indicamos. Para diferenciar los errores que surgen de la propia logica del negocio
de aquellos errores correswpondientes a JAVA*/
    public ExcepcionServicio(String msn){
     super(msn);    
    }

}
