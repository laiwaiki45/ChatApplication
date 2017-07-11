package com.lwk.chatapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        final String user = getIntent().getStringExtra("ac");
        final String pw = getIntent().getStringExtra("pw");
        TextView hello = (TextView) findViewById(R.id.helloText);
        hello.setText("Hello "+ user);

        final EditText message = (EditText) findViewById(R.id.messageText);

        Button send = (Button) findViewById(R.id.sendBtn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty(message))
                    Toast.makeText(MessageActivity.this, "Please enter the message", Toast.LENGTH_LONG).show();
                else{
                    final String[] exception = new String[1];
                    new AsyncTask<String, Void, Integer>(){
                        @Override
                        protected Integer doInBackground(String... strings) {

                            try{
                                String address = "http://chat.alesberger.cz/send.php?login=" + strings[0] + "&password=" + strings[1] +"&message=";
                                address += URLEncoder.encode(strings[2], "UTF-8");
                                URL url = new URL(address);
                                // Create the request to server, and open the connection
                                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                                urlConnection.connect();

                                return  urlConnection.getResponseCode();
                            }catch (Exception ex){
                                exception[0] = ex.getMessage();
                                return 0;
                            }



                        }

                        @Override
                        protected void onPostExecute(Integer response) {

                            switch (response){
                                case 200:
                                    Toast.makeText(MessageActivity.this, "Message sent", Toast.LENGTH_LONG).show();
                                    break;
                                case 400:
                                    Toast.makeText(MessageActivity.this, "Bad request", Toast.LENGTH_LONG).show();
                                    break;
                                case 401:
                                    Toast.makeText(MessageActivity.this, "Message Error", Toast.LENGTH_LONG).show();
                                    break;
                                case 0:
                                    Toast.makeText(MessageActivity.this, exception[0], Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(MessageActivity.this, "Error with response code: "+String.valueOf(response), Toast.LENGTH_LONG).show();
                            }

                        }
                    }.execute(user,pw,message.getText().toString());
                }
            }
        });
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }


}
