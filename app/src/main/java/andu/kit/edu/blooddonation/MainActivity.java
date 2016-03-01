package andu.kit.edu.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{
    Button bLogout,bEmail;
    EditText etLastName,etFirstName,etUsername,etCNP;
    UserLocalStore userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLastName = (EditText)findViewById(R.id.etLastName);
        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etCNP = (EditText)findViewById(R.id.etCNP);

        bLogout= (Button)findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);

        bEmail= (Button)findViewById(R.id.bEmail);
        bEmail.setOnClickListener(this);


        userLocalStore = new UserLocalStore(this);//context this
    }

    @Override//make sure a user is logged in
    protected void onStart() {
        super.onStart();
        if(authenticate()==true){
            displayUserDetails();
        }else{
            startActivity(new Intent(MainActivity.this,Login.class));
        }

    }


    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails(){
       User user = userLocalStore.getLoggedInUser();

        etUsername.setText(user.username);
        etLastName.setText(user.lastName);
        etFirstName.setText(user.firstName);
        etCNP.setText(user.cnp+"");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogout:
            userLocalStore.clearUserData();
            userLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(this, Login.class));
                break;
            case R.id.bEmail:
                startActivity(new Intent(this, Mail.class));

        }
    }
}
