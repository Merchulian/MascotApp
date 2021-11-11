
package com.mascotas.Repositorio;

import com.mascotas.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    
    //metodo que devuelve un usuario por el email
    @Query("SELECT c FROM Usuario c WHERE c.email = :email")
    public Usuario buscarPorMail(@Param("email") String email);
    
    @Query("SELECT c FROM Usuario c WHERE c.id = :id")
    public Usuario buscarPorId(@Param("id") String id);
    
}
