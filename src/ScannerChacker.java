import java.util.*;

public class ScannerChacker {
    private int inputInt;
    Scanner in = new Scanner(System.in);

    public int checkIfInt() {
        boolean inputNotNull = true;

        while (inputNotNull) {
            String line = in.nextLine();

            try {
                inputInt = Integer.parseInt(line);
                inputNotNull = false;
            } catch (NumberFormatException e) {
                System.err.println("Wrong input! Input only integer numbers please: " + e.getMessage());
            }
        }

        return inputInt;
    }

    public int checkIfIntBetween(int min, int max) {
        boolean isBetween = false;
        int inputInt = 0;

        while (!isBetween) {
            inputInt = this.checkIfInt();

            if(inputInt >= min && inputInt <= max) {
                isBetween = true;
            } else {
                System.out.println("Please insert value between " + min + " and " + max);
            }
        }

        return inputInt;
    }

    public boolean converIntToBoolean() {
        boolean dataIsGood = false;
        boolean userDataIn = false;

        while (!dataIsGood) {
            int userInput = this.checkIfInt();

            if (userInput == 1) {
                dataIsGood = true;
                return true;
            } else if (userInput == 0) {
                dataIsGood = true;
                return false;
            } else {
                System.out.println("Please input 1 for True or 0 for False");
            }
        }

        return false;
    }

}
