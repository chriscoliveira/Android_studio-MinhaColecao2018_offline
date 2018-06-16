package com.christian.minhacolecao2018;

/*
 * 12-11-15 adicionado ao sistemde exportacao a criacao de 2 arquivos csv Notas e Moedas. 
 * 090216 corrigido falha na troca de telas e organização por pais/ano
 * 15062018 -> acrescentado na tabela o campo datacadastro. e modificado as funçoes de importar e exportar e o dialog para trabalhar com este campo.
-> modificado a tela inicial com mais 1 tela de notas brasileiras.
-> colocado na tela inicial a data de ccompilação do apk
-> ao importar e exportar aparece um alerta mostrando as informações de notas e moedas no banco.
-> corrigido o erro ao cadastrar algo a partir da tela inicial
-> adicionado ao anexo do email o resumoColecao.txt, este arquivo precisa ser colocado na pasta Download do celular antes de importar os dados.

 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuppressLint("DefaultLocale")
public class ZInfoDB {

    public static String NOME_BANCO = "Numismatica";
    public static String NOME_TABELA = "Colecao";
    public static String ID = "_id";
    public static String PAIS = "pais";
    public static String ANO = "ano";
    public static String KRAUSE = "krause";
    public static String VALOR = "valor";
    public static String MOEDA = "moeda";
    public static String ANVERSO = "anverso";
    public static String REVERSO = "reverso";
    public static String MATERIAL = "material";
    public static String DIAMETRO = "diametro";
    public static String DETALHE = "detalhe";
    public static String TIPO = "tipo";
    public static String QUALIDADE = "qualidade";
    public static String DATACADASTRO = "datacadastro";
    protected SQLiteDatabase bancoDados = null;
    ZUtilitarios zutilitarios = new ZUtilitarios();
    Dialog dialog = null;
    Button btAcaoDialog, btApagar;
    CheckBox cSituacao;
    ListView MostraDados;
    SimpleCursorAdapter adapterLista;
    Cursor cursor;
    Integer Situa = 0;
    String Pago, ValoresMoedas = "", ValoresNotas = "", Valores = "", Resumo = "";
    String CRITERIO = null;

    public void CriaBanco(Activity activity) {
        String criaTabela = "CREATE TABLE IF NOT EXISTS " + NOME_TABELA + "(" + ID + " INTEGER PRIMARY KEY, " + PAIS
                + " TEXT, " + ANO + " INTEGER, " + KRAUSE + " TEXT," + VALOR + " TEXT, " + MOEDA + " TEXT," + TIPO
                + " TEXT," + QUALIDADE + " TEXT," + MATERIAL + " TEXT," + DIAMETRO + " TEXT," + DETALHE + " TEXT,"
                + ANVERSO + " TEXT," + REVERSO + " TEXT," + DATACADASTRO + " TEXT" + ")";

        // uso do context devido a classe ser simples e nao poder executar o
        // comando em outra classe
        bancoDados = activity.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
        bancoDados.execSQL(criaTabela);
        bancoDados.getPath();
        //Log.i("bancodadosLocal", "Caminho " + bancoDados.getPath());
        bancoDados.close();
    }

    public void AbreBanco(Activity activity) {
        bancoDados = activity.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
        bancoDados.getPath();
        //Log.i("bancodadosLocal", "Caminho " + bancoDados.getPath());
    }

    public void FechaBanco() {
        bancoDados.close();
    }

    public void dialog(final Activity activity, final String acao, final String tipo, String texto) {
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.tela_cadastro); // xml com o conteudo do
        // dialog
        dialog.setTitle(texto); // titulo do dialog Cadastrar Nova Moedas"
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        String[] TIPOITEM = new String[]{"Moeda", "Nota"};
        String[] TODOSPAISES = new String[]{"Acores", "Afeganistao", "Africa Central Beac", "Africa Do Sul", "Africa Equatorial Francesa", "Africa Occidental", "Africa Ocidental Britanica", "Africa Ocidental Francesa", "Africa Oriental Alema", "Africa Oriental Britanica", "Albania", "Alderney", "Alemanha", "Alemanha Gdr", "Alemanha Terceiro Reich", "Andorra", "Angola", "Anguilla", "Antigua E Barbuda", "Antilhas Holandesas", "Arabia Do Sul", "Arabia Saudita", "Argelia", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijao", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgica", "Belize", "Benin", "Bermudas", "Biafra", "Birmania", "Boemia E Moravia", "Bolivia", "Borneu Do Norte", "Bosnia Herzegovina", "Botsuana", "Brasil", "Brunei", "Bulgaria", "Burquina Faso", "Burundi", "Butao", "Cabo Verde", "Camaroes", "Cambodja", "Canada", "Cazaquistao", "Ceilao", "Chade", "Chile", "China", "China Japones", "China Republica", "Chipre", "Cidade Do Vaticano", "Cidade Livre De Danzig", "Cochinchina Francesa", "Colombia", "Comores", "Congo", "Congo Belga", "Congo Rdc", "Coreia Do Norte", "Coreia Do Sul", "Costa Do Marfim", "Costa Rica", "Cracovia", "Creta", "Croacia", "Cuba", "Curacao", "Dinamarca", "Djibouti", "Dominica", "Dominio De Terra Nova", "Egito", "El Salvador", "Emirados Arabes Unidos", "Equador", "Eritreia", "Eslovaquia", "Eslovenia", "Espanha", "Espanha Guerra Civil", "Estabelecimentos Dos Estreitos", "Estado Livre Do Congo", "Estados Da Africa Equatorial", "Estados Do Caribe Oriental", "Estados Papais", "Estonia", "Etiopia", "Eua", "Fiji", "Filipinas", "Finlandia", "Franca", "Frances Dos Afars E Issas", "Gabao", "Gambia", "Gana", "Georgia", "Georgia Do Sul", "Gibraltar", "Granada", "Grecia", "Groenlandia", "Guatemala", "Guernsey", "Guiana", "Guiana Britanica", "Guine", "Guine-Bissau", "Guine Equatorial", "Haiti", "Hiderabade", "Holanda", "Honduras", "Honduras Britanicas", "Hong Kong", "Hungria", "Iemen", "Iemen Do Sul", "Ilha De Ascensao", "Ilha De Man", "Ilhas Cayman", "Ilhas Cook", "Ilhas Falklands", "Ilhas Faroe", "Ilhas Marshall", "Ilhas Pitcairn", "Ilhas Salomao", "Ilhas Turcas E Caicos", "Ilhas Virgens Britanicas", "Imperio Alemao", "Imperio Otomano", "India", "India Britanica", "India Portuguesa", "Indias Orientais Neerlandesas", "Indochina Francesa", "Indonesia", "Ira", "Iraque", "Irlanda", "Islandia", "Israel", "Italia", "Iugoslavia", "Jamaica", "Japao", "Jersey", "Jordania", "Katanga", "Kiribati", "Kuwait", "Laos", "Lesoto", "Letonia", "Libano", "Liberia", "Libia", "Liechtenstein", "Lituania", "Lombardo-Veneto", "Luxemburgo", "Macau", "Macedonia", "Madagascar", "Malasia", "Malasia Peninsular", "Malasia Peninsular E Borneu Britanico", "Malawi", "Maldivas", "Mali", "Malta", "Marrocos", "Mauricias", "Mauritania", "Mexico", "Mocambique", "Moldavia", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Nagorno-Karabakh", "Namibia", "Nauru", "Nepal", "Nicaragua", "Niger", "Nigeria", "Niue", "Noruega", "Nova Caledonia", "Nova Guine", "Nova Guine Alema", "Novas Hebridas", "Nova Zelandia", "Oceania Francesa", "Oman", "Ordem De Malta", "Palau", "Palestina", "Panama", "Papua Nova Guine", "Paquistao", "Paraguai", "Peru", "Polinesia Francesa", "Polonia", "Portugal", "Qatar", "Qatar E Dubai", "Quenia", "Quirguistao", "Reino Unido", "Republica Centro-Africana", "Republica Checa", "Republica Dominicana", "Republica Sul-Africana", "Reuniao", "Rodesia", "Rodesia Do Sul", "Rodesia E Niassalandia", "Romenia", "Ruanda", "Ruanda-Burundi", "Ruanda-Urundi", "Russia", "Saara Ocidental", "Samoa", "San Marino", "Santa Helena", "Santa Helena E Ascensao", "Santa Lucia", "Sao Cristovao E Nevis", "Sao Pedro E Miquelao", "Sao Tome E Principe", "Sao Vicente E Granadinas", "Sarawak", "Sarre", "Seicheles", "Senegal", "Serra Leoa", "Servia", "Singapura", "Siria", "Somalia", "Somalia Italiana", "Somalilandia", "Somalilandia Francesa", "Spitsbergen", "Sri Lanka", "Suazilandia", "Sudao", "Sudao Do Sul", "Suecia", "Suica", "Suriname", "Tailandia", "Taiwan", "Tajiquistao", "Tanzania", "Tchecoslovaquia", "Territorio Antartico Britanico", "Territorio Britanico Do Oceano Indico", "Timor Leste", "Timor Portugues", "Togo", "Tokelau", "Tonga", "Transnistria", "Trinidad E Tobago", "Tristao Da Cunha", "Tunisia", "Turquemenistao", "Turquia", "Tuva", "Tuvalu", "Ucrania", "Uganda", "Uniao Sovietica", "Uruguai", "Uzbequistao", "Vanuatu", "Venezuela", "Vietnam", "Vietnam Do Sul", "Zaire", "Zambia", "Zanzibar", "Zimbabue"};
        String[] TODOSMATERIAIS = new String[]{"ACO", "ACO BRONZE", "ACO COBRE", "ACO COBRE NIQUEL", "ACO INOX", "ACO LATAO", "ACO NIQUEL", "ALUMINIO", "ALUMINIO BRONZE", "ALUMINIO COBRE NIQUEL", "ALUMINIO NIQUEL BRONZE", "BI METALICA", "BRONZE", "BRONZE ACO", "BRONZE NIQUEL", "COBRE", "COBRE ACO", "COBRE CUPRO NIQUEL", "COBRE FERRO", "COBRE LATAO", "COBRE NIQUEL", "COBRE NIQUEL ZINCO", "COBRE ZINCO", "COBRE ZINCO MAGNESIO NIQUEL", "COBRE ZINCO NIQUEL", "FERRO NIQUEL", "INOX", "LATAO", "LATAO ACO", "LATAO CUPRO NIQUEL", "LATAO DE ACO REVESTIDO", "LATAO REVESTIDO DE ACO", "MANGANES LATAO", "NIQUEL", "NIQUEL ACO", "NIQUEL BRONZE", "NIQUEL LATAO", "PAPEL", "PAPEL BAMBU", "POLIMERO", "PRATA", "OURO"
        };
        String[] CLASSIFICACAO = new String[]{"BNC", "FC", "FE", "SOB", "BELA", "MBC", "BC", "REG", "UTG", "REPOR"};
        String[] TIPOMOEDA = new String[]{"AFGHANIS", "ARIARY", "AUSTRALES", "BAHT", "BAISA", "BILETOV", "BOLIVAR", "BOLIVIANO", "CORDOBA", "COROA", "COUPON", "CROWN", "CRUZADO", "CRUZADO NOVO", "CRUZEIRO", "CRUZEIRO NOVO", "CRUZEIRO REAL", "DENAR", "DINAR", "DINARA", "DIRHAM", "DIRHAN", "DOLAR", "DOLAR AUSTRALIANO", "DOLAR BAAMIANO", "DOLAR CANADENSE", "DONG", "DRACHMA", "DRAM", "ESCUDO", "EURO", "FEN", "FLORIN", "FORINT", "FRANCO", "FRANCO SUICO", "GOURDE", "GROSCHEN", "GROSZ", "GROSZE", "GROSZY", "GUARANI", "GULDEN", "HALALAS", "HRYVNIA", "INTIS", "JIAO", "KARBOVANTE", "KINA", "KIP", "KOPECK", "KRONA", "KRONE", "KRONEN", "KROON", "KROONI", "KUNA", "KURUS", "KWACHA", "KWANCHA", "KWANZA", "KYAT", "LARI", "LEI", "LEU", "LEV", "LEVA", "LIBRA", "LIRA", "LIRE", "LITA", "LIVRE", "MARK", "MARKKA", "MARKKAA", "METICAIS", "MONGO", "NAIRA", "NEW PENCE", "NEW PENNY", "NEW SHEQEL", "NGULTRUM", "NOUVEAUX MAKUTA", "NUEVO PESO", "NUEVO SOL", "ORE", "ORE DANMARK", "ORE NORGE", "ORE SVERIGE", "PAISA", "PAISE", "PARA", "PATACA", "PENCE", "PENNIA", "PENNY", "PESETA", "PESO", "PESO BOLIVIANO", "PESO URUGUAIO", "PFENNING", "PIASTRE", "PISO", "POISHA", "POUND", "POUND SUDANESE", "QUETZAL", "RAND", "RAPPEN", "REAL", "REICH", "REICHMARK", "REICHSPFENNING", "REIS", "RIAL", "RIEL", "RINGGIT", "RUBLE", "RUBLEI", "RUBLO", "RUPEE", "RUPIAH", "RYAL", "RYEL", "SATANG", "SCHILLING", "SEN", "SHEQEL", "SLOTY", "SOL DE ORO", "SOM", "SOMONI", "STOTINOV", "SUCRE", "TAKA", "TENGE", "TOGROG", "TOLAR", "TUGRIK", "TYIYN", "WHAN", "WON", "WU JIAO", "YEN", "YUAN", "ZLOTY", "ZLOTYCH"};

        ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, TIPOITEM);

        ArrayAdapter<String> adapterPais = new ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, TODOSPAISES);

        ArrayAdapter<String> adapterMaterial = new ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, TODOSMATERIAIS);

        ArrayAdapter<String> adapterQualidade = new ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, CLASSIFICACAO);

        ArrayAdapter<String> adapterMoeda = new ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, TIPOMOEDA);

        //AutoCompleteTextView textView = (AutoCompleteTextView) dialog.findViewById(R.id.etTipo);
        //textView.setAdapter(adapter);

        final TextView tvi = (TextView) dialog.findViewById(R.id.tvi);
        final EditText etAno = (EditText) dialog.findViewById(R.id.etAno);
        final AutoCompleteTextView etPais = (AutoCompleteTextView) dialog.findViewById(R.id.etPais);
        final EditText etKrause = (EditText) dialog.findViewById(R.id.etKrause);
        final EditText etValor = (EditText) dialog.findViewById(R.id.etValor);
        final AutoCompleteTextView etMoeda = (AutoCompleteTextView) dialog.findViewById(R.id.etMoeda);
        final AutoCompleteTextView etTipo = (AutoCompleteTextView) dialog.findViewById(R.id.etTipo);
        final AutoCompleteTextView etQualidade = (AutoCompleteTextView) dialog.findViewById(R.id.etQualidade);
        final AutoCompleteTextView etMaterial = (AutoCompleteTextView) dialog.findViewById(R.id.etMaterial);
        final EditText etDiametro = (EditText) dialog.findViewById(R.id.etDiametro);
        final EditText etDetalhe = (EditText) dialog.findViewById(R.id.etDetalhe);
        final EditText etAnverso = (EditText) dialog.findViewById(R.id.etAnverso);
        final EditText etReverso = (EditText) dialog.findViewById(R.id.etReverso);
        final TextView tvdatacadastro = (TextView) dialog.findViewById(R.id.tv_datacadastro);
        etPais.setAdapter(adapterPais);
        etTipo.setAdapter(adapterTipo);
        etMoeda.setAdapter(adapterMoeda);
        etQualidade.setAdapter(adapterQualidade);
        etMaterial.setAdapter(adapterMaterial);


        btAcaoDialog = (Button) dialog.findViewById(R.id.btGravarMoeda);

        if (texto == "Cadastrar Moedas Nacional" || texto == "Cadastrar Moeda Estrangeira") {
            etTipo.setText("Moeda");
        }
        if (texto == "Cadastrar Nota") {
            etTipo.setText("Nota");
        }

        if (acao == "add") {
            btAcaoDialog.setText("Cadastrar");
            // TODO definir opcao para ocultar o checkbox
        }
        if (acao == "del") {
            btAcaoDialog.setText("Apagar");
        }
        if (acao == "atz") {
            btAcaoDialog.setText("Atualizar");
        }

        btAcaoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (acao == "add") {
                    insert(activity, NOME_TABELA, etPais.getText().toString(), etAno.getText().toString(),
                            etKrause.getText().toString(), etValor.getText().toString(), etMoeda.getText().toString(),
                            etTipo.getText().toString(), etQualidade.getText().toString(),
                            etMaterial.getText().toString(), etDiametro.getText().toString(),
                            etDetalhe.getText().toString(), etAnverso.getText().toString(),
                            etReverso.getText().toString());
                }
                if (acao == "del")
                    delete(activity, NOME_TABELA, tvi.getText().toString(), tipo);
                if (acao == "atz") {
                    atualiza(activity, NOME_TABELA, etPais.getText().toString(), etAno.getText().toString(),
                            etKrause.getText().toString(), etValor.getText().toString(), etMoeda.getText().toString(),
                            etTipo.getText().toString(), etQualidade.getText().toString(),
                            etMaterial.getText().toString(), etDiametro.getText().toString(),
                            etDetalhe.getText().toString(), etAnverso.getText().toString(),
                            etReverso.getText().toString(), tvi.getText().toString());
                }
                dialog.cancel();
            }

        });

        btApagar = (Button) dialog.findViewById(R.id.btApagar);
        btApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(activity, NOME_TABELA, tvi.getText().toString(), tipo);
            }
        });

        Button btFechar = (Button) dialog.findViewById(R.id.btFechar);
        btFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        dialog.show();
    }

    public void enviaDadosDialog(Activity activity, String tabela, int Posicao) {
        try {
            AbreBanco(activity);
            cursor = bancoDados.query(tabela, null, ID + "=" + Posicao, null, null, null, null);
            // sql(tabela, ID+"="+Posicao,tipo);

            final TextView tvi = (TextView) dialog.findViewById(R.id.tvi);
            final EditText etAno = (EditText) dialog.findViewById(R.id.etAno);
            final EditText etPais = (EditText) dialog.findViewById(R.id.etPais);
            final EditText etKrause = (EditText) dialog.findViewById(R.id.etKrause);
            final EditText etValor = (EditText) dialog.findViewById(R.id.etValor);
            final EditText etMoeda = (EditText) dialog.findViewById(R.id.etMoeda);
            final EditText etTipo = (EditText) dialog.findViewById(R.id.etTipo);
            final EditText etQualidade = (EditText) dialog.findViewById(R.id.etQualidade);
            final EditText etMaterial = (EditText) dialog.findViewById(R.id.etMaterial);
            final EditText etDiametro = (EditText) dialog.findViewById(R.id.etDiametro);
            final EditText etDetalhe = (EditText) dialog.findViewById(R.id.etDetalhe);
            final EditText etAnverso = (EditText) dialog.findViewById(R.id.etAnverso);
            final EditText etReverso = (EditText) dialog.findViewById(R.id.etReverso);
            final TextView tvdatacadastro = (TextView) dialog.findViewById(R.id.tv_datacadastro);
            // Typeface font = Typeface.createFromAsset(activity,
            // "fonts/TT0627M.TTF");
            // bt1.setTypeface(font);

            while (cursor.moveToNext()) {
                etPais.setText(cursor.getString(cursor.getColumnIndex(PAIS)));
                etAno.setText(cursor.getString(cursor.getColumnIndex(ANO)));
                etKrause.setText(cursor.getString(cursor.getColumnIndex(KRAUSE)));
                etValor.setText(cursor.getString(cursor.getColumnIndex(VALOR)));
                etMoeda.setText(cursor.getString(cursor.getColumnIndex(MOEDA)));
                etTipo.setText(cursor.getString(cursor.getColumnIndex(TIPO)));
                etQualidade.setText(cursor.getString(cursor.getColumnIndex(QUALIDADE)));
                etMaterial.setText(cursor.getString(cursor.getColumnIndex(MATERIAL)));
                etDiametro.setText(cursor.getString(cursor.getColumnIndex(DIAMETRO)));
                etDetalhe.setText(cursor.getString(cursor.getColumnIndex(DETALHE)));
                etAnverso.setText(cursor.getString(cursor.getColumnIndex(ANVERSO)));
                etReverso.setText(cursor.getString(cursor.getColumnIndex(REVERSO)));
                tvi.setText(cursor.getString(cursor.getColumnIndex(ID)));
                tvdatacadastro.setText(cursor.getString(cursor.getColumnIndex(DATACADASTRO)));
            }

            FechaBanco();

        } catch (Exception er) {
            zutilitarios.toast(activity, "Erro ao enviar os dados para o Dialog: " + er);
            //Log.i("dialog()", "" + er);
        }
    }
    // -----------------------------------------------------------------------------------

    // TODO

    @SuppressWarnings("deprecation")
    public void FiltrarRegistros(Activity activity, String tipo, String pais, String ordem) {
        soma(activity, tipo, pais, "");
        MostraDados = (ListView) activity.findViewById(R.id.lvListagem);

        if (VerificaFiltrarRegistros(activity, tipo, pais, ordem)) {
            adapterLista = new SimpleCursorAdapter(activity, R.layout.tela_listagem_itens, cursor,
                    new String[]{TIPO, PAIS, ANO, KRAUSE, VALOR, MOEDA, ID, DIAMETRO, MATERIAL, ANVERSO, REVERSO},
                    new int[]{R.id.tvTipo, R.id.tvPais, R.id.tvAno, R.id.tvKrause, R.id.tvValor, R.id.tvMoeda,
                            R.id.tvId, R.id.tvDiametro, R.id.tvMaterial, R.id.tvAnverso, R.id.tvReverso});
            MostraDados.setAdapter(adapterLista);
        } else {
            zutilitarios.toast(activity, "Não existem dados a exibir: ");
            MostraDados.setAdapter(null);
        }
    }

    private boolean VerificaFiltrarRegistros(Activity activity, String tipo, String pais, String ordem) {
        try {
            AbreBanco(activity);

            // verifica os criterios do filtro
            if (tipo == "Nota" & pais == "")
                CRITERIO = "TIPO = 'Nota'";

            if (tipo == "Nota" & pais == "Brasil")
                CRITERIO = "TIPO = 'Nota' and PAIS = 'Brasil'";

            if (tipo == "Nota" & pais == "noBrasil")
                CRITERIO = "TIPO = 'Nota' and PAIS != 'Brasil'";

            if (tipo == "Moeda" & pais == "Brasil")
                CRITERIO = "TIPO = 'Moeda' and PAIS = 'Brasil'";

            if (tipo == "Moeda" & pais == "")
                CRITERIO = "TIPO = 'Moeda' and PAIS != 'Brasil'";

            if (tipo == "" & pais != "")
                CRITERIO = "PAIS = '" + pais + "'";

            if (ordem == "")
                ordem = "_id desc";

            cursor = bancoDados.query(NOME_TABELA, null, CRITERIO, null, null, null, ordem);

            if (cursor.getCount() != 0) // se existir registro
            {
                cursor.moveToFirst(); // movimenta para o 1º registro
                return true;
            } else
                return false;
        } catch (Exception er) {
            zutilitarios.toast(activity, "Nao existe registros a exibir");
            return false;
        } finally {
            FechaBanco();
        }

    }


    // TODO

    @SuppressWarnings("deprecation")
    public void FiltroPesquisa(Activity activity, String texto) {

        MostraDados = (ListView) activity.findViewById(R.id.lvListagem);

        if (VerificaFiltrarPesquisa(activity, texto)) // verifica
        // se a função é true

        {
            adapterLista = new SimpleCursorAdapter(activity, R.layout.tela_listagem_pesquisa, cursor,
                    new String[]{TIPO, PAIS, ANO, KRAUSE, VALOR, MOEDA, ID, MATERIAL},
                    new int[]{R.id.tvTipo, R.id.tvPais, R.id.tvAno, R.id.tvKrause, R.id.tvValor, R.id.tvMoeda,
                            R.id.tvId, R.id.tvMaterial});
            MostraDados.setAdapter(adapterLista); // executa a ação
        } else {
            zutilitarios.toast(activity, "Não existem dados a exibir: ");
            MostraDados.setAdapter(null);
        }
    }

    private boolean VerificaFiltrarPesquisa(Activity activity, String texto) {
        try {
            AbreBanco(activity);

            CRITERIO = "DETALHE like '%" + texto + "%' OR ANVERSO  like '%" + texto + "%' OR REVERSO like '%" + texto +
                    "%' OR KRAUSE like '%" + texto + "%' OR MATERIAL like '%" + texto + "%' OR MOEDA like '%" + texto +
                    "%' OR ANO like '%" + texto + "%' OR PAIS like '%" + texto + "%' OR QUALIDADE like '%" + texto +
                    "%' OR DIAMETRO like '%" + texto + "%' OR VALOR like '%" + texto + "%'";


            cursor = bancoDados.query(NOME_TABELA, null, CRITERIO, null,
                    null, null, null);

            if (cursor.getCount() != 0) // se existir registro
            {
                cursor.moveToFirst(); // movimenta para o 1º registro
                return true;
            } else
                return false;
        } catch (Exception er) {
            zutilitarios.toast(activity, "Nao existe registros a exibir");
            return false;
        } finally {
            FechaBanco();
        }

    }


    @SuppressWarnings("deprecation")
    public void FiltrarPaisPorPais(Activity activity, String tabela, String pais, String tipo) {
        soma(activity, tipo, pais, "");
        MostraDados = (ListView) activity.findViewById(R.id.lvListagemPais);
        if (VerificaFiltropaisporpais(activity, tabela, tipo, 0)) {
            adapterLista = new SimpleCursorAdapter(activity, R.layout.tela_listagem_pais_itens, cursor,
                    new String[]{PAIS, ID}, new int[]{R.id.tvPais, R.id.tvId});
            MostraDados.setAdapter(adapterLista); // executa a ação
        } else {
            zutilitarios.toast(activity, "Não existem dados a exibir: ");
            MostraDados.setAdapter(null);
        }
    }

    private boolean VerificaFiltropaisporpais(Activity activity, String tabela, String tipo, int naoUsar) {
        try {
            AbreBanco(activity);
            cursor = bancoDados.query(true, tabela, null, null, null, "pais", null, "pais asc", null, null);

            if (cursor.getCount() != 0) // se existir registro
            {
                cursor.moveToFirst(); // movimenta para o 1º registro
                return true;
            } else
                return false;
        } catch (Exception er) {
            zutilitarios.toast(activity, "Nao existe registros a exibir");
            return false;
        } finally {
            FechaBanco();
        }
    }

    // TODO

    // ordenação por menu
    @SuppressWarnings("deprecation")
    public void CarregaOrdenado(Activity activity, String tipo, String order, String pais) {

        soma(activity, tipo, pais, "");
        MostraDados = (ListView) activity.findViewById(R.id.lvListagem);
        if (VerificaOrdenado(activity, tipo, order, pais)) // verifica se a
        // função é true
        {
            adapterLista = new SimpleCursorAdapter(activity, R.layout.tela_listagem_itens, cursor,
                    new String[]{TIPO, PAIS, ANO, KRAUSE, VALOR, MOEDA, ID, DIAMETRO, MATERIAL, ANVERSO, REVERSO},
                    new int[]{R.id.tvTipo, R.id.tvPais, R.id.tvAno, R.id.tvKrause, R.id.tvValor, R.id.tvMoeda,
                            R.id.tvId, R.id.tvDiametro, R.id.tvMaterial, R.id.tvAnverso, R.id.tvReverso});
            MostraDados.setAdapter(adapterLista); // executa a ação
        } else {
            zutilitarios.toast(activity, "Não existem dados a exibir: ");
            MostraDados.setAdapter(null);
        }
    }

    private boolean VerificaOrdenado(Activity activity, String tipo, String order, String pais) {
        try {
            AbreBanco(activity);

            // verifica os criterios do filtro
            if (tipo == "Nota" & pais == "")
                CRITERIO = "TIPO = 'Nota'";

            if (tipo == "Moeda" & pais == "Brasil")
                CRITERIO = "TIPO = 'Moeda' and PAIS = 'Brasil'";

            if (tipo == "Moeda" & pais == "")
                CRITERIO = "TIPO = 'Moeda' and PAIS != 'Brasil'";

            if (tipo == "" & pais != "")
                CRITERIO = "PAIS = '" + pais + "'";

            if (order == "")
                order = "_id desc";

            cursor = bancoDados.query(NOME_TABELA, null, CRITERIO, null, null, null, order);

            if (cursor.getCount() != 0) // se existir registro
            {
                cursor.moveToFirst(); // movimenta para o 1º registro
                return true;
            } else
                return false;
        } catch (Exception er) {
            zutilitarios.toast(activity, "Nao existe registros a exibir");
            return false;
        } finally {
            FechaBanco();
        }

    }

    public void insert(Activity activity, String tabela, String pais, String ano, String krause, String valor,
                       String moeda, String tipo, String qualidade, String material, String diametro, String detalhe,
                       String anverso, String reverso) {
        Date date = new Date();
        String data = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
        try {
            AbreBanco(activity);
            ContentValues contentValuesCampos = new ContentValues();
            contentValuesCampos.put(PAIS, pais);
            contentValuesCampos.put(ANO, ano);
            contentValuesCampos.put(VALOR, valor);
            contentValuesCampos.put(KRAUSE, krause);
            contentValuesCampos.put(VALOR, valor);
            contentValuesCampos.put(MOEDA, moeda);
            contentValuesCampos.put(TIPO, tipo);
            contentValuesCampos.put(QUALIDADE, qualidade);
            contentValuesCampos.put(MATERIAL, material);
            contentValuesCampos.put(DIAMETRO, diametro);
            contentValuesCampos.put(DETALHE, detalhe);
            contentValuesCampos.put(ANVERSO, anverso);
            contentValuesCampos.put(REVERSO, reverso);
            contentValuesCampos.put(DATACADASTRO, data);

            bancoDados.insert(tabela, null, contentValuesCampos);
            zutilitarios.toast(activity, "Cadastro ok!");
        } catch (Exception e) {
            //Log.i("errrrroooooo", "xxx " + e);
        }
        try {
            FiltrarRegistros(activity, tipo, "", "pais asc, ano asc");
        }catch (Exception e){
            return;
        }
        dialog.cancel();

    }

    public void deleteNotas(Activity activity) {
        try {
            String texto = TIPO + " = 'Nota'";
            AbreBanco(activity);
            bancoDados.delete(NOME_TABELA, texto, null);
            FechaBanco();
            // FiltrarRegistros(activity, tipo, "", "");
            dialog.cancel();
        } catch (Exception e) {
            //Log.i("delete", "erro " + e);
        }

    }

    public void delete(Activity activity, String tabela, String id, String tipo) {
        try {
            String texto = ID + " = " + id;
            AbreBanco(activity);
            bancoDados.delete(tabela, texto, null);
            FechaBanco();
            FiltrarRegistros(activity, tipo, "", "pais asc, ano asc");
            dialog.cancel();
        } catch (Exception e) {
            //Log.i("delete", "erro " + e);
        }

    }

    public void atualiza(Activity activity, String tabela, String pais, String ano, String krause, String valor,
                         String moeda, String tipo, String qualidade, String material, String diametro, String detalhe,
                         String anverso, String reverso, String id) {
        try {
            String texto = ID + " = " + id;
            AbreBanco(activity);
            ContentValues contentValuesCampos = new ContentValues();
            contentValuesCampos.put(PAIS, pais);
            contentValuesCampos.put(ANO, ano);
            contentValuesCampos.put(VALOR, valor);
            contentValuesCampos.put(KRAUSE, krause);
            contentValuesCampos.put(VALOR, valor);
            contentValuesCampos.put(MOEDA, moeda);
            contentValuesCampos.put(TIPO, tipo);
            contentValuesCampos.put(QUALIDADE, qualidade);
            contentValuesCampos.put(MATERIAL, material);
            contentValuesCampos.put(DIAMETRO, diametro);
            contentValuesCampos.put(DETALHE, detalhe);
            contentValuesCampos.put(ANVERSO, anverso);
            contentValuesCampos.put(REVERSO, reverso);
            bancoDados.update(tabela, contentValuesCampos, texto, null);
            FechaBanco();
            FiltrarRegistros(activity, tipo, pais, "pais asc, ano asc");

        } catch (Exception e) {
            //Log.i("delete", "erro " + e);
        }
    }

    public void soma(Activity activity, String tipo, String pais, String texto) {
        Integer cont = 0;
        AbreBanco(activity);
        // verifica os criterios do filtro
        if (tipo == "Nota" & pais == "")
            CRITERIO = "TIPO = 'Nota'";

        if (tipo == "Moeda" & pais == "Brasil")
            CRITERIO = "TIPO = 'Moeda' and PAIS = 'Brasil'";

        if (tipo == "Moeda" & pais == "")
            CRITERIO = "TIPO = 'Moeda' and PAIS != 'Brasil'";

        if (tipo == "" & pais != "")
            CRITERIO = "PAIS = '" + pais + "'";

        if (tipo == "Nota" & pais == "noBrasil")
            CRITERIO = "TIPO = 'Nota' and PAIS != 'Brasil'";

        if (tipo == "Nota" & pais == "Brasil")
            CRITERIO = "TIPO = 'Nota' and PAIS = 'Brasil'";


        if (tipo == "pesquisa")
            CRITERIO = "DETALHE like '%" + texto + "%' OR ANVERSO  like '%" + texto + "%' OR REVERSO like '%" + texto +
                    "%' OR KRAUSE like '%" + texto + "%' OR MATERIAL like '%" + texto + "%' OR MOEDA like '%" + texto +
                    "%' OR ANO like '%" + texto + "%' OR PAIS like '%" + texto + "%' OR QUALIDADE like '%" + texto +
                    "%' OR DETALHE like '%" + texto + "%'";
        cursor = bancoDados.query(NOME_TABELA, null, CRITERIO, null, null, null, null);

        while (cursor.moveToNext()) {
            cont++;
        }

        TextView tvresumo = (TextView) activity.findViewById(R.id.tvResumao);
        tvresumo.setText("Total de " + tipo + " = " + cont);
        FechaBanco();

    }

    public void somaTudo(Activity activity) {
        Integer cont1 = 0, cont2 = 0, cont3 = 0, cont4 = 0;
        AbreBanco(activity);

        TextView tvNotaBrasil = (TextView) activity.findViewById(R.id.tvNotasNacional);
        TextView tvMoedaBrasil = (TextView) activity.findViewById(R.id.tvMoedaNacional);
        TextView tvMoedas = (TextView) activity.findViewById(R.id.tvMoedasTotal);
        TextView tvNotas = (TextView) activity.findViewById(R.id.tvNotasTotal);
        TextView tvColecao = (TextView) activity.findViewById(R.id.tvColecaoTotal);

        // verifica os criterios do filtro

        CRITERIO = "TIPO = 'Nota'";
        cursor = bancoDados.query(NOME_TABELA, null, CRITERIO, null, null, null, null);

        while (cursor.moveToNext()) {
            cont1++;
        }

        CRITERIO = "TIPO = 'Nota' and PAIS = 'Brasil'";
        cursor = bancoDados.query(NOME_TABELA, null, CRITERIO, null, null, null, null);

        while (cursor.moveToNext()) {
            cont2++;
        }

        CRITERIO = "TIPO = 'Moeda' and PAIS = 'Brasil'";
        cursor = bancoDados.query(NOME_TABELA, null, CRITERIO, null, null, null, null);

        while (cursor.moveToNext()) {
            cont3++;
        }

        CRITERIO = "TIPO = 'Moeda'";
        cursor = bancoDados.query(NOME_TABELA, null, CRITERIO, null, null, null, null);

        while (cursor.moveToNext()) {
            cont4++;
        }

        tvNotas.setText("Notas Total = " + cont1);
        tvNotaBrasil.setText("Notas Brasil = " + cont2);
        tvMoedaBrasil.setText("Moedas Brasil = " + cont3);
        tvMoedas.setText("Moedas Total = " + cont4);
        Integer total = cont1 + cont4;
        tvColecao.setText("Colecao Total = " + total);
        FechaBanco();

    }

    public void somapais(Activity activity, String pais) {
        Integer cont = 0;
        AbreBanco(activity);
        cursor = bancoDados.query(NOME_TABELA, null, PAIS + "='" + pais + "'", null, null, null, null);
        while (cursor.moveToNext()) {
            cont++;
        }

        TextView tvresumo = (TextView) activity.findViewById(R.id.tvResumao);
        tvresumo.setText("Total de moedas do " + pais + " = " + cont);
        FechaBanco();

    }

    @SuppressWarnings("unused")
    public void CriaListaParaExporacao(Activity activity) {
        int contagem = 0;

        AbreBanco(activity);
        Cursor cc = bancoDados.query(NOME_TABELA, null, null, null, null, null, null);

        while (cc.moveToNext()) {
            Valores += "INSERT INTO " + NOME_TABELA + " (" + PAIS + "," + ANO + "," + KRAUSE + "," + VALOR + "," + MOEDA
                    + "," + TIPO + "," + QUALIDADE + "," + MATERIAL + "," + DIAMETRO + "," + DETALHE + "," + ANVERSO
                    + "," + REVERSO + "," + DATACADASTRO + ") VALUES ('";

            Valores += cc.getString(cc.getColumnIndex(PAIS)).toString() + "',"
                    + cc.getString(cc.getColumnIndex(ANO)).toString() + ",'"
                    + cc.getString(cc.getColumnIndex(KRAUSE)).toString() + "','"
                    + cc.getString(cc.getColumnIndex(VALOR)).toString() + "','"
                    + cc.getString(cc.getColumnIndex(MOEDA)).toString() + "','"
                    + cc.getString(cc.getColumnIndex(TIPO)).toString() + "','"
                    + cc.getString(cc.getColumnIndex(QUALIDADE)).toString() + "','"
                    + cc.getString(cc.getColumnIndex(MATERIAL)).toString() + "','"
                    + cc.getString(cc.getColumnIndex(DIAMETRO)).toString() + "','"
                    + cc.getString(cc.getColumnIndex(DETALHE)).toString() + "','"
                    + cc.getString(cc.getColumnIndex(ANVERSO)).toString() + "','"
                    + cc.getString(cc.getColumnIndex(REVERSO)).toString() + "','"
                    + cc.getString(cc.getColumnIndex(DATACADASTRO)).toString() + "'),; \n";
            contagem++;

        }

        Valores = Valores.replaceAll(",;", ";");

        SalvarArquivo(Valores, activity);
        cc.close();
        zutilitarios.toast(activity, "Exportacao dos dados realizada com sucesso!");
        FechaBanco();

    }

    private void SalvarArquivo(String valor, Activity activity) {

        File arq;
        FileOutputStream fos;
        byte[] dados;
        try {
            arq = new File(Environment.getExternalStorageDirectory(), "/Download/bancoMoedas.txt");
            fos = new FileOutputStream(arq.toString());
            // transforma o texto digitado em array de bytes
            dados = valor.getBytes();
            // escreve os dados e fecha o arquivo
            fos.write(dados);
            fos.flush();
            fos.close();
            // enviarEmail(activity);
        } catch (IOException e) {
            zutilitarios.toast(activity, "Exportação com erro! " + e);
            // trace("Erro : " + e.getMessage());
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unused")
    public void ExportaCSV(Activity activity) {
        int contagem = 0;
        AbreBanco(activity);

        Date date = new Date();
        String data = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);

        Cursor cc = bancoDados.query(NOME_TABELA, null, "tipo = 'Moeda'", null, null, null, "pais asc");
        zutilitarios.toast(activity, "Exportando..");
        ValoresMoedas = "";
        ValoresMoedas += PAIS + "," + ANO + "," + KRAUSE + "," + VALOR + "," + MOEDA + "," + TIPO + "," + QUALIDADE
                + "," + MATERIAL + "," + DIAMETRO + "," + DETALHE + "," + ANVERSO + "," + REVERSO + "," + DATACADASTRO + "; \n";
        while (cc.moveToNext()) {
            ValoresMoedas += cc.getString(cc.getColumnIndex(PAIS)).toString() + ","
                    + cc.getString(cc.getColumnIndex(ANO)).toString() + ","
                    + cc.getString(cc.getColumnIndex(KRAUSE)).toString() + ","
                    + cc.getString(cc.getColumnIndex(VALOR)).toString() + ","
                    + cc.getString(cc.getColumnIndex(MOEDA)).toString() + ","
                    + cc.getString(cc.getColumnIndex(TIPO)).toString() + ","
                    + cc.getString(cc.getColumnIndex(QUALIDADE)).toString() + ","
                    + cc.getString(cc.getColumnIndex(MATERIAL)).toString() + ","
                    + cc.getString(cc.getColumnIndex(DIAMETRO)).toString() + ","
                    + cc.getString(cc.getColumnIndex(DETALHE)).toString() + ","
                    + cc.getString(cc.getColumnIndex(ANVERSO)).toString() + ","
                    + cc.getString(cc.getColumnIndex(REVERSO)).toString() + ","
                    + cc.getString(cc.getColumnIndex(DATACADASTRO)).toString() + "; \n";
            contagem++;
        }
        Cursor cc1 = bancoDados.query(NOME_TABELA, null, "tipo = 'Nota'", null, null, null, "pais asc");

        ValoresNotas = "";
        ValoresNotas += PAIS + "," + ANO + "," + KRAUSE + "," + VALOR + "," + MOEDA + "," + TIPO + "," + QUALIDADE + ","
                + MATERIAL + "," + DIAMETRO + "," + DETALHE + "," + ANVERSO + "," + REVERSO + "," + DATACADASTRO + "; \n";
        while (cc1.moveToNext()) {
            ValoresNotas += cc1.getString(cc1.getColumnIndex(PAIS)).toString() + ","
                    + cc1.getString(cc1.getColumnIndex(ANO)).toString() + ","
                    + cc1.getString(cc1.getColumnIndex(KRAUSE)).toString() + ","
                    + cc1.getString(cc1.getColumnIndex(VALOR)).toString() + ","
                    + cc1.getString(cc1.getColumnIndex(MOEDA)).toString() + ","
                    + cc1.getString(cc1.getColumnIndex(TIPO)).toString() + ","
                    + cc1.getString(cc1.getColumnIndex(QUALIDADE)).toString() + ","
                    + cc1.getString(cc1.getColumnIndex(MATERIAL)).toString() + ","
                    + cc1.getString(cc1.getColumnIndex(DIAMETRO)).toString() + ","
                    + cc1.getString(cc1.getColumnIndex(DETALHE)).toString() + ","
                    + cc1.getString(cc1.getColumnIndex(ANVERSO)).toString() + ","
                    + cc1.getString(cc1.getColumnIndex(REVERSO)).toString() + ","
                    + cc1.getString(cc1.getColumnIndex(DATACADASTRO)).toString() + "; \n";
            contagem++;
        }

        ValoresNotas = ValoresNotas.replaceAll(",;", ";");
        ValoresMoedas = ValoresMoedas.replaceAll(",;", ";");
        SalvarArquivoCSV(ValoresNotas, ValoresMoedas, activity);

        cc.close();
        zutilitarios.toast(activity, "Exportacao CSV dos dados realizada com sucesso!");
        FechaBanco();

    }

    private void SalvarArquivoCSV(String valorNotas, String valorMoedas, Activity activity) {

        File arq;
        FileOutputStream fos;
        byte[] dados;
        try {
            arq = new File(Environment.getExternalStorageDirectory(), "/Download/bancoNotas.csv");
            fos = new FileOutputStream(arq.toString());
            // transforma o texto digitado em array de bytes
            dados = valorNotas.getBytes();
            // escreve os dados e fecha o arquivo
            fos.write(dados);
            fos.flush();
            fos.close();

            arq = new File(Environment.getExternalStorageDirectory(), "/Download/bancoMoedas.csv");
            fos = new FileOutputStream(arq.toString());
            // transforma o texto digitado em array de bytes
            dados = valorMoedas.getBytes();
            // escreve os dados e fecha o arquivo
            fos.write(dados);
            fos.flush();
            fos.close();
            // enviarEmail(activity);
        } catch (IOException e) {
            zutilitarios.toast(activity, "Exportação com erro! " + e);
            // trace("Erro : " + e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    public void enviarEmail(Activity activity) {
        Date date = new Date();
        String stringDate = DateFormat.getDateTimeInstance().format(date);
        String subject = "Colecao de Moedas - backup";
        String message = "Segue em anexo um backup dos dados - "+stringDate;

        Uri uri1 = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/Download/bancoMoedas.txt"));
        Uri uri2 = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/Download/bancoMoedas.csv"));
        Uri uri3 = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/Download/bancoNotas.csv"));
        Uri uri4 = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/Download/resumoColecao.txt"));

        Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, message);
        ArrayList<Uri> uris = new ArrayList<Uri>();
        uris.add(uri1);
        uris.add(uri2);
        uris.add(uri3);
        uris.add(uri4);
        i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        activity.startActivity(Intent.createChooser(i, "Send mail"));

    }

    public void deletar(Activity activity) {
        AbreBanco(activity);
        try {
            bancoDados.delete(NOME_TABELA, null, null);
            zutilitarios.toast(activity, "Dados Apagados com sucesso!");
        } catch (Exception er) {
            zutilitarios.toast(activity, "Erro: " + er);
        }
        FechaBanco();
    }

    @SuppressWarnings("resource")
    public void importarLista(Activity activity) {
        try {
            zutilitarios.toast(activity, "Importando registros...");
            File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(sdcard, "/Download/bancoMoedas.txt");
            AbreBanco(activity);

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                //Log.i("sql", "" + line);
                String sql = line;
                bancoDados.execSQL(sql);
            }
            zutilitarios.toast(activity, "Registros Importados com Sucesso!");
        } catch (IOException e) {
            zutilitarios.toast(activity, "Ocorreu um erro ao importar os dados: " + e);
            //Log.i("aviso", "ERRO IMPORTAR:: " + e);
        }

        FechaBanco();
    }

    public void contaPais(Activity activity) {
        AbreBanco(activity);
        Integer contagem = 0, pais = 0;
        int count = 0, countMoeda = 0;
        Integer paisCont = 0;

        //pega quantidade de cadastros
        cursor = bancoDados.query(true, NOME_TABELA, null, null, null, "pais", null, "pais asc", null, null);
        while (cursor.moveToNext()) {
            contagem++;
        }

        Log.i("PAIS", "Total " + contagem);
        String StringPais[] = new String[contagem];
        String StringPaisContNotas[] = new String[contagem];
        String StringPaisContMoeda[] = new String[contagem];
        String StringPaisFinal[] = new String[contagem];

        // pega os paises únicos e armazena em um array
        Log.i("PAIS", "Total array " + StringPais.length);
        cursor = bancoDados.query(true, NOME_TABELA, null, null, null, "pais", null, "pais asc", null, null);
        while (cursor.moveToNext()) {
            StringPais[pais] = cursor.getString(cursor.getColumnIndex("pais"));
            Log.i("PAIS", "" + StringPais[pais]);
            pais++;
        }

        for (int i = 0; i < pais; i++) {
            // conta quantas repetiçoes existem de cada um com base no array anterior NOTAS
            Cursor mCount = bancoDados.rawQuery("select count(*) from Colecao where pais='" + StringPais[i] + "' and tipo='Nota'", null);
            mCount.moveToFirst();
            count = mCount.getInt(0);
            StringPaisContNotas[i] = "" + count;
            mCount.close();
        }


        for (int i = 0; i < pais; i++) {
            // conta quantas repetiçoes existem de cada um com base no array anterior Moeda
            Cursor mCountMoeda = bancoDados.rawQuery("select count(*) from Colecao where pais='" + StringPais[i] + "' and tipo='Moeda'", null);
            mCountMoeda.moveToFirst();
            countMoeda = mCountMoeda.getInt(0);
            StringPaisContMoeda[i] = "" + countMoeda;
            mCountMoeda.close();
        }

        //trata as informações em uma linha
        for (int i = 0; i < pais; i++) {
            StringPaisFinal[i] = StringPais[i] + "-     Moedas=" + StringPaisContMoeda[i] + "    Notas=" + StringPaisContNotas[i];
        }
        Log.i("PAIS", "" + StringPaisFinal[3]);

        final List<String> list = new ArrayList<String>(Arrays.asList(StringPaisFinal));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (activity, android.R.layout.simple_list_item_1, list);
        ListView MostrarDados = (ListView) activity.findViewById(R.id.lvListagemPais);
        MostrarDados.setAdapter(arrayAdapter);

        TextView tvresumo = (TextView) activity.findViewById(R.id.tvResumao);
        tvresumo.setText("Total de paises " + contagem);
    }

    public void ResumoColecao(Activity activity) {
        Integer cont1 = 0, cont2 = 0, cont3 = 0, cont4 = 0;
        AbreBanco(activity);

        TextView tvNotaBrasil = (TextView) activity.findViewById(R.id.tvNotasNacional);
        TextView tvMoedaBrasil = (TextView) activity.findViewById(R.id.tvMoedaNacional);
        TextView tvMoedas = (TextView) activity.findViewById(R.id.tvMoedasTotal);
        TextView tvNotas = (TextView) activity.findViewById(R.id.tvNotasTotal);
        TextView tvColecao = (TextView) activity.findViewById(R.id.tvColecaoTotal);

        // verifica os criterios do filtro

        CRITERIO = "TIPO = 'Nota'";
        cursor = bancoDados.query(NOME_TABELA, null, CRITERIO, null, null, null, null);

        while (cursor.moveToNext()) {
            cont1++;
        }

        CRITERIO = "TIPO = 'Nota' and PAIS = 'Brasil'";
        cursor = bancoDados.query(NOME_TABELA, null, CRITERIO, null, null, null, null);

        while (cursor.moveToNext()) {
            cont2++;
        }

        CRITERIO = "TIPO = 'Moeda' and PAIS = 'Brasil'";
        cursor = bancoDados.query(NOME_TABELA, null, CRITERIO, null, null, null, null);

        while (cursor.moveToNext()) {
            cont3++;
        }

        CRITERIO = "TIPO = 'Moeda'";
        cursor = bancoDados.query(NOME_TABELA, null, CRITERIO, null, null, null, null);

        while (cursor.moveToNext()) {
            cont4++;
        }

        Date date = new Date();
        String stringDate = DateFormat.getDateTimeInstance().format(date);
        Resumo = Resumo + "Data do Backup: " + stringDate + "\n \n";
        Resumo = Resumo + "Notas Brasil = " + cont2 + "   \n";
        Integer notaEst = cont1 - cont2;
        Resumo = Resumo + "Notas Estrangeiras= " + notaEst + "   \n";
        Resumo = Resumo + "\n";
        Resumo = Resumo + "Moedas Brasil = " + cont3 + "   \n";
        Integer moedaEst = cont4 - cont3;
        Resumo = Resumo + "Moedas Estrangeiras = " + moedaEst + "   \n";
        Resumo = Resumo + "\n";
        Resumo = Resumo + "Notas Total = " + cont1 + "   \n";
        Resumo = Resumo + "Moedas Total = " + cont4 + "   \n";
        Integer total = cont1 + cont4;
        Resumo = Resumo + "Colecao Total = " + total + "   \n";
        FechaBanco();

        File arq1;
        FileOutputStream fos1;
        byte[] dados;
        try {
            arq1 = new File(Environment.getExternalStorageDirectory(), "/Download/resumoColecao.txt");
            fos1 = new FileOutputStream(arq1.toString());
            // transforma o texto digitado em array de bytes
            dados = Resumo.getBytes();
            // escreve os dados e fecha o arquivo
            fos1.write(dados);
            fos1.flush();
            fos1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}