package gustavorivera.proyectogrado.gbba.modulomedicogbba;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

/**
 * Created by gustavo on 24/03/16.
 */
public class Login extends AppCompatActivity {

    // Take in consideration for animating the splash and/or the login
    // https://stackoverflow.com/questions/6055984/display-activity-from-bottom-to-top

    private final static String TAG_ACTIVITY = "Login Activity: ";
    // UI references.
    private EditText mNombreLogin;
    private EditText mCedulaLogin;
    private UserVerificationTask mVerificationTask;
    private View mProgressView;
    private LinearLayout mLoginButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // Set up the login form.
        mNombreLogin = findViewById(R.id.login_campo_nombre);
        mCedulaLogin = findViewById(R.id.login_campo_cedula);
        mLoginButtons = findViewById(R.id.layoutButtons);


        Button mEmailSignInButton = findViewById(R.id.boton_ingresar);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mProgressView = findViewById(R.id.login_progress);
        mProgressView.setVisibility(View.GONE);
    }

    private void attemptLogin() {

        mNombreLogin.setError(null);
        mCedulaLogin.setError(null);

        String email = mNombreLogin.getText().toString();
        String password = mCedulaLogin.getText().toString();

        Boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mCedulaLogin.setError(getString(R.string.error_cedula_invalida));
            focusView = mCedulaLogin;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            mNombreLogin.setError(getString(R.string.error_campo_requerido));
            focusView = mNombreLogin;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mNombreLogin.setError(getString(R.string.error_nombre_incorrecta));
            focusView = mNombreLogin;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            //TODO Show progressbar
            mProgressView.setVisibility(View.VISIBLE);
            mLoginButtons.setVisibility(View.GONE);
            mVerificationTask = new UserVerificationTask(email, password, this);
            mVerificationTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return !email.equals("");
    }

    private boolean isPasswordValid(String password) {

        int lenght = password.length();
        return lenght > 6;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    private static class UserVerificationTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private int mIdUsuario = -1;
        private WeakReference<Login> loginReference;


        UserVerificationTask(String email, String password, Login context) {
            mEmail = email;
            mPassword = password;
            loginReference = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result = false;

            try {
                Thread.sleep(1500); // Simulate response time
                /*
                 * G. Rivera: For prototyping purpose, the connection to the actual
                 * server will be simulated with a local database.
                 */

                // Suggested call -> GBBAWebApi.getUserVerification(mEmail, mPassword)

                // mIdUsuario = DatabaseHandler.getHistorialUsuario(mEmail, mPassword)
                result = mIdUsuario > 0;

            } catch (InterruptedException e) {
                //TODO LogException
                Log.d(TAG_ACTIVITY, "Interruption message: " + e.getMessage());
            } catch (Exception e) {
                Log.d(TAG_ACTIVITY, "General exception: " + e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            Login loginActivity = loginReference.get();
            if (loginActivity == null || loginActivity.isFinishing()) return;

            loginActivity.mVerificationTask = null;
            loginActivity.mProgressView.setVisibility(View.GONE);
            loginActivity.mLoginButtons.setVisibility(View.VISIBLE);

            if (success) {
                Intent i = new Intent(loginActivity, ListaFichas.class);

                // Usuario usuarioIngresado = DatabaseHandler.getHistorialUsuario(idUsuarioSeleccionado)

                // i.putExtra(PACIENTE_SELECCIONADO, usuarioIngresado);

                loginActivity.startActivity(i);
                loginActivity.finish();
            } else {
                loginActivity.mCedulaLogin.setError(loginActivity.getString(R.string.error_cedula_incorrecta));
                loginActivity.mCedulaLogin.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Login loginActivity = loginReference.get();
            if (loginActivity == null || loginActivity.isFinishing()) return;
            loginActivity.mVerificationTask = null;
            loginActivity.mProgressView.setVisibility(View.GONE);
            loginActivity.mLoginButtons.setVisibility(View.VISIBLE);

        }
    }
}
