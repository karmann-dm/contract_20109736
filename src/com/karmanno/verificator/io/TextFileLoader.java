package com.karmanno.verificator.io;

import com.karmanno.verificator.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class TextFileLoader {
    public ArrayList<User> loadModels(File file) throws Exception {
        ArrayList<User> users = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();

        while (line != null) {
            String[] credentials = line.split(" ");

            if(credentials.length < 2)
                throw new RuntimeException("File has wrong format");

            users.add(new User(credentials[0], credentials[1]));
            line = reader.readLine();
        }

        return users;
    }
}
