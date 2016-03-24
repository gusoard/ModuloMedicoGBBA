package gustavorivera.proyectogrado.gbba.modulomedicogbba;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by gustavo on 23/03/16.
 */
public class Ficha extends AppCompatActivity {


    /*
    * STRINGS DE CONTROL
    * */
    private static final String TAG = "Control Ficha";
    public static final String EXTRA = "Ficha_Seleccionada";
    public static final String DEVOLVER = "Ficha_Devuelta";

    /*
    * STRINGS DE SELECCION
    * */
    public static final String selectAltura = "Altura";
    public static final String selectECG = "ECG";
    public static final String selectVisualizar = "Visualizar ECG";
    public static final String selectSpO2 = "SpO2";
    public static final String selectEspirometro = "Espirometro";


    FichaModelo mFichaModelo;

    TextView mTextoFecha, mTextoSpo2, mTextoPpm, mTextoAltura;
    Button mMedirAltura, mMedirEsp, mVerEsp, mMedirSpo2, mMedirECG, mVerECG;


    /*
     *  CONTROL DE MEDICIONES
     * */

    private boolean isBaseLista;
    private float mBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ficha);

        //mFichaModelo = (FichaModelo) getIntent().getSerializableExtra(EXTRA);

        isBaseLista = false;
        mBase = 0;

        inicializarWidgets();
        // setTexto();
//        setClickListeners();

    }


    private void inicializarWidgets() {

        // Texto: Fecha de Ficha
        mTextoFecha = (TextView) findViewById(R.id.ficha_fecha);

        // Texto: Valor de Altura
        // Boton: Medicion de Altura
        mTextoAltura = (TextView) findViewById(R.id.ficha_valor_altura);
        mMedirAltura = (Button) findViewById(R.id.boton_medir_ALTURA);

        // Boton: Medicion de Espirometria
        // Boton: Visualizacion de Espirometria
        mMedirEsp = (Button) findViewById(R.id.boton_medir_ESP);
        mVerEsp = (Button) findViewById(R.id.boton_visualizar_ESP);

        // Texto: Valor de SpO2
        // Boton: Medicion de SpO2
        mTextoSpo2 = (TextView) findViewById(R.id.texto_spo2);
        mMedirSpo2 = (Button) findViewById(R.id.boton_medir_SPO2);

        // Texto: Texto de Pulsaciones Por Minuto (PPM)
        mTextoPpm = (TextView) findViewById(R.id.texto_ppm);

        // Boton: Medicion de ECG
        // Boton: Visualizacion de ECG
        mMedirECG = (Button) findViewById(R.id.boton_medir_ECG);
        mVerECG = (Button) findViewById(R.id.boton_visualizar_ECG);

    }

    private void setTexto() {
        /*
        * Si es una ficha nueva, los valores estaran vacios:
        *       En el caso de PPM se coloca el simbolo de valor nulo '--'
        *       En el caso de los otros valores, se ocultan los textos (SpO2 y Altura)
        *       y los botones de visualizar (Espirometria, ECG)
        *
        * Si es una ficha vieja, se muestran los valores guardados:
        *       En el caso de PPM, SpO2 y Altura, se habilita el texto (se deshabilita el boton)
        *       En el caso de Espirometria y ECG, se habilita el boton de Visualizar
        * */

        if (mFichaModelo.isNuevo()) {
            // La Ficha es nueva

            // Fecha

            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            mFichaModelo.setFecha(dateFormat.format(c.getTime())); // renders as 11/29/2009
            mTextoFecha.setText(mFichaModelo.getFecha());

            // Altura
            mTextoAltura.setVisibility(View.GONE);
            mMedirAltura.setVisibility(View.VISIBLE);

            // Espirometria
            mVerEsp.setVisibility(View.GONE);
            mMedirEsp.setVisibility(View.VISIBLE);

            // SpO2
            mTextoSpo2.setVisibility(View.GONE);
            mMedirSpo2.setVisibility(View.VISIBLE);

            // PPM
            mTextoPpm.setText(R.string.ficha_valor_nulo);

            // ECG
            mVerECG.setVisibility(View.GONE);
            mMedirECG.setVisibility(View.VISIBLE);


        } else {
            // La Ficha NO es nueva

            // FECHA
            if (mFichaModelo.getFecha() == null) {
                // En caso que no se haya guardado fecha
                mTextoFecha.setText(R.string.ficha_nohayfecha);
            } else {
                // Cargar la fecha guardada
                mTextoFecha.setText(mFichaModelo.getFecha());
            }


            // Altura
            mMedirAltura.setVisibility(View.GONE);
            if (mFichaModelo.getAltura() == null) {
                // En caso que no se haya guardado la medida de altura
                mTextoAltura.setText(R.string.ficha_valor_nulo);
            } else {
                // String Format -> Texto + formato (%f para float) , values in order
                mTextoAltura.setText(String.format("%f", mFichaModelo.getAltura()));
            }


            // Espirometria
            if (mFichaModelo.getEspirometria().isEmpty()) {
                // En caso que no se haya guardado la medida de Espirometria
                // se deja el boton visible, mas no es clickeable
                mVerEsp.setVisibility(View.VISIBLE);
                mMedirEsp.setVisibility(View.GONE);
                mVerEsp.setClickable(false);
                mVerEsp.setText(R.string.ficha_valor_nulo);
            } else {
                mVerEsp.setVisibility(View.VISIBLE);
                mMedirEsp.setVisibility(View.GONE);
                mVerEsp.setClickable(true);
            }

            // SpO2
            mMedirSpo2.setVisibility(View.GONE);
            if (mFichaModelo.getSpo2() == null) {
                // No se guardo nada
                mTextoSpo2.setText(R.string.ficha_valor_nulo);
            } else {
                // Cargamos el porcentaje de spo2
                // String Format -> Texto + formato (%d para Integer) , values in order
                mTextoSpo2.setText(String.format("%d%%", mFichaModelo.getSpo2()));
            }

            // PPM
            if (mFichaModelo.getPpm() == null) {
                // En caso que no se haya guardado la medida de PPM
                mTextoPpm.setText(R.string.ficha_valor_nulo);
            } else {
                // String Format -> Texto + formato (%f para float) , values in order
                mTextoPpm.setText(String.format("%d", mFichaModelo.getPpm()));
            }

            // ECG
            if (mFichaModelo.getEcg().isEmpty()){
                // No se guardo nada
                mVerECG.setVisibility(View.VISIBLE);
                mMedirECG.setVisibility(View.GONE);
                mVerECG.setClickable(false);
                mVerECG.setText(R.string.ficha_valor_nulo);
            }else{
                // Mostrar boton de visualizar
                mVerECG.setVisibility(View.VISIBLE);
                mMedirECG.setVisibility(View.GONE);
                mVerECG.setClickable(true);
            }


        }


    }

    private void setClickListeners() {

        /**
         *
         * TODO Botones para aplicar ClickListener:
         *          - Medir Altura: Llama a BT para medir altura. (Recordar medir Base)
         *          - Medir Esp: Lleva a otra actividad donde se muestra la grafica y otros datos.
         *          - Visualizar Esp: Lleva a la misma actv, pero sin activar BT.
         *          - Medir SpO2: Igual que altura
         *          - Medir y Visualizar ECG: Igual que Espirometria
         *
         *
         */

        // ALTURA
        mMedirAltura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isBaseLista) {
                    // Mide la Altura
                } else {
                    // Mide la Base



                }

            }
        });

        // ESPIROMETRO
        mMedirEsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Activity con opcion de BT
            }
        });

        mVerEsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Activity sin opcion de BT
            }
        });

        // SPO2
        mMedirSpo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mide SpO2
            }
        });

        // ECG
        mMedirECG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Activity con opcion de BT
            }
        });

        mVerECG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Activity sin opcion de BT

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_ficha, menu);

        return true;

    }
}