package com.github.jokoziol.subnetting;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Subnetting {

    private static byte[] BINARY_IP_ADDRESS;
    private static byte[] BINARY_NET_MASK;

    private static int MAX_NETWORKS;

    private static final List<String> addressList = new ArrayList<>();

    public static void create(String ipAddress, String netMask, int subnetCount){

        if(!validIpAddress(ipAddress)){
            System.out.println("Your ip address is wrong");
            return;
        }

        if(!validNetMask(netMask)){
            System.out.println("Your subnet mask is wrong");
            return;
        }

        if(!validSubnetCount(subnetCount)){
            System.out.println("Your subnet count is wrong");
            return;
        }

        MAX_NETWORKS = subnetCount;

        System.out.println("\n" + generateNewIpAddressRange() + "\n");
        generateIpAddress();

    }

    private static String generateNewIpAddressRange(){

        String firstIpAddress;
        String lastIpAddress;

        int index = getBitIndex();

        for(int i = 0; i < BINARY_IP_ADDRESS.length; i++){
            if(i >= index){
                BINARY_IP_ADDRESS[i] = 0x00;
            }
        }

        firstIpAddress = byteToIpAddress(BINARY_IP_ADDRESS);

        for(int i = 0; i < BINARY_IP_ADDRESS.length; i++){
            if(i >= index){
                BINARY_IP_ADDRESS[i] = 0x01;
            }
        }

        lastIpAddress = byteToIpAddress(BINARY_IP_ADDRESS);

        return "Address range: " + firstIpAddress + " - " + lastIpAddress;
    }
    private static void generateIpAddress(){

        int index = getBitIndex();
        int count = 0;

        for(int i = 0; i < BINARY_NET_MASK.length; i++){
            if(i >= index){
                count ++;
            }
        }

        resetChangedIpAddress();

        for(int i = 0; i < MAX_NETWORKS; i++){
            String binary = Integer.toBinaryString(i);
            String filledBinary = String.format("%" + count + "s", binary).replace(" ", "0");
            String reversedBinary = new StringBuilder(filledBinary).reverse().toString();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            for(int j = 0; j < reversedBinary.length(); j++){
                byteArrayOutputStream.write(Integer.parseInt(Character.toString(reversedBinary.charAt(j))));
            }

            byte[] binaryMaxNetworks = byteArrayOutputStream.toByteArray();

            for(int j = 0; j < BINARY_IP_ADDRESS.length; j++){
                if(j >= index){
                    BINARY_IP_ADDRESS[j] = binaryMaxNetworks[j - index];
                }
            }

            addressList.add(byteToIpAddress(BINARY_IP_ADDRESS));

            for(int j = 0; j < BINARY_IP_ADDRESS.length; j++){
                if(j >= (index + binary.length())){
                    BINARY_IP_ADDRESS[j] = 0x01;
                }
            }

            resetChangedIpAddress();
        }

        addressList.sort((o1, o2) -> {
            String[] ip1 = o1.split("\\.");
            String[] ip2 = o2.split("\\.");

            String formattedIp1 = String.format("%3s.%3s.%3s.%3s", ip1[0], ip1[1], ip1[2], ip1[3]);
            String formattedIp2 = String.format("%3s.%3s.%3s.%3s", ip2[0], ip2[1], ip2[2], ip2[3]);

            return formattedIp1.compareTo(formattedIp2);
        });

        for(String item : addressList){
            System.out.println(item);
        }

    }

    private static boolean validIpAddress(String ipAddress){
        String[] split_ip_address = ipAddress.split("\\.");

        if(split_ip_address.length != 4){
            return false;
        }

        for(String item : split_ip_address){
            int number = Integer.parseInt(item);

            if(number < 0 || number > 255){
                return false;
            }
        }

        BINARY_IP_ADDRESS = stringToByte(split_ip_address);

        return true;
    }
    private static boolean validNetMask(String netMask){
        String[] split_subnet_mask = netMask.split("\\.");

        if(split_subnet_mask.length != 4){
            return false;
        }

        for(String item : split_subnet_mask){
            int number = Integer.parseInt(item);

            if(number < 0 || number > 255){
                return false;
            }
        }

        BINARY_NET_MASK = stringToByte(split_subnet_mask);

        boolean firstZero = false;

        for(byte item : BINARY_NET_MASK){
            if(item == 0x00 && !firstZero){
                firstZero = true;
            }

            if(item != 0x00 && firstZero){
                return false;
            }
        }
        return true;
    }
    private static boolean validSubnetCount(int subnetCount){

        int remainingBits = 32 - getBitIndex();
        int bit = Integer.toBinaryString(subnetCount).length();

        return bit <= remainingBits;
    }

    private static byte[] stringToByte(String[] str){
        StringBuilder binaryString = new StringBuilder();

        for(String octetString : str){
            int octet = Integer.parseInt(octetString);
            String filledBits = String.format("%8s", Integer.toBinaryString(octet)).replace(' ', '0');

            binaryString.append(filledBits);
        }

        String[] binaryStringArray = binaryString.toString().split("(?!^)(?=.)");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        for(String item : binaryStringArray){
            byteArrayOutputStream.write(Integer.parseInt(item));
        }

        return byteArrayOutputStream.toByteArray();
    }
    private static String byteToIpAddress(byte[] bytes){

        StringBuilder currentIpAddress = new StringBuilder();
        StringBuilder newIpAddress = new StringBuilder();

        for(byte item : bytes){
            currentIpAddress.append(String.valueOf(item));
        }

        newIpAddress.append(Integer.parseInt(currentIpAddress.substring(0, 8), 2)).append(".");
        newIpAddress.append(Integer.parseInt(currentIpAddress.substring(8, 16), 2)).append(".");
        newIpAddress.append(Integer.parseInt(currentIpAddress.substring(16, 24), 2)).append(".");
        newIpAddress.append(Integer.parseInt(currentIpAddress.substring(24, 32), 2));

        return newIpAddress.toString();
    }

    private static int getBitIndex(){
        int index = 0;

        for(int i = 0; i < BINARY_NET_MASK.length; i++){
            if(BINARY_NET_MASK[i] == 0x00){
                index = i;
                break;
            }
        }

        return index;
    }
    private static void resetChangedIpAddress(){
        int index = getBitIndex();

        for(int i = 0; i < BINARY_IP_ADDRESS.length; i++){
            if(i >= index){
                BINARY_IP_ADDRESS[i] = 0x00;
            }
        }
    }

}
