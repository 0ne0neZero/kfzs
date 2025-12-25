package com.wishare.contract.utiltest;

import com.wishare.contract.infrastructure.utils.build.Builder;

import java.util.HashMap;

/**
 * 功能解释
 *
 * @author long
 * @date 2023/7/25 9:11
 */
public class BuilderTest {
    public static void main(String[] args) {
        HashMap<Object, Object> hashMap = Builder.of(HashMap::new)
                .with(HashMap::put, 1, "a")
                .with(HashMap::put, 2, "b")
                .build();
        hashMap.forEach((k, v) -> {
            System.out.print(k);
            System.out.println(v);
        });
    }
}
