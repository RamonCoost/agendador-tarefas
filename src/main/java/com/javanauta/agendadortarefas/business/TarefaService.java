package com.javanauta.agendadortarefas.business;

import com.javanauta.agendadortarefas.business.dto.TarefasDTO;
import com.javanauta.agendadortarefas.business.mapper.TarefaUpdateConverte;
import com.javanauta.agendadortarefas.business.mapper.TarefasConverter;
import com.javanauta.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.javanauta.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.javanauta.agendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.javanauta.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefasRepository tarefaRepository;
    private final TarefasConverter tarefaConverter;
    private final JwtUtil jwtUtil;
    private final TarefaUpdateConverte tarefaUpdateConverte;

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto) {

        String email = jwtUtil.extrairEmailToken(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);

        return tarefaConverter.paraTarefaDTO(
                tarefaRepository.save(entity));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {

        return tarefaConverter.paraListaTarefasDTO(
                tarefaRepository.findByDataEventoBetween(dataInicial, dataFinal));
    }

    public List<TarefasDTO> buscaTarefasPorEmail(String token) {

        String email = jwtUtil.extrairEmailToken(token.substring(7));
        List<TarefasEntity> listaTarefas = tarefaRepository.findByEmailUsuario(email);

        return tarefaConverter.paraListaTarefasDTO(listaTarefas);
    }

    public void deletaTarefasPorId(String id) {
        try {
            tarefaRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao deletar tarefas por id, id inexistente " + id,
                    e.getCause());
        }
    }

    public TarefasDTO alteraStatus(StatusNotificacaoEnum status, String id) {
        try {
            TarefasEntity entity = tarefaRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Tarefa não encontrada " + id));

            entity.setStatusNotificacaoEnum(status);
            return tarefaConverter.paraTarefaDTO(tarefaRepository.save(entity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa " + e.getCause());
        }
    }

    public TarefasDTO updateTarefas(TarefasDTO dto, String id) {
        try {
            TarefasEntity entity = tarefaRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada " + id));

            tarefaUpdateConverte.updateTarefas(dto, entity);
            return tarefaConverter.paraTarefaDTO(tarefaRepository.save(entity));

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa " + e.getCause());
        }
    }
}
