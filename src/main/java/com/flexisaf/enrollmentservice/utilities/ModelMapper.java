package com.flexisaf.enrollmentservice.utilities;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.io.IOException;
import java.util.stream.Stream;

public class ModelMapper {

    public static <T,S> void map(T source, S target){
       BeanUtils.copyProperties(source, target);
    }

    public static <T, S> void mapNotNullValues(T source, S target){
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static <T> T parseJson(String modelString, Class<T> modelClass){

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            T model = objectMapper.readValue(modelString, modelClass);
            return model;
        }catch(IOException err){
            err.printStackTrace();
        }

        return null;
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }


}
