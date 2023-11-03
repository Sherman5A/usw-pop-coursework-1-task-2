package usw.employeepay;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        try {
            RateIO rateIO = new RateIO("rates.csv");

        } catch (IOException e) {
            System.out.println(e);
        }
        Scanner scanner = new Scanner(System.in);
        UserInterface userInput = new UserInterface(scanner);
        userInput.userLoop();

    }
}