/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */

public class AttributeCollection
{
    private static Map<String, Map<String, Map<String, Item>>> collection = new HashMap<>();
    private static List<String> attKeys = new ArrayList<>();
    
    public static <T extends Item> void add(String attrKey, String attrValue, T item)
    {
        ensureInitialization(attrKey, attrValue);
        collection.get(attrKey).get(attrValue).put(item.getItemId(), item);
    }
    
    public static <T extends Item> Map<String, Item> getCluster(String attrKey, String attrValue)
    {
        ensureInitialization(attrKey, attrValue);
        return collection.get(attrKey).get(attrValue);
    }
    
    private static void ensureInitialization(String attrKey, String attrValue)
    {
        if(!collection.containsKey(attrKey))
        {
            collection.put(attrKey, new HashMap<String, Map<String, Item>>());
        }
        if(!collection.get(attrKey).containsKey(attrValue))
        {
            collection.get(attrKey).put(attrValue, new HashMap<String, Item>());
        }
    }
    
    public static List<String> getAttributeKeys()
    {
        if(attKeys.isEmpty())
        {
            for(String key : collection.keySet())
            {
                attKeys.add(key);
            }
        }
        return attKeys;
    }
    
    public static List<String> getAttributeValues(String attKey)
    {
        List<String> attValues = new ArrayList<>();
        for(String value : collection.get(attKey).keySet())
        {
            attValues.add(value);
        }
        return attValues;
    }
    
    public static int size()
    {
        return collection.size();
    }
    
    public static boolean isEmpty()
    {
        return collection.isEmpty();
    }
    
    public static void clear()
    {
        collection.clear();
    }

}
