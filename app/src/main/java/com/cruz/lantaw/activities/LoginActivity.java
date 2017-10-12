package com.cruz.lantaw.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cruz.lantaw.R;
import com.cruz.lantaw.models.Users;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "tagg";
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
//    private ProgressDialog mProgressDialog;

    private ImageView btnSignIn;
    private Button btnSignOut;
    private ProgressBar mProgressDialog;
    DatabaseReference databaseReference;


    private FirebaseAuth mAuth;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if(!isNetworkAvailable()) {
            if (!isOnline()) {
                Toast.makeText(this, "Network is not enabled!", Toast.LENGTH_SHORT).show();
            }
            mAuth = FirebaseAuth.getInstance();
            mProgressDialog = (ProgressBar) findViewById(R.id.progressBar);
            mProgressDialog.setVisibility(View.GONE);


            btnSignIn = (ImageView) findViewById(R.id.btn_sign_in);
            btnSignOut = (Button) findViewById(R.id.btn_sign_out);

            btnSignIn.setOnClickListener(this);
            btnSignOut.setOnClickListener(this);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        }

    private void signIn() {
        if(!isNetworkAvailable()){
            Toast.makeText(this, "Network is not enabled!", Toast.LENGTH_SHORT).show();
        }else {
            mProgressDialog.setVisibility(View.VISIBLE);

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);

        }

    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;

            case R.id.btn_sign_out:
                signOut();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            mProgressDialog.setVisibility(View.VISIBLE);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                mProgressDialog.setVisibility(View.GONE);

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                final String email = account.getGivenName();

                databaseReference = FirebaseDatabase.getInstance().getReference("user");
                final String key = databaseReference.push().getKey();
                String url = "https://my-project-151421.firebaseio.com/user.json";

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        Firebase reference = new Firebase("https://my-project-151421.firebaseio.com/user");

                        if(s.equals("null")) {
                            reference.child(email).child("key").setValue(key);
                            Toast.makeText(LoginActivity.this, "Successful Login", Toast.LENGTH_LONG).show();
                            //login();
                        }
                        else {
                            try {
                                JSONObject obj = new JSONObject(s);

                                if (!obj.has(email)) {
                                    reference.child(email).child("key").setValue(key);
                                    Toast.makeText(LoginActivity.this, "Successful Login", Toast.LENGTH_LONG).show();
                                    Users.username= email;
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                }else{
                                    Toast.makeText(LoginActivity.this, "Successful Login", Toast.LENGTH_LONG).show();
                                    Users.username= email;
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // pd.dismiss();
                    }

                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError );

                    }
                });
                RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
                rQueue.add(request);

            } else {
                Toast.makeText(LoginActivity.this, "Google Sign In failed", Toast.LENGTH_LONG).show();
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser!=null){
            showProgressDialog();
            updateUI(true);
        }else {
            updateUI(false);
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {

    }

    private void hideProgressDialog() {
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            hideProgressDialog();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        } else {
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user!=null){
                                updateUI(true);
                            }else {
                                updateUI(false);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(false);
                        }
                    }
                });
    }
}