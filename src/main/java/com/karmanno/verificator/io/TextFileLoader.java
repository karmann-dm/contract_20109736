package com.karmanno.verificator.io;

import com.karmanno.verificator.model.User;

import java.io.*;
import java.util.ArrayList;

public class TextFileLoader implements FileLoader {
    @Override
    public ArrayList<User> load(File file) throws Exception {
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

    @Override
    public void save(File file, Object object) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        ArrayList<User> models = (ArrayList<User>)object;

        for(User model : models) {
            writer.write(model.toString());
            writer.newLine();
        }
        writer.newLine();
        writer.close();
    }
}
