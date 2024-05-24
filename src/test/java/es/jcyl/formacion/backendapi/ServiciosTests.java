package es.jcyl.formacion.backendapi;

import es.jcyl.formacion.backendapi.modelos.TareaModelo;
import es.jcyl.formacion.backendapi.persistencia.entidades.Tarea;
import es.jcyl.formacion.backendapi.persistencia.entidades.Usuario;
import es.jcyl.formacion.backendapi.persistencia.repositorios.*;

import es.jcyl.formacion.backendapi.servicios.TareaMapeo;
import es.jcyl.formacion.backendapi.servicios.TareaServicio;
import es.jcyl.formacion.backendapi.servicios.TareaServicioImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ServiciosTests {

    // inyectar
    @Autowired
    private TareasRepositorio tareasRepo;

    // inyectar
    @Autowired
    private UsuariosRepositorio usuariosRepo;

    // inyectar
    @Autowired
    private TareaMapeo mapeo;

    private TareaServicio servicio;


    private Usuario usuarioTest;


    @BeforeEach
    void setUp() {
        servicio = new TareaServicioImpl( tareasRepo,usuariosRepo,mapeo );

        if ( usuariosRepo.findByCorreo("fede.valverde@fmail.com").isEmpty()) {
            Usuario usuario = Usuario.builder()
                    .nombreCompleto("Fede Valverde")
                    .iniciales("FV")
                    .correo("fede.valverde@fmail.com")
                    .clave("Password1234#")
                    .build();
            usuarioTest = usuariosRepo.save(usuario);
        }

    }

    @Test
    @Order(1)
    public void deberiaCrearTarea() {

        Usuario result = usuariosRepo.findByCorreo("fede.valverde@fmail.com")
                .orElse(null);

        // TODO: se crea la tarea
        // TODO: el correo corresponde al usuario
        Assertions.assertEquals("fede.valverde@fmail.com",result.getCorreo());

        TareaModelo miTarea = TareaModelo.builder()
                .nombre("Primera Tarea")
                .estado(0)
                .color("green-500")
                .usuarioCorreo("fede.valverde@fmail.com")
                .build();

        TareaModelo tareaResult = servicio.crearTarea( miTarea );

        // se ha creado la tarea
        Assertions.assertNotNull(tareaResult);
        // tiene el mismo nombre que se ha asignado
        Assertions.assertEquals(tareaResult.getNombre(),"Primera Tarea");
        // se le asignado un id
        Assertions.assertNotNull(tareaResult.getId());
        // pertenece al usuario
        Assertions.assertEquals(tareaResult.getUsuarioCorreo(),"fede.valverde@fmail.com");

    }

    @Test
    @Order(2)
    public void deberiaObtenerListaTareas() {
        TareaModelo miTarea = TareaModelo.builder()
                .nombre("Segunda Tarea")
                .estado(0)
                .color("green-500")
                .usuarioCorreo("fede.valverde@fmail.com")
                .build();

        TareaModelo tareaResult = servicio.crearTarea( miTarea );

        List<TareaModelo> listado = servicio.obtenerTareas("fede.valverde@fmail.com" );

        // se obtine una lista
        Assertions.assertFalse(listado.isEmpty());
        // la lista tiene un registro
        Assertions.assertTrue(listado.size()==1);
    }

    @Test
    @Order(3)
    public void deberiaLanzarExcepcionUsuarioNoExiste() {
        String correo = "no.existe@fmail.com";

        // lanza EntityNotFoundException.class cuando
        Assertions.assertThrows(EntityNotFoundException.class, () -> servicio.obtenerTareas(correo));

    }



    @Test
    @Order(4)
    public void deberiaModificarTarea() {
        TareaModelo miTarea = TareaModelo.builder()
                .nombre("Tercera Tarea")
                .estado(0)
                .color("green-500")
                .usuarioCorreo("fede.valverde@fmail.com")
                .build();

        TareaModelo tareaResult = servicio.crearTarea( miTarea );

        tareaResult.setEstado(90);

        TareaModelo tareaModif = servicio.modificarTarea( tareaResult );

        // la tarea existe
        Assertions.assertNotNull(tareaResult);
        // conserva el mismo id
        Assertions.assertEquals(tareaResult.getId(),tareaModif.getId());
        //conserva el mismo nombre
        Assertions.assertEquals(tareaResult.getNombre(),tareaModif.getNombre());
        // se ha actualizado el campo
        Assertions.assertEquals(90, tareaResult.getEstado());

    }

    @Test
    @Order(4)
    public void deberiaBorrarTarea() {

        TareaModelo miTarea = TareaModelo.builder()
                .nombre("Cuarta Tarea")
                .estado(0)
                .color("green-500")
                .usuarioCorreo("fede.valverde@fmail.com")
                .build();

        TareaModelo tareaResult = servicio.crearTarea( miTarea );

        // la tarea existe
        Assertions.assertNotNull(tareaResult);

        Integer id = tareaResult.getId();

        servicio.borrarTarea( id );

        Optional<Tarea> result = tareasRepo.findById( id );
        // comprobar que se ha borrado
        Assertions.assertFalse(result.isPresent());

    }

    @Test
    @Order(5)
    public void deberiaValidarCorreo () {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        var validator = factory.getValidator();

        TareaModelo miTarea = TareaModelo.builder()
                .nombre("Cuarta Tarea")
                .estado(0)
                .color("green-500")
                .usuarioCorreo("fmail.com")
                .build();

        var violationes = validator.validate(miTarea);

        // comprobar que el correo es invalido
        Assertions.assertFalse(violationes.isEmpty());


    }






}
