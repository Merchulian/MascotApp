
package com.mascotas.Configuracion;

import com.mascotas.Servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration

public class Config extends WebSecurityConfigurerAdapter{
    @Autowired
    private ServicioUsuario sUsuario;
    @Autowired
    public void ConfigureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(sUsuario)   //servicio utilizado para autentificar usuario
                .passwordEncoder(new BCryptPasswordEncoder());  // servicio utilizado para encriptar
                
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/css/*", "/js/*", "/img/*", "/**") //todo lo que este en esta carpeta puede ser accedido x cualquier usuario, este logueado o no
                .permitAll()
                .and().formLogin() //indicamos detalles del login
                    .loginPage("/login")  // vista donde se encuentra el formulario de login
                    //.csrf()
                    //.disable(), y todo lo demas se comenta
                    .loginProcessingUrl("/logincheck")  // la url con la que vamos a validar el ingreso
                                                    // deberemos hacer un metodo controlador @PostMapping
                    .usernameParameter("username")  //como se denomina el campo con el nombre de usuario    
                    .passwordParameter("password")  //como se denomina el campo con la contraseña    
                    .defaultSuccessUrl("/inicio")   //si el loginm tiene exito adonde se redirige
                    .permitAll()
                .and().logout()                     //similar protocolo para el logout
                        .logoutUrl("/logout")       // la url para la accion de cerrar la sesion
                        .logoutSuccessUrl("/login?logout")      // redireccion en caso de logout exitoso
                        .permitAll()
                .and().csrf() // esto no permitia que me loguee guau!!!!!!
                .disable();
                     
                                
    }

}
