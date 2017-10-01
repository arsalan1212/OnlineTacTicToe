package com.onlinetactictoe;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    String myEmail;
    EditText editTextEmail;

    private int ActivePlayer =1;
    private String PlayerSymbol;
    private String sessionID="dump";
    private ArrayList<Integer> player1 = new ArrayList<>();
    private ArrayList<Integer> player2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent()!=null)
        myEmail = getIntent().getStringExtra("email");

        editTextEmail = (EditText) findViewById(R.id.UserEmail);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        IncommingCalls();
    }


    //All Buttons
    public void MyAllButtons(View view){

        Button buttonSelected = (Button) view;
        int cellID=1;
        switch (buttonSelected.getId()){

           case R.id.btn1:
               cellID =1;
            break;

            case R.id.btn2:
                cellID =2;
                break;

            case R.id.btn3:
                cellID =3;
                break;

            case R.id.btn4:
                cellID =4;
                break;

            case R.id.btn5:
                cellID =5;
                break;

            case R.id.btn6:
                cellID =6;
                break;

            case R.id.btn7:
                cellID =7;
                break;

            case R.id.btn8:
                cellID =8;
                break;

            case R.id.btn9:
                cellID =9;
                break;
        }


        mRef.child("PlayerOnline").child(sessionID).child(String.valueOf(cellID)).setValue(myEmail);
    }

    private void PlayGame(int cellID, Button buttonSelected){

        if(ActivePlayer ==1){
            buttonSelected.setText("X");
            buttonSelected.setTextColor(Color.WHITE);
            buttonSelected.setBackgroundColor(Color.BLUE);
            player1.add(cellID);
            ActivePlayer = 2;
        }
        else{
            buttonSelected.setText("O");
            buttonSelected.setTextColor(Color.WHITE);
            buttonSelected.setBackgroundColor(Color.CYAN);
            player2.add(cellID);
            ActivePlayer = 1;
        }
        buttonSelected.setEnabled(false);
        checkWinner();

    }

    private void checkWinner() {

        int winer=-1;

        // row 1
        if(player1.contains(1) && player1.contains(2) && player1.contains(3)){
            winer=1;
        }
        if(player2.contains(1) && player2.contains(2) && player2.contains(3)){
            winer=2;
        }


        // row 2
        if(player1.contains(4) && player1.contains(5) && player1.contains(6)){
            winer=1;
        }
        if(player2.contains(4) && player2.contains(5) && player2.contains(6)){
            winer=2;
        }




        // row 3
        if(player1.contains(7) && player1.contains(8) && player1.contains(9)){
            winer=1;
        }
        if(player2.contains(7) && player2.contains(8) && player2.contains(9)){
            winer=2;
        }



        // col 1
        if(player1.contains(1) && player1.contains(4) && player1.contains(7)){
            winer=1;
        }
        if(player2.contains(1) && player2.contains(4) && player2.contains(7)){
            winer=2;
        }



        // col 2
        if(player1.contains(2) && player1.contains(5) && player1.contains(8)){
            winer=1;
        }
        if(player2.contains(2) && player2.contains(5) && player2.contains(8)){
            winer=2;
        }


        // col 3
        if(player1.contains(3) && player1.contains(6) && player1.contains(9)){
            winer=1;
        }
        if(player2.contains(3) && player2.contains(6) && player2.contains(9)){
            winer=2;
        }


        if( winer != -1){

            if (winer==1){
                android.widget.Toast.makeText(this," Player 1  win the game", android.widget.Toast.LENGTH_LONG).show();
            }else{
                android.widget.Toast.makeText(this," Player 2  win the game", android.widget.Toast.LENGTH_LONG).show();

            }

        }
    }

    private void AutoPlay(int cellID){
        Button buttonSelected=null;

        switch (cellID){

            case 1:
                buttonSelected = (Button) findViewById(R.id.btn1);
                break;

            case 2:
                buttonSelected = (Button) findViewById(R.id.btn2);
                break;

            case 3:
                buttonSelected = (Button) findViewById(R.id.btn3);
                break;

            case 4:
                buttonSelected = (Button) findViewById(R.id.btn4);
                break;

            case 5:
                buttonSelected = (Button) findViewById(R.id.btn5);
                break;

            case 6:
                buttonSelected = (Button) findViewById(R.id.btn6);
                break;

            case 7:
                buttonSelected = (Button) findViewById(R.id.btn7);
                break;

            case 8:
                buttonSelected = (Button) findViewById(R.id.btn8);
                break;

            case 9:
                buttonSelected = (Button) findViewById(R.id.btn9);
                break;
        }

        PlayGame(cellID,buttonSelected);
    }
    public void RequestButtonEvent(View view){
        String userEmail = editTextEmail.getText().toString();
        mRef.child("Users").child(SplitString(userEmail)).child("Request").push().setValue(myEmail);
        PlayerSymbol ="X";
        PlayerOnline(SplitString(myEmail)+SplitString(userEmail));
    }

    public void AcceptButtonEvent(View view){
        String userEmail = editTextEmail.getText().toString();
        mRef.child("Users").child(SplitString(userEmail)).child("Request").push().setValue(myEmail);
        PlayerSymbol ="O";
        PlayerOnline(SplitString(userEmail)+SplitString(myEmail));
    }


    private void IncommingCalls(){

        mRef.child("Users").child(SplitString(myEmail)).child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    String key = snapshot.getKey();
                    String value = (String) snapshot.getValue();

                    editTextEmail.setText(value.toString());

                    mRef.child("Users").child(SplitString(myEmail)).child("Request").setValue("true");
                    break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void PlayerOnline(String sessionID){
        this.sessionID=sessionID;
        mRef.child("PlayerOnline").removeValue();
        mRef.child("PlayerOnline").child(sessionID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                player1.clear();
                player2.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    String key = snapshot.getKey();
                    String value = (String) snapshot.getValue();

                    if(!value.equals(myEmail)){

                        if(PlayerSymbol.equals("X"))
                            ActivePlayer=1;
                        else
                            ActivePlayer=2;
                    }
                    else{

                        if(PlayerSymbol.equals("X"))
                            ActivePlayer=2;
                        else
                            ActivePlayer=1;
                    }

                    AutoPlay(Integer.parseInt(key));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private String SplitString(String str){

        String split[]= str.split("@");
        return split[0];
    }
}
