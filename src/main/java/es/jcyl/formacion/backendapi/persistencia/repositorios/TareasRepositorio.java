package es.jcyl.formacion.backendapi.persistencia.repositorios;

import es.jcyl.formacion.backendapi.persistencia.entidades.Tarea;
import es.jcyl.formacion.backendapi.persistencia.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TareasRepositorio extends JpaRepository<Tarea,Integer> {

  /** lista las tareas que pertenecen a un usuario */
  List<Tarea> findByUsuario(Usuario usuario);

  /** lista de tareas cuyo estado esta por encima del 90% */
  @Query("SELECT t FROM Tarea t WHERE t.estado > 90")
  List<Tarea> encuentraTareasCuyoEstadoEsSuperiorAl90Porciento();

}
