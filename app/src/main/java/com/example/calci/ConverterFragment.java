package com.example.calci;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class ConverterFragment extends Fragment {
    View layout;
    Spinner spin;
    String profile;
    EditText input, bin, dec, oct, hex;
    TextView wrong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_converter, container, false);
        spin = layout.findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView spinnerText = (TextView) spin.getChildAt(0);
                spinnerText.setTextColor(Color.WHITE);
                TextView t = (TextView) view;
                profile = t.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        input = layout.findViewById(R.id.inputNum);
        bin = layout.findViewById(R.id.bin);
        oct = layout.findViewById(R.id.oct);
        dec = layout.findViewById(R.id.dec);
        hex = layout.findViewById(R.id.hex);
        wrong = layout.findViewById(R.id.wrong);

        Button button = layout.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = input.getText().toString();
                switch (profile) {
                    case "Binary":
                        if (binCheck(s)) {
                            String d = bin2dec(s);
                            bin.setText(s);
                            oct.setText(dec2N(Long.parseLong(d), 8));
                            dec.setText(d);
                            hex.setText(dec2N(Long.parseLong(d), 16));
                            input.setTextColor(Color.WHITE);
                            wrong.setVisibility(View.INVISIBLE);
                        } else {
                            input.setTextColor(Color.RED);
                            wrong.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "Octal":
                        if (checkOctal(s)) {
                            String d = String.valueOf(toDeci(s, 8));
                            bin.setText(dec2N(Long.parseLong(d), 2));
                            oct.setText(s);
                            dec.setText(d);
                            hex.setText(dec2N(Long.parseLong(d), 16));
                            input.setTextColor(Color.WHITE);
                            wrong.setVisibility(View.INVISIBLE);
                        } else {
                            input.setTextColor(Color.RED);
                            wrong.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "Decimal":
                        if (checkDecimal(s)) {
                            bin.setText(dec2N(Long.parseLong(s), 2));
                            oct.setText(dec2N(Long.parseLong(s), 8));
                            dec.setText(s);
                            hex.setText(dec2N(Long.parseLong(s), 16));
                            input.setTextColor(Color.WHITE);
                            wrong.setVisibility(View.INVISIBLE);
                        } else {
                            input.setTextColor(Color.RED);
                            wrong.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "Hexadecimal":
                        s = s.toUpperCase();
                        if (checkHexa(s)) {
                            String d = String.valueOf(toDeci(s, 16));
                            bin.setText(dec2N(Long.parseLong(d), 2));
                            oct.setText(dec2N(Long.parseLong(d), 8));
                            dec.setText(d);
                            hex.setText(s);
                            input.setTextColor(Color.WHITE);
                            wrong.setVisibility(View.INVISIBLE);

                        } else {
                            input.setTextColor(Color.RED);
                            wrong.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
        });
        return layout;
    }

    private static String dec2N(long s, int base) {
        StringBuilder res = new StringBuilder();
        while (s != 0) {
            res.insert(0, newval((int) (s % base)));
            s /= base;
        }
        return res.toString();
    }

    private static String newval(int l) {
        if (l >= 0 && l <= 9)
            return String.valueOf(l);
        else return String.valueOf((char) ('A' + l - 10));
    }

    static long toDeci(String str,
                       int base) {
        int len = str.length();
        long power = 1;
        long num = 0;
        int i;
        for (i = len - 1; i >= 0; i--) {
            num += val(str.charAt(i)) * power;
            power = power * base;
        }
        return num;
    }

    private static String bin2dec(String s) {
        if (s.matches("[0]+"))
            return "0";
        int k = s.length() - 1;
        long res = 0l;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '1')
                res = res + (1L << k);
            k--;
        }
        return String.valueOf(res);
    }

    private static boolean binCheck(String s) {
        if (s.matches("[0-1]+"))
            return true;
        else return false;
    }

    private static boolean checkOctal(String s) {
        if (s.matches("[0-7]+"))
            return true;
        else return false;
    }

    private static boolean checkDecimal(String s) {
        if (s.matches("[0-9]+"))
            return true;
        else return false;
    }

    private static boolean checkHexa(String s) {
        if (s.matches("[0-9a-fA-F]+"))
            return true;
        else return false;
    }

    static int val(char c) {
        if (c >= '0' && c <= '9')
            return (int) c - '0';
        else
            return (int) c - 'A' + 10;
    }

}