package com.wishare.finance.apps.service.yuanyang;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
public class OrderTitleGet100FirstStringSplitter {

    private static List<String> splitByNonChinese(String input) {
        List<String> result = new ArrayList<>();
        StringBuilder currentSegment = new StringBuilder();
        boolean lastCharWasChinese = isChinese(input.charAt(0));

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (isChinese(ch)) {
                if (!lastCharWasChinese && currentSegment.length() > 0) {
                    result.add(currentSegment.toString());
                    currentSegment.setLength(0);
                }
                currentSegment.append(ch);
                lastCharWasChinese = true;
            } else {
                if (lastCharWasChinese && currentSegment.length() > 0) {
                    result.add(currentSegment.toString());
                    currentSegment.setLength(0);
                }
                currentSegment.append(ch);
                lastCharWasChinese = false;
            }
        }

        if (currentSegment.length() > 0) {
            result.add(currentSegment.toString());
        }

        return result;
    }

    public static String getFirst100CharactersNoExc(String input) {
        if (StrUtil.isBlank(input)){
            return input;
        }
        try {
            List<String> segments = splitByNonChinese(input);
            int sum = 0;
            StringBuilder result = new StringBuilder();

            for (String segment : segments) {
                if (isChinese(segment.charAt(0))) {

                    int length = (segment.length()+1) * 2;
                    if (sum + length > 100) {
                        int remainingLength = 100 - sum;
                        if (length>=100 && sum==0){
                            result.append(segment.substring(0, 49));
                            break;
                        }
                        result.append(segment.substring(0, remainingLength / 2));
                        break;
                    } else {
                        result.append(segment);
                        sum += length;
                    }
                } else {

                    int length = segment.length();
                    if (sum + length > 100) {
                        int remainingLength = 100 - sum;
                        result.append(segment.substring(0, remainingLength));
                        break;
                    } else {
                        result.append(segment);
                        sum += length;
                    }
                }

                if (sum >= 100) {
                    break;
                }
            }

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
           log.error("OrderTitleGet100FirstStringSplitter异常:{}",e.getMessage());
        }
        return input;
    }

    private static boolean isChinese(char ch) {
        return ch >= '\u4e00' && ch <= '\u9fff';
    }

    public static void main(String[] args) {

        String input = "南京地铁11号线一期工程施工总承包D.011.X-TA03标项目"; // 58
        String input1 = "中国水电基础局有限公司武清区水系连通工程（三期）施工（第2标段）项目部";//77
        String input2 = "中国水电基础局有限公司武清区水系连通工程中国水电基础局有限公司武清区水系连通工程中国水电基础局有限公的hiR1111";//首字符串超102
        String input3 = "中国水电基础局有限公司武清区水系连通工程中国水电基础局有限公司武清区水系连通工程中国水电基础局限公R1111";//首字符串100
        String input4 = "中国水电基础局有限公司武清区水系连通工程中国水电基础局有限公司武清区水系连通工程中国水电基础局限R1111";//98
        String input5 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA99中国馆";//99
        String input6 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA100ANo中国馆";//100+行字
        String input7 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA的个股事AAAAAAAAAAAAAAAAAAAA挨打的份挨打的份挨打的份挨打的份第逗号100A中国馆";//100+数字
        System.out.println(getFirst100CharactersNoExc(input));
        System.out.println(getFirst100CharactersNoExc(input1));
        System.out.println(getFirst100CharactersNoExc(input2));
        System.out.println(getFirst100CharactersNoExc(input3));
        System.out.println(getFirst100CharactersNoExc(input4));
        System.out.println(getFirst100CharactersNoExc(input5));
        System.out.println(getFirst100CharactersNoExc(input6));
        System.out.println(getFirst100CharactersNoExc(input7));
        // Test case 3: Exactly 100 characters
        String input33 = "abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxy";
        assertEquals(input33, OrderTitleGet100FirstStringSplitter.getFirst100CharactersNoExc(input33));


    }



}