package com.example.jelln.medispache.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jelln.medispache.MainActivity;
import com.example.jelln.medispache.R;
import com.example.jelln.medispache.model.Produtos;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

public class Atualizar_Produto extends AppCompatActivity {

    ImageButton botao_cadastro;
    Intent intent;
    String nome, valor, quantidade, id;
    EditText valor_cadastro, quantidade_cadastro, nome_cadastro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar__produto);
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        nome = bundle.getString("nome");
        quantidade = bundle.getString("quantidade");
        valor = bundle.getString("valor");
        id = bundle.getString("id");
        inicializarcomponentes();
        eventobotao();
        nome_cadastro.setText(nome);
        valor_cadastro.setText(valor);
        quantidade_cadastro.setText(quantidade);



    }




    private void eventobotao() {
        botao_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nome_cadastro.getText().toString().trim();
                String valor = valor_cadastro.getText().toString().trim();

                String quantidade = quantidade_cadastro.getText().toString().trim();
                adicionarprodutos(nome, valor, quantidade, id);


            }
        });
    }

    private void adicionarprodutos(String nome, String valor, String quantidade, String id) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProdutosEmpresa").child(user.getUid()).child(id);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("nome", nome);
        hashMap.put("quantidade", quantidade);
        hashMap.put("valor", valor);
        reference.updateChildren(hashMap);
        Toast.makeText(getApplicationContext(), "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }


    private void inicializarcomponentes() {
        botao_cadastro = findViewById(R.id.botao_cadastro);
        valor_cadastro = findViewById(R.id.valor_cadastro);
        quantidade_cadastro = findViewById(R.id.quantidade_cadastro);
        nome_cadastro = findViewById(R.id.nome_cadastro);
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(quantidade_cadastro, smf);
        quantidade_cadastro.addTextChangedListener(maskTextWatcher);
        valor_cadastro.addTextChangedListener(new MascaraMonetaria(valor_cadastro));
        valor_cadastro.setText("0.00");

    }
    private class MascaraMonetaria implements TextWatcher{

        final EditText campo;

        public MascaraMonetaria(EditText campo) {
            super();
            this.campo = campo;
        }

        private boolean isUpdating = false;
        DecimalFormat nf = new DecimalFormat("#,##0.00");
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int after) {

            if (isUpdating) {
                isUpdating = false;
                return;
            }

            isUpdating = true;
            String str = s.toString();
            boolean hasMask = ((str.indexOf(".") > -1 || str.indexOf(",") > -1));
            // Verificamos se existe máscara
            if (hasMask) {
                str = str.replaceAll("[^\\d]", "");


            }

            try {

                str = nf.format(Double.parseDouble(str) / 100);
                campo.setText(str);
                campo.setSelection(campo.getText().length());
            } catch (NumberFormatException e) {
                s = "";
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
