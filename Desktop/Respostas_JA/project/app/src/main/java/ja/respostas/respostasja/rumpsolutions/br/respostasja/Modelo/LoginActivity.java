package ja.respostas.respostasja.rumpsolutions.br.respostasja.Modelo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.DeviceLoginButton;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ja.respostas.respostasja.rumpsolutions.br.respostasja.R;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private Button btnCadastro;
    private Context context;
    private FirebaseAuth mAuth;
    private GoogleApiClient googleApiClient;
    private EditText edt_email;
    private EditText edt_senha;
    private Button btnLogin;
    private SignInButton singG;
    private LoginButton facebookLogin;
    private CallbackManager callbackManager;
    public static final int SIGN_IN_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        context = this;
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();


        //inicio configuração do google login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        //final da configuração de login com google

        singG = (SignInButton) findViewById(R.id.signG);
        edt_email   = findViewById(R.id.edt_login_email);
        edt_senha   = findViewById(R.id.edt_login_senha);
        btnLogin    = findViewById(R.id.btn_logar);
        facebookLogin = (LoginButton) findViewById(R.id.facebookLogin);
        btnCadastro = findViewById(R.id.btn_login_cadastrar);

        btnLogin.setOnClickListener(realizarLogin());

        btnCadastro.setOnClickListener(evtBotaoCadastrar());

        singG.setOnClickListener(evtGoogleLogin());

        //Fases do Login com Facebook
        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                logFacebook();
            }

            @Override
            public void onCancel() {
                cancelLogFacebook();
            }

            @Override
            public void onError(FacebookException error) {
                errorLoginFacebook();
            }
        });
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    // metodo onde pega o resultado do request code que declarei estatico para o login GOOGLE
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignResult(result);
        }
    }
    //metodo onde verifica se o login deu sucesso ou não GOOGLE
    private void handleSignResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            goMainScreen();
        }else{
            Toast.makeText(this,"Não foi possível iniciar a sessão pelo Google+", Toast.LENGTH_SHORT).show();

        }
    }

    // metodo que lhe joga para tela inicial GOOGLE
    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    //Onclick do botao do google
    private View.OnClickListener evtGoogleLogin(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        };
    }

    //inicio dos metodos do facebook para logar
    private void logFacebook(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void cancelLogFacebook(){
        Toast.makeText(this,"A operação de login foi cancelada pelo usuário",Toast.LENGTH_SHORT).show();
    }

    private void errorLoginFacebook(){
        Toast.makeText(this,"Ocorreu um erro ao executar login via Facebook",Toast.LENGTH_SHORT).show();
    }
    // fim dos metodos
    //HOJE DIA 2018/04/15 O APLICATIVO ENCONTRA-SE COM ERRO NÃO ABRE APRESENTA ERRO ACREDITO EU RELACIONADO A ID DO FACEBOOK



}

