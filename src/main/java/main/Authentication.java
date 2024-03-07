package main;

import main.Operator;
import user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class Authentication {/*
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    public boolean auth(Operator operator) throws IOException {
        System.out.println("Login[1]");
        System.out.println("Register[2]");
        User user = new User();
        int input = Integer.parseInt(in.readLine());
        if (input == 1) {
            return login(operator);
        } else if (input == 2) {
            return register();
        } else {
            System.out.println("Diese Funktion existiert nicht!");
        }
        return false;
    }

    private boolean login() throws IOException{
        String username = in.readLine();
        String pass = in.readLine();
        for (Optional<User> user : IUser.getAll())
        if (username.equals(user.get().username) && pass.equals(user.get().password)) {

            return true;
        }
    }

    private boolean register() {
        return false;
    }
   */
}
