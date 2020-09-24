package br.com.servtec.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.servtec.api.model.ComentarioInput;
import br.com.servtec.api.model.ComentarioRepresentationModel;
import br.com.servtec.domain.exception.EntidadeNaoEncontradaException;
import br.com.servtec.domain.model.Comentario;
import br.com.servtec.domain.model.OrdemServico;
import br.com.servtec.domain.repository.OrdemServicoRepository;
import br.com.servtec.domain.service.GestaoOrdemServicoService;

@RestController
@RequestMapping("/ordens-servico/{ordemServicoId}/comentarios")
public class ComentarioController {

	@Autowired
	private GestaoOrdemServicoService gestaoOrdemServicoService;

	@Autowired
	private OrdemServicoRepository osRepository;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public List<ComentarioRepresentationModel> buscarComentarios(@PathVariable Long ordemServicoId) {
		OrdemServico ordemServico = osRepository.findById(ordemServicoId).
				orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada!"));

		return toCollectionModel(ordemServico.getComentarios());
	}

	private List<ComentarioRepresentationModel> toCollectionModel(List<Comentario> comentarios) {
		
		return comentarios.stream().map(comentario -> modelMapper
				.map(comentario, ComentarioRepresentationModel.class)).collect(Collectors.toList());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ComentarioRepresentationModel adicionar(@PathVariable Long ordemServicoId, @Valid @RequestBody ComentarioInput comentarioInput) {
		Comentario comentario = gestaoOrdemServicoService.adicionarComentario(ordemServicoId, comentarioInput.getDescricao());

		return toModel(comentario);
	}

	private ComentarioRepresentationModel toModel(Comentario comentario) {
		return modelMapper.map(comentario, ComentarioRepresentationModel.class);
	}
	
	
}
