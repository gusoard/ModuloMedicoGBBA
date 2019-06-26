package gustavorivera.proyectogrado.gbba.modulomedicogbba;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

/**
 * Created by gustavo on 29/03/16.
 */
public class FichaGrafica extends Activity {


    LineChart mLineChart;
    Button mButton, mParar;
    CheckBox check;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == Bluetooth.MESSAGE_READ) {

                byte[] bufferLectura = (byte[]) msg.obj;

                String stringEntrada = new String(bufferLectura);

                mensajeLog("String que recibo: " + stringEntrada);
                Float dataEntrada = Float.parseFloat(stringEntrada);
                mensajeLog("Float que convierto: " + dataEntrada);

                LineData data = mLineChart.getData();

                if (data != null) {
                    LineDataSet set = data.getDataSetByIndex(0);
                    // set.addEntry(...); // can be called as well

                    if (set == null) {
                        set = createSet();
                        data.addDataSet(set);
                    }

                    data.addXValue("" + (data.getXValCount() + 1));
                    data.addEntry(new Entry(dataEntrada, set.getEntryCount()), 0);

                    mLineChart.notifyDataSetChanged();
                    mLineChart.invalidate();
                }


            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ficha_grafica);
        Bluetooth.gethandler(mHandler);

        //  Set views
        mLineChart = findViewById(R.id.ficha_prueba_grafica);
        mButton = findViewById(R.id.ficha_prueba_streamdata);
        mParar = findViewById(R.id.ficha_prueba_stopdata);
        check = findViewById(R.id.checkBox);

        // Cuando no hay data
        mLineChart.setDescription("");
        mLineChart.setNoDataTextDescription("Hace falta data.");

        // Habilitar eventos de touch screen
        mLineChart.setTouchEnabled(true);

        // Habilita acciones de movimiento y escalada en la grafica
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setDrawGridBackground(false); // GRID IMPORTANTE!!!

        mLineChart.setPinchZoom(true); // Garantizar igual escalado en x y y

        // set an alternative background color
        mLineChart.setBackgroundColor(Color.LTGRAY);


        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // agregar data vacia a la grafica
        mLineChart.setData(data);


        /*
         *
         * TODO Modificar laleyenda segun lo necesario
         *
         * */


        setOnClickListenersButtons();

    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleSize(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }


    private void setOnClickListenersButtons() {


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar Stream
                mensajeToast("Iniciando stream...");
                Bluetooth.hiloConectado.write("P");
                check.setChecked(true);


            }
        });

        mParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Parar Stream
                mensajeToast("Deteniendo stream...");
                Bluetooth.hiloConectado.write("Q");
                check.setChecked(false);

            }
        });


    }


    private void mensajeToast(String mensaje) {
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mensajeLog(String mensaje) {
        Log.d("Grafica Prueba", mensaje);
    }


}
