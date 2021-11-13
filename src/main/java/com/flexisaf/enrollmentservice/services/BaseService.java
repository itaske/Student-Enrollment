package com.flexisaf.enrollmentservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexisaf.enrollmentservice.exceptions.BadRequestException;
import com.flexisaf.enrollmentservice.exceptions.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public abstract class BaseService<T, R extends JpaRepository<T,K>, K> {
    @Autowired
    R repository;

    public boolean deleteById(K key){
         repository.deleteById(key);
         return true;
    }

    public T editModel(T editedModel){
         T model = repository.saveAndFlush(editedModel);
         return model;
    }

    public T editModel(Map<String,Object> map, K id){

        if (!repository.existsById(id)) {

            throw new BadRequestException("No Resource with ID "+id);
        }

            T objectToBeEdited = repository.findById(id).get();

            ObjectMapper objectMapper = new ObjectMapper();

            T copiedUser = (T) objectMapper.convertValue(map, objectToBeEdited.getClass());

            copyProperties(copiedUser, objectToBeEdited, map.keySet());

            T editedObject = repository.saveAndFlush(objectToBeEdited);

            return editedObject;
    }

    public T findById(K key){
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        T model = repository.findById(key)
                .orElseThrow(()-> new ResourceNotFoundException(String.format("%s with ID %s not found",
                        tClass.getSimpleName(), key)));
        return model;
    }

    public Page<T> getAll(int currentPage, int size, String attribute, String direction)
    {
        Pageable pageable = PageRequest.of(currentPage, size, Sort.by(Sort.Direction.valueOf(direction), attribute));
        Page<T> page = repository.findAll(pageable);
        return page;
    }


    public T save(T model){
         return repository.save(model);
    }

    private static void copyProperties(Object src, Object trg, Set<String> props) {

        String[] excludedProperties =
                Arrays.stream(BeanUtils.getPropertyDescriptors(src.getClass()))
                        .map(PropertyDescriptor::getName)
                        .filter(name -> !props.contains(name))
                        .toArray(String[]::new);
        BeanUtils.copyProperties(src, trg, excludedProperties);

    }

}

