package io.security.springsecuritymaster;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class Test {

    public static void test() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        System.out.println("Please enter your email: ");
        String email = scanner.nextLine();
        System.out.println("Please enter your age: ");
        int age = scanner.nextInt();
        scanner.close();
    }

}
