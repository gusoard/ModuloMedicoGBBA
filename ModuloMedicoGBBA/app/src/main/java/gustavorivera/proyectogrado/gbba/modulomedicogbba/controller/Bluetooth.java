package gustavorivera.proyectogrado.gbba.modulomedicogbba.controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import gustavorivera.proyectogrado.gbba.modulomedicogbba.R;

/**
 * Created by gustavo on 23/03/16.
 */

// TODO Change logic of bluetooth connection - 30/06/2019
public class Bluetooth extends Activity {

    private static final String TAG = "Bluetooth";

    public static void gethandler(Handler handler) {
        mHandler = handler;
    }

    static Handler mHandler = new Handler();

    static ConnectedThread hiloConectado;

    public static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    protected static final int SUCCESS_CONNECT = 0;
    public static final int MESSAGE_READ = 1; // TODO change -> NOT public


    ListView listaDisp;
    ArrayAdapter<String> listaAdapter;
    static BluetoothAdapter btAdapter;
    Set<BluetoothDevice> arregloDisp;
    ArrayList<String> dispAsociados;
    ArrayList<BluetoothDevice> dispositivos;
    IntentFilter filtroIntent;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth);

        init();

        if (btAdapter == null) {
            Toast.makeText(Bluetooth.this, "No BT detected", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (!btAdapter.isEnabled()) {
                turnOnBT();
            }
            getPairedDevices();
            startDiscovery();
        }
    }


    /*
    *
    * PRENDER BLUETOOTH Y OBTENER LISTA DE DISPOSITIVOS
    *
    * */
    private void turnOnBT() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, 1);
    }

    private void getPairedDevices() {
        btAdapter.cancelDiscovery();
        btAdapter.startDiscovery();

    }

    private void startDiscovery() {
        arregloDisp = btAdapter.getBondedDevices();
        if (arregloDisp.size() > 0) {
            for (BluetoothDevice device : arregloDisp) {
                dispAsociados.add(device.getName());
            }
        }
    }

    /*
    *
    *
    * INICIALIZAR VIEWS Y VARIABLES DE BLUETOOTH
    *
    *
    * */

    private void init() {

        listaDisp = (ListView) findViewById(R.id.bluetooth_lista_asociados);
        listaDisp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (btAdapter.isDiscovering()) {
                    btAdapter.cancelDiscovery();
                }

                if (listaAdapter.getItem(position).contains("(Asociado)")) {

                    BluetoothDevice dispositivoSeleccionado = dispositivos.get(position);
                    ConnectThread hiloConectado = new ConnectThread(dispositivoSeleccionado);

                    hiloConectado.start();
                } else {
                    mensajeToast("Dispositivo no esta asociado...");
                }

            }
        });

        listaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 0);
        listaDisp.setAdapter(listaAdapter);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        dispAsociados = new ArrayList<>();
        filtroIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        dispositivos = new ArrayList<BluetoothDevice>();


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String accion = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(accion)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    dispositivos.add(device);

                    String s = "";
                    for (int a = 0; a < dispAsociados.size(); a++) {
                        if (device.getName().equals(dispAsociados.get(a))) {
                            s = "(Asociado)";
                            break;
                        }
                    }


                    listaAdapter.add(device.getName() + " " + s + " " + "\n" + device.getAddress());

                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(accion)) {

                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(accion)) {
                    mensajeToast("Listo con Discovery de dispositivos");

                } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(accion)) {
                    if (btAdapter.getState() == btAdapter.STATE_OFF) {
                        turnOnBT();
                    }
                }

            }
        };

        registerReceiver(receiver, filtroIntent);

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(receiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(receiver, filter);
    }

    public static void desconectar() {
        if (hiloConectado != null) {
            hiloConectado.cancel();
            hiloConectado = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            mensajeToast("Bluetooth debe estar habilitado para continuar");
        }
    }

    /*
    *
    * FUNCIONES DE MENSAJES
    *
    * */


    private void mensajeToast(String mensaje) {
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mensajeLog(String mensaje) {
        Log.d(TAG, mensaje);
    }
        /*
    *
    * FIN FUNCIONES DE MENSAJES
    *
    * */

    /**
     * SACADO DE LA PAGINA DE DEVELOPERS
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(mUUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            btAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }


    static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer;  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {

                    try {
                        sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    buffer = new byte[1024];
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String income) {
            try {
                mmOutStream.write(income.getBytes());

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * HASTA ACA
     */

}