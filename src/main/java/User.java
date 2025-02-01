import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private String name;
    private String password;
    private String username;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String name , String username , String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }

}
