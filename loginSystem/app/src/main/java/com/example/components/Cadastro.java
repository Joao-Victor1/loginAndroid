package com.example.components;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.app.AlertDialog;
import android.content.DialogInterface;
import static android.text.TextUtils.isEmpty;

public class Cadastro extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailAddressInput);
        passwordEditText = findViewById(R.id.passwordInput);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordInput);

        CheckBox termsCheckBox = findViewById(R.id.checkBox);
        Button registerButton = findViewById(R.id.registerButton);

        TextView useTerms = findViewById(R.id.useTerms);
        useTerms.setPaintFlags(useTerms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        useTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTermsDialog();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (termsCheckBox.isChecked()) {
                    registerUser();
                } else {
                    Toast.makeText(Cadastro.this, "Você deve aceitar os termos de uso para continuar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showTermsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Termos de Uso");
        builder.setView(R.layout.termos_de_uso);
        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void registerUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if(isEmpty(email)){
            Toast.makeText(this, "Insira um e-mail!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(isEmpty(password)){
            Toast.makeText(this, "Insira uma senha!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(isEmpty(confirmPassword)){
            Toast.makeText(this, "Confirme sua senha!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "As senhas estão diferentes!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Cadastro.this, "Registro bem-sucedido.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Cadastro.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Cadastro.this, "Falha no registro.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
