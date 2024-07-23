package com.cn.demo.controller;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateCsv {

    private static final String[] GAME_NAMES = {
            "GameNameOne", "GameNameTwo", "GameNameThree", "GameNameFour", "GameNameFive",
            "GameNameSix", "GameNameSeven", "GameNameEight", "GameNameNine", "GameNameTen"
    };
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new Random();

    // generate csv file
    public static void main(String[] args) {
        String fileName = "games.csv";
        int numRows = 1000000;
        String[] header = {"id", "game_no", "game_name", "game_code", "type", "cost_price", "tax", "sale_price", "date_of_sale"};
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            writer.append("id, game_no, game_name, game_code, type, cost_price, tax, sale_price, date_of_sale\n");

            for (int i = 0; i < numRows; i++) {
                int id = i;
                int gameNo = RANDOM.nextInt(100) + 1;
                String gameName = GAME_NAMES[RANDOM.nextInt(GAME_NAMES.length)];
                String gameCode = generateGameCode(RANDOM.nextInt(5) + 1);
                int type = RANDOM.nextInt(2) + 1;
                double costPrice = generateCostPrice();
                double tax = 0.09;
                double salePrice = costPrice + costPrice * 0.09;
                Long dateOfSale = generateRandomTimestamp();


                writer.append(String.valueOf(id + 1)).append(",").append(String.valueOf(gameNo)).append(",")
                        .append(gameName).append(",").append(gameCode).append(",").append(String.valueOf(type))
                        .append(",").append(String.format("%.2f", costPrice)).append(",").append(String.format("%.2f", tax))
                        .append(",").append(String.format("%.2f", salePrice)).append(",").append(String.valueOf(dateOfSale)).append("\n");

                System.out.println(i);
            }

            System.out.println("CSV file created successfully.");

        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    private static String generateGameCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    private static double generateCostPrice() {
        return 100 * RANDOM.nextDouble();
    }

    private static Long generateRandomTimestamp() {
        long startMillis = new Date(124, 3, 1).getTime(); // 2024-04-01
        long endMillis = new Date(124, 3, 30).getTime(); // 2024-04-30
        long rs = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
        return rs;
    }


    public static void main1(String[] args) {
        System.out.println(generateRandomTimestamp());
    }


}
