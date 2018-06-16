package com.christian.minhacolecao2018;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MoedasNacionais extends Activity {
    ZInfoDB zinfodb = new ZInfoDB();
    ListView MostrarDados;
    int pos = 1;

    @SuppressWarnings("static-access")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_listagemnac);

        zinfodb.FiltrarRegistros(this, "Moeda", "Brasil", "ano asc");

        MostrarDados = (ListView) findViewById(R.id.lvListagem);
        MostrarDados.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int posicao, long arg3) {
                final TextView t_id = (TextView) view.findViewById(R.id.tvId);
                pos = Integer.parseInt((String) t_id.getText());
                zinfodb.dialog(MoedasNacionais.this, "atz", "Moeda", "Editar Moeda Nacional");
                zinfodb.enviaDadosDialog(MoedasNacionais.this, zinfodb.NOME_TABELA, pos);
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_VoltarPagina:
                Intent intent = new Intent(this, TPrincipal.class);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.menu_Adcionar:
                NovoCadastro(MoedasNacionais.this);
                return true;
            case R.id.menu_Est:
                zinfodb.FiltrarRegistros(this, "Moeda", "", "pais asc, ano asc");
                return true;
            case R.id.menu_Nac:
                zinfodb.FiltrarRegistros(this, "Moeda", "Brasil", "ano asc");
                return true;
            case R.id.menu_AnoAZ:
                zinfodb.CarregaOrdenado(this, "Moeda", "ano asc", "Brasil");
                return true;
            case R.id.menu_AnoZA:
                zinfodb.CarregaOrdenado(this, "Moeda", "ano desc", "Brasil");
                return true;
            case R.id.menu_ID_AZ:
                zinfodb.CarregaOrdenado(this, "Moeda", "_id asc", "Brasil");
                return true;
            case R.id.menu_ID_ZA:
                zinfodb.CarregaOrdenado(this, "Moeda", "_id desc", "Brasil");
                return true;
            case R.id.menu_Material:
                zinfodb.CarregaOrdenado(this, "Moeda", "material asc", "Brasil");
                return true;
            case R.id.menu_Diametro:
                zinfodb.CarregaOrdenado(this, "Moeda", "diametro asc", "Brasilo");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void NovoCadastro(Activity activity) {
        zinfodb.dialog(activity, "add", "Moeda", "Cadastrar Moedas Nacional");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TPrincipal.class);
        startActivity(intent);
        this.finish();
    }

}
