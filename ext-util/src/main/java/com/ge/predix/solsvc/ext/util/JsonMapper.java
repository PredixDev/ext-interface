package com.ge.predix.solsvc.ext.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.mimosa.osacbmv3_3.DataEvent;
import org.mimosa.osacbmv3_3.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ge.predix.entity.fielddata.Data;
import com.ge.predix.entity.filter.Filter;
import com.ge.predix.entity.identifier.Identifier;
import com.ge.predix.entity.model.Model;
import com.ge.predix.entity.model.Typed;

/**
 * This class helps implement Polymorphic (Animal, Cat, Dog) marshaling and unmarshaling.
 * 
 * use @JsonTypeInfo annotation on the Parent class to be marshaled or unmarshaled, then add it here
 * When using maps or lists as a property, use @JsonSerialize and @JsonDeserialize annotations on the property to be unmarshaled
 * or marshaled, the keys or values that are polymorphic will be serialized or deserialized if they inherit a parent class registered here
 * 
 * @author 212325745
 */
@Component
public final class JsonMapper
{

    static private final ObjectMapper    mapper          = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    private static Map<String, Class<?>> subtypeClassMap = new HashMap<String, Class<?>>();

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
        // use @JsonTypeInfo annotation on the Parent class to be marshaled or unmarshaled, then add it here
        // When using maps or lists as a property, use @JsonSerialize and @JsonDeserialize annotations on the property to be unmarshaled
        // or marshaled, the keys or values that are polymorphic will be serialized or deserialized if they inherit a parent class registered here

        List<Class<?>> classesToEvaluate = new ArrayList<Class<?>>();
        // some FDH parent objects
        classesToEvaluate.add(Identifier.class);
        classesToEvaluate.add(Filter.class);
        classesToEvaluate.add(Data.class);
        // some OSA objects to be backwards compatible
        classesToEvaluate.add(Value.class);
        classesToEvaluate.add(DataEvent.class);
        classesToEvaluate.add(org.mimosa.osacbmv3_3.SelectionFilter.class);
        // some Asset classes
        classesToEvaluate.add(Model.class);
        classesToEvaluate.add(Typed.class);

        Class<?>[] subtypeClasses = getSubtypes(classesToEvaluate);
        mapper.registerSubtypes(subtypeClasses);

        for (Class<?> subtypeClass : subtypeClasses)
        {
            subtypeClassMap.put(subtypeClass.getSimpleName(), subtypeClass);
        }

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
        catch (Throwable e)
        {
            return "";
        }
    }

    /**
     * @param object -
     * @return -
     */
    @SuppressWarnings("nls")
    public <T> String toJson(T object)
    {

        try
        {
            if ( object instanceof List )
            {
                // TODO move this to the overridden Jackson mapper
                String json = "[";
                for (Object item : (List<?>) object)
                {
                    json += mapper.writeValueAsString(item) + ",";
                }
                if ( json.endsWith(",") ) json = json.substring(0, json.length() - 1);
                json += "]";
                return json;
            }
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
     * If you know the class to unmarshal to pass it in here.
     * 
     * 
     * 
     * @param json -
     * @param clazz -
     * @return -
     */
    @SuppressWarnings("all")
    public <T> T fromJson(String json, Class<T> clazz)
    {
        try
        {
            json = json.trim();
            if ( json.startsWith("{") )
            {
                // check if the Class is polymorphic and if so ensure json has a "complexType" attribute/value pair. Otherwise Jackson will be angry.
                Class<?> jsonTypeInfoClass = hasJsonTypeInfo(clazz);
                if ( jsonTypeInfoClass != null && !json.contains("complexType") )
                {
                    json = "[" + json + "]";
                    List<T> list = fromJsonArray(json, clazz);
                    return list.get(0);
                }

                // unmarshal it
                return mapper.readValue(json, clazz);
            }
            throw new UnsupportedOperationException("please use fromJsonArray for arrays");
        }
        catch (

        IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * If it's a json array, pass in the Class to unmarshal to for each element.
     *
     * Check if the Class is polymorphic and if so ensure json has a "complexType" attribute/value pair. Otherwise Jackson will be angry.
     * 
     * @param clazz -
     * @param json -
     * @return -
     */
    @SuppressWarnings("nls")
    public <T> List<T> fromJsonArray(String json, Class<T> clazz)
    {
        Class<?> jsonTypeInfoClass = hasJsonTypeInfo(clazz);

        List<T> results = new ArrayList<>();
        JSONArray array;
        try
        {
            array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);
                try
                {
                    boolean polymorphicAndMissingComplexTypeAttribute = ((jsonTypeInfoClass != null)
                            && !jsonObject.has("complexType"));
                    if ( polymorphicAndMissingComplexTypeAttribute )
                    {
                        jsonObject.put("complexType", clazz.getSimpleName());
                    }
                    results.add(mapper.readValue(jsonObject.toString(), clazz));
                }
                catch (JSONException | IOException e)
                {
                    throw new RuntimeException("unable to unmarshal json=" + jsonObject.toString(), e);
                }
            }
        }
        catch (JSONException e)
        {
            throw new RuntimeException("unable to unmarshal json=" + json, e);
        }
        return results;

    }

    /**
     * You have a json array, you know the Parent (P), you know the spelling of the child class, but you don't have a handle to the child java Class.
     * Since we registered all the child class upon startup, it looks through the registered child classes until it finds a name match.
     * 
     * e.g. Say you have an Engine P. And you have a physical GE90Engine extends Engine.
     * 
     * This happens when processing many different types of Assets from PredixAsset through the same code.
     * 
     * @param json -
     * @param clazz -
     * @return -
     */
    @SuppressWarnings(
    {
            "nls", "unchecked"
    })
    public <P> List<P> fromJsonArray(String json, String clazz)
    {
        try
        {
            String complexType = getBaseClassName(clazz);
            List<P> results = new ArrayList<>();
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++)
            {
                if ( !array.getJSONObject(i).has("complexType") )
                    array.getJSONObject(i).put("complexType", complexType);

                // well we can't unmarshal without a Class reference, let's find one
                Class<P> subClass = (Class<P>) JsonMapper.subtypeClassMap.get(complexType);
                if ( subClass != null )
                    results.add(mapper.readValue(array.getString(i), subClass));
                else
                    results.add((P) mapper.readValue(array.getString(i), Object.class));

            }
            return results;
        }
        catch (JSONException | IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param modelName
     * @return -
     */
    @SuppressWarnings("nls")
    private String getBaseClassName(String className)
    {
        if ( className.contains(".") ) className = className.substring(className.lastIndexOf(".") + 1);
        className = className.substring(0, 1).toUpperCase() + className.substring(1);
        return className;
    }

    /**
     * @param clazz
     * @return -
     */
    @SuppressWarnings(
    {
            "nls", "unused"
    })
    private <T> String hasJsonTypeInfoReturnProperty(Class<T> clazz)
    {
        Class<?> jsonTypeInfoClass = hasJsonTypeInfo(clazz);
        String property = null;
        if ( jsonTypeInfoClass != null )
        {
            JsonTypeInfo annotation = jsonTypeInfoClass.getAnnotation(JsonTypeInfo.class);
            Class<? extends Annotation> type = annotation.annotationType();
            for (Method method : type.getDeclaredMethods())
            {
                try
                {
                    if ( method.getName().equals("property") )
                    {
                        property = (String) method.invoke(annotation, (Object[]) null);
                        break;
                    }
                }
                catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
        return property;
    }

    /**
     * @param clazz
     * @return -
     */
    private <T> Class<?> hasJsonTypeInfo(Class<T> clazz)
    {
        Class<?> classToInspect = clazz;
        Class<?> jsonTypeInfoClass = null;
        while (classToInspect != null)
        {
            if ( classToInspect.isAnnotationPresent(JsonTypeInfo.class) )
            {
                jsonTypeInfoClass = classToInspect;
                break;
            }
            if ( classToInspect == clazz.getSuperclass() ) break;
            classToInspect = clazz.getSuperclass();
        }
        return jsonTypeInfoClass;
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
        catch (Throwable e)
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
            if ( result == null ) throw new UnsupportedOperationException(
                    "class=" + classToEvaluate + " does not have any @XmlSeeAlso annotations?");
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
