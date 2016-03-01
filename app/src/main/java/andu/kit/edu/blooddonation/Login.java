package andu.kit.edu.blooddonation;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Login extends ActionBarActivity implements View.OnClickListener {
Button bLogin;
EditText etUsername,etPassword;
    TextView tvRegisterLink;
    UserLocalStore userLocalDatabase;//to store data on the phone
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    etUsername  = (EditText)findViewById(R.id.etUsername);
    etPassword  = (EditText)findViewById(R.id.etPassword);
        bLogin= (Button)findViewById(R.id.bLogin);
    tvRegisterLink = (TextView)findViewById(R.id.tvRegisterLink);

        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);

        userLocalDatabase = new UserLocalStore(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){ //of the view
            case R.id.bLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                User user = new User(username,password);
                autheticate(user);

                userLocalDatabase.storeUserData(user);
                userLocalDatabase.setUserLoggedIn(true);
                break;

            case R.id.tvRegisterLink:

                startActivity(new Intent(this,Register.class));
                break;
        }
    }
    private void autheticate(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user,new GetUserClassBack() {
            @Override
            public void done(User returnedUser) {
                if(returnedUser == null){
                    showErrorMessage();
                }else{
                    logUserIn(returnedUser);
                }
            }
        });
    }
    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("ok",null);
        dialogBuilder.show();
    }
    private void logUserIn(User returnedUser){
        userLocalDatabase.storeUserData(returnedUser);
        userLocalDatabase.setUserLoggedIn(true);
        startActivity(new Intent(this, MainActivity.class));
    }
}
