package br.com.servtec.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.servtec.api.model.OrdemServicoInput;
import br.com.servtec.api.model.OrdemServicoRepresentationModel;
import br.com.servtec.domain.model.OrdemServico;
import br.com.servtec.domain.repository.OrdemServicoRepository;
import br.com.servtec.domain.service.GestaoOrdemServicoService;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

	@Autowired
	private GestaoOrdemServicoService gestaoOrdemServicoService;
	
	@Autowired
	private OrdemServicoRepository osRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	private OrdemServicoRepresentationModel criar(@Valid @RequestBody OrdemServicoInput ordemServicoInput) {
		OrdemServico ordemServico = toEntity(ordemServicoInput);
		
		return toModel(gestaoOrdemServicoService.criarOrdemServico(ordemServico));
	}
	
	@GetMapping
	private List<OrdemServicoRepresentationModel> listar() {
		return toCollectionModel(osRepository.findAll());
	}
	
	@GetMapping("/{clienteId}")
	private ResponseEntity<OrdemServicoRepresentationModel> buscar(@PathVariable Long clienteId) {
		Optional<OrdemServico> ordemServico = osRepository.findById(clienteId);
		
		if (ordemServico.isPresent()) {
			OrdemServicoRepresentationModel osModel = toModel(ordemServico.get());
			return ResponseEntity.ok(osModel);
		}
		
		return ResponseEntity.notFound().build();
	}

	private OrdemServicoRepresentationModel toModel(OrdemServico ordemServico) {
		return modelMapper.map(ordemServico, OrdemServicoRepresentationModel.class);
	}
	
	private List<OrdemServicoRepresentationModel> toCollectionModel(List<OrdemServico> listaOrdemServico) {
		return listaOrdemServico.stream().map(ordemServico -> toModel(ordemServico)).collect(Collectors.toList());
	}
	
	private OrdemServico toEntity(OrdemServicoInput ordemServicoInput) {
		return modelMapper.map(ordemServicoInput, OrdemServico.class); 
	}
	
	@PutMapping("{ordemServicoId}/finalizacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void finalizar(@PathVariable Long ordemServicoId) {
		gestaoOrdemServicoService.finalizar(ordemServicoId);
	}
	
}
