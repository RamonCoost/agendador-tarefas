package com.javanauta.agendadortarefas.business.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioDTO {

    private String email;
    private String senha;
}
