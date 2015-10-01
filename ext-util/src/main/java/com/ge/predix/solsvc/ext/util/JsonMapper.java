package com.ge.predix.solsvc.ext.util;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.mimosa.osacbmv3_3.DataEvent;
import org.mimosa.osacbmv3_3.Value;
import org.springframework.stereotype.Component;

import com.ge.dsp.aaas.entity.identifier.Identifier;
import com.ge.dsp.pm.ext.entity.fielddata.Data;
import com.ge.dsp.pm.ext.entity.selectionfilter.SelectionFilter;

/**
 * 
 * @author 212325745
 */
@Component
public final class JsonMapper
{

    static private final ObjectMapper mapper = new ObjectMapper().configure(
                                                     DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                                     .setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

    /**
     * -
     */
    @PostConstruct()
    public void init()
    {
        initialize();
    }

    /**
     * - add parent classes with @JsonTypeInfo and @XmlSeeAlso annotations which allows for polymorphic deserialization
     */
    private static void initialize()
    {
        List<Class<?>> classesToEvaluate = new ArrayList<Class<?>>();
        // some FDH parent objects
        classesToEvaluate.add(Identifier.class);
        classesToEvaluate.add(SelectionFilter.class);
        classesToEvaluate.add(Data.class);
        // some OSA objects to be backwards compatible
        classesToEvaluate.add(Value.class);
        classesToEvaluate.add(DataEvent.class);
        classesToEvaluate.add(org.mimosa.osacbmv3_3.SelectionFilter.class);

        Class<?>[] classes = getSubtypes(classesToEvaluate);
        mapper.registerSubtypes(classes);
    }

    /**
     * @param object -
     * @return -
     */
    @SuppressWarnings("nls")
    public <T> String toJsonOrBlank(T object)
    {
        try
        {
            return toJson(object);
        }
        catch (Exception e)
        {
            return "";
        }
    }

    /**
     * @param object -
     * @return -
     */
    public <T> String toJson(T object)
    {

        try
        {
            return mapper.writeValueAsString(object);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param object -
     * @return -
     */
    public <T> String toPrettyJson(T object)
    {

        try
        {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param json -
     * @param clazz -
     * @return -
     */
    public <T> T fromJson(String json, Class<T> clazz)
    {

        try
        {
            return mapper.readValue(json, clazz);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param type -
     * @param json -
     * @return -
     */
    public <T> T fromJsonType(TypeReference<T> type, String json)
    {

        try
        {
            return mapper.readValue(json, type);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param clazz -
     * @param json -
     * @return -
     */
    public <T> T fromJsonOrNull(Class<T> clazz, String json)
    {
        try
        {
            return fromJson(json, clazz);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * @param classToEvaluate
     * @return
     */
    private static Class<?>[] getSubtypes(List<Class<?>> classesToEvaluate)
    {
        List<Class<?>> subtypes = new ArrayList<Class<?>>();
        for (Class<?> classToEvaluate : classesToEvaluate)
        {
            subtypes.addAll(getSubtypes(null, classToEvaluate));
        }
        Class<?>[] classes = new Class<?>[subtypes.size()];
        int i = 0;
        for (Class<?> clz : subtypes)
        {
            classes[i++] = clz;
        }
        return classes;
    }

    /**
     * @param clazz -
     * @param json -
     * @return -
     */
    @SuppressWarnings("nls")
    public <T> List<T> fromJsonArray(String json, Class<T> clazz)
    {
        try
        {
            List<T> results = new ArrayList<>();
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++)
            {
                array.getJSONObject(i).put("@type", clazz.getSimpleName());
                results.add(fromJson(array.getString(i), clazz));
            }
            return results;
        }
        catch (JSONException e)
        {
           throw new RuntimeException(e);
        }
    }

    /**
     * @return
     * 
     */
    @SuppressWarnings("nls")
    private static List<Class<?>> getSubtypes(List<Class<?>> resultArg, Class<?> classToEvaluate)
    {
        {
            List<Class<?>> result = resultArg;
            for (Annotation annotation : classToEvaluate.getAnnotations())
            {
                if ( annotation.annotationType().equals(XmlSeeAlso.class) )
                {
                    if ( result == null ) result = new ArrayList<Class<?>>();
                    XmlSeeAlso xmlSeeAlso = classToEvaluate.getAnnotation(XmlSeeAlso.class);
                    for (Class<?> clz : xmlSeeAlso.value())
                    {
                        result.add(clz);
                        if ( clz.getAnnotation(XmlSeeAlso.class) != null )
                        {
                            result = getSubtypes(result, clz);
                        }
                    }
                }
            }
            if ( result == null )
                throw new UnsupportedOperationException("class=" + classToEvaluate
                        + " does not have any @XmlSeeAlso annotations?");
            return result;

        }

    }

    /**
     * @param clazz -
     */
    public void addSubtype(Class<?> clazz)
    {
        mapper.registerSubtypes(clazz);
    }

    /**
     * @param clazz -
     */
    public void addAllXmlSeeAlsoSubtypes(Class<?> clazz)
    {
        List<Class<?>> classesToEvaluate = new ArrayList<Class<?>>();
        classesToEvaluate.add(clazz);
        Class<?>[] classes = getSubtypes(classesToEvaluate);
        mapper.registerSubtypes(classes);
    }
}
