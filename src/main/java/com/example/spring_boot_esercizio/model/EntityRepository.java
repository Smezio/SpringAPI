package com.example.spring_boot_esercizio.model;

import java.util.List;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends Neo4jRepository<Entity, String> {

    /**
     * 
     * Get all entities
     * 
     * @return List of entities
     */
    @Query("MATCH (e:Entity)\r\n" + //
                "OPTIONAL MATCH (e)<-[r:PARENT]-(p:Entity)\r\n" + //
                "RETURN e, collect(p.id) as parents")
    List<Entity> getAll();

    /**
     * 
     * Get Entity by Id 
     *
     * @param id
     * @return 
     */
    @Query("MATCH(e:Entity{id:$id}) RETURN e")
    Optional<Entity> getEntityById (String id);


    /**
     * 
     * Get Entities by Id list 
     * 
     * @param
     * @return
     */
    @Query("MATCH(e:Entity)\r\n" + //
                "WHERE e.id IN $ids\r\n" + //
                "OPTIONAL MATCH(e:Entity)<-[:PARENT]-(p:Entity) \r\n" + //
                "WHERE e.id IN $ids\r\n" + //
                "RETURN e, collect(p.id) as parents")
    List<Entity> getEntitiesById (List<String> ids);


    /**
     * 
     *  Get first entity 
     * 
     * @return
     */
    @Query("MATCH (e:Entity)<-[:PARENT]-(p:Entity) RETURN e, collect(p.id) as parents LIMIT 1")
    Optional<Entity> getFirstEntity();


    /**
     * 
     * Insert new Entity 
     *
     * @param
     * @return
     */
    @Query("UNWIND $entities as entity \r\n" + //
                "MERGE (e:Entity{id:randomUUID(), value:entity.__properties__.value}) \r\n" + //
                "WITH e, entity\r\n" + //
                "MATCH (p:Entity) WHERE p.id in entity.__properties__.parents\r\n" + //
                "MERGE (e)<-[:PARENT]-(p)\r\n" + //
                "RETURN e as entity, collect(p.id) as parents")
    List<Entity> saveMany (List<Entity> entities);


    /**
     * 
     * Update existing Entity 
     * 
     * @param entity 
     * @return
     */
    @Query("MATCH (e:Entity{id:$entity.__id__}) \r\n" + //
                "WITH e, e.value as oldValue, $entity.__properties__.parents as parents, toFloat($entity.__properties__.value) as input \r\n" + //
                "SET e.value = input\r\n" + //
                "WITH *\r\n" + //
                "CALL (e, parents) {\r\n" + //
                "    MATCH (e)<-[r:PARENT]-(o:Entity) WHERE NOT(o.id IN parents) \r\n" + //
                "    DELETE r\r\n" + //
                "}\r\n" + //
                "CALL (e, parents) {\r\n" + //
                "    MATCH (n:Entity) WHERE n.id IN parents \r\n" + //
                "    MERGE (n)-[:PARENT]->(e)\r\n" + //
                "}\r\n" + //
                "CALL (e, oldValue, parents, input) { \r\n" + //
                "    OPTIONAL MATCH (e)-[:PARENT]->(c:Entity) \r\n" + //
                "    WITH c, \r\n" + //
                "    CASE oldValue \r\n" + //
                "        WHEN 0 THEN input \r\n" + //
                "        ELSE c.value * (input/oldValue) \r\n" + //
                "    END AS resultChild\r\n" + //
                "    SET c.value = resultChild\r\n" + //
                "    WITH *\r\n" + //
                "    OPTIONAL MATCH (p:Entity)-[:PARENT]->(e)\r\n" + //
                "    WITH p,\r\n" + //
                "    CASE oldValue \r\n" + //
                "        WHEN 0 THEN input \r\n" + //
                "        ELSE p.value * (input/oldValue) \r\n" + //
                "    END AS resultParent \r\n" + //
                "    SET p.value = resultParent\r\n" + //
                "    RETURN DISTINCT p\r\n" + //
                "}\r\n" + //
                "RETURN e, collect(p.id) as parent")
    Optional<Entity> updateOne (Entity entity);
}
