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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthLis;
    private TextView nametxt;
    private TextView select1;
    private String email2;
    private String name2;
    private String id2;
    private String prov;
    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nametxt = findViewById(R.id.nombre);
   //     provetxt = findViewById(R.id.prove);
        select1 = findViewById(R.id.select);

        obtenerCategoria();

        mAuthLis = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    onSetDataUser(user.getDisplayName(),user.getEmail());
                    name2 = user.getDisplayName();
                    email2 =user.getEmail();
                    id2 = user.getUid();
                    prov = user.getProviderId();

                    insertFirebase(name2, email2,id2,prov);



                    //   selectFirebase(email2);

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
                            .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), googleIdp, facebookIkdp))
                            .setLogo(R.drawable.helpmecook)
                            .setTheme(R.style.ThemaLogin)
                            .build(), RC_SIGN_IN);




                     //  insertFirebase(name2, email2);
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

        //Intent i = new Intent(this, ScreenPrincipal.class);
        //startActivity(i);

    }

    private void obtenerCategoria(){

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());

        databaseAccess.open();
        String name = "1";
        ArrayList<Categorias> cate = databaseAccess.lista(name);

        for (int i = 0; i < cate.size(); i ++) {
            Log.d("INICIA ","categorias= "+cate.get(i).getCategoria());

        }

//        select1.setText(cate.get(0).getCategoria()+"-"+cate.get(1).getCategoria()+"prueba");


        databaseAccess.close();
    }


    private void onLimpiarcache(){
        onSetDataUser("","");
    }

    private void onSetDataUser(String username, String email) {
     //   correotxt.setText(email);
        nametxt.setText(username);

       // insertFirebase(username,email);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
          //  Toast.makeText(MainActivity.this,"Bienvenido....",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(MainActivity.this,"Error al logear",Toast.LENGTH_SHORT).show();
        }
    }

    private void insertFirebase(String username, String email, String id, String prove){
        //
        Map<String, Object> map = new HashMap<>();
        map.put("nombre",username);
     // map.put("proveedor",prove);
        map.put("correo",email);
        map.put("tipoUsuario","Normal");
        map.put("tipoCuenta","Gratuita");
        map.put("fechaCreacion", "18/03/2020");
        map.put("FechaIniSuscri","30/03/2020");
        map.put("FechaFinSuscrip","30/04/2020");

        mDatabase.child("Users").child(id).setValue(map);

    }


    private void selectFirebase(String emailL){


        Query query = mDatabase.child("Users");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){


                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String correoF = ds.child("correo").getValue().toString();


                        if(correoF.equals(emailL)){
                            String susF = ds.child("tipoCuenta").getValue().toString();
                            Toast.makeText(MainActivity.this,"tipo de sus:"+susF,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed(){
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
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
