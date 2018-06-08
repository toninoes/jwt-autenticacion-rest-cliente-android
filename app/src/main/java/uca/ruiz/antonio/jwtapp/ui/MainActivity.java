package uca.ruiz.antonio.jwtapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
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
    private TextView tv_fn, tv_ln, tv_em;
    private CheckBox chk_act, chk_adm, chk_san, chk_pac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        token = Preferencias.get(this).getString("token", "token");

        tv_fn = (TextView) findViewById(R.id.tv_fn);
        tv_ln = (TextView) findViewById(R.id.tv_ln);
        tv_em = (TextView) findViewById(R.id.tv_em);
        chk_act = (CheckBox) findViewById(R.id.chk_act);
        chk_adm = (CheckBox) findViewById(R.id.chk_adm);
        chk_san = (CheckBox) findViewById(R.id.chk_san);
        chk_pac = (CheckBox) findViewById(R.id.chk_pac);

        tv_fn.setText(Preferencias.get(this).getString("nombre", "nombre"));
        tv_ln.setText(Preferencias.get(this).getString("apellidos", "apellidos"));
        tv_em.setText(Preferencias.get(this).getString("email", "email"));

        if(Preferencias.get(this).getBoolean("activo", false))
            chk_act.setChecked(true);

        if(Preferencias.get(this).getBoolean("ROLE_ADMIN", false))
            chk_adm.setChecked(true);

        if(Preferencias.get(this).getBoolean("ROLE_SANITARIO", false))
            chk_san.setChecked(true);

        if(Preferencias.get(this).getBoolean("ROLE_PACIENTE", false))
            chk_pac.setChecked(true);

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
