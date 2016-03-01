package andu.kit.edu.blooddonation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andu on 26.12.2015.
 */
public class ServerRequests {
    //loading bar
    ProgressDialog progressDialog;// show loding bar when server request is beeing executed
    public static final int CONNECTION_TIMEOUT = 1000* 15;
    public static final String SERVER_ADDRESS = "http://cells.netai.net/";

     public ServerRequests(Context context){
         progressDialog = new ProgressDialog(context);
         progressDialog.setCancelable(false);
         progressDialog.setTitle("Processing");
         progressDialog.setMessage("Please wait...");
     }

    public void storeUserDataInBackground(User user, GetUserClassBack userCallback){
        progressDialog.show();
        new StoredUserDataAsyncTask(user,userCallback).execute();
    }

    public void fetchUserDataInBackground(User user,GetUserClassBack userCallback){
        progressDialog.show();
        new fetchUserDataAsyncTask(user,userCallback).execute();
    }

    public class StoredUserDataAsyncTask extends AsyncTask<Void,Void,Void> {
        User user;
        GetUserClassBack userCallback;


        public StoredUserDataAsyncTask(User user, GetUserClassBack userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("lastName", user.lastName));
            dataToSend.add(new BasicNameValuePair("firstName", user.firstName));
            dataToSend.add(new BasicNameValuePair("cnp", Integer.toString(user.cnp)));
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));
//attribute for the httpParam
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
//make request to the server
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);

            super.onPostExecute(aVoid);
        }
    }
        public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
            User user;
            GetUserClassBack userCallback;


            public fetchUserDataAsyncTask(User user, GetUserClassBack userCallback) {
                this.user = user;
                this.userCallback = userCallback;
            }

            @Override
            protected User doInBackground(Void... params) {
                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("username", user.username));
                dataToSend.add(new BasicNameValuePair("password", user.password));


                HttpParams httpRequestParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

                User returnedUser= null;
                try {
                    post.setEntity(new UrlEncodedFormEntity(dataToSend));
                    HttpResponse httpResponse = client.execute(post);

                    HttpEntity entity = httpResponse.getEntity();
                    String result = EntityUtils.toString(entity);
                   String[] separated = result.split("<");

                    if(separated.length < 2 ){
                        throw new Exception("Invalid user retured from DB");
                    }
                    JSONObject jObject = new JSONObject(separated[0]);

                    //no user with return
                    if(jObject.length() ==0){
                        user =null;
                    } else{
                        String lastName = jObject.getString("lastName");
                        String firstName = jObject.getString("firstName");
                        int cnp = jObject.getInt("cnp");
                        returnedUser = new User(lastName,firstName,cnp,user.username,user.password);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return returnedUser;
            }

            @Override
            protected void onPostExecute(User returnedUser) {
                progressDialog.dismiss();
                userCallback.done(returnedUser);
                super.onPostExecute(returnedUser);
            }
        }

}
