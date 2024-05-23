package es.jcyl.formacion.backendapi.controladores;


import es.jcyl.formacion.backendapi.servicios.TareaServicio;
import es.jcyl.formacion.backendapi.modelos.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tareas") // poner el recuros tareas
@RequiredArgsConstructor
public class TareaControlador {

    //inyectar el servicio: TareaServicio servicio;
    private final TareaServicio servicio;

    @PostMapping(consumes = {} ,produces = {})
    public ResponseEntity<TareaModelo>  nuevaTarea (
            @RequestBody TareaModelo modelo
            ) {
        // recoger del body un obejto valido TareaModelo modelo
        // llamar al servicio para crear nueva tarea
        return ResponseEntity.ok(servicio.crearTarea(modelo));

    }

    @GetMapping()
    public ResponseEntity<List<TareaModelo>> listadoTareas (
            @RequestParam("correo") String correo
            ) {
        // recoger el parametro correo: String correo
        // llamar al servicio para recuperar datos
        return ResponseEntity.ok(servicio.obtenerTareas(correo));
    }

    @PutMapping()
    public ResponseEntity<TareaModelo> editarTarea (
            @Valid @RequestBody TareaModelo modelo) {
        return ResponseEntity.ok( servicio.modificarTarea( modelo ));
    }

    @DeleteMapping()
    public Integer deleteTodo(@RequestParam("tareaId") Integer id) {
        return servicio.borrarTarea( id );
    }

}
