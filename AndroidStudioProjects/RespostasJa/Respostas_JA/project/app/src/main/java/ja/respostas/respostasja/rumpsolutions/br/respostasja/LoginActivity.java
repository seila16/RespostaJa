package ja.respostas.respostasja.rumpsolutions.br.respostasja;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button btnCadastro;
    private Context context;
    private FirebaseAuth mAuth;

    private EditText edt_email;
    private EditText edt_senha;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        mAuth = FirebaseAuth.getInstance();

        edt_email   = findViewById(R.id.edt_login_email);
        edt_senha   = findViewById(R.id.edt_login_senha);
        btnLogin    = findViewById(R.id.btn_logar);

        btnCadastro = findViewById(R.id.btn_login_cadastrar);

        btnLogin.setOnClickListener(realizarLogin());

        btnCadastro.setOnClickListener(evtBotaoCadastrar());




    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }



    //realizar login
    private View.OnClickListener realizarLogin() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edt_email.getText().toString();
                String password = edt_senha.getText().toString();
                if (  (email != null && !email.isEmpty()) && (password != null && !password.isEmpty())  ) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        Toast.makeText(context, "Nenhum usuário encontrado.\n Email ou senha inválidos.", Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                }else{
                    if(email == null || email.isEmpty()){

                        Toast.makeText(context, "Campo email não digitado.", Toast.LENGTH_SHORT).show();

                    }else if(password == null || password.isEmpty()){

                        Toast.makeText(context, "Campo senha não digitado.", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        };
    }


    //botao para se cadastrar
    private View.OnClickListener evtBotaoCadastrar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CadastroActivity.class);
                startActivity(intent);
            }
        };
    }
}
