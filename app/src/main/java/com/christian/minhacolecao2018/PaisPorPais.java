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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class PaisPorPais extends Activity {
    ZInfoDB zinfodb = new ZInfoDB();
    int pos = 1;
    ListView MostrarDados;

    @SuppressWarnings("static-access")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_listagem_pais);
        //zinfodb.FiltrarPaisPorPais(this, zinfodb.NOME_TABELA, "", "Moeda Estrangeira");
        zinfodb.contaPais(this);

        MostrarDados = (ListView) findViewById(R.id.lvListagemPais);
        MostrarDados.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
                //final TextView t_id = (TextView) view.findViewById(R.id.tvPais);
                irParaPais(arg2);
            }

        });

    }

    public void irParaPais(int arg2) {
        Intent intent = new Intent(PaisPorPais.this, ListaMoedasPaisSelecionado.class);

        //TODO preciso capturar somente o nome do pais da linha clidada
        String[] enviapais = MostrarDados.getItemAtPosition(arg2).toString().split("-");
        Log.i("PAIS", "" + enviapais[0]);
        intent.putExtra("pais", enviapais[0]);
        startActivity(intent);
        PaisPorPais.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_paispais, menu);

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
