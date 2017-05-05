package com.ge.predix.solsvc.ext.util;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

public interface IJsonMapper {

	/**
	 * @param object -
	 * @return -
	 */
	<T> String toJsonOrBlank(T object);

	/**
	 * @param object -
	 * @return -
	 */
	<T> String toJson(T object);

	/**
	 * @param object -
	 * @return -
	 */
	<T> String toPrettyJson(T object);

	/**
	 * If you know the class to unmarshal to pass it in here.
	 * 
	 * 
	 * 
	 * @param json -
	 * @param clazz -
	 * @return -
	 */
	<T> T fromJson(String json, Class<T> clazz);

	/**
	 * If it's a json array, pass in the Class to unmarshal to for each element.
	 *
	 * Check if the Class is polymorphic and if so ensure json has a "complexType" attribute/value pair. Otherwise Jackson will be angry.
	 * 
	 * @param clazz -
	 * @param json -
	 * @return -
	 */
	<T> List<T> fromJsonArray(String json, Class<T> clazz);

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
	<P> List<P> fromJsonArray(String json, String clazz);

	/**
	 * @param type -
	 * @param json -
	 * @return -
	 */
	<T> T fromJsonType(TypeReference<T> type, String json);

	/**
	 * @param clazz -
	 * @param json -
	 * @return -
	 */
	<T> T fromJsonOrNull(Class<T> clazz, String json);

}