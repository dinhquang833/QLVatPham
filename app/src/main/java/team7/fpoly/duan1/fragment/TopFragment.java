package team7.fpoly.duan1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import team7.fpoly.duan1.R;
import team7.fpoly.duan1.adapter.TopAdapter;

public class TopFragment extends Fragment {

    Button btnSoPhieu,btnVon,btnLai;
    TopAdapter adapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        btnSoPhieu = view.findViewById(R.id.btnSoPhieu);
        btnVon = view.findViewById(R.id.btnVon);
        btnLai = view.findViewById(R.id.btnLai);
        recyclerView = view.findViewById(R.id.recyclerTop);

        adapter = new TopAdapter(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        btnSoPhieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.changeDataSet("SoPhieu");
            }
        });

        btnVon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.changeDataSet("Von");
            }
        });

        btnLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.changeDataSet("Lai");
            }
        });

        return view;
    }
}