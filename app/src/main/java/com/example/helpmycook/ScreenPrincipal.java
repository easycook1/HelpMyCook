package com.example.helpmycook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ScreenPrincipal extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthLis;

    //Variables de menu deslizante////
    RecyclerView reciclerView;
    ArrayList<MainModel> mainModels;
    MainAdapter mainAdapter;
    //Variables de buscador////
    InputMethodManager inputMethodManager;
    ImageButton searchButton;
    ImageButton buttonDelete;
    EditText editTextSearch;

    boolean keyboardActive = false;
    ConstraintLayout constraintLayout;
    ConstraintLayout constrain11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_principal);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
      //  new MainActivity().onCreate(null);

        searchButton = findViewById(R.id.searchButton);
        buttonDelete = findViewById(R.id.buttonDelete);
        editTextSearch = findViewById(R.id.editTextSearch);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteText();
            }
        });

        constraintLayout=findViewById(R.id.principal_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //constrain11 =  findViewById(R.id.constraintFood);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        double l1 = size.y/3;
        double l2 = size.y/1.4;
/*try {

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.background_cook, null);
        constrain11 = (ConstraintLayout) view.findViewById(R.id.sumadre);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) constrain11.getLayoutParams();
        params.height = size.y; // here is one modification for example. modify anything else you want :)
        constrain11.setLayoutParams(params); // request the view to use the new modified params
}catch (Exception e){
    Toast.makeText(this,e+"",Toast.LENGTH_SHORT).show();
}*/


        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                validateKeyboard();
            }});

        callReciclerView();

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(after > 0){
                    buttonDelete.setVisibility(View.VISIBLE);
                }else{
                    buttonDelete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void startSearch(){

        inputMethodManager = (InputMethodManager) getApplicationContext() .getSystemService(this.INPUT_METHOD_SERVICE);

        if(keyboardActive){
            keyboardActive = false;
            inputMethodManager.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
        }else{
            keyboardActive = true;
            inputMethodManager.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void  deleteText(){
        editTextSearch.setText("");
    }


    private void obtenerCategoria(){

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());

        databaseAccess.open();
        String name = "1";
        ArrayList<Categorias> cate = databaseAccess.lista(name);

        for (int i = 0; i < cate.size(); i ++) {
            Log.d("INICIA ","categorias= "+cate.get(i).getCategoria());

        }
        Toast.makeText(this,cate.get(0).categoria,Toast.LENGTH_SHORT).show();

//        select1.setText(cate.get(0).getCategoria()+"-"+cate.get(1).getCategoria()+"prueba");


        databaseAccess.close();
    }


    public void callReciclerView(){
        try {

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());

        databaseAccess.open();
        String name = "1";
        ArrayList<Categorias> cate = databaseAccess.lista(name);

        reciclerView = findViewById(R.id.reciclerView);

        ImageButton prueba = new ImageButton(this);
/*
        Integer[] langlogo = {
                R.drawable.im0,
                R.drawable.im1,
                R.drawable.im3,
                R.drawable.im4,
                R.drawable.im0,
                R.drawable.im1,
                R.drawable.im3,
                R.drawable.im4,
                R.drawable.im0,
                R.drawable.im1,
                R.drawable.im3,
                R.drawable.im4};

*/

        String langname [] = new String[50];
        Integer langlogo [] = new Integer[50];
        int int1;


        for (int i = 0; i < cate.size(); i ++) {


            String base64String = cate.get(4).icono;
            String base64Image = base64String.split(",")[1];

            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            prueba.setImageBitmap(decodedByte);
            int1 = prueba.getId();


            langname[i] = cate.get(i).categoria;
            langlogo[i] = int1;

        }

        Log.e("INICIA3 ","categorias "+cate.get(4).id);
        Log.e("INICIA4 ","categorias "+cate.get(4).categoria);
        Log.e("INICIA5 ","categorias "+cate.get(4).icono);


        databaseAccess.close();

            mainModels = new ArrayList<>();
            for(int j = 0; j < langlogo.length; j++){
                MainModel model = new MainModel(langlogo[j],langname[j]);
                mainModels.add(model);
            }





                LinearLayoutManager layoutManager = new LinearLayoutManager(
                        ScreenPrincipal.this,LinearLayoutManager.HORIZONTAL,false
                );
                reciclerView.setLayoutManager(layoutManager);
                reciclerView.setItemAnimator(new DefaultItemAnimator());
                mainAdapter  = new MainAdapter(ScreenPrincipal.this, mainModels);
                reciclerView.setAdapter(mainAdapter);
            }catch (Exception e){
                Toast.makeText(this,""+e,Toast.LENGTH_LONG).show();

            }
    }

    public void validateKeyboard() {
        Rect r = new Rect();
        constraintLayout.getWindowVisibleDisplayFrame(r);
        int screenHeight = constraintLayout.getRootView().getHeight();
        int keypadHeight = screenHeight - r.bottom;
        if (keypadHeight > screenHeight * 0.15) {
            searchButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.keyboard_close));
            editTextSearch.requestFocus();
            keyboardActive = true;
        } else{
            searchButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.magnifying_glass));
            editTextSearch.clearFocus();
            keyboardActive = false;
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onBackPressed(){

          //  super.onPause();
            return;

    }


}
