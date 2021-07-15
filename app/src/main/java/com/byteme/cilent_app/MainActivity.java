package com.byteme.cilent_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.widget.Toast;

import com.byteme.cilent_app.models.GameSave;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String startingGameTAG = "startingGame";
    public static final String currentGameTAG = "currentGame";
    public static final String newGameTag = "newGame";
    private static final int SIGN_IN_REQUEST_ON_CREATE = 1;
    private static final int Sudoku_End_GAME = 4;
    private DatabaseReference firebaseDatabase;

    private Button loadGame;
    private Button NewGame;

    private String startingGame;
    private String currentGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadGame = findViewById(R.id.loadGame);
        NewGame = findViewById(R.id.newGame);
        NewGame.setOnClickListener(this);
        loadGame.setOnClickListener(this);
        //------------------------------------

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build());

                        Intent signInIntent = AuthUI.getInstance()
                                .createSignInIntentBuilder().setIsSmartLockEnabled(false)
                                .setAvailableProviders(providers)
                                .build();

            startActivityForResult(signInIntent, SIGN_IN_REQUEST_ON_CREATE);
        } else {
            checkBoardSaves();
            toastWithDetails(true);
        }


    }

    private void checkBoardSaves() {

        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        firebaseDatabase.child(currentGameTAG).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                currentGame = snapshot.getValue(String.class);
                if (currentGame != null)
                    loadGame.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });


    }



    @Override
    public void onClick(View v) {
        Intent i =  new Intent(this,Game.class);
        switch (v.getId()) {
            case R.id.newGame:
                i.putExtra(newGameTag, true);
                startActivityForResult(i,Sudoku_End_GAME);
                break;
            case R.id.loadGame:
//                checkBoardSaves();

//                i.putExtra(currentGameTAG, currentGame);
//                i.putExtra(startingGameTAG, startingGame);
                i.putExtra(newGameTag, false);
                startActivityForResult(i,Sudoku_End_GAME);
                break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the sign-in activity that is now finished was originated by the onCreate method
        if (requestCode == SIGN_IN_REQUEST_ON_CREATE) {
            // an activity was created because the user was not signed in
            if (resultCode == RESULT_OK) {
                // check if the activity for sign-in was finished successfully
                checkBoardSaves();
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
                NewGame.setText("congrats");
            } else {
                //TODO game did not end give opson to resume
            }
        }
    }

}