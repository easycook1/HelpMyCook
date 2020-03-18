package com.example.helpmycook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.Scopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthLis;
    private TextView correotxt;
    private TextView provetxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        correotxt = findViewById(R.id.correo);
        provetxt = findViewById(R.id.prove);

        mAuthLis = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    onSetDataUser(user.getDisplayName(),user.getEmail());
                }else{

                    onLimpiarcache();

                    AuthUI.IdpConfig facebookIkdp = new AuthUI.IdpConfig.FacebookBuilder()
                            .setPermissions(Arrays.asList("user_friends","user_gender"))
                            .build();

                    AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.GoogleBuilder()
                 //         .setScopes(Arrays.asList(Scopes.GAMES))
                            .build();

                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setTosAndPrivacyPolicyUrls(
                                    "https://example.com/terms.html",
                                    "https://example.com/privacy.html")
                  //          .setTosUrl("http://databaseremote.esy.es/RegisterLite/html/privaciddad.html")
                            .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), googleIdp, facebookIkdp))

                            .build(), RC_SIGN_IN);
                }
            }
        };


        //codigo para serial SHA facebook

        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(
                    "com.example.helpmycook",
                    PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        for (Signature signature : info.signatures) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }

        // fin - SHA



    }


    private void onLimpiarcache(){
        onSetDataUser("","");
    }

    private void onSetDataUser(String username, String email) {
        correotxt.setText(email);
        provetxt.setText(username);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            if(requestCode == RESULT_OK){
               // Toast.makeText(this,"bienvenido.........",Toast.LENGTH_SHORT).show();
            }else{
               // Toast.makeText(this,"algo fallo",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthLis);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthLis != null){
            mAuth.removeAuthStateListener(mAuthLis);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_salir:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

}
