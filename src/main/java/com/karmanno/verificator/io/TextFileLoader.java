package com.karmanno.verificator.io;

import com.karmanno.verificator.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextFileLoader {
    public ArrayList<User> loadModels(File file) throws Exception {
        ArrayList<User> users = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();

        while (line != null) {
            String[] credentials = line.split(":");

            if(credentials.length < 2)
                throw new RuntimeException("File has wrong format");

            users.add(new User(credentials[0], credentials[1]));
            line = reader.readLine();
        }

        reader.close();

        return users;
    }

    public void saveModels(File file, List<User> models) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for(User model : models) {
            writer.write(model.toString());
            writer.newLine();
        }
        writer.newLine();
        writer.close();
    }
}
