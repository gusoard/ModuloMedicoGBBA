package gustavorivera.proyectogrado.gbba.modulomedicogbba;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public static final String selectAltura = "A";
    public static final String selectBase = "B";
    public static final String selectECG = "E";
    public static final String selectVisualizar = "Visualizar ECG";
    public static final String selectSpO2 = "S";
    public static final String selectEspirometro = "P";


    FichaModelo mFichaModelo;

    private String mInstruccion;

    TextView mTextoFecha, mTextoSpo2, mTextoPpm, mTextoAltura;
    Button mMedirAltura, mMedirEsp, mVerEsp, mMedirSpo2, mMedirECG, mVerECG;

    boolean fichaNueva;


    /*
     *  CONTROL DE MEDICIONES
     * */

    private boolean isBaseLista;
    private double mBase, mAltura, mDistancia;

    private Integer mPpm, mSpo2;

    private ArrayList<Float> mECGArray = new ArrayList<>();
    private ArrayList<Float> mEspArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ficha);


        Bluetooth.gethandler(mHandler);

        mFichaModelo = (FichaModelo) getIntent().getSerializableExtra(EXTRA);

        fichaNueva = mFichaModelo.isNuevo();

        isBaseLista = false;
        mBase = 0;

        inicializarWidgets();
        setTexto();
        setClickListeners();

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
        mTextoSpo2 = (TextView) findViewById(R.id.ficha_valor_spo2);
        mMedirSpo2 = (Button) findViewById(R.id.boton_medir_SPO2);

        // Texto: Texto de Pulsaciones Por Minuto (PPM)
        mTextoPpm = (TextView) findViewById(R.id.ficha_valor_ppm);

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

        if (fichaNueva) {
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
                mTextoAltura.setText(String.format("%f" + R.string.centimetros, mFichaModelo.getAltura()));
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
                mTextoSpo2.setText(String.format("%d" + R.string.porcentaje, mFichaModelo.getSpo2()));
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
            if (mFichaModelo.getEcg().isEmpty()) {
                // No se guardo nada
                mVerECG.setVisibility(View.VISIBLE);
                mMedirECG.setVisibility(View.GONE);
                mVerECG.setClickable(false);
                mVerECG.setText(R.string.ficha_valor_nulo);
            } else {
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

                    if (Bluetooth.hiloConectado != null) {
                        mInstruccion = selectAltura;
                        mensajeToast("Quiero Altura");
                        Bluetooth.hiloConectado.write(mInstruccion);
                    }else {
                        mensajeToast("Debe iniciar el BT");
                    }

                } else {
                    // Mide la Base

                    if (Bluetooth.hiloConectado != null) {
                        mensajeToast("Quiero Base");
                        mInstruccion = selectBase;
                        Bluetooth.hiloConectado.write(mInstruccion);
                    }else {
                        mensajeToast("Debe iniciar el BT");
                    }


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

                startActivity(new Intent(Ficha.this,FichaGrafica.class));

            }
        });

        mVerECG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Activity sin opcion de BT


            }
        }
        );
    }

// TODO REMOVER EL SUPPRESS LINT CUANDO RESUELVA EL LEAK

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msj) {
            super.handleMessage(msj);

            switch (msj.what) {
                case Bluetooth.SUCCESS_CONNECT:
                    Bluetooth.hiloConectado = new Bluetooth.ConnectedThread((BluetoothSocket) msj.obj);
                    mensajeToast("Conectado");
                    String s = "Conectado bien";
                    Bluetooth.hiloConectado.start();
                    break;
                case Bluetooth.MESSAGE_READ:

                    byte[] bufferLectura = (byte[]) msj.obj;



                    /**
                     *
                     * STRING(byte DATA, int OFFSET, int BYTECOUNT)
                     * BYTE COUNT DEBERIA SER DINAMICO O UN NUMERO FIJO QUE BASTE PARA TODOS LOS DATOS
                     * SIN IMPORTAR LA MEDICION QUE SE HAGA
                     *
                     * */
                    String stringEntrada = new String(bufferLectura, 0, 5);

                    mensajeLog(stringEntrada);
                    switch (mInstruccion) {
                        case selectBase:
                            // Almacenar Base
                            mensajeLog("Recibiendo Base");
                            if (stringEntrada.indexOf('b') == 0 && stringEntrada.indexOf(".") == 2) {
                                stringEntrada = stringEntrada.replace("b", "");
                                if (isDoubleNumber(stringEntrada)) {
                                    mBase = Double.parseDouble(stringEntrada);
                                    isBaseLista = true;
                                }
                            }
                            break;
                        case selectAltura:
                            // Valor
                            mensajeLog("Recibiendo Distancia");
                            if (isBaseLista) {
                                // Just in case..

                                if (stringEntrada.indexOf('a') == 0 && stringEntrada.indexOf(".") == 2) {
                                    stringEntrada = stringEntrada.replace("a", "");
                                    if (isDoubleNumber(stringEntrada)) {
                                        mDistancia = Double.parseDouble(stringEntrada);

                                        if (mBase > mDistancia) {
                                            mAltura = mBase - mDistancia;
                                        }

                                        mMedirAltura.setText(String.format("%f" + R.string.centimetros, mAltura));


                                    }
                                }

                            }

                            break;
                        case selectSpO2:
                            // Valor

                            break;
                        case selectECG:
                            // Grafica
                            // Sacar ppm una vez termine

                            break;
                        case selectEspirometro:
                            // Grafica

                            break;
                        default:
                    }


                    /**
                     *
                     * REVISAR
                     * Si no me equivoco, con esto se verifica con que se inicia y ubica el punto
                     *
                     * */
                    break;
                default:
                    break;
            }
        }

        public boolean isDoubleNumber(String num) {
            try {
                Double.parseDouble(num);
            } catch (NumberFormatException nfe) {
                return false;
            }
            return true;
        }

    };


    private void mensajeToast(String mensaje) {
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mensajeLog(String mensaje) {
        Log.d(TAG, mensaje);
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ficha_conectar, menu);
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
            startActivity(new Intent("android.intent.action.BTTesis"));
            return true;
        }else if (id == R.id.desc){
            Bluetooth.desconectar();
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_ficha, menu);

        return true;

    }

    */
}