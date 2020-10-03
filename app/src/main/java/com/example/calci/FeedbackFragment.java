package com.example.calci;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FeedbackFragment extends Fragment {
    EditText name, msg;
    FloatingActionButton fab;

    View layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_feedback, container, false);
        name = layout.findViewById(R.id.textName);
        msg = layout.findViewById(R.id.textFeedback);
        fab = layout.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a, b, message;

                a = name.getText().toString();
                b = msg.getText().toString();
                if (a.length() < 1) {
                    Toast.makeText(getActivity(), "Enter valid name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (b.length() < 1) {
                    Toast.makeText(getActivity(), "Enter Feedback", Toast.LENGTH_SHORT).show();
                    return;
                }

                message = b + "\n\nFeedback by: " + a;
                String[] address={"priyanshu.grv@outlook.com"};
                Intent send=new Intent(Intent.ACTION_SENDTO);
                send.putExtra(Intent.EXTRA_EMAIL,address);
                send.putExtra(Intent.EXTRA_SUBJECT,"Feedback for Calci App");
                send.putExtra(Intent.EXTRA_TEXT,message);

                send.setType("message/rfc822");
                try {
                    startActivity(Intent.createChooser(send, "Send email with...?"));
                } catch (android.content.ActivityNotFoundException exception) {
                    Toast.makeText(getActivity(), "No email clients installed on device!", Toast.LENGTH_LONG).show();
                }

            }
        });
        return layout;
    }
}