package com.example.spring_boot_esercizio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import com.example.spring_boot_esercizio.model.Entity;
import com.example.spring_boot_esercizio.model.EntityRepository;

@DataNeo4jTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntityRepositoryUnitTests {

	@Autowired
	private EntityRepository entityRepository;

	@Test
	@DisplayName("T1 - Get new Entities")
	@Order(1)
	public void getEntities() {
		
		List<Entity> result = entityRepository.getAll();
		
        // Verify
		Assertions.assertNotNull(result);
	}

	@Test
	@DisplayName("T2 - Save new Entities")
	@Order(2)
	public void saveEntity() {
		ArrayList<String> parents = new ArrayList<String>();
		parents.add("A");
		parents.add("B");
		List<Entity> entities = new ArrayList<>();
		entities.add(new Entity(null, 10.0, parents));

		List<Entity> result = entityRepository.saveMany(entities);

        // Verify
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.size() == 1);
		Assertions.assertTrue(
			result.get(0).getValue().equals(entities.get(0).getValue())
			);
		Assertions.assertTrue(
			result.get(0).getParents().equals(entities.get(0).getParents())
			);
	}

	@Test
	@DisplayName("T3 - Save new Entities")
	@Order(3)
	public void saveEntities() {
		List<String> parents1 = new ArrayList<String>();
		parents1.add("A");
		parents1.add("B");
		List<String> parents2 = new ArrayList<String>();

		List<Entity> entities = new ArrayList<>();
		entities.add(new Entity(null, 10.0, parents1));
		entities.add(new Entity(null, 20.0, parents2));

		List<Entity> result = entityRepository.saveMany(entities);

        // Verify
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.size() > 0);
	}

	@Test
	@DisplayName("T4 - Update an entity")
	@Order(4)
	public void updateEntity() {
        Optional<Entity> entity = entityRepository.getFirstEntity();
        List<Entity> parents = entityRepository.getEntitiesById(entity.get().getParents());

        Double newValue = 10.;
        Double oldValue = entity.get().getValue();

        entity.get().setValue(newValue);

        Optional<Entity> result = entityRepository.updateOne(entity.get());        
        
		// Verify
        // Check starting Entity
		Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().getValue().equals(newValue));

        // Check parent updates
        parents.forEach(parent->{
            Optional<Entity> updatedParent = entityRepository.getEntityById(parent.getId());
        
			if(updatedParent.isPresent()) {
				if(newValue.equals(0))
					Assertions.assertTrue(
						updatedParent.get().getValue().equals(0)
						);
				else
					Assertions.assertTrue(
						(updatedParent.get().getValue()/parent.getValue()) == (newValue/oldValue)
						);
			}
        });
	}
}