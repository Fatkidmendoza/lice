package andu.kit.edu.blooddonation;

/**
 * Created by Andu on 24.12.2015.
 */
public class User {
    String lastName,firstName,username,password;

    int cnp;

    public User(String lastName,String firstName,int cnp,String username,String password){
        this.lastName=lastName;
        this.firstName=firstName;
        this.cnp=cnp;
        this.username=username;
        this.password=password;
    }
    public User(String username,String password){
        this.username=username;
        this.password=password;
        this.cnp=-1;
        this.firstName="";
        this.lastName="";
    }
}
