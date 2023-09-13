package test;

import java.util.Scanner;

public class cycle {
    public static void main(String[] args) {
        Boolean flag = true;
        int num = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入:::");
        int i = scanner.nextInt();
        do {
            if (i > 0){
                num = num +1;
                System.out.println(num);
            }else {
                flag = false;
            }
        }while (flag);
    }
}
