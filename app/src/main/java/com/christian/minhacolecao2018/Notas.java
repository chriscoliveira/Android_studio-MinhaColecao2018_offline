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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Notas extends Activity {
	ZUtilitarios zutilitarios = new ZUtilitarios();
	ZInfoDB zinfodb = new ZInfoDB();

	EditText etId, etPais, etAno, etValor, etUnMonetaria, etAvaliado, etAparencia, etInfo, etTipo, etRepetida;
	Button GravarMoeda;
	ListView MostrarDados;
	int pos = 1;

	// String criterio = "Brasil";

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tela_listagemnot);

		zinfodb.AbreBanco(this);
		zinfodb.FiltrarRegistros(this, "Nota", "noBrasil", "pais asc, ano asc");

		MostrarDados = (ListView) findViewById(R.id.lvListagem);
		MostrarDados.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View view, int posicao, long arg3) {
				final TextView t_id = (TextView) view.findViewById(R.id.tvId);
				pos = Integer.parseInt((String) t_id.getText());
				zinfodb.dialog(Notas.this, "atz", "Nota", "Editar Nota");
				zinfodb.enviaDadosDialog(Notas.this, zinfodb.NOME_TABELA, pos);
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_notas, menu);
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
			NovoCadastro(Notas.this);
			return true;
			case R.id.menu_Est:
				zinfodb.FiltrarRegistros(this, "Nota", "noBrasil", "pais asc, ano asc");
				return true;
			case R.id.menu_Nac:
				zinfodb.FiltrarRegistros(this, "Nota", "Brasil", "ano asc");
				return true;
		case R.id.menu_AnoAZ:
			zinfodb.CarregaOrdenado(this, "Nota", "ano asc", "");
			return true;
		case R.id.menu_AnoZA:
			zinfodb.CarregaOrdenado(this, "Nota", "ano desc", "");
			return true;
		case R.id.menu_PaisAZ:
			zinfodb.CarregaOrdenado(this, "Nota", "pais asc", "");
			return true;
		case R.id.menu_PaisZA:
			zinfodb.CarregaOrdenado(this, "Nota", "pais desc", "");
			return true;
		case R.id.menu_ID_AZ:
			zinfodb.CarregaOrdenado(this, "Nota", "_id asc", "");
			return true;
		case R.id.menu_ID_ZA:
			zinfodb.CarregaOrdenado(this, "Nota", "_id desc", "");
			return true;
		case R.id.menu_Por_Moeda_az:
			zinfodb.CarregaOrdenado(this, "Nota", "moeda asc", "");
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void NovoCadastro(Activity activity) {
		zinfodb.dialog(activity, "add", "Nota", "Cadastrar Nota");
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, TPrincipal.class);
		startActivity(intent);
		this.finish();
	}

}
