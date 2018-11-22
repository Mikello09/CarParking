package com.carpark.mls.carparking.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.carpark.mls.carparking.AppConfig.Parking;
import com.carpark.mls.carparking.AppConfig.Utils;
import com.carpark.mls.carparking.R;

import java.util.List;

public class ParkingListaAdapter extends RecyclerView.Adapter<ParkingListaAdapter.ViewHolder> {


    private Context context;
    private List<Parking> lista;
    private LayoutInflater mInflater;

    public ParkingListaAdapter(Context context, List<Parking> lista){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.parking_lista_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final int posicion = i;

        viewHolder.parkingIcono.setTypeface(Utils.setFont(context,"fontawesome",true));
        viewHolder.parkingTitulo.setText(lista.get(i).getTitulo());
        viewHolder.parkingDistancia.setText(lista.get(i).getDistancia());
        viewHolder.parkingVicinity.setText(lista.get(i).getVicinity());
        viewHolder.parkingstars.setRating((float)lista.get(i).getRating());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //TODO
            }
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView parkingIcono;
        private TextView parkingTitulo;
        private TextView parkingDistancia;
        private TextView parkingVicinity;
        private RatingBar parkingstars;

        public ViewHolder(View itemView){
            super(itemView);
            parkingIcono = itemView.findViewById(R.id.parkingIcono);
            parkingTitulo = itemView.findViewById(R.id.parkingTitulo);
            parkingDistancia = itemView.findViewById(R.id.parkingDistancia);
            parkingVicinity = itemView.findViewById(R.id.parkingVicinity);
            parkingstars = itemView.findViewById(R.id.parkingStars);

        }
    }
}
