package SystemITR.JosueGuinea1A.Usuarios.Service;

import SystemITR.JosueGuinea1A.Usuarios.DTO.ResponseDTO;
import SystemITR.JosueGuinea1A.Usuarios.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class usuarioService {

    private final UsuarioRepository repository;

    public usuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<ResponseDTO>obtenerTodos(){
        return repository.findAll()
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    public ResponseDTO obtenerPorId(Long id){
        Usuario usuario = repository.findById(id)
                .orElseThrow() -> new RuntimeException("Usuario" + id);
        return mapearAResponse(usuario);
    }

    private ResponseDTO mapearAResponse(Usuario usuario) {

    }
}
