package team7.fpoly.duan1.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import team7.fpoly.duan1.R;
import team7.fpoly.duan1.adapter.LoaiAdapter;
import team7.fpoly.duan1.dao.LoaiDAO;
import team7.fpoly.duan1.model.Loai;

public class LoaiFragment extends Fragment {

    Context context;
    LoaiDAO dao;

    RecyclerView recyclerView;
    LoaiAdapter adapter;

    FloatingActionButton fabAdd;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.dao = new LoaiDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loai, container, false);
        recyclerView = view.findViewById(R.id.recyclerLoai);
        fabAdd = view.findViewById(R.id.fabAdd);
        adapter = new LoaiAdapter(context);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogThem();
            }
        });

        return view;
    }

    private void showDialogThem(){
        Loai l = new Loai();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thêm Loại");
        View diaView = LayoutInflater.from(context).inflate(R.layout.dialog_loai,null);
        TextInputEditText edtTenLoai = diaView.findViewById(R.id.dia_edtTenLoai);

        builder.setView(diaView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean checkEmpty = edtTenLoai.getText().length() == 0;

                if (checkEmpty){
                    Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    l.setTenLoai(edtTenLoai.getText().toString().trim());
                    boolean check = dao.themLoai(l);
                    if (check){
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        adapter.refreshData();
                    } else {
                        Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        builder.setView(diaView);
    }
}