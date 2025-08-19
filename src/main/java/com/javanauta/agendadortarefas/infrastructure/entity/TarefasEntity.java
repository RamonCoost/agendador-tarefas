package com.javanauta.agendadortarefas.infrastructure.entity;

import com.javanauta.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document("tarefa")
public class TarefasEntity {

    @Id
    private String id;
    private String nomeTarefa;
    private String descricao;
    private String emailUsuario;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataEvento;
    private LocalDateTime dataAlteracao;
    private StatusNotificacaoEnum statusNotificacaoEnum;

}
