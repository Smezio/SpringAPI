### Spring Boot RestAPI with Neo4j
RestAPI developed using Spring Boot framework.
The goal was to define 3 entry-points to get, create and edit data about an entity. The entity represents a node with the following information:
- id : String -> unique identifier 
- value : Double -> floating point number of the node
- parents ?: List<String> -> list of parents of node

The entry-points are:
- GET '/entity': get all nodes
- POST '/entity': add one or more nodes
- PATCH '/entity/{id}': edit a node and its related nodes (parents and children)
