package ja.respostas.respostasja.rumpsolutions.br.respostasja.Modelo;

/**
 * Created by jonat on 18/01/2018.
 */
// getter and setters do usuario
public class Usuario {


    private String nick;
    private String email;
    private String senha;


    public Usuario(){

    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}


