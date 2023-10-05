package com.example.components;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Paint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private CheckBox termsCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        TextView linkTextView = findViewById(R.id.createAccount);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        linkTextView.setPaintFlags(linkTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgotPassword.setPaintFlags(forgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button loginButton = findViewById(R.id.loginButtonConfirm);
        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Cadastro.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithEmailAndPassword();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoverPassword();
            }
        });
}

    private void signInWithEmailAndPassword() {
        EditText editTextEmail = findViewById(R.id.emailAddressInput);
        EditText editTextPassword = findViewById(R.id.passwordInput);

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (isEmpty(email)) {
            Toast.makeText(this, "O e-mail deve ser inserido!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(isEmpty(password)){
            Toast.makeText(this, "A senha deve ser inserida!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Bem-Vindo, " + user.getEmail() + "!",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MainActivity.this, "Usuário não encontrado!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void recoverPassword(){
        EditText editTextEmail = findViewById(R.id.emailAddressInput);
        String email = editTextEmail.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Insira o e-mail!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "E-mail enviado com sucesso!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao enviar o e-mail!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
