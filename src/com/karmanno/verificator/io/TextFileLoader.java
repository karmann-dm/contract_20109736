package com.karmanno.verificator.io;

import com.karmanno.verificator.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TextFileLoader {
    public static ArrayList<User> loadModels(String filename) throws IOException {
        ArrayList<User> users = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
        String line = reader.readLine();

        while (line != null) {
            String[] credentials = line.split(" ");
            users.add(new User(credentials[0], credentials[1]));
            line = reader.readLine();
        }

        return users;
    }
}
