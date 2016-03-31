package gustavorivera.proyectogrado.gbba.modulomedicogbba;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListaFichas extends AppCompatActivity {

    private final static String TAG = "Control Lista de Fichas";

    private Sujeto mSujeto;

    private FichaModeloAdapter mFichaModeloAdapter;

    private ListView mListaVista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_fichas);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO Que se cargue el sujeto (Obtener info de fichas guardadas)

        /**
         *
         * Algo tipo, GetExtras -> Nombre de paciente -> Carga fichas
         * -> Carga views
         *
         * Si es nuevo, inicializar la lista para que no haya problemas de que
         * es nulo
         *
         * */


        /** Logica del asunto:

         Cargar al sujeto.

         Agarrar el ArrayList de fichas que tiene el sujeto y pasarlos por el adaptador (ver abajo)

         Poner un ClickListener en cada "fila de ficha" el cual lleva a la FICHA (Ficha.java)

         Cuando se da click, pasar info de la fila de ficha seleccionada a Ficha.java

         Que Ficha.java haga lo que le de la gana

         Regresar de Ficha.java y almacenar esa ficha en el sujeto

         -----

         En caso de crear una nueva ficha, que esta vaya directo a Ficha.java

         **/

        mSujeto = new Sujeto();
        mSujeto.setNombre("Paciente Prueba");


        if (mSujeto.getFichas() == null){
            mSujeto.setFichas(new ArrayList<FichaModelo>());
        }


        for (int i = 0; i < 4; i++) {
            FichaModelo ficha1 = new FichaModelo();
            ficha1.setAltura(1.73+i);
            ficha1.setFecha(i + "/" + i * 2 + "/2015");
            ficha1.setNuevo(false);
            mSujeto.getFichas().add(ficha1);
        }

        for (int i = 4; i < 6; i++) {
            FichaModelo ficha1 = new FichaModelo();
            ficha1.setNuevo(true);
            ficha1.setFecha(i + "/" + i * 2 + "/2015");
            mSujeto.getFichas().add(ficha1);
        }

        mFichaModeloAdapter = new FichaModeloAdapter(mSujeto.getFichas());
        mListaVista = (ListView)findViewById(R.id.listaficha_listview);
        mListaVista.setAdapter(mFichaModeloAdapter);

        mListaVista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FichaModelo fichaModelo = mSujeto.getFichas().get(position);


                Intent i = new Intent(ListaFichas.this, Ficha.class);
                i.putExtra(Ficha.EXTRA, fichaModelo);
                startActivityForResult(i, position);



            }
        });


        // TODO AGREGAR NUEVA FICHAMODELO



    }


    private class FichaModeloAdapter extends ArrayAdapter<FichaModelo> {
        FichaModeloAdapter(ArrayList<FichaModelo> fichaModeloAdapter) {
            super(ListaFichas.this, R.layout.fichamodelocolumna, R.id.fichamodelo_fecha, fichaModeloAdapter);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            FichaModelo fichaModelo = getItem(position);
            TextView fechaFicha = (TextView) convertView.findViewById(R.id.fichamodelo_fecha);
            fechaFicha.setText(fichaModelo.getFecha());
            return convertView;
        }
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


    private void addFichaModelo(){
        FichaModelo nuevaFicha = new FichaModelo();
        nuevaFicha.setFecha("8/8/88");
        mSujeto.getFichas().add(nuevaFicha);
        ((BaseAdapter) mListaVista.getAdapter()).notifyDataSetChanged();
    }

    private void mensaje(String mensaje) {
        Log.i(TAG, mensaje);
    }
}
