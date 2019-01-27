package org.security.example.basicDemo.org.learn;

import org.junit.Test;

public class Math {

    @Test
    public  void test2b(){
        int three = 0b11;
        int rightOne = 0b1;
        int leftOne = 0b110;
        System.out.println(three>>1);
        System.out.println(rightOne);
        System.out.println(three<<1);
        System.out.println(leftOne);
    }
}
