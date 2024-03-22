package main;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The type Reader.
 */
public class Reader {

    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));


    /**
     * Gets string answer.
     *
     * @param question the question
     * @return the string answer
     */
    public String getStringAnswer(String question) {
        try {
            System.out.println(question);
            return in.readLine();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Gets int answer.
     *
     * @param question the question
     * @return the int answer
     */
    public int getIntAnswer(String question) {
        do {
            try {
                System.out.println(question);
                return Integer.parseInt(in.readLine());
            } catch (Exception e) {
                System.out.println("Incorrect Input! Please use Numbers only!");
            }
        } while (true);
    }

    /**
     * Gets double answer.
     *
     * @param question the question
     * @return the double answer
     */
    public double getDoubleAnswer(String question) {
        do {
            try {
                System.out.println(question);
                return Double.parseDouble(in.readLine());
            } catch (Exception e) {
                System.out.println("Incorrect Input! Please use Numbers only!");
            }
        } while (true);
    }

    /**
     * Gets password.
     *
     * @param question the question
     * @return the password
     */
    public String getPassword(String question) {
        do {
            try {
                System.out.println(question);
                String pass = in.readLine();
                System.out.println("Repeat Password:");
                String checkPass = in.readLine();
                if (pass.equals(checkPass)) {
                    return pass;
                } else {
                    System.out.println("Password Does not Equal. Try Again!");
                }
            } catch (Exception ignored) {
            }
        } while (true);
    }
}
