package com.company;


import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        FileWriter fw = new FileWriter("out.txt", true);
        Object o = new Object();

        Thread t1 = new Thread(() -> {
            int temp1 = 1;
            int temp2 = 1;
            for (int i = 2; i < 15; i++) {
                int temp = temp2;
                temp2 += temp1;
                temp1 = temp;
            }

            synchronized (o) {
                try {
                    fw.write("фибоначи: " + temp2 + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            int sum = 0;
            try (BufferedReader br = new BufferedReader(new FileReader("numbers.txt"))) {
                for (int i = 0; i < 10; i++) {
                    sum += Integer.parseInt(br.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            synchronized (o) {
                try {
                    fw.write("сумма: " + sum + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t3 = new Thread(() -> {
            StringBuilder res = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader("test.txt"))) {
                res.append("номера: ");
                while (br.ready()) {
                    String x = br.readLine();
                    if (x.matches("\\+380\\d{9}")) {
                        res.append(x).append(", ");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            synchronized (o) {
                try {
                    fw.write(res.substring(0, res.length() - 2) + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        fw.close();
        System.out.println("Все операции завершены");

    }
}
