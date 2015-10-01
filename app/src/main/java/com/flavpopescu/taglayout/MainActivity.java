package com.flavpopescu.taglayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.flavpopescu.TagLayout;

import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TagLayout tagLayout = (TagLayout) findViewById(R.id.tagLayout);
        Button submit = (Button) findViewById(R.id.submit_button);

        final String[] tags = getResources().getStringArray(R.array.tags_sample);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<Integer> selectedTags = tagLayout.getSelectedTags();
                Iterator<Integer> iterator = selectedTags.iterator();
                String message;
                if (iterator.hasNext()) {
                    StringBuilder builder = new StringBuilder("You selected: ");
                    builder.append(tags[iterator.next()]);
                    while (iterator.hasNext()) {
                        builder.append(", ");
                        builder.append(tags[iterator.next()]);
                    }
                    message = builder.toString();
                } else {
                    message = "Please make a selection";
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
