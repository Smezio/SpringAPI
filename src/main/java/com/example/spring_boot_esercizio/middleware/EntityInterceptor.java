package com.example.spring_boot_esercizio.middleware;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import com.example.spring_boot_esercizio.model.Entity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class EntityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        CachedBodyHttpServletRequest requestWrapper = new CachedBodyHttpServletRequest((HttpServletRequest) request);
        String body = new String(requestWrapper.getRequestBody());

        System.out.println("Checking the request structure...");
        System.out.println(request.getMethod() + " " + request.getContextPath());

        // Check Content-Type
        if(request.getContentType() != null) {
            if(!request.getContentType().equals("application/json")) {
                response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                return false;
            }
        }
        

        // No check for GET request
        if(requestWrapper.getMethod().equals("GET")) return true;

        try {
            System.out.println(body);
            System.out.println("Deserializing the request body...");
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<Entity> entities = new ArrayList<>();

            // Check the initial character to decide how to deserialize the JSON body
            if(body.startsWith("["))
                entities.addAll(mapper.readValue(body, mapper.getTypeFactory().constructCollectionType(ArrayList.class, Entity.class)));
            else if(body.startsWith("{"))
                entities.add(mapper.readValue(body, Entity.class));
            else
                throw new Exception("Payload must be a JSON object or an array of JSON objects");
            
            // PATCH request case: must be a single JSON object
            if(requestWrapper.getMethod().equals("PATCH")) {
                if(entities.size()>1) throw new Exception("One entity is expected for PATCH request.");
            }

            validateBody(entities);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCause());
        }
    }

    /**
     * Check validation rules for entities in the body
     * 
     * @param entities
     * @throws Exception
     */
    private void validateBody(List<Entity> entities) throws Exception {
        System.out.println("Validating body...");
        Iterator<Entity> it = entities.iterator();
        while (it.hasNext()) {
            Entity ent = it.next();

            if(ent.getValue() == null)  throw new Exception("Entity doesn't have value assigned.");
            if(ent.getValue() < 0) throw new Exception("Entity contains a negative value. Natural number is expected.");
        }
    }
}
