package com.golden.ms.utils;

import com.golden.ms.model.User;

import java.io.*;
import java.util.Map;

public class FileUtil {
    public static void writeMapToFile(Map<Integer, User> map){
        try {
            FileOutputStream fos = new FileOutputStream("temp_db");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
            oos.close();
            fos.close();

            System.out.println("Map was serialized to file.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, User> readMapFromFile(){
        try {
            FileInputStream fis = new FileInputStream("temp_db");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Map<Integer, User> deserializedMap = (Map<Integer, User>) ois.readObject();
            ois.close();
            fis.close();

            return deserializedMap;
        }
        catch (FileNotFoundException e){
            System.out.println("DB File is not initialized");
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
