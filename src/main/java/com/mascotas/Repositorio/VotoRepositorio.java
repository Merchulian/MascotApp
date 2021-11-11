
package com.mascotas.Repositorio;

import com.mascotas.Entidades.Voto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepositorio extends JpaRepository<Voto , String> {

    @Query("SELECT c FROM Voto c WHERE c.mascota1.id = :id ORDER BY c.voto DESC")
    public List<Voto> mostrarMisVotos(@Param ("id") String id);
    
    @Query("SELECT c FROM Voto c WHERE c.mascota2.id = :id ORDER BY c.voto DESC")
    public List<Voto> mostrarVotosRecibidos(@Param ("id") String id);
}
