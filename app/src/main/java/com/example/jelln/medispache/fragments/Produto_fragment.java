package com.example.jelln.medispache.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jelln.medispache.Adapter.ProdutosAdapter;
import com.example.jelln.medispache.MainActivity;
import com.example.jelln.medispache.R;
import com.example.jelln.medispache.control.Conexao;
import com.example.jelln.medispache.model.Produtos;
import com.example.jelln.medispache.view.Cadastrar_Produto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Produto_fragment extends Fragment {

    private RecyclerView recyclerView;
    private ProdutosAdapter produtosAdapter;
    private List<Produtos> mProdutos;
    ImageButton cadastro;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_produto_fragment, container, false);

        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cadastro = view.findViewById(R.id.cadastrar);
        mProdutos = new ArrayList<>();
        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Cadastrar_Produto.class);
                getActivity().startActivity(intent);

            }
        });
        readUsers();


        return view;
    }





    private void readUsers() {
        FirebaseAuth firebaseAuth = Conexao.getFirebaseAuth();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("ProdutosEmpresa").child(firebaseUser.getUid()).orderByChild("nome");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    mProdutos.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Produtos produtos = snapshot.getValue(Produtos.class);
                        assert produtos != null;
                        mProdutos.add(produtos);
                    }

                    produtosAdapter = new ProdutosAdapter(getContext(), mProdutos);
                    recyclerView.setAdapter(produtosAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
