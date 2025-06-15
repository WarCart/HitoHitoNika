package net.warcar.hito_hito_nika.helpers;

import net.minecraft.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.api.enums.HakiType;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiDataCapability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquationHelper {
    protected static final Map<String, Operator> operators;
    static {
        operators = new HashMap<>();
        //User based
        operators.put("doriki", (user, numbers) -> EntityStatsCapability.get(user).getDoriki());
        operators.put("totalHakiXp", (user, numbers) -> hakixp(user, HakiType.HAOSHOKU));
        operators.put("busoHakiXp", (user, numbers) -> hakixp(user, HakiType.BUSOSHOKU));
        operators.put("kenHakiXp", (user, numbers) -> hakixp(user, HakiType.KENBUNSHOKU));
        //Math
        //Unlimited
        operators.put("multiply", (user, numbers) -> multiply(numbers));
        operators.put("add", (user, numbers) -> add(numbers));
        operators.put("min", (user, numbers) -> min(numbers));
        operators.put("max", (user, numbers) -> max(numbers));
        //Fixed
        operators.put("negate", (user, numbers) -> negate(numbers));
        operators.put("power", (user, numbers) -> power(numbers));
        operators.put("divide", (user, numbers) -> divide(numbers));
        operators.put("sqrt", (user, numbers) -> sqrt(numbers));
        operators.put("subtract", (user, numbers) -> subtract(numbers));
    }

    private static double subtract(double[] numbers) {
        return numbers[0] - numbers[1];
    }

    private static double max(double[] numbers) {
        double max = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }
        return max;
    }

    private static double min(double[] numbers) {
        double min = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] < min) {
                min = numbers[i];
            }
        }
        return min;
    }

    private static double hakixp(LivingEntity user, HakiType hakiType) {
        switch (hakiType) {
            case HAOSHOKU:
                return HakiDataCapability.get(user).getTotalHakiExp();
            case BUSOSHOKU:
                return HakiDataCapability.get(user).getBusoshokuHakiExp();
            case KENBUNSHOKU:
            default:
                return HakiDataCapability.get(user).getKenbunshokuHakiExp();
        }
    }

    private static double sqrt(double[] numbers) {
        return Math.sqrt(numbers[0]);
    }

    private static double divide(double[] numbers) {
        return numbers[0] / numbers[1];
    }

    private static double power(double[] numbers) {
        return Math.pow(numbers[0], numbers[1]);
    }

    private static double negate(double[] numbers) {
        return -numbers[0];
    }

    private static double add(double[] numbers) {
        double result = 0;
        for (double number : numbers) {
            result += number;
        }
        return result;
    }

    private static double multiply(double[] numbers) {
        double result = 1;
        for (double number : numbers) {
            result *= number;
        }
        return result;
    }

    public static ParseResult parseEquation(String equation, LivingEntity entity, Map<String, Double> bonus) {
        equation = equation.replace(" ", "");
        int firstBracket = equation.indexOf('(');
        String op = equation.substring(0, firstBracket);
        if (op.equalsIgnoreCase("n")) {
            String num = equation.substring(firstBracket + 1, equation.indexOf(")"));
            //HitoHitoNoMiNikaMod.LOGGER.info("Flat number: {} with equation {}", num, equation);
            return new ParseResult(3 + num.length(), Float.parseFloat(num));
        } else if (bonus.containsKey(op)) {
            //HitoHitoNoMiNikaMod.LOGGER.info("Remapped from bonus {}: {}", op, bonus.get(op));
            return new ParseResult(op.length() + 2, bonus.get(op));
        }
        Operator operator = operators.get(op);
        int currentPos = firstBracket;
        List<Double> numbers = new ArrayList<>();
        while (equation.length() > currentPos && equation.charAt(currentPos) != ')') {
            String equation1 = equation.substring(currentPos + 1);
            if (equation1.startsWith(")")) {
                break;
            }
            ParseResult parseResult = parseEquation(equation1, entity, bonus);
            numbers.add(parseResult.getValue());
            currentPos += parseResult.position + 1;
        }
        if (numbers.isEmpty()) {
            currentPos++;
        }
        double[] numbersArray = new double[numbers.size()];
        for (int i = 0; i < numbersArray.length; i++) {
            numbersArray[i] = numbers.get(i);
        }
        double value = operator.apply(entity, numbersArray);
        //HitoHitoNoMiNikaMod.LOGGER.info("returning {}: {} (equation is {})", op, value, equation);
        return new ParseResult(currentPos + 1, value);
    }

    @FunctionalInterface
    public interface Operator {
        double apply(LivingEntity user, double... numbers);
    }

    public static class ParseResult {
        private final int position;
        private final double value;

        public ParseResult(int position, double value) {
            this.position = position;
            this.value = value;
        }

        public double getValue() {
            return value;
        }
    }
}
