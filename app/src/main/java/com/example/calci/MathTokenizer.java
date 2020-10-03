package com.example.calci;

import java.util.ArrayList;

public class MathTokenizer {
    String input;
    ArrayList<String> tokens;

    private int countTokens() {
        return tokens.size();
    }

    MathTokenizer(String s) {
        input = s;
        tokens = new ArrayList<>();
        parse();
    }

    private void parse() {
        System.out.println(input+'\n');
        int start = 0;
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (!Character.isDigit(ch) && ch!='.') {
                String sub=input.substring(start, i);
                if (sub.length()>0)
                    tokens.add(sub);
                tokens.add(String.valueOf(ch));
                start = i + 1;
            }
        }
        if (start<=input.length()-1)
            tokens.add(input.substring(start));

        for(String s:tokens)
        {
            if (s.charAt(0)=='.')
                s="0"+s;

            if (s.indexOf('.')!=s.lastIndexOf('.') || s.indexOf('.')==s.length()-1)
            {
                tokens=null;
                return;
            }
        }
    }
    public ArrayList<String> getTokens()
    {
        return tokens;
    }
}

