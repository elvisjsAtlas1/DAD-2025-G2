package com.example.ms_cliente.service.impl;

import com.example.ms_cliente.entity.Cliente;
import com.example.ms_cliente.repository.ClienteRepository;
import com.example.ms_cliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String clienteServiceUrl = "http://ms-catalogo-service/cliente/";

    @Override
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> listarPorId(Integer id) {
        return clienteRepository.findById(id);
    }

    @Override
    public void eliminarPorId(Integer id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public Optional<Cliente> obtenerClienteConNombreCategoria(Integer id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            String categoriaNombre = obtenerNombreCategoria(cliente.getCategoriaId());
            cliente.setCategoriaNombre(categoriaNombre);
            return Optional.of(cliente);
        }
        return Optional.empty();
    }

    private String obtenerNombreCategoria(Integer categoriaId) {
        try {
            // Llamada al servicio de cliente para obtener los datos de la categoría
            Cliente cliente = restTemplate.getForObject(clienteServiceUrl + categoriaId, Cliente.class);
            if (cliente != null) {
                return cliente.getNombre(); // Devolvemos el nombre de la categoría
            }
        } catch (Exception e) {
            return "Categoría no encontrada"; // Si ocurre un error, devolvemos un mensaje por defecto
        }
        return null;
    }
}
