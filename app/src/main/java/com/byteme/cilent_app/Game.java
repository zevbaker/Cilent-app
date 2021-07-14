package com.byteme.cilent_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Game extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = Game.class.getSimpleName().toString();
    private static final String BundleSAVEKEY = "Board";
    Button[] buttons;
    TextView[][] Board;
    Button selectedBtn;
    TextView selectedCell;
    LinearLayout root;
    Sudoku borad;
    int[][] IDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_test);
        root = findViewById(R.id.root);


        borad = new Sudoku(new int[][]{
                new int[]{5,3,0,0,7,0,0,0,0},
                new int[]{6,0,0,1,9,5,0,0,0},
                new int[]{0,9,8,0,0,0,0,6,0},
                new int[]{8,0,0,0,6,0,0,0,3},
                new int[]{4,0,0,8,0,3,0,0,1},
                new int[]{7,0,0,0,2,0,0,0,6},
                new int[]{0,6,0,0,0,0,2,8,0},
                new int[]{0,0,0,4,1,9,0,0,5},
                new int[]{0,0,0,0,8,0,0,7,9},
        });

        IDS = new int[][]{
                new int[]{5,3,0,0,7,0,0,0,0},
                new int[]{6,0,0,1,9,5,0,0,0},
                new int[]{0,9,8,0,0,0,0,6,0},
                new int[]{8,0,0,0,6,0,0,0,3},
                new int[]{4,0,0,8,0,3,0,0,1},
                new int[]{7,0,0,0,2,0,0,0,6},
                new int[]{0,6,0,0,0,0,2,8,0},
                new int[]{0,0,0,4,1,9,0,0,5},
                new int[]{0,0,0,0,8,0,0,7,9},
        };


        //todo tryed to save that data for next time
//        if(savedInstanceState != null){
//            if(savedInstanceState.containsKey(BundleSAVEKEY)){
//                borad = Sudoku.GetInstance(savedInstanceState.getString(BundleSAVEKEY));
//            }else{
//                savedInstanceState.putString(BundleSAVEKEY,borad.send());
//            }
//        }

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
                T1.setBackground(getDrawable( R.drawable.border));
                T1.setTextColor(getColor(R.color.cell_Text_color));


                int Tid = View.generateViewId();
                IDS[row][cell] = Tid;
                T1.setId(Tid);

                if(borad.getBoard()[row][cell] != 0){
                    T1.setText(borad.getBoard()[row][cell]+"");
                    //todo set to a diffrent color
                }else{
                    T1.setOnClickListener(this);
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

    }

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
                selectedCell.setBackground(getDrawable( R.drawable.border));
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
        borad.updateBoard(row-1,cell-1,Integer.parseInt(val));

        SendToServer("Ck",borad);
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

                            upErrorsOnBoard(Indexs);
                            Log.d(TAG, "run: "+ Arrays.toString(Indexs));
                            break;
                        }
                        case "EndGame":{
                            Toast.makeText(Game.this, "End of game !", Toast.LENGTH_SHORT).show();
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

    private void upErrorsOnBoard(Index[] indexs) {
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


//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//        savedInstanceState.putString(BundleSAVEKEY, borad.send());
//        // etc.
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        borad = Sudoku.GetInstance(savedInstanceState.getString(BundleSAVEKEY));
//    }
}