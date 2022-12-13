package com.example.projectg104;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private Button btnRegisterReg;
    private EditText editEmailReg, editPassReg, editConfirmReg;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegisterReg = (Button) findViewById(R.id.btnRegisterReg);
        editEmailReg = (EditText) findViewById(R.id.editEmailReg);
        editPassReg = (EditText) findViewById(R.id.editPassReg);
        editConfirmReg = (EditText) findViewById(R.id.editConfirmReg);
        mAuth = FirebaseAuth.getInstance();

        btnRegisterReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmailReg.getText().toString().trim();
                String pass = editPassReg.getText().toString().trim();
                String confirm = editConfirmReg.getText().toString().trim();

                if(pass.compareTo(confirm) == 0){
                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Usuario creado",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error en Registro", Toast.LENGTH_SHORT).show();
                                    Log.e("UserCreate", e.toString());
                                }
                            });
                }else{
                    Toast.makeText(getApplicationContext(),"Contraseña no coincide",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}