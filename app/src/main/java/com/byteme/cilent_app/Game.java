package com.byteme.cilent_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.byteme.cilent_app.models.GameSave;
import com.byteme.cilent_app.models.Index;
import com.byteme.cilent_app.models.Sudoku;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

public class Game extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = Game.class.getSimpleName().toString();
    DatabaseReference firebaseDatabase;
    Button selectedBtn;
    TextView selectedCell;
    LinearLayout root;
    boolean hasError = false;
    int[][] IDS;
    private GameSave game;
    Boolean newgame;

    String generateBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_test);
        root = findViewById(R.id.root);
        game = new GameSave();
        checkNewGame();
    }

    private void checkNewGame() {
        String difficulty="";
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            newgame = extras.getBoolean(MainActivity.newGameTag);
            difficulty = extras.getString(MainActivity.difficultyTag);
        }

        if (newgame){
            makeNewGame(difficulty);
        }else{
            getGameState();
        }

    }


    private void setupBoard() {

        IDS = new int[9][9];
        for (int i = 0; i < 9; i++) {
            IDS[i] = new int[9];
        }

        for (int row = 0; row < 9;row++){
            LinearLayout l1 = new LinearLayout(this);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            l1.setLayoutParams(param);
            l1.setGravity(Gravity.CENTER);
            l1.setOrientation(LinearLayout.HORIZONTAL);
            int id = View.generateViewId();
            l1.setId(id);
            root.addView(l1);

            for (int cell = 0; cell<9;cell++){
                TextView T1 = new TextView(this);
                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                T1.setGravity(Gravity.CENTER);
                T1.setTextSize(20.0f);
                T1.setLayoutParams(param1);

                T1.setTag((row+1)+","+(cell+1));
                if(row/3==cell/3 || (row/3==2 && cell/3==0) || (cell/3==2 && row/3==0))
                    T1.setBackground(getDrawable( R.drawable.border));
                else{
                    T1.setBackground(getDrawable( R.drawable.border2));
                }
                T1.setTextColor(getColor(R.color.cell_Text_color));


                int Tid = View.generateViewId();
                IDS[row][cell] = Tid;
                T1.setId(Tid);

                if(game.getStartingBoard().getBoard()[row][cell] != 0){
                    T1.setText(game.getStartingBoard().getBoard()[row][cell]+"");
                    //todo set to a diffrent color
                }else{
                    T1.setOnClickListener(this);
                }

                if (game.getStartingBoard().getBoard()[row][cell] != game.getCurrentBoard().getBoard()[row][cell]){
                    if(game.getCurrentBoard().getBoard()[row][cell] !=0)
                        T1.setText(game.getCurrentBoard().getBoard()[row][cell]+"");

                    T1.setOnClickListener(this);
                    game.getStartingBoard().getBoard()[row][cell] = game.getCurrentBoard().getBoard()[row][cell];
                }

                ((LinearLayout)findViewById(id)).addView(T1);
            }
        }

        for (int row = 0; row < 2;row++) {
            LinearLayout l1 = new LinearLayout(this);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0.9f
            );
            l1.setLayoutParams(param);
            l1.setGravity(Gravity.CENTER);
            l1.setOrientation(LinearLayout.HORIZONTAL);
            int id = View.generateViewId();
            l1.setId(id);
            root.addView(l1);

            for (int cell = 0; cell<5;cell++){
                Button T1 = new Button(this);
                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                T1.setGravity(Gravity.CENTER);
                T1.setTextSize(20.0f);
                T1.setLayoutParams(param1);
                T1.setText((cell+1)+"");
                T1.setTag((cell+1)+"");
                if(row==1){
                    T1.setText((5+cell+1)+"");
                    T1.setTag(5+(cell+1)+"");
                }

                if((row+1)*(cell+1) == 10){
                    T1.setText("del");
                    T1.setTag("del");
                    T1.setId(R.id.del);
                }
                T1.setOnClickListener(this);
                T1.setBackground(getDrawable( R.drawable.border));
                T1.setBackgroundColor(getColor(R.color.button_default_color));
                ((LinearLayout)findViewById(id)).addView(T1);
            }
        }

//        updateBoardToCurrentState(game.getCurrentBoard());

    }

    private void makeNewGame(String difficulty) {
        if(GenerateBoard(difficulty)){
            game.setStartingBoard(Sudoku.GetInstance(generateBoard));
        }



//        game.setStartingBoard( new Sudoku(new int[][]{
//                new int[]{5,3,0,0,7,0,0,0,0},
//                new int[]{6,0,0,1,9,5,0,0,0},
//                new int[]{0,9,8,0,0,0,0,6,0},
//                new int[]{8,0,0,0,6,0,0,0,3},
//                new int[]{4,0,0,8,0,3,0,0,1},
//                new int[]{7,0,0,0,2,0,0,0,6},
//                new int[]{0,6,0,0,0,0,2,8,0},
//                new int[]{0,0,0,4,1,9,0,0,5},
//                new int[]{0,0,0,0,8,0,0,7,9},
//        }));


        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(MainActivity.startingGameTAG).setValue(game.getStartingBoard().send()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Game.this, "Saved Starting game", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Game.this, "failed to save", Toast.LENGTH_SHORT).show();
                    }
                }
            });

                //testing end
//        borad = new Sudoku(new int[][]{
//                new int[]{5,3,4,6,7,8,9,1,2},
//                new int[]{6,0,2,1,9,5,3,4,8},
//                new int[]{1,9,8,3,4,2,5,6,7},
//                new int[]{8,5,9,7,6,1,4,2,3},
//                new int[]{4,2,6,8,5,3,7,9,1},
//                new int[]{7,1,3,9,2,4,8,5,6},
//                new int[]{9,6,1,5,3,7,2,8,4},
//                new int[]{2,8,7,4,1,9,6,3,5},
//                new int[]{3,4,5,2,8,6,1,7,9},
//        });
        game.setCurrentBoard(game.getStartingBoard());
        setupBoard();
    }

    private boolean GenerateBoard(String difficulty){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Socket socket = new Socket("10.0.2.2",8010);
                        ObjectOutputStream toServer =new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream fromServer =new ObjectInputStream(socket.getInputStream());

                        toServer.writeObject("Generate");
                        toServer.writeObject(difficulty);

                        generateBoard = (String)fromServer.readObject();

                        stopServer(socket,toServer,fromServer);

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
            try {
                thread.join();
                return true;
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        return false;
    }


    private void getGameState() {
        final String[] startingGame = new String[1];
        final String[] currentGame = new String[1];

        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        firebaseDatabase.child(MainActivity.startingGameTAG).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                 startingGame[0] = snapshot.getValue(String.class);
                firebaseDatabase.child(MainActivity.currentGameTAG).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        currentGame[0] = snapshot.getValue(String.class);

                        game = new GameSave(Sudoku.GetInstance(startingGame[0]),Sudoku.GetInstance(currentGame[0]));
                        setupBoard();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        System.out.println("The read failed: " + error.getCode());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });



    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View v) {
        if (v instanceof Button){
            Button btn = (Button)v;
            if (selectedBtn != null){
                selectedBtn.setBackgroundColor(getColor(R.color.button_default_color));
            }

            if(selectedBtn != btn ){
                selectedBtn = btn;
                selectedBtn.setBackgroundColor(getColor(R.color.selected_button_color));
            }else{
                selectedBtn = null;
            }
        }else {
            TextView cell = (TextView)v;

            if (selectedCell!= null){
                int row = Integer.parseInt(selectedCell.getTag().toString().charAt(0)+"")-1;
                int col = Integer.parseInt(selectedCell.getTag().toString().charAt(2)+"")-1;
                if(row/3==col/3 || (row/3==2 && col/3==0) || (col/3==2 && row/3==0))
                    selectedCell.setBackground(getDrawable( R.drawable.border));
                else{
                    selectedCell.setBackground(getDrawable( R.drawable.border2));
                }
            }
            selectedCell = cell;
            selectedCell.setBackgroundColor(getColor(R.color.selected_cell_color));

            if (selectedBtn != null){
                if (selectedBtn.getText().toString().equals(((Button)(findViewById(R.id.del))).getText().toString())){
                    selectedCell.setText("");
                    updateBoard(selectedCell.getTag().toString(),"0");
	            }else{
                    selectedCell.setText(selectedBtn.getText());
                    if (selectedBtn != null){
                        updateBoard(selectedCell.getTag().toString(),selectedBtn.getText().toString());
                    }
                }
            }

        }



    }

    private void updateBoard(String selectedCellTag, String val) {
        int row = Integer.parseInt(selectedCellTag.charAt(0)+"");
        int cell = Integer.parseInt(selectedCellTag.charAt(2)+"");
        game.getCurrentBoard().updateBoard(row-1,cell-1,Integer.parseInt(val));

        SendToServer("Ck", game.getCurrentBoard());
    }


    public void SendToServer(final String command,final Sudoku borad){

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Socket socket = new Socket("10.0.2.2",8010);
                    ObjectOutputStream toServer =new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream fromServer =new ObjectInputStream(socket.getInputStream());

                    toServer.writeObject(command);
                    toServer.writeObject(borad.send());

                    String ResultCommand = (String)fromServer.readObject();
                    switch (ResultCommand){
                        case "index":{
                            String[] resIndexs = (String[])fromServer.readObject();
                            Index[] Indexs = new Index[resIndexs.length];
                            for (int i = 0; i < resIndexs.length;i++){
                                Indexs[i] = Index.GetInstance(resIndexs[i]);
                            }
                            updateErrorsOnBoard(Indexs);
                            hasError = Indexs.length>0;
                            break;
                        }
                        case "EndGame":{
                            setResult(RESULT_OK);
                            finish();
                            break;
                        }

                    }

                    stopServer(socket,toServer,fromServer);

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void stopServer(Socket socket, ObjectOutputStream toServer, ObjectInputStream fromServer) throws IOException {
        toServer.writeObject("stop");
        fromServer.close();
        toServer.close();
        socket.close();
    }

    private void updateErrorsOnBoard(Index[] indexs) {
        for (int[] row:IDS) {
            for (int cell:row){
                TextView t = findViewById(cell);
                t.setTextColor(getColor(R.color.white));
            }
        }

        for (Index in:indexs) {
            TextView t = findViewById(IDS[in.getRow()-1][in.getCell()-1]);
            t.setTextColor(getColor(R.color.Red));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        saveGame();
        return super.onOptionsItemSelected(item);
    }

    private void saveGame() {
        if (hasError == false) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(MainActivity.currentGameTAG).setValue(game.getCurrentBoard().send()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Game.this, "Saved", Toast.LENGTH_SHORT).show();
                        finishAffinity();
                    } else {
                        Toast.makeText(Game.this, "failed to save", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(Game.this, "you have an error cant save", Toast.LENGTH_SHORT).show();
        }
    }


    /*
                    checkBoardSaves();

     */
}