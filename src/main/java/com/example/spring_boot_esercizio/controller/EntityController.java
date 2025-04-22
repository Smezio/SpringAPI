package com.example.spring_boot_esercizio.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.spring_boot_esercizio.model.Entity;
import com.example.spring_boot_esercizio.model.EntityService;

@RestController
public class EntityController {

    @Autowired
    private EntityService entityService; // Service for Entity
    
    /**
     * 
     * Get all entities
     * 
     * @return list of entities
     */
    @RequestMapping(value = "/entity", method = RequestMethod.GET)
	List<Entity> getAllEntities() {
        try {
            List<Entity> entities = entityService.getAll();
            return entities;
        } catch(Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCause());
        }
	}

    /**
     * 
     * Create a new entity
     * 
     * @param entity: List of new entities
     * @return list of entities
     */
	@RequestMapping(value = "/entity", method = RequestMethod.POST)
	List<Entity> create(@RequestBody List<Entity> entity) {
		try {
            List<Entity> entities = entityService.saveMany(entity);
            return entities;
        } catch(Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCause());
        }
	}
    

    /**
     * 
     * Update the entity and its parents and childern
     * 
     * @param entity List with one entity
     * @param id Identifier of entity
     * @return list of entities
     */
	@RequestMapping(value = "/entity/{id}", method = RequestMethod.PATCH)
	List<Entity> updateEntity(@RequestBody List<Entity> entity, @PathVariable String id) {
		try {
            if(!id.equals(""))
                entity.get(0).setId(id);
            
            List<Entity> entities = entityService.updateOne(entity.get(0));
            return entities;
        } catch(Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCause());
        }
	}

}
