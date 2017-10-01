package com.onlinetactictoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private EditText editTextEmail,editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }


    public void SignUpButton(View view){

        if(!TextUtils.isEmpty(editTextEmail.getText().toString()) && !TextUtils.isEmpty(editTextPassword.getText().toString())){

            LoginUser(editTextEmail.getText().toString(),editTextPassword.getText().toString());
            Toast.makeText(this, "Email: "+editTextEmail, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Fill the Required Fields", Toast.LENGTH_SHORT).show();
        }

    }

    private void LoginUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if(currentUser!=null){
                        LoadMain();
                        mRef.child(SplitString(currentUser.getEmail())).child("Request").setValue(currentUser.getEmail());
                    }

                }else{
                    Log.d("TAG",task.getException().toString());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadMain();
    }

    private void LoadMain() {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null){
            Toast.makeText(this, "GO to MianActivity", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("email",currentUser.getEmail());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }


    private String SplitString(String str){

        String split[] = str.split("@");

        return split[0];
    }
}
