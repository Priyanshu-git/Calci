package com.example.calci;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CalculatorFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "CalculatorFragment";

    StringBuilder input, top;
    TextView disp, disp2;
    View layout;
    boolean eqlFlag = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout =inflater.inflate(R.layout.fragment_calculator, container,false);
        listener();
        disp =  layout.findViewById(R.id.display);
        disp2 = layout.findViewById(R.id.top);
        if (savedInstanceState == null) {
            input = new StringBuilder();
            top = new StringBuilder();
        } else {
            disp.setText(savedInstanceState.getString("disp1"));
            disp2.setText(savedInstanceState.getString("disp2"));
        }
        return layout;
    }

    private void listener() {
        layout.findViewById(R.id.b1).setOnClickListener(this);
        layout.findViewById(R.id.b0).setOnClickListener(this);
        layout.findViewById(R.id.b2).setOnClickListener(this);
        layout.findViewById(R.id.b3).setOnClickListener(this);
        layout.findViewById(R.id.b4).setOnClickListener(this);
        layout.findViewById(R.id.b5).setOnClickListener(this);
        layout.findViewById(R.id.b6).setOnClickListener(this);
        layout.findViewById(R.id.b7).setOnClickListener(this);
        layout.findViewById(R.id.b8).setOnClickListener(this);
        layout.findViewById(R.id.b9).setOnClickListener(this);
        layout.findViewById(R.id.bDot).setOnClickListener(this);
        layout.findViewById(R.id.bBrackClose).setOnClickListener(this);
        layout.findViewById(R.id.bBrackOpen).setOnClickListener(this);
        layout.findViewById(R.id.bDiv).setOnClickListener(this);
        layout.findViewById(R.id.bMinus).setOnClickListener(this);
        layout.findViewById(R.id.bMul).setOnClickListener(this);
        layout.findViewById(R.id.bPlus).setOnClickListener(this);
        layout.findViewById(R.id.bDiv).setOnClickListener(this);
        layout.findViewById(R.id.bExp).setOnClickListener(this);

        layout.findViewById(R.id.bEq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eq = disp.getText().toString();
                if (checkBrackets(eq) && checkExpression(eq)) {
                    try {
                        ArrayList<String> postfix = postfix(eq);
                        Stack<Double> ans = new Stack<>();
                        for (int i = 0; i < postfix.size(); i++) {
                            String s = postfix.get(i);
                            if ("+*^/-".contains(s)) {
                                Double a = ans.pop();
                                Double b = ans.pop();
                                switch (s.charAt(0)) {
                                    case '+':
                                        ans.push(a + b);
                                        break;
                                    case '-':
                                        ans.push(b - a);
                                        break;
                                    case '*':
                                        ans.push(a * b);
                                        break;
                                    case '/':
                                        ans.push(b / a);
                                        break;
                                    case '^':
                                        ans.push(Math.pow(b, a));
                                        break;
                                }
                            } else {
                                ans.push(Double.parseDouble(s));
                            }
                        }
                        String x=String.valueOf(ans.pop());
                        disp.setText(x);
                        eqlFlag = true;
                    } catch (Exception e) {
                        Log.d(TAG, "onEqual: Error msg: " + e.getMessage());
                        disp.setText("Invalid Expression");
                        input = new StringBuilder();
                    }
                } else {
                    disp.setText("Error");
                    input = new StringBuilder();
                }
            }
        });

        layout.findViewById(R.id.bBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("error".equalsIgnoreCase(disp.getText().toString()))
                    disp.setText("0");
                else if (input.length() > 1) {
                    input.deleteCharAt(input.length() - 1);
                    disp.setText(input.toString());
                } else {
                    input = new StringBuilder();
                    disp.setText("0");
                }
            }
        });

        layout.findViewById(R.id.bReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.delete(0, input.length());
                disp2.setText("0");
                disp.setText("0");
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("disp1", disp.getText().toString());
        outState.putString("disp2", disp2.getText().toString());
    }

    private ArrayList<String> postfix(String eq) throws Exception {
        Log.d(TAG, "fix: String received: " + eq);
        Map<String, Integer> priority = new HashMap<>();
        priority.put("-", 1);
        priority.put("+", 1);
        priority.put("*", 3);
        priority.put("/", 4);
        priority.put("^", 5);
        priority.put("(", 0);

        MathTokenizer ob = new MathTokenizer(input.toString());
        ArrayList<String> tokens = ob.getTokens();

        ArrayList<String> s = new ArrayList<>();


        if (tokens == null) {
            throw new Exception("DoubleDot");
        }

        Stack<String> stack = new Stack<>();

        for (int i = 0; i < tokens.size(); i++) {
            String ch = tokens.get(i);

            if (ch.matches("[0-9]+[.]?[0-9]*"))
                s.add(ch);
            else {
                if (stack.isEmpty())
                    stack.push(ch);
                else if (ch.equals(")")) {
                    String x = stack.peek();
                    while (!x.equals("(")) {
                        s.add(stack.pop());
                        x = stack.peek();
                    }
                    stack.pop();
                } else if (ch.equals("(") || priority.get(ch) > priority.get(stack.peek()))
                    stack.push(ch);
                else {
                    while (!stack.isEmpty())
                        s.add(stack.pop());
                    stack.push(ch);
                }
            }
        }
        while (!stack.isEmpty())
            s.add(stack.pop());
        Log.d(TAG, "postfix: Returning: " + s.toString());
        return s;
    }

    private boolean checkBrackets(String eq) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < eq.length(); i++) {
            char input = eq.charAt(i);
            try {
                if (input == '(') {
                    stack.push(input);
                } else if (input == ')') {
                    char peek = (char) stack.peek();
                    if (peek == '(' && input == ')') {
                        stack.pop();
                        continue;
                    }
                    System.out.println(peek + " " + input);
                    if (input == '(') {
                        if (peek != ')')
                            return false;
                    }
                }
            } catch (EmptyStackException e) {
                return false;
            }
        }
        if (stack.isEmpty())
            return true;
        return false;
    }

    private boolean checkExpression(String str) {
        for (int i = 0; i < str.length() - 1; i++) {
            if (str.charAt(i) != '(' && str.charAt(i) != ')' && str.charAt(i + 1) != '(' && str.charAt(i + 1) != ')' && !Character.isDigit(str.charAt(i)) && !Character.isDigit(str.charAt(i + 1))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        if (eqlFlag) {
            if ("+-/*^".contains(b.getText().toString())) {
                disp2.setText(input.toString() + "=" + disp.getText());
                input = new StringBuilder(disp.getText());
            }
            else {
                disp2.setText(input.toString() + "=" + disp.getText());
                input = new StringBuilder();
            }
            eqlFlag = false;
        }

        String i = b.getText().toString();

        if ("error".equalsIgnoreCase(disp.getText().toString()))
            disp.setText("0");
        else if ("0".equals(input.toString())) {
            input = new StringBuilder();
        }
        if (i.charAt(0) == '.' && input.length() > 0 && !"0987654321.".contains(String.valueOf(input.charAt(input.length() - 1))))
            input.append("0.");
        else
            input.append(i);

        disp.setText(input.toString());
    }
}