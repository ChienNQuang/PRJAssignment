/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.utils;

/**
 *
 * @author Ezarp
 */
public class StringHelper {
    private StringHelper() {
    }
    
    private final static CharSequence CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static String randomString(int n) {
        StringBuilder sb = new StringBuilder(n);
        int length = CHARACTERS.length();
        
        for (int i = 0; i < n; ++i) {
            int k = (int) Math.round(Math.random() * (length - 1));
            sb.append(CHARACTERS.charAt(k));
        }
        
        return sb.toString();
    }
    
    public static String randomOrderId() {
        return randomString(5);
    }
}