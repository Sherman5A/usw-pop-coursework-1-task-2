package usw.employeepay;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

class UserInterfaceTest {

    @Test
    @DisplayName("Valid input in name field")
    void nameValidInput() {

        String dataIn = "Jake Real\n4324324\n423432";
        ByteArrayInputStream in = new ByteArrayInputStream(dataIn.getBytes());
        System.setIn(in);

        Scanner scanner = new Scanner(System.in);

        UserInterface userInput = new UserInterface(scanner);
        userInput.createEmployeeLoop();

    }
}
