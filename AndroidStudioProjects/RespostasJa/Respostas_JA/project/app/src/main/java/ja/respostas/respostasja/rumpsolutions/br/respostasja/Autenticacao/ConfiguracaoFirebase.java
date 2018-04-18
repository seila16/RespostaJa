package ja.respostas.respostasja.rumpsolutions.br.respostasja.Autenticacao;

/**
 * Created by jonat on 18/01/2018.
 */
//autenticacao manual do firebase onde o Mauth virou autenticacao nos codigos

import com.google.firebase.auth.FirebaseAuth;


public final class ConfiguracaoFirebase {
    private static FirebaseAuth autenticacao;


    public static FirebaseAuth getFirebaseAutenticacao(){
        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();

        }
        return autenticacao;
    }
    //comentario

}
