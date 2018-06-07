package uca.ruiz.antonio.jwtapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uca.ruiz.antonio.jwtapp.data.Preferencias;
import uca.ruiz.antonio.jwtapp.data.io.MyApiAdapter;
import uca.ruiz.antonio.jwtapp.R;


public class MainActivity extends AppCompatActivity {

    Button btn_get;
    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        token = Preferencias.get(this).getString("token", "token");

        btn_get = (Button) findViewById(R.id.btn_get);
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCosas();
            }
        });
    }


    private void getCosas() {
        Call<ResponseBody> call = MyApiAdapter.getApiService().getCosas(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        Toast.makeText(MainActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Token incorrecto !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
