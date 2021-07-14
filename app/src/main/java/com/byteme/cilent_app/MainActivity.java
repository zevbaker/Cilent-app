package com.byteme.cilent_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SIGN_IN_REQUEST_ON_CREATE = 1;
    private static final int Sudoku_End_GAME = 4;

    private Button TestSever;
    private Button NewGame;

    private ObjectInputStream fromServer;
    private ObjectOutputStream toServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO fix

        TestSever = findViewById(R.id.Test);
        NewGame = findViewById(R.id.newGame);
        NewGame.setOnClickListener(this);
        TestSever.setOnClickListener(this);
        //------------------------------------

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build());

                        Intent signInIntent = AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build();

            startActivityForResult(signInIntent, SIGN_IN_REQUEST_ON_CREATE);

        } else {
            toastWithDetails(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Test:
                sendMessage();
                break;
            case R.id.newGame:
                startActivity(new Intent(this,Game.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_out,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SignOut();
        return super.onOptionsItemSelected(item);
    }

    private void SignOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    private void toastWithDetails(boolean success) {
        if (success) {
            // user has successfully either signed-in or signed-up
            String userDetails = "Hi, your display name is:  " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                    + " id: " + FirebaseAuth.getInstance().getCurrentUser().getUid();
            Toast.makeText(this, userDetails, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendMessage() {

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Sudoku borad = new Sudoku(new int[][]{
                            new int[]{1,2,3,4,5,6,7,8,9},
                            new int[]{1,2,3,4,5,6,7,8,9},
                            new int[]{1,2,3,4,5,6,7,8,9},

                            new int[]{1,2,3,4,5,6,7,8,9},
                            new int[]{1,2,3,4,5,6,7,8,9},
                            new int[]{1,2,3,4,5,6,7,8,9},

                            new int[]{1,2,3,4,5,6,7,8,9},
                            new int[]{1,2,3,4,5,6,7,8,9},
                            new int[]{1,2,3,4,5,6,7,8,9},
                    });


                    Socket socket = new Socket("10.0.2.2",8010);

                    toServer =new ObjectOutputStream(socket.getOutputStream());
                    fromServer =new ObjectInputStream(socket.getInputStream());

                    toServer.writeObject("Ck");
                    toServer.writeObject(borad.send());
                    Boolean[][] ck = (Boolean[][])fromServer.readObject();
                    String res="";
                    for (int i = 0; i < 9; i++){
                        for (int j = 0; j < 9; j++){
                            res +=(ck[i][j]+"|");
                        }
                        res+="\n";
                    }
                    Log.d(TAG, "doFakeWork: got res of: "+res);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the sign-in activity that is now finished was originated by the onCreate method
        if (requestCode == SIGN_IN_REQUEST_ON_CREATE) {
            // an activity was created because the user was not signed in
            if (resultCode == RESULT_OK) {
                // check if the activity for sign-in was finished successfully
                toastWithDetails(true);
            } else {
                // either sign-in or sign-up failed (SignInActivity using signInIntent)
                toastWithDetails(false);
                // only registered users can message each other.
                // terminate the application
                finish(); // close MainActivity
            }

        }else if (requestCode == Sudoku_End_GAME){
            if (resultCode == RESULT_OK) {
                //TODO ended game and give new one
            } else {
                //TODO game did not end give opson to resume
            }
        }
    }

}