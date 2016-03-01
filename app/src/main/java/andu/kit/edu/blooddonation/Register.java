package andu.kit.edu.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Register extends ActionBarActivity implements View.OnClickListener {

    Button bRegister;
    EditText etLastName,etFirstName,etUsername,etCNP,etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        etLastName = (EditText)findViewById(R.id.etLastName);
        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etCNP = (EditText)findViewById(R.id.etCNP);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        bRegister= (Button)findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bRegister:

                String lastName = etLastName.getText().toString();
                String firstName = etFirstName.getText().toString();

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                int cnp = Integer.parseInt(etCNP.getText().toString());
                User registerData = new User(lastName,firstName,cnp,username,password);

                registerUser(registerData);
                break;
        }
    }

    private void registerUser(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user,new GetUserClassBack() {
            @Override
            // aici pot sa pun  un boolean  successful   to know if any error is strored in the background
            public void done(User returnedUser) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });
    }
}
