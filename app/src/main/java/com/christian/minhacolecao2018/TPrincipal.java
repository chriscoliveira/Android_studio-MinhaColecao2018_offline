package com.christian.minhacolecao2018;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class TPrincipal extends Activity {
    ZInfoDB zinfodb = new ZInfoDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.inicial);
        try {
            TextView tvVersao = (TextView) findViewById(R.id.tvVersa);
            Date buildDate = new Date(BuildConfig.BUILD_TIME);
            tvVersao.setText(" " + buildDate.toString());
        } catch (Exception e) {
            Log.i("TESTE", "erro ao exibir a versao" + e);
        }
        Button btMnacional = (Button) findViewById(R.id.btMNacional);
        Button btMestrangeira = (Button) findViewById(R.id.btMEstrangeira);
        Button btNota = (Button) findViewById(R.id.btNotas);
        Button btNotaBr = (Button) findViewById(R.id.btNotasBr);
        Button btTMoedas = (Button) findViewById(R.id.btTMoedas);
        Button btOpcao = (Button) findViewById(R.id.btOpcoes);
        Button btPesquisa = (Button) findViewById(R.id.btPesquisa);
        Button btNovo = (Button) findViewById(R.id.btNovo);

        zinfodb.somaTudo(this);


        btMnacional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TPrincipal.this, MoedasNacionais.class);
                startActivity(intent);
                TPrincipal.this.finish();
            }
        });
        btMestrangeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TPrincipal.this, TodasMoedasEstrangeiras.class);
                startActivity(intent);
                TPrincipal.this.finish();
            }
        });
        btNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TPrincipal.this, Notas.class);
                startActivity(intent);
                TPrincipal.this.finish();
            }
        });
        btNotaBr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TPrincipal.this, NotasBr.class);
                startActivity(intent);
                TPrincipal.this.finish();
            }
        });
        btTMoedas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TPrincipal.this, PaisPorPais.class);
                startActivity(intent);
                TPrincipal.this.finish();
            }
        });
        btNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NovoCadastro(TPrincipal.this);
            }
        });

        btOpcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TPrincipal.this, TOpcoes.class);
                startActivity(intent);
                TPrincipal.this.finish();
            }
        });
        btPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TPrincipal.this, TPesquisa.class);
                startActivity(intent);
                TPrincipal.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        mensagemSaida(this, "Sair da aplicacao", "Deseja sair?");
    }

    public void mensagemSaida(Activity activity, String titulo, String mensagem) {
        AlertDialog.Builder CaixaAlerta = new AlertDialog.Builder(activity);
        CaixaAlerta.setMessage(mensagem);
        CaixaAlerta.setTitle(titulo);
        CaixaAlerta.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        CaixaAlerta.setNegativeButton("NAO", null);
        CaixaAlerta.show();

    }

    private void NovoCadastro(Activity activity) {
        zinfodb.dialog(activity, "add", "Nota", "Cadastrar Nota");
    }
}
