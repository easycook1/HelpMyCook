package com.example.helpmycook;

import androidx.appcompat.app.AppCompatActivity;

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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ScreenPrincipal extends AppCompatActivity{

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

    public void callReciclerView(){
        reciclerView = findViewById(R.id.reciclerView);

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

        String[] langname = {"Vegetales","Proteinas","Lacteos","Cereales","Vegetales","Proteinas","Lacteos","Cereales","Vegetales","Proteinas","Lacteos","Cereales"};

        mainModels = new ArrayList<>();
        for(int i = 0; i < langlogo.length; i++){
            MainModel model = new MainModel(langlogo[i],langname[i]);
            mainModels.add(model);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                ScreenPrincipal.this,LinearLayoutManager.HORIZONTAL,false
        );

        reciclerView.setLayoutManager(layoutManager);
        reciclerView.setItemAnimator(new DefaultItemAnimator());
        mainAdapter  = new MainAdapter(ScreenPrincipal.this, mainModels);
        reciclerView.setAdapter(mainAdapter);
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
}