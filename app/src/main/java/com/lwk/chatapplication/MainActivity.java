package com.lwk.chatapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText ac = (EditText) findViewById(R.id.username);
        final EditText pw = (EditText) findViewById(R.id.password);

        Button login = (Button) findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty(ac))
                    Toast.makeText(MainActivity.this,"Please Enter Username",Toast.LENGTH_LONG).show();
                else if(isEmpty(pw))
                    Toast.makeText(MainActivity.this,"Please Enter Password",Toast.LENGTH_LONG).show();
                else {
                    final String username = ac.getText().toString();
                    final String password = pw.getText().toString();
                    final String[] exception = new String[1];
                    new AsyncTask<String, Void, Integer>(){
                        @Override
                        protected Integer doInBackground(String... strings) {

                            try{
                                URL url = new URL("http://chat.alesberger.cz/login.php?login=" + strings[0] + "&password=" + strings[1]);
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
                                  Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                                  intent.putExtra("ac", username);
                                  intent.putExtra("pw", password);
                                  startActivity(intent);
                                  break;
                              case 400:
                                  Toast.makeText(MainActivity.this, "Bad request", Toast.LENGTH_LONG).show();
                                  break;
                              case 401:
                                  Toast.makeText(MainActivity.this, "Bad username or password", Toast.LENGTH_LONG).show();
                                  break;
                              case 0:
                                  Toast.makeText(MainActivity.this, exception[0], Toast.LENGTH_LONG).show();
                                  break;
                              default:
                                  Toast.makeText(MainActivity.this, "Error with response code: "+String.valueOf(response) , Toast.LENGTH_LONG).show();
                          }

                        }
                    }.execute(username,password);
                }
            }
        });
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
