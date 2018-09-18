package com.gmail.gpolomicz.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    ImageView image;
    ArrayList<String> actorImageArray;
    ArrayList<String> actorAltArray;
    ArrayList<Integer> randomNumbers;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = findViewById(R.id.actorImageView);
        button1 = findViewById(R.id.actorButton1);
        button2 = findViewById(R.id.actorButton2);
        button3 = findViewById(R.id.actorButton3);
        button4 = findViewById(R.id.actorButton4);

        String result;

        DownloadData data = new DownloadData();

        try {
            result = data.execute("http://www.posh24.se/kandisar").get();

            Pattern p = Pattern.compile("<img src=\"(.*?)\"");
            Matcher m = p.matcher(result);

            actorImageArray = new ArrayList<>();

            while (m.find()) {
                actorImageArray.add(m.group(1));
            }

            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(result);

            actorAltArray = new ArrayList<>();

            while (m.find()) {
                actorAltArray.add(m.group(1));
            }


        } catch (Exception e) {
            Log.e(TAG, "onCreate: ERROR! " + e);
        }

        randomNumbers = new ArrayList<>();

        for(int i=0; i<94; i++) {
            randomNumbers.add(i);
        }
        Collections.shuffle(randomNumbers);

        nextQuestion();

        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch(v.getId()) {
                    case R.id.actorButton1:
                        if(button1.getText().equals(actorAltArray.get(randomNumbers.get(position-1)))) {
                        Toast.makeText(MainActivity.this, "DOBRZE!", Toast.LENGTH_SHORT).show();
                    } else {
                            Toast.makeText(MainActivity.this, "Źle!", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.actorButton2:
                        if(button2.getText().equals(actorAltArray.get(randomNumbers.get(position-1)))) {
                            Toast.makeText(MainActivity.this, "DOBRZE!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Źle!", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.actorButton3:
                        if(button3.getText().equals(actorAltArray.get(randomNumbers.get(position-1)))) {
                            Toast.makeText(MainActivity.this, "DOBRZE!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Źle!", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.actorButton4:
                        if(button4.getText().equals(actorAltArray.get(randomNumbers.get(position-1)))) {
                            Toast.makeText(MainActivity.this, "DOBRZE!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Źle!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                nextQuestion();
            }
        };

        button1.setOnClickListener(click);
        button2.setOnClickListener(click);
        button3.setOnClickListener(click);
        button4.setOnClickListener(click);
    }

    static public class DownloadImages extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                return BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.d(TAG, "doInBackground: ERROR " +e);
            }

            return null;
        }
    }

    static public class DownloadData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String result = "";

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = 0;

                while (data != -1) {
                    data = reader.read();
                    char current = (char) data;
                    result += current;

                }

                String[] splitResult = result.split("<div class=\"listedArticles\">");

                return splitResult[0];

            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ERROR!" + e);
                return null;
            }

        }
    }

    public void nextQuestion() {

        DownloadImages downloadImages = new DownloadImages();

        try {
            Bitmap bitmap = downloadImages.execute(actorImageArray.get(randomNumbers.get(position))).get();
            image.setImageBitmap(bitmap);

        } catch (Exception e) {
            Log.d(TAG, "nextQuestion: ERROR" +e);
        }

        ArrayList<String> answers = new ArrayList<>();
        answers.add(actorAltArray.get(randomNumbers.get(position)));

        String fakeAnsswer;
        Random random = new Random();

        for (int i=0; i<3; i++) {
            fakeAnsswer = actorAltArray.get(random.nextInt(94));
            while(fakeAnsswer.equals(actorAltArray.get(position))) {
                fakeAnsswer = actorAltArray.get(random.nextInt(94));
            }
            answers.add(fakeAnsswer);
        }

        Collections.shuffle(answers);

        button1.setText(answers.get(0));
        button2.setText(answers.get(1));
        button3.setText(answers.get(2));
        button4.setText(answers.get(3));

        position++;
        if(position == 94) {
            position=0;
        }
    }
}
