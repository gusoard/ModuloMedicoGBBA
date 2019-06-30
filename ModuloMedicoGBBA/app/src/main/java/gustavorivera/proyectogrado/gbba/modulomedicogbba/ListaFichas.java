package gustavorivera.proyectogrado.gbba.modulomedicogbba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gustavorivera.proyectogrado.gbba.modulomedicogbba.controller.AppController;
import gustavorivera.proyectogrado.gbba.modulomedicogbba.model.FichaModelo;
import gustavorivera.proyectogrado.gbba.modulomedicogbba.model.Usuario;

public class ListaFichas extends AppCompatActivity {

    private final static String TAG = "FichasActivity:";

    private Usuario mUsuario;

    private int mIdUsuario = -1;
    private ArrayList<FichaModelo> mListFichas = new ArrayList<>();
    private FichaModeloAdapter mFichaModeloAdapter;

    private ListView mListaVista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_fichas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsuario = (Usuario) getIntent().getSerializableExtra(Login.USUARIO_SELECCIONADO);
        mListFichas = AppController.Companion.getInstance().getFichasUsuario(mUsuario.getId());

        mUsuario = new Usuario();
        mUsuario.setNombre("Paciente Prueba");


        for (int i = 0; i < 4; i++) {
            FichaModelo ficha1 = new FichaModelo();
            ficha1.setAltura(1.73 + i);
            ficha1.setFecha(i + "/" + i * 2 + "/2015");
            ficha1.setNuevo(false);
            mListFichas.add(ficha1);
        }

        for (int i = 4; i < 6; i++) {
            FichaModelo ficha1 = new FichaModelo();
            ficha1.setNuevo(true);
            ficha1.setFecha(i + "/" + i * 2 + "/2015");
            mListFichas.add(ficha1);
        }

        mFichaModeloAdapter = new FichaModeloAdapter(mListFichas);
        mListaVista = findViewById(R.id.listaficha_listview);
        mListaVista.setAdapter(mFichaModeloAdapter);

        mListaVista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FichaModelo fichaModelo = mListFichas.get(position);


                Intent i = new Intent(ListaFichas.this, Ficha.class);
                i.putExtra(Ficha.EXTRA, fichaModelo);
                startActivityForResult(i, position);


            }
        });


        // TODO AGREGAR NUEVA FICHAMODELO


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_fichas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            addFichaModelo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addFichaModelo() {
        FichaModelo nuevaFicha = new FichaModelo();
        nuevaFicha.setFecha("8/8/88");
        mListFichas.add(nuevaFicha);
        ((BaseAdapter) mListaVista.getAdapter()).notifyDataSetChanged();
    }

    private void mensaje(String mensaje) {
        Log.i(TAG, mensaje);
    }

    private class FichaModeloAdapter extends ArrayAdapter<FichaModelo> {
        FichaModeloAdapter(ArrayList<FichaModelo> fichaModeloAdapter) {
            super(ListaFichas.this, R.layout.fichamodelocolumna, R.id.fichamodelo_fecha, fichaModeloAdapter);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            FichaModelo fichaModelo = getItem(position);
            TextView fechaFicha = convertView.findViewById(R.id.fichamodelo_fecha);
            fechaFicha.setText(fichaModelo.getFecha());
            return convertView;
        }
    }
}
