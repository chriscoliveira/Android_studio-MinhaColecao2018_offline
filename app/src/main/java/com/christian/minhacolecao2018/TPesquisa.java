package com.christian.minhacolecao2018;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TPesquisa extends Activity {
    ZUtilitarios zutilitarios = new ZUtilitarios();
    ZInfoDB zinfodb = new ZInfoDB();
    protected SQLiteDatabase bancoDados = null;
    Cursor cc;
    ListView MostrarDados;
    int pos = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_pesquisa);
        zinfodb.AbreBanco(this);

		/*
            coleta o texto digitado e a opcao de filtro desejada, ai faz uma busca no banco de dados em busca do resultado
		*/

        final EditText edtPesquisa = (EditText) findViewById(R.id.edtpesquisa);

        edtPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valorPesquisa = edtPesquisa.getText().toString();
                zinfodb.FiltroPesquisa(TPesquisa.this, valorPesquisa);
                zinfodb.soma(TPesquisa.this,"pesquisa","",valorPesquisa);
            }
        });

        MostrarDados = (ListView) findViewById(R.id.lvListagem);

        MostrarDados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int posicao, long arg3) {
                final TextView t_id = (TextView) view.findViewById(R.id.tvId);
                pos = Integer.parseInt((String) t_id.getText());
                zinfodb.dialog(TPesquisa.this, "atz", "", "Resultado da Pesquisa: " + edtPesquisa.getText().toString());
                zinfodb.enviaDadosDialog(TPesquisa.this, zinfodb.NOME_TABELA, pos);
                return false;
            }
        });
        Button btVoltaPagina = (Button) findViewById(R.id.btVoltaPagina);

        btVoltaPagina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TPesquisa.this, TPrincipal.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TPrincipal.class);
        startActivity(intent);
        this.finish();
    }

}
