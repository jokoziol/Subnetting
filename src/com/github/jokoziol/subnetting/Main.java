package com.github.jokoziol.subnetting;

import java.util.Scanner;

public class Main {

    private static String IP_ADDRESS = "";
    private static String NET_MASK = "";
    private static String SUBNET_COUNT = "";

    public static void main(String[] args) {
        getIpAddress();
        getNetMask();
        getSubnetCount();

        Subnetting.create(IP_ADDRESS, NET_MASK, Integer.parseInt(SUBNET_COUNT));
    }

    private static void getIpAddress(){

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ip address: ");
        IP_ADDRESS = scanner.nextLine();
    }

    private static void getNetMask(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the net mask: ");
        NET_MASK = scanner.nextLine();
    }

    private static void getSubnetCount(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of subnets: ");
        SUBNET_COUNT = scanner.nextLine();
    }

}
