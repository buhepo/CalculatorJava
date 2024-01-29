import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Бесконечный цикл
        while (true) {
            // Ввод выражения с консоли
            System.out.print("Введите арифметическое выражение (или 'stop' для завершения): ");
            String input = scanner.nextLine();

            // Проверка на выход из программы
            if (input.equalsIgnoreCase("stop")) {
                System.out.println("Программа завершена.");
                break; // Выход из цикла
            }

            // Вычисление и вывод результата
            String result = calc(input);
            System.out.println("Результат: " + result);
        }
    }

    public static String calc(String input) {
        Map<String, Integer> romanNumerals = new HashMap<>();
        romanNumerals.put("I", 1);
        romanNumerals.put("II", 2);
        romanNumerals.put("III", 3);
        romanNumerals.put("IV", 4);
        romanNumerals.put("V", 5);
        romanNumerals.put("VI", 6);
        romanNumerals.put("VII", 7);
        romanNumerals.put("VIII", 8);
        romanNumerals.put("IX", 9);
        romanNumerals.put("X", 10);

        String[] tokens = input.split(" ");
        Stack<Integer> numbers = new Stack<>();
        Stack<String> operations = new Stack<>();
        int operatorsCount = 0;
        boolean isRoman = false;
        boolean isArabic = false;

        for (String token : tokens) {
            if (isNumber(token, romanNumerals)) {
                try {
                    int num = parseNumber(token, romanNumerals);
                    numbers.push(num);

                    if (isRoman(token)) {
                        isRoman = true;
                    } else {
                        isArabic = true;
                    }

                } catch (Exception e) {
                    return "Ошибка: " + e.getMessage();
                }
            } else if (isOperator(token)) {
                operatorsCount++;
                while (!operations.isEmpty() && hasHigherPrecedence(operations.peek(), token)) {
                    applyOperation(numbers, operations);
                }
                operations.push(token);
            }
        }

        if (tokens.length <= 2) {
            return "Ошибка: строка не является математической операцией";
        }

        if (operatorsCount != 1) {
            return "Ошибка: можно использовать только два операнда и один оператор (+, -, *, /)";
        }

        while (!operations.isEmpty()) {
            applyOperation(numbers, operations);
        }

        // Проверка на наличие смешивания римских и арабских чисел
        if (isRoman && isArabic) {
            return "Ошибка: использование римских и арабских чисел в одном выражении недопустимо";
        }

        return String.valueOf(numbers.pop());
    }

    private static boolean isNumber(String input, Map<String, Integer> romanNumerals) {
        return isRoman(input) || isArabic(input);
    }

    private static boolean isArabic(String input) {
        return input.matches("\\d+");
    }

    private static boolean isOperator(String input) {
        return "+".equals(input) || "-".equals(input) || "*".equals(input) || "/".equals(input);
    }

    private static int parseNumber(String input, Map<String, Integer> romanNumerals) throws Exception {
        if (isRoman(input)) {
            if (romanNumerals.containsKey(input)) {
                return romanNumerals.get(input);
            } else {
                throw new Exception("Недопустимое римское число");
            }
        } else {
            int num = Integer.parseInt(input);
            if (num < 1 || num > 10) {
                throw new Exception("Число должно быть от 1 до 10 включительно");
            }
            return num;
        }
    }

    private static boolean hasHigherPrecedence(String op1, String op2) {
        return ("*".equals(op1) || "/".equals(op1)) && ("+".equals(op2) || "-".equals(op2));
    }

    private static void applyOperation(Stack<Integer> numbers, Stack<String> operations) {
        int num2 = numbers.pop();
        int num1 = numbers.pop();
        String op = operations.pop();
        int result;
        switch (op) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 == 0) {
                    throw new ArithmeticException("Деление на ноль");
                }
                result = num1 / num2;
                break;
            default:
                throw new IllegalArgumentException("Недопустимая операция");
        }
        numbers.push(result);
    }

    private static boolean isRoman(String input) {
        return input.matches("[IVX]+");
    }
}
