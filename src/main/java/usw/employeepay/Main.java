package usw.employeepay;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Scanner scanner = new Scanner(System.in);
        UserInterface userInput = new UserInterface(scanner);
        userInput.userLoop();

    }
}