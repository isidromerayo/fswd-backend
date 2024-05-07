package es.jcyl.formacion.backendapi.persistencia.entidades;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "APP_TAREAS")
public class Tarea extends EntidadBase {

    @Id
    @GeneratedValue
    @Column(name = "ID_TAREA")
    private Integer id;

    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;

    @Column (name="estado")
    @Min(value = 0, message = "el estado debe estar entre 0 y 100")
    @Max(value = 100, message = "el estado debe estar entre 0 y 100")
    private Integer estado;

    @Column(name = "color", length=50)
    private String color;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

}
