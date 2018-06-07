package uca.ruiz.antonio.jwtapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uca.ruiz.antonio.jwtapp.R;
import uca.ruiz.antonio.jwtapp.data.Preferencias;
import uca.ruiz.antonio.jwtapp.data.io.MyApiAdapter;
import uca.ruiz.antonio.jwtapp.data.mapping.Login;
import uca.ruiz.antonio.jwtapp.data.mapping.TokenResponse;

import static uca.ruiz.antonio.jwtapp.data.Preferencias.getEditor;

/**
 * Created by toni on 07/06/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private ProgressDialog progressDialog;
    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.entrando));

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    intentoLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intentoLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }


    /**
     * Intenta iniciar sesión mediante el formulario de inicio de sesión.
     * Si hay errores de formulario (correo electrónico no válido, campos faltantes, etc.), se
     * presentan los errores y no se realiza ningún intento de inicio de sesión.
     */
    private void intentoLogin() {
        // Resetear errores
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Valida campo password
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!esPasswordValida(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Valida campo email.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!esEmailValido(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // Se ha producido un error: no se intenta el login y se focaliza en el
            // primer campo del formulario con error.
            focusView.requestFocus();
        } else {
            login(email, password);
        }
    }

    private boolean esEmailValido(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean esPasswordValida(String password) {
        return password.length() >= 4;
    }

    /**
     * Aquí es donde hacemos la llamada al servidor para hacer login gracias a Retrofit.
     * @param username
     * @param password
     */
    private void login(String username, String password) {
        progressDialog.show();

        Login login = new Login(username, password);

        Call<TokenResponse> call = MyApiAdapter.getApiService().login(login);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if(response.isSuccessful()) {
                    token = "Bearer " + response.body().getToken();
                    irMain(token);
                } else {
                    progressDialog.cancel();
                    Toast.makeText(LoginActivity.this, "Login incorrecto !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                progressDialog.cancel();
                Toast.makeText(LoginActivity.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void irMain(String token) {
        Preferencias.getEditor(this).putString("token", token).commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
