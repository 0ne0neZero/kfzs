package com.wishare.finance.infrastructure.utils;

public class NhwReceiptNoUtil {
  
    public static void main(String[] args) {  
        String originalNumberStr = "000000031";  
        String incrementedNumberStr = incrementNumberAndKeepLeadingZeros(originalNumberStr);  
        System.out.println(incrementedNumberStr); // 输出: 000000032

        System.out.println(getFirstNo());
    }  
  
    public static String incrementNumberAndKeepLeadingZeros(String numberStr) {  
        // 将字符串转换为长整型  
        long number = Long.parseLong(numberStr);  
        // 加一  
        number++;  
          
        // 确定原始字符串的长度（包括前导零）  
        int originalLength = numberStr.length();  
          
        // 使用String.format来格式化输出，确保有足够的前导零  
        // 注意：%0<length>d中的<length>是变量，表示原始字符串的长度  
        return String.format("%0" + originalLength + "d", number);  
    }

    public static String getFirstNo() {
        return String.format("%09d", 1);
    }
}