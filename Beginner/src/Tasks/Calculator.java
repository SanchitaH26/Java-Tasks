//Beginner level-Task 1:Enhanced Console-Based Calculator 
package Tasks;
import java.util.*;
import java.io.IOException;
import java.lang.Math;

public class Calculator {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("Enhanced Console-based Calculator");
        char op;

        do {
            System.out.println("\nChoose Calculator Type:\nA - Arithmetic Calculator\nS - Scientific Calculator\nU - Unit Conversion\nE - Exit");

            op = sc.next().charAt(0);
            switch (Character.toLowerCase(op)) {
                case 'a':
                    performArithmetic();
                    break;
                case 's':
                    performScientific();
                    break;
                case 'u':
                    performUnitConversion();
                    break;
                case 'e':
                    System.out.println("Exiting Calculator. Goodbye!");
                    
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }while(Character.toLowerCase(op)!='e');
    }

    public static void performArithmetic() {
        try {
            System.out.println("Enter two numbers:");
            int num1 = sc.nextInt();
            int num2 = sc.nextInt();

            System.out.println("Select operation:\n1 - Addition\n2 - Subtraction\n3 - Multiplication\n4 - Division");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Addition: " + (num1 + num2));
                    break;
                case 2:
                    System.out.println("Subtraction: " + (num1 - num2));
                    break;
                case 3:
                    System.out.println("Multiplication: " + (num1 * num2));
                    break;
                case 4:
                    try {
                        System.out.println("Division: " + (num1 / num2));
                    } catch (ArithmeticException e) {
                        System.out.println("Error: Cannot divide by zero."+e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Invalid arithmetic choice.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter numbers only.");
            sc.nextLine();
        }
    }

    public static void performScientific() {
        System.out.println("Select operation:\n5 - Square root\n6 - Cube root\n7 - Power\n8 - Sine\n9 - Cosine\n10 - Tangent\n11 - Inverse Sine\n12 - Inverse Cosine\n13 - Inverse Tangent\n14 - Natural log\n15 - Base 10 log\n16 - Logarithm with any base\n17 - Exponential (e^x)");

        int choice = sc.nextInt();
        double num, rad;

        switch (choice) {
            case 5:
                num = getDouble("Enter number:");
                System.out.println("Square root: " + Math.sqrt(num));
                break;
            case 6:
                num = getDouble("Enter number:");
                System.out.println("Cube root: " + Math.cbrt(num));
                break;
            case 7:
                double base = getDouble("Enter base:");
                double exp = getDouble("Enter exponent:");
                System.out.println("Result: " + Math.pow(base, exp));
                break;
            case 8:
                rad = Math.toRadians(getDouble("Enter degrees:"));
                System.out.println("Sine: " + Math.sin(rad));
                break;
            case 9:
                rad = Math.toRadians(getDouble("Enter degrees:"));
                System.out.println("Cosine: " + Math.cos(rad));
                break;
            case 10:
                rad = Math.toRadians(getDouble("Enter degrees:"));
                System.out.println("Tangent: " + Math.tan(rad));
                break;
            case 11:
                rad = Math.toRadians(getDouble("Enter degrees:"));
                System.out.println("Inverse Sine: " + Math.asin(rad));
                break;
            case 12:
                rad = Math.toRadians(getDouble("Enter degrees:"));
                System.out.println("Inverse Cosine: " + Math.acos(rad));
                break;
            case 13:
                rad = Math.toRadians(getDouble("Enter degrees:"));
                System.out.println("Inverse Tangent: " + Math.atan(rad));
                break;
            case 14:
                num = getDouble("Enter number:");
                System.out.println("Natural log: " + Math.log(num));
                break;
            case 15:
                num = getDouble("Enter number:");
                System.out.println("Base 10 log: " + Math.log10(num));
                break;
            case 16:
                double n = getDouble("Enter number:");
                double b = getDouble("Enter base:");
                System.out.println("Logarithm: " + Math.log(n) / Math.log(b));
                break;
            case 17:
                num = getDouble("Enter number:");
                System.out.println("e^" + num + " = " + Math.exp(num));
                break;
            default:
                System.out.println("Invalid scientific choice.");
        }
    }

    public static void performUnitConversion() {
        System.out.println("Select conversion:\n18 - Meters to Kilometers\n19 - Kilometers to Miles\n20 - Kilometers to Meters\n21 - Miles to Kilometers\n22 - Celsius to Fahrenheit\n23 - Fahrenheit to Celsius\n24 - Kilograms to Pounds\n25 - Pounds to Kilograms");

        int choice = sc.nextInt();
        double value;

        switch (choice) {
            case 18:
                value = getDouble("Enter meters:");
                System.out.println("Result: " + (value / 1000) + " km");
                break;
            case 19:
                value = getDouble("Enter kilometers:");
                System.out.println("Result: " + (value * 0.621371) + " miles");
                break;
            case 20:
                value = getDouble("Enter kilometers:");
                System.out.println("Result: " + (value * 1000) + " meters");
                break;
            case 21:
                value = getDouble("Enter miles:");
                System.out.println("Result: " + (value * 1.60934) + " km");
                break;
            case 22:
                value = getDouble("Enter Celsius:");
                System.out.println("Result: " + ((value * 9 / 5) + 32) + " °F");
                break;
            case 23:
                value = getDouble("Enter Fahrenheit:");
                System.out.println("Result: " + ((value - 32) * 5 / 9) + " °C");
                break;
            case 24:
                value = getDouble("Enter kilograms:");
                System.out.println("Result: " + (value * 2.20462) + " lbs");
                break;
            case 25:
                value = getDouble("Enter pounds:");
                System.out.println("Result: " + (value * 0.453592) + " kg");
                break;
            default:
                System.out.println("Invalid conversion choice.");
        }
    }

    public static double getDouble(String message) {
        System.out.println(message);
        while (!sc.hasNextDouble()) {
            System.out.println("Invalid input! Please enter a number.");
            sc.next(); 
        }
        return sc.nextDouble();
    }
}
