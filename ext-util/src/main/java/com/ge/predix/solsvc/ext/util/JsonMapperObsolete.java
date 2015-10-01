package com.ge.predix.solsvc.ext.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONArray;

/**
 * 
 * @author predix
 */
public final class JsonMapperObsolete
{
    static private final ObjectMapper mapper = new ObjectMapper().configure(
                                                     DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                                                     .setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

    private JsonMapperObsolete()
    {
    }

    /**
     * @param object -
     * @return -
     */
    public static <T> String toAllFieldsJson(T object)
    {
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
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
    public static <T> String toJson(T object)
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
    public static <T> String toPrettyJson(T object)
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
     * @param object -
     * @return -
     */
    public static <T> String toJsonOrBlank(T object)
    {
        return toJson(object);
    }

    /**
     * @param json -
     * @param clazz -
     * @return -
     */
    public static <T> T fromJson(String json, Class<T> clazz)
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
     * @param json -
     * @param valueTypeRef -
     * @return -
     */
    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef)
    {
        try
        {
            return mapper.readValue(json, valueTypeRef);
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
     * @throws IOException -
     * @throws JsonMappingException - 
     * @throws JsonParseException  -
     */
    public static <T> T fromJson(TypeReference<T> type, String json) throws JsonParseException, JsonMappingException, IOException
    {
        return mapper.readValue(json, type);
    }

    /**
     * @param clazz -
     * @param json -
     * @return -
     */
    public static <T> T fromJsonOrNull(Class<T> clazz, String json)
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
     * @param clazz -
     * @param json -
     * @return -
     */
    public static <T> List<T> fromJsonArray(Class<T> clazz, String json)
    {
        try
        {
            List<T> results = new ArrayList<>();
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++)
            {
                results.add(fromJson(array.getString(i), clazz));
            }
            return results;
        }
        catch (Exception e)
        {
            return Collections.emptyList();
        }
    }

}
