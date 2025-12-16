//git commit -m "Day 1: Implemented project structure, loadData(), mostProfitableCommodityInMonth(), and totalProfitOnDay()" Eljan - 20240602089

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;

    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June",
            "July","August","September","October","November","December"};

    static int[][][] profitData = new int[MONTHS][DAYS][COMMS];

    public static void loadData() {
       
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                for (int c = 0; c < COMMS; c++) {
                    profitData[m][d][c] = 0;
                }
            }
        }

        for (int m = 0; m < MONTHS; m++) {
            String fileName = "Data_Files/" + months[m] + ".txt";
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length != 3) continue;

                    int day = parseIntSafe(parts[0].trim(), -1) - 1;
                    String commodity = parts[1].trim();
                    int profit = parseIntSafe(parts[2].trim(), 0);

                    int cIndex = getCommodityIndex(commodity);
                    if (cIndex != -1 && day >= 0 && day < DAYS) {
                        profitData[m][day][cIndex] = profit;
                    }
                }
            } catch (IOException e) {
               
            }
        }
    }

    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month >= MONTHS) return "INVALID_MONTH";

        int bestC = 0;
        int bestSum = Integer.MIN_VALUE;

        for (int c = 0; c < COMMS; c++) {
            int sum = 0;
            for (int d = 0; d < DAYS; d++) {
                sum += profitData[month][d][c];
            }

            if (sum > bestSum) {
                bestSum = sum;
                bestC = c;
            }
        }
        return commodities[bestC] + " " + bestSum;
    }

    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month >= MONTHS || day < 1 || day > DAYS) return -99999;

        int d = day - 1;
        int total = 0;
        for (int c = 0; c < COMMS; c++) {
            total += profitData[month][d][c];
        }
        return total;
    }

    private static int getCommodityIndex(String commodity) {
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(commodity)) return i;
        }
        return -1;
    }

    private static int parseIntSafe(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return fallback;
        }
    }
}
