package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;

import com.alonso.vipera.training.springboot_apirest.exception.SpecieNotFoundException;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;

/**
 * Servicio para la gestión de especies de mascotas.
 * Proporciona operaciones de consulta para entidades Specie.
 */
public interface SpecieService {

    /**
     * Obtiene todas las especies disponibles.
     *
     * @return Lista de todas las especies registradas en el sistema
     */
    List<SpecieOutDTO> getAll();

    /**
     * Busca una especie por su nombre exacto.
     *
     * @param name Nombre de la especie a buscar
     * @return DTO de la especie encontrada
     * @throws SpecieNotFoundException Si no se encuentra una especie con el nombre especificado
     */
    SpecieOutDTO findByName(String name);

    /**
     * Verifica si existe una especie con el nombre especificado.
     *
     * @param name Nombre de la especie a verificar
     * @return true si existe una especie con ese nombre, false en caso contrario
     */
    boolean existsByName(String name);

}