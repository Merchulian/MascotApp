/*repository: Es el package que contiene las interfaces que extienden de JPA 
para que estas clases se conecten a la base de datos. Estas gestionan 
información ya sea de buscar, borrar, actualizar o crear un registro en la 
base de datos.*/
package com.mascotas.Repositorio;
import com.mascotas.Entidades.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MascotaRepositorio extends JpaRepository<Mascota, String> {
    
}
