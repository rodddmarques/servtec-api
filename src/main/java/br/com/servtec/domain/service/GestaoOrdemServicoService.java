package br.com.servtec.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.servtec.domain.exception.EntidadeNaoEncontradaException;
import br.com.servtec.domain.exception.NegocioException;
import br.com.servtec.domain.model.Cliente;
import br.com.servtec.domain.model.Comentario;
import br.com.servtec.domain.model.OrdemServico;
import br.com.servtec.domain.model.StatusOrdemServico;
import br.com.servtec.domain.repository.ClienteRepository;
import br.com.servtec.domain.repository.ComentarioRepository;
import br.com.servtec.domain.repository.OrdemServicoRepository;

@Service
public class GestaoOrdemServicoService {
	
	@Autowired
	private OrdemServicoRepository osRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ComentarioRepository comentarioRepository;
	
	public OrdemServico criarOrdemServico(OrdemServico os) {
		
		Cliente cliente = clienteRepository.findById(os.getCliente().getId())
				.orElseThrow(() -> new NegocioException("Cliente não encontrado!"));
		
		os.setCliente(cliente);
		os.setStatusOS(StatusOrdemServico.ABERTA);
		os.setDataAbertura(OffsetDateTime.now());
		
		return osRepository.save(os);
	}
	
	public void finalizar(Long ordemServicoId) {
		OrdemServico ordemServico = buscarOS(ordemServicoId);
		ordemServico.finalizar();
		
		osRepository.save(ordemServico);
	}
	
	public Comentario adicionarComentario(Long ordemServicoId, String descricao) {
		
		OrdemServico ordemServico = buscarOS(ordemServicoId);
		
		var comentario = new Comentario();
		comentario.setOrdemServico(ordemServico);
		comentario.setDescricao(descricao);
		comentario.setDataEnvio(OffsetDateTime.now());
		
		return comentarioRepository.save(comentario); 
		
	}

	private OrdemServico buscarOS(Long ordemServicoId) {
		return osRepository.findById(ordemServicoId).orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada!"));
	}
}
