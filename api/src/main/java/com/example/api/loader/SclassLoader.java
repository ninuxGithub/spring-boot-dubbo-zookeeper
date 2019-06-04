package com.example.api.loader;

import java.io.*;

/**
 *
 * slef define class loader
 * @author shenzm
 * @date 2019-6-4
 * @description 作用
 */
public class SclassLoader extends ClassLoader {

    private String classPath;

    public SclassLoader(ClassLoader parent,String classPath){
        super(parent);
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        FileInputStream fin = null;
        try {
            String rname = name.replace(".","\\");
            String path = classPath + rname + ".class";
            System.out.println("加载类的路径： "+path);
            fin = new FileInputStream(new File(path));
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int b;
            while ((b = fin.read()) != -1) {
                bout.write(b);
            }
            byte[] bytes = bout.toByteArray();
            return defineClass(name, bytes, 0, bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fin){
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
