package ja.respostas.respostasja.rumpsolutions.br.respostasja.Modelo;

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

import ja.respostas.respostasja.rumpsolutions.br.respostasja.R;

public class CadastroActivity extends AppCompatActivity {

    private Button btn_cadastro;
    private EditText edt_nick;
    private EditText edt_email;
    private EditText edt_senha;
    private Context context;
    private Usuario usuario;


    private String emailUser;
    private String senhaUser;

    private FirebaseAuth autenticacao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);



        //Definições de Variaveis;
        context = this;
        autenticacao = FirebaseAuth.getInstance();

        edt_email = findViewById(R.id.edt_cadastro_email);
        edt_nick = findViewById(R.id.edt_cadastro_nick);
        edt_senha = findViewById(R.id.edt_cadastro_senha);


        btn_cadastro = findViewById(R.id.btn_cadastro);
            btn_cadastro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    emailUser = edt_email.getText().toString();
                    senhaUser = edt_senha.getText().toString();

                    if(senhaUser.length() < 6){
                        toast("A senha deve ter no mínimo 6 digitos.");
                    }else{
                        btnCadastrar(emailUser, senhaUser);
                    }


                }
            });



    }

    private void btnCadastrar(final String email, final String senha) {
        System.out.println("Email digitado :" + email);
        System.out.println("Senha digitado :" + senha);


        autenticacao.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            toastl("Usuário cadastrado com sucesso!");
                            FirebaseUser user = autenticacao.getCurrentUser();
                            realizarLogin(email,senha);
                        } else {
                            toastl("Cadastro não realizado.\nTente novamente.");
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = autenticacao.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

        if(currentUser != null){
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    private void realizarLogin(String email, String password){
        autenticacao.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = autenticacao.getCurrentUser();
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                    }
                });
    }





    private void toast(String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
    private void toastl(String s){
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }
}
