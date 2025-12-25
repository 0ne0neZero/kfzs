package com.wishare.contract;

import com.baomidou.mybatisplus.generator.WishareFastAutoGenerator;

import java.io.IOException;

public class Test01 {
    public static void main(String[] args) throws IOException {
        WishareFastAutoGenerator.generate("jdbc:mysql://10.100.50.31:3380/wishare_contract",
                "huixiang",
                "Ka9Gd&gsQzqFS",
                "revision.income",
                null,
                false,
                "chenglong",
                "contract_income_conclude_expand"
        );
    }
}
