package com.eavc.examen.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eavc.examen.R;
import com.eavc.examen.Vista1Activity;
import com.eavc.examen.model.Employees;

import java.util.ArrayList;
import java.util.List;


public class EmpleadosAdapterRecyclerView extends RecyclerView.Adapter<EmpleadosAdapterRecyclerView.EmpleadosViewHolder>
{
    private List<Employees> Empleados;
    private int resource;
    private Vista1Activity vista1Activity;

    public EmpleadosAdapterRecyclerView(List<Employees> Empleados, int resource, Vista1Activity vista1_Activity)
    {
        this.Empleados = Empleados;
        this.resource = resource;
        this.vista1Activity = vista1_Activity;
    }

    @NonNull
    @Override
    public EmpleadosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource,viewGroup,false);

        return new EmpleadosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadosViewHolder pedidosViewHolder, int i) {
        final Employees empleado = Empleados.get(i);
        final int indice =i;

        pedidosViewHolder.tv_id.setText("ID: "+empleado.id);
        pedidosViewHolder.tv_nombre.setText("NOMBRE: "+empleado.name);
        pedidosViewHolder.tv_correo.setText("CORREO: "+empleado.mail);

        pedidosViewHolder.linearEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vista1Activity.seleccionar(indice);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Empleados.size();
    }

    public class EmpleadosViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_id;
        private TextView  tv_nombre;
        private TextView  tv_correo;
        private LinearLayout linearEmpleado;


        public EmpleadosViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_id = itemView.findViewById(R.id.tv_id);
            tv_nombre= itemView.findViewById(R.id.tv_nombre);
            tv_correo= itemView.findViewById(R.id.tv_correo);
            linearEmpleado = itemView.findViewById(R.id.linearEmpleado);

        }
    }

}
