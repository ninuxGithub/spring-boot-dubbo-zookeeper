package com.example.api.mode.prototype;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class ShapeCache {

    public static Map<String, Shape> shapeCache = new HashMap<>();

    public static Shape getShape(String type) {
        Shape shape = shapeCache.get(type);
        return (Shape) shape.clone();
    }


    public static <T extends Serializable> T copy(Shape input) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(input);
            oos.flush();

            byte[] bytes = baos.toByteArray();
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            Object result = ois.readObject();
            return (T) result;
        } catch (IOException e) {
            throw new IllegalArgumentException("Object can't be copied", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to reconstruct serialized object due to invalid class definition", e);
        } finally {
            closeQuietly(oos);
            closeQuietly(baos);
            closeQuietly(bis);
            closeQuietly(ois);
        }

    }

    private static void closeQuietly(AutoCloseable ac) {
        if (ac != null) {
            try {
                ac.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadCache() {
        Circle circle = new Circle();
        System.out.println("loadCache color hash " + circle.hashCode());
        System.out.println("loadCache color type hash " + circle.type.hashCode());
        shapeCache.put("1", circle);
        shapeCache.put("2", new Rectangle());
        shapeCache.put("3", new Square());
    }
}
