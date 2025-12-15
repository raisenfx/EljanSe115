// AutoGrader.java – A sample of the automated test script that will be used to grade each students Main.java file
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

public class AutoGrader {
    static final int TOTAL_TESTS = 10;

     // There will be a total of 100 tests in the full version during grading.
     // This sample version tests each function with a dummy test value, 
     // so students can see if their version of Main.java is compatible with the final AutoGrader test script.
     // When students implement the functions, the tests with dummy values of course will fail.
    static final Object[][] TESTS = {
        {"mostProfitableCommodityInMonth", new Object[]{0}, "DUMMY"},
        {"totalProfitOnDay", new Object[]{0, 15}, 1234},
        {"commodityProfitInRange", new Object[]{"Gold", 1, 14}, 1234},
        {"bestDayOfMonth", new Object[]{0}, 1234},
        {"bestMonthForCommodity", new Object[]{"Gold"}, "DUMMY"},
        {"consecutiveLossDays", new Object[]{"Gold"}, 1234},
        {"daysAboveThreshold", new Object[]{"Gold", 2000}, 1234},
        {"biggestDailySwing", new Object[]{0}, 1234},
        {"compareTwoCommodities", new Object[]{"Gold", "Oil"}, "DUMMY is better by 1234"},
        {"bestWeekOfMonth", new Object[]{20}, "DUMMY"},
    };

    public static void main(String[] args) throws Exception {
        int score = 0;
        try {
            score = testStudent();
        } catch (Exception e) {
            System.out.println("Expected file or folder not found or compile error! THIS SHOULD NOT HAPPEN" + e);
        }

        System.out.println("\nAll tests completed.");
    }

    // Compile student version of Main.java and perform all tests on this Main.    
    static int testStudent() throws Exception {
        // compile Main.java
        Process compile = Runtime.getRuntime().exec("javac Main.java");
        if (compile.waitFor() != 0) {
            System.out.println("COMPILE ERROR - THIS SHOULD NOT HAPPEN");
            return 0;
        }

        URL url = new File("").toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{url});
        Class<?> cls = loader.loadClass("Main");
        cls.getMethod("loadData").invoke(null);
        
        int score = 0;
        
        for (int i = 0; i < TESTS.length; i++) {
            String methodName = (String) TESTS[i][0];
            Object[] paramsRaw = (Object[]) TESTS[i][1];
            Object expected = TESTS[i][2];

            try {
                Object[] invokeParams = new Object[paramsRaw.length];
                Class<?>[] paramTypes = new Class<?>[paramsRaw.length];

                for (int j = 0; j < paramsRaw.length; j++) {
                    Object param = paramsRaw[j];

                    if (param == null || "null".equals(param)) {
                        invokeParams[j] = null;
                        paramTypes[j] = String.class;
                    } 
                    else if (param instanceof String) {
                        String s = (String) param;
                        if (s.matches("-?\\d+")) {
                            invokeParams[j] = Integer.parseInt(s);
                            paramTypes[j] = int.class;
                        } else {
                            invokeParams[j] = s;
                            paramTypes[j] = String.class;
                        }
                    } 
                    else {
                        invokeParams[j] = param;
                        paramTypes[j] = int.class;
                    }
                }

                Method m = cls.getMethod(methodName, paramTypes);
                Object result = m.invoke(null, invokeParams);

                boolean correct = Objects.equals(expected, result) ||
                        (expected instanceof Number && result instanceof Number &&
                         ((Number)expected).intValue() == ((Number)result).intValue());

                if (correct) {
                    score++;
                    System.out.println("Test " + (i+1) + " PASS");
                } else {
                    System.out.println("Test " + (i+1) + " FAIL - Got: " + result + " | Expected: " + expected);
                }

            } catch (Exception e) {
                if (expected instanceof String && ((String)expected).contains("INVALID") ||
                    expected.equals(-99999) || expected.equals(-1)) {
                    score++;
                    System.out.println("Test " + (i+1) + " PASS (trap test – exception expected)");
                } else {
                    System.out.println("Test " + (i+1) + " EXCEPTION - " + e.toString());
                }
            }
        }

        loader.close();
        return score;
    }
}
