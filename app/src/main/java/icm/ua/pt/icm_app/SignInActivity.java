package icm.ua.pt.icm_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import icm.ua.pt.icm_app.Entity.Exhib;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private GoogleSignInClient mGoogleSignInClient;
    //Google SignIn Button
    private SignInButton mSignInButton;
    private Button signInButton;
    private Button signUpButton;
    private GoogleApiClient mGoogleApiClient;
    final DatabaseReference root = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.setTitle("Login to SmartMuseum");
        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        signInButton = (Button)findViewById(R.id.btnSignIn);
        signInButton.setOnClickListener(this);
        signUpButton = (Button) findViewById(R.id.btnSignUp);
        signUpButton.setOnClickListener(this);
        mSignInButton = (SignInButton)findViewById(R.id.sign_in_button);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);


                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(SignInActivity.this, "Authentication with firebase Failed.",
                        Toast.LENGTH_SHORT).show();
                // [START_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
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
                            final FirebaseUser user = mAuth.getCurrentUser();

                            final DatabaseReference users = root.child("Users");
                            users.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.child(user.getUid()).exists()) {
                                        // run some code
                                    }else{
                                        //users.push().setValue(user.getUid());
                                        //root.push().setValue(user.getUid()+"Favs");
                                        //Exhib ex = new Exhib();
                                        //DatabaseReference dbr = root.child(user.getUid()+"Favs");
                                        //ex.title="Mona Lisa";
                                        //dbr.push().setValue(ex);
                                        //root.push().setValue(user.getUid()+"Past");
                                        //dbr = root.child(user.getUid()+"Past");
                                        //dbr.push().setValue(ex);
                                        users.child(user.getUid()).child("PastExib").child("Title").setValue("Mona Lisa");
                                        users.child(user.getUid()).child("Favs").child("Title").setValue("Mona Lisa");
                                        //users.push().child(user.getUid()).child("Favs").child("Title").setValue("Mona Lisa");
                                        //users.push().child(user.getUid()).child("PastExib").child("Title").setValue("Mona Lisa");


                                        // Create new post at /user-posts/$userid/$postid and at
                                        // /posts/$postid simultaneously
                                        //String key = root.child("Users").push().getKey();
                                        //Exhib post = new Exhib(user.getUid(),user.getDisplayName(),"Mona Lisa","BODY");
                                        //Map<String, Object> postValues = post.toMap();

                                        //Map<String, Object> childUpdates = new HashMap<>();
                                        //childUpdates.put("/User/" + "TT", postValues);
                                        //childUpdates.put("/User-Fav/" + user.getUid() + "/" + "Exhib", postValues);



                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                            startActivity(new Intent(SignInActivity.this,MainActivity.class));
                            //finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]


    private void loginWithUser(){
        EditText textEmail = (EditText) findViewById(R.id.emailText);
        String email = textEmail.toString();

        EditText textPassword = (EditText) findViewById(R.id.passwordText);
        String password = textPassword.toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignInActivity.this, "singInUser:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        //generateUser(email, password)

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        }else if (i== R.id.btnSignIn){
            loginWithUser();
        }
    }




}






