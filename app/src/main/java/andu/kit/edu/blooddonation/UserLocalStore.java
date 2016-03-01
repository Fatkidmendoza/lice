package andu.kit.edu.blooddonation;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Andu on 24.12.2015.
 */
//store user data on the phone
    //gets details of the logged in user
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;//allow us to store data on the phone

    public UserLocalStore(Context context){//give the context and give the sharedPreference to instanciete
        //context from the activity
        //for all activity that uses this class
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public void storeUserData(User user){
        //edidt what is contained in sharedPref.
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("lastName",user.lastName);
        spEditor.putString("firstName",user.firstName);
        spEditor.putInt("cnp",user.cnp);
        spEditor.putString("username",user.username);
        spEditor.putString("password",user.password);
        spEditor.commit();
    }

    public User getLoggedInUser(){

        String lastName = userLocalDatabase.getString("lastName","");
        String firstName = userLocalDatabase.getString("firstName","");
        int cnp = userLocalDatabase.getInt("cnp",-1);
        String username = userLocalDatabase.getString("username","");
        String password = userLocalDatabase.getString("password","");

        User storedUser = new User(lastName,firstName,cnp,username,password);
        return storedUser;
    }
    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn",loggedIn);
        spEditor.commit();
    }
    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("loggedIn",false)==true){
            return true;//if user is logged in return true
        }else{
            return false;
        }
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}