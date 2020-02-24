package com.example.gitam_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView t= findViewById(R.id.textView);
        t.setText("This program uses the following:" +
                "\n1.Java" +
                "\n2.Android Studio" +
                "\n3.Xml" +
                "\n4.Dialog flow" +
                "\n5.Common sense" +
                "\n\n\nCreators:Sai Kowshik B6004" +
                "\n             Dr.Lakshmeeshwari garu " +
                "\n\n\n\nReferences:" +
                "\n                 Youtube");
    }
}
