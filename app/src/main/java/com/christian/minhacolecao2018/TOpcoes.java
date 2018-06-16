package com.christian.minhacolecao2018;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TOpcoes extends Activity {
    ZUtilitarios zutilitarios = new ZUtilitarios();
    ZInfoDB zinfodb = new ZInfoDB();
    protected SQLiteDatabase bancoDados = null;
    Cursor cc;
    private static final int REQUEST_INTERNET = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opcoes);
        if (ContextCompat.checkSelfPermission(TOpcoes.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TOpcoes.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_INTERNET);
        }

        Button btApagar = (Button) findViewById(R.id.btApagar);
        Button btExportar = (Button) findViewById(R.id.btExportar);

        Button btImportar = (Button) findViewById(R.id.btImportar);
        Button btVoltaPagina = (Button) findViewById(R.id.btVoltaPagina);

        btVoltaPagina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TOpcoes.this, TPrincipal.class);
                startActivity(intent);
                finish();
            }
        });

        btImportar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //zutilitarios.toast(TOpcoes.this, "Importando registros...");
                File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard, "/Download/resumoColecao.txt");
                BufferedReader br = null;
                String line;
                String sql = "\n";
                try {
                    br = new BufferedReader(new FileReader(file));


                    while ((line = br.readLine()) != null) {
                        //Log.i("sql", "" + line);
                        sql += line + " \n";

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(TOpcoes.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(TOpcoes.this);
                }
                builder.setTitle("Importar registros")
                        .setMessage(sql + "\nTem certeza que deseja importar Registros para o banco?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                zinfodb.importarLista(TOpcoes.this);
                                Intent intent = new Intent(TOpcoes.this, TPrincipal.class);
                                startActivity(intent);
                                TOpcoes.this.finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        btExportar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                zutilitarios.toast(TOpcoes.this, "Criando lista para exportar...");
                zinfodb.ResumoColecao(TOpcoes.this);
                zutilitarios.toast(TOpcoes.this, "Criando lista...");
                zinfodb.CriaListaParaExporacao(TOpcoes.this);
                zutilitarios.toast(TOpcoes.this, "Exportando dados...");
                zinfodb.ExportaCSV(TOpcoes.this);
                zutilitarios.toast(TOpcoes.this, "Enviando email...");
                zinfodb.enviarEmail(TOpcoes.this);

                File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard, "/Download/resumoColecao.txt");
                BufferedReader br = null;
                String line;
                String sql = "\n";
                try {
                    br = new BufferedReader(new FileReader(file));

                    while ((line = br.readLine()) != null) {
                        //Log.i("sql", "" + line);
                        sql += line + " \n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(TOpcoes.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(TOpcoes.this);
                }
                builder.setTitle("Registros Exportados")
                        .setMessage(sql)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


        btApagar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(TOpcoes.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(TOpcoes.this);
                }
                builder.setTitle("Apagar Banco de Dados")
                        .setMessage("Tem certeza que deseja apagar o banco de dados?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                zinfodb.deletar(TOpcoes.this);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_INTERNET) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //start audio recording or whatever you planned to do
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(TOpcoes.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show an explanation to the user *asynchronously*
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("This permission is important to record audio.")
                            .setTitle("Important permission required");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(TOpcoes.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_INTERNET);
                        }
                    });

                    ActivityCompat.requestPermissions(TOpcoes.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_INTERNET);

                } else {
                    //Never ask again and handle your app without permission.
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_opcoes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_moedas_euro1:
                setContentView(R.layout.op_euro1);
                return true;
            case R.id.menu_moedas_euro2:
                setContentView(R.layout.op_euro2);
                return true;
            case R.id.menu_moedas_euro3:
                setContentView(R.layout.op_euro3);
                return true;
            case R.id.menu_moedas_euro4:
                setContentView(R.layout.op_euro4);
                return true;
            case R.id.menu_locais_euro:
                setContentView(R.layout.op_cunhagem_euro);
                return true;
            case R.id.menu_moedas_japao:
                setContentView(R.layout.op_data_japao);
                return true;
            case R.id.menu_simbolos_moedas1:
                setContentView(R.layout.op_simbol1);
                return true;
            case R.id.menu_simbolos_moedas2:
                setContentView(R.layout.op_simbol2);
                return true;
            case R.id.menu_simbolos_moedas3:
                setContentView(R.layout.op_simbol3);
                return true;
            case R.id.menu_simbolos_moedas4:
                setContentView(R.layout.op_simbol4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TPrincipal.class);
        startActivity(intent);
        this.finish();
    }

}
