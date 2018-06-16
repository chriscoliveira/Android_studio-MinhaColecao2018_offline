package com.christian.minhacolecao2018;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ListaMoedasPaisSelecionado extends Activity {
    ZUtilitarios zutilitarios = new ZUtilitarios();
    ZInfoDB zinfodb = new ZInfoDB();

    EditText etId, etPais, etAno, etValor, etUnMonetaria, etAvaliado, etAparencia, etInfo, etTipo, etRepetida;
    Button GravarMoeda;
    ListView MostrarDados;
    int pos = 1;
    // String criterio = "Brasil";
    String pais;

    @SuppressWarnings("static-access")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle parametrosRecebidos = intent.getExtras();
        pais = parametrosRecebidos.getString("pais");
        Log.i("appNumismatica", "." + pais + ".");

        if (pais != "Brasil") {
            Log.i("appNumismatica", "brasil");
        }

        setContentView(R.layout.tela_listagempaisselec);

        zinfodb.AbreBanco(this);
        zinfodb.FiltrarRegistros(this, "", pais, "tipo");
        // zinfodb.carregarPais(this,pais,"Moeda","");

        MostrarDados = (ListView) findViewById(R.id.lvListagem);

        MostrarDados.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int posicao, long arg3) {
                final TextView t_id = (TextView) view.findViewById(R.id.tvId);
                pos = Integer.parseInt((String) t_id.getText());
                zinfodb.dialog(ListaMoedasPaisSelecionado.this, "atz", "Moeda", "Editar Moeda");
                zinfodb.enviaDadosDialog(ListaMoedasPaisSelecionado.this, zinfodb.NOME_TABELA, pos);
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_moeda_colecao, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_Adcionar:
                NovoCadastro(ListaMoedasPaisSelecionado.this);
                return true;
            case R.id.menu_VoltarPagina:
                voltarpagina();
                return true;
            case R.id.menu_AnoAZ:
                zinfodb.FiltrarRegistros(this, "Moeda", pais, "ano asc");
                return true;
            case R.id.menu_AnoZA:
                zinfodb.FiltrarRegistros(this, "Moeda", pais, "ano desc");
                return true;
            case R.id.menu_Por_Moeda_az:
                zinfodb.FiltrarRegistros(this, "Moeda", pais, "unidade asc");
                return true;
            case R.id.menu_ID_AZ:
                zinfodb.FiltrarRegistros(this, "Moeda", pais, "_id asc");
                return true;
            case R.id.menu_ID_ZA:
                zinfodb.FiltrarRegistros(this, "Moeda", pais, "_id desc");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void NovoCadastro(Activity activity) {
        zinfodb.dialog(activity, "add", "Moeda", "Cadastrar Moeda");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PaisPorPais.class);
        startActivity(intent);
        this.finish();
    }

    public void voltarpagina() {
        Intent intent = new Intent(this, PaisPorPais.class);
        startActivity(intent);
        this.finish();
    }
}
