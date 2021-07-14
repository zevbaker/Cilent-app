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

public class Game extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = Game.class.getSimpleName().toString();
    private static final String BundleSAVEKEY = "Board";
    Button[] buttons;
    TextView[][] Board;
    Button selectedBtn;
    TextView selectedCell;
    LinearLayout root;
    Sudoku borad;

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
//        Board = new TextView[][]{
//                new TextView[]{findViewById(R.id.row1cell1),findViewById(R.id.row1cell2),findViewById(R.id.row1cell3),findViewById(R.id.row1cell4),findViewById(R.id.row1cell5),findViewById(R.id.row1cell6),findViewById(R.id.row1cell7),findViewById(R.id.row1cell8),findViewById(R.id.row1cell9)},
//                new TextView[]{findViewById(R.id.row2cell1),findViewById(R.id.row2cell2),findViewById(R.id.row2cell3),findViewById(R.id.row2cell4),findViewById(R.id.row2cell5),findViewById(R.id.row2cell6),findViewById(R.id.row2cell7),findViewById(R.id.row2cell8),findViewById(R.id.row2cell9)},
//                new TextView[]{findViewById(R.id.row3cell1),findViewById(R.id.row3cell2),findViewById(R.id.row3cell3),findViewById(R.id.row3cell4),findViewById(R.id.row3cell5),findViewById(R.id.row3cell6),findViewById(R.id.row3cell7),findViewById(R.id.row3cell8),findViewById(R.id.row3cell9)},
//                new TextView[]{findViewById(R.id.row4cell1),findViewById(R.id.row4cell2),findViewById(R.id.row4cell3),findViewById(R.id.row4cell4),findViewById(R.id.row4cell5),findViewById(R.id.row4cell6),findViewById(R.id.row4cell7),findViewById(R.id.row4cell8),findViewById(R.id.row4cell9)},
//                new TextView[]{findViewById(R.id.row5cell1),findViewById(R.id.row5cell2),findViewById(R.id.row5cell3),findViewById(R.id.row5cell4),findViewById(R.id.row5cell5),findViewById(R.id.row5cell6),findViewById(R.id.row5cell7),findViewById(R.id.row5cell8),findViewById(R.id.row5cell9)},
//                new TextView[]{findViewById(R.id.row6cell1),findViewById(R.id.row6cell2),findViewById(R.id.row6cell3),findViewById(R.id.row6cell4),findViewById(R.id.row6cell5),findViewById(R.id.row6cell6),findViewById(R.id.row6cell7),findViewById(R.id.row6cell8),findViewById(R.id.row6cell9)},
//                new TextView[]{findViewById(R.id.row7cell1),findViewById(R.id.row7cell2),findViewById(R.id.row7cell3),findViewById(R.id.row7cell4),findViewById(R.id.row7cell5),findViewById(R.id.row7cell6),findViewById(R.id.row7cell7),findViewById(R.id.row7cell8),findViewById(R.id.row7cell9)},
//                new TextView[]{findViewById(R.id.row8cell1),findViewById(R.id.row8cell2),findViewById(R.id.row8cell3),findViewById(R.id.row8cell4),findViewById(R.id.row8cell5),findViewById(R.id.row8cell6),findViewById(R.id.row8cell7),findViewById(R.id.row8cell8),findViewById(R.id.row8cell9)},
//                new TextView[]{findViewById(R.id.row9cell1),findViewById(R.id.row9cell2),findViewById(R.id.row9cell3),findViewById(R.id.row9cell4),findViewById(R.id.row9cell5),findViewById(R.id.row9cell6),findViewById(R.id.row9cell7),findViewById(R.id.row9cell8),findViewById(R.id.row9cell9)},
//        };
//
//        buttons = new Button[]{
//                findViewById(R.id.button1),
//                findViewById(R.id.button2),
//                findViewById(R.id.button3),
//                findViewById(R.id.button4),
//                findViewById(R.id.button5),
//                findViewById(R.id.button6),
//                findViewById(R.id.button7),
//                findViewById(R.id.button8),
//                findViewById(R.id.button9),
//                findViewById(R.id.del),
//        };
//
//        for (TextView[] row: Board) {
//            for (TextView cell: row){
//                cell.setOnClickListener(this);
//            }
//        }
//
//        for (Button btn: buttons){
//            btn.setOnClickListener(this);
//        }
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

                    if (fromServer.readObject().getClass() == Boolean.class && (Boolean)fromServer.readObject()) {
                        //todo end game
                        Toast.makeText(Game.this, "End of game !", Toast.LENGTH_SHORT).show();
                    }else{
                        Boolean[][] ck = (Boolean[][])fromServer.readObject();
                    }


                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
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