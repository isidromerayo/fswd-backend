package es.jcyl.formacion.backendapi;

import es.jcyl.formacion.backendapi.persistencia.entidades.Usuario;
import es.jcyl.formacion.backendapi.persistencia.repositorios.TareasRepositorio;
import es.jcyl.formacion.backendapi.persistencia.repositorios.UsuariosRepositorio;
import es.jcyl.formacion.backendapi.servicios.TareaMapeo;
import es.jcyl.formacion.backendapi.servicios.TareaServicio;
import es.jcyl.formacion.backendapi.servicios.TareaServicioImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ControladoresTests {

    //inyectar
    @Autowired
    private UsuariosRepositorio usuariosRepo;

    // inyectar
    @Autowired
    private MockMvc mockMvc;

    private Usuario usuarioTest;

    @BeforeEach
    void setUp() {

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
    public void testNuevaTarea() throws Exception {
        String requestBody = """
                {
                        "nombre": "Demo",
                        "estado": 0,
                        "color": "red-500",
                        "usuarioCorreo": "fede.valverde@fmail.com"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tareas")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // comprobar que el nombre devuelto es Demo
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Demo"))
                // comprobar que se ha asignado un id
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
                ;
    }


    @Test
    public void testListadoTareas() throws Exception {

        String correo = "fede.valverde@fmail.com";

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tareas")
                        .param("correo", correo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // comprobar que devuelve un array
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                // comprobar que se ha devuelto un dato en el primero registro
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                ;

    }


}
