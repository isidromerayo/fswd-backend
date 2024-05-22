package es.jcyl.formacion.backendapi.persistencia.repositorios;

import es.jcyl.formacion.backendapi.persistencia.entidades.Rol;
import es.jcyl.formacion.backendapi.persistencia.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuariosRepositorio extends JpaRepository<Usuario,Integer> {


    /* buscar un usuario por el correo*/
    Optional<Usuario> findByCorreo(String correo);

    /* Buscar ususario por correo y clave */
    Optional<Usuario> findByCorreoAndAndClave(String correo, String clave);

    /* ver si un usuario con un correo existe */
    Optional<Usuario> findByCorreoIs(String correo);

    /* ver si un usuario tiene un rol */
    List<Usuario> findByRolesIs(Rol role);

    /* listado de los usuarios que tienen el rol de administrador */
    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.nombre='ADMINISTRADOR'")
    List<Usuario> listadoAdministradores ();



}
