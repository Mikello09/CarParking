package com.carpark.mls.carparking.PopUp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carpark.mls.carparking.AppConfig.Utils;
import com.carpark.mls.carparking.Interfaces.GuardarInterface;
import com.carpark.mls.carparking.Interfaces.MainInterface;
import com.carpark.mls.carparking.Interfaces.NavigationInterface;
import com.carpark.mls.carparking.R;
import com.google.android.gms.vision.text.Line;
import com.squareup.picasso.Picasso;

public class Dialog
{

    public static String[] colores = {"negro","azul","rojo","verde","amarillo","morado","marron","blanco","gris","naranja"};
    public static String colorSeleccionado = "";


    public static void dialogoTeclado(Context context, final String tipo, String numero){

        final GuardarInterface interfazNumero = (GuardarInterface) context;

        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.teclado_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//Para que se vean bien los bordes


        TextView eliminarIcono = (TextView)dialog.findViewById(R.id.elimar_texto);
        TextView aceptarIcono = (TextView)dialog.findViewById(R.id.aceptar_texto);
        TextView titulo = (TextView)dialog.findViewById(R.id.tituloTecladoTexto);

        LinearLayout uno = (LinearLayout)dialog.findViewById(R.id.uno);
        LinearLayout dos = (LinearLayout)dialog.findViewById(R.id.dos);
        LinearLayout tres = (LinearLayout)dialog.findViewById(R.id.tres);
        LinearLayout cuatro = (LinearLayout)dialog.findViewById(R.id.cuatro);
        LinearLayout cinco = (LinearLayout)dialog.findViewById(R.id.cinco);
        LinearLayout seis = (LinearLayout)dialog.findViewById(R.id.seis);
        LinearLayout siete = (LinearLayout)dialog.findViewById(R.id.siete);
        LinearLayout ocho = (LinearLayout)dialog.findViewById(R.id.ocho);
        LinearLayout nueve = (LinearLayout)dialog.findViewById(R.id.nueve);
        LinearLayout cero = (LinearLayout)dialog.findViewById(R.id.cero);
        LinearLayout eliminar = (LinearLayout)dialog.findViewById(R.id.eliminar_layout);
        LinearLayout aceptar = (LinearLayout)dialog.findViewById(R.id.aceptarLayout);
        final TextView texto = (TextView)dialog.findViewById(R.id.tecladoTexto);

        switch (tipo){
            case "piso":
                titulo.setText("Piso");
                break;
            case "plaza":
                titulo.setText("Plaza");
                break;
            default:
                break;
        }

        eliminarIcono.setTypeface(Utils.setFont(context,"fontawesome",true));
        aceptarIcono.setTypeface(Utils.setFont(context,"fontawesome",true));


        uno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.append("1");
            }
        });
        dos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.append("2");
            }
        });
        tres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.append("3");
            }
        });
        cuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.append("4");
            }
        });
        cinco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.append("5");
            }
        });
        seis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.append("6");
            }
        });
        siete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.append("7");
            }
        });
        ocho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.append("8");
            }
        });
        nueve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.append("9");
            }
        });
        cero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.append("0");
            }
        });

        texto.setText(numero);

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (texto.getText().toString() != "") {
                        texto.setText(texto.getText().toString().substring(0, texto.getText().toString().length() - 1));
                    } else {
                        dialog.dismiss();
                    }
                }catch (Exception e){
                    dialog.dismiss();
                }
            }
        });
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!texto.getText().toString().equals("") || texto.getText().toString() != null) {
                        interfazNumero.seleccionarNumero(tipo, Integer.parseInt(texto.getText().toString()));
                    }else{
                        interfazNumero.seleccionarNumero(tipo, -1992);
                    }
                    dialog.dismiss();
                }catch (Exception e){
                    interfazNumero.seleccionarNumero(tipo, -1992);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
    public static void dialogoColores(final Context context, String color){


        final GuardarInterface interfazColor = (GuardarInterface) context;

        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.colores_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//Para que se vean bien los bordes



        LinearLayout layoutNegro = (LinearLayout)dialog.findViewById(R.id.color_negro);
        LinearLayout layoutAzul = (LinearLayout)dialog.findViewById(R.id.color_azul);
        LinearLayout layoutRojo = (LinearLayout)dialog.findViewById(R.id.color_rojo);
        LinearLayout layoutVerde = (LinearLayout)dialog.findViewById(R.id.color_verde);
        LinearLayout layoutAmarillo = (LinearLayout)dialog.findViewById(R.id.color_amarillo);
        LinearLayout layoutMorado = (LinearLayout)dialog.findViewById(R.id.color_morado);
        LinearLayout layoutMarron = (LinearLayout)dialog.findViewById(R.id.color_marron);
        LinearLayout layoutBlanco = (LinearLayout)dialog.findViewById(R.id.color_blanco);
        LinearLayout layoutGris = (LinearLayout)dialog.findViewById(R.id.color_gris);
        LinearLayout layoutNaranja = (LinearLayout)dialog.findViewById(R.id.color_naranja);

        LinearLayout bordeNegro = (LinearLayout)dialog.findViewById(R.id.negro_borde);
        LinearLayout bordeAzul = (LinearLayout)dialog.findViewById(R.id.azul_borde);
        LinearLayout bordeRojo = (LinearLayout)dialog.findViewById(R.id.rojo_borde);
        LinearLayout bordeVerde = (LinearLayout)dialog.findViewById(R.id.verde_borde);
        LinearLayout bordeAmarillo = (LinearLayout)dialog.findViewById(R.id.amarillo_borde);
        LinearLayout bordeMorado = (LinearLayout)dialog.findViewById(R.id.morado_borde);
        LinearLayout bordeMarron = (LinearLayout)dialog.findViewById(R.id.marron_borde);
        LinearLayout bordeBlanco = (LinearLayout)dialog.findViewById(R.id.blanco_borde);
        LinearLayout bordeGris = (LinearLayout)dialog.findViewById(R.id.gris_borde);
        LinearLayout bordeNaranja = (LinearLayout)dialog.findViewById(R.id.naranja_borde);

        TextView checkNegro = (TextView)dialog.findViewById(R.id.check_negro);
        TextView checkAzul = (TextView)dialog.findViewById(R.id.check_azul);
        TextView checkRojo = (TextView)dialog.findViewById(R.id.check_rojo);
        TextView checkVerde = (TextView)dialog.findViewById(R.id.check_verde);
        TextView checkAmarillo = (TextView)dialog.findViewById(R.id.check_amarillo);
        TextView checkMorado = (TextView)dialog.findViewById(R.id.check_morado);
        TextView checkMarron = (TextView)dialog.findViewById(R.id.check_marron);
        TextView checkBlanco = (TextView)dialog.findViewById(R.id.check_blanco);
        TextView checkGris = (TextView)dialog.findViewById(R.id.check_gris);
        TextView checkNaranja = (TextView)dialog.findViewById(R.id.check_naranja);

        final LinearLayout[] bordes = {bordeNegro,bordeAzul,bordeRojo,bordeVerde,bordeAmarillo,bordeMorado,bordeMarron,bordeBlanco,bordeGris,bordeNaranja};
        final TextView[] checks = {checkNegro,checkAzul,checkRojo,checkVerde,checkAmarillo,checkMorado,checkMarron,checkBlanco,checkGris,checkNaranja};

        checkNegro.setTypeface(Utils.setFont(context,"fontawesome",true));
        checkAzul.setTypeface(Utils.setFont(context,"fontawesome",true));
        checkRojo.setTypeface(Utils.setFont(context,"fontawesome",true));
        checkVerde.setTypeface(Utils.setFont(context,"fontawesome",true));
        checkAmarillo.setTypeface(Utils.setFont(context,"fontawesome",true));
        checkMorado.setTypeface(Utils.setFont(context,"fontawesome",true));
        checkMarron.setTypeface(Utils.setFont(context,"fontawesome",true));
        checkBlanco.setTypeface(Utils.setFont(context,"fontawesome",true));
        checkGris.setTypeface(Utils.setFont(context,"fontawesome",true));
        checkNaranja.setTypeface(Utils.setFont(context, "fontawesome", true));

        colorSeleccionado = seleccionar(context,bordes,checks,color);

        layoutNegro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSeleccionado = seleccionar(context,bordes,checks,"negro");
                interfazColor.seleccionarColor(colorSeleccionado);
                cerrarDialogoColores(dialog);
            }
        });
        layoutAzul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSeleccionado = seleccionar(context,bordes,checks,"azul");
                interfazColor.seleccionarColor(colorSeleccionado);
                cerrarDialogoColores(dialog);
            }
        });
        layoutRojo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSeleccionado = seleccionar(context,bordes,checks,"rojo");
                interfazColor.seleccionarColor(colorSeleccionado);
                cerrarDialogoColores(dialog);
            }
        });
        layoutVerde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSeleccionado = seleccionar(context,bordes,checks,"verde");
                interfazColor.seleccionarColor(colorSeleccionado);
                cerrarDialogoColores(dialog);
            }
        });
        layoutAmarillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSeleccionado = seleccionar(context,bordes,checks,"amarillo");
                interfazColor.seleccionarColor(colorSeleccionado);
                cerrarDialogoColores(dialog);
            }
        });
        layoutMorado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSeleccionado = seleccionar(context,bordes,checks,"morado");
                interfazColor.seleccionarColor(colorSeleccionado);
                cerrarDialogoColores(dialog);
            }
        });
        layoutMarron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSeleccionado = seleccionar(context,bordes,checks,"marron");
                interfazColor.seleccionarColor(colorSeleccionado);
                cerrarDialogoColores(dialog);
            }
        });
        layoutBlanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSeleccionado = seleccionar(context,bordes,checks,"blanco");
                interfazColor.seleccionarColor(colorSeleccionado);
                cerrarDialogoColores(dialog);
            }
        });
        layoutGris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSeleccionado = seleccionar(context,bordes,checks,"gris");
                interfazColor.seleccionarColor(colorSeleccionado);
                cerrarDialogoColores(dialog);
            }
        });
        layoutNaranja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSeleccionado = seleccionar(context,bordes,checks,"naranja");
                interfazColor.seleccionarColor(colorSeleccionado);
                cerrarDialogoColores(dialog);
            }
        });

        dialog.show();

    }
    public static void dialogoFoto(final Context context, Uri foto, boolean cambiar){



        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.foto_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//Para que se vean bien los bordes

        ImageView fotoImage = (ImageView)dialog.findViewById(R.id.fotoDialog);
        TextView cambiarFotoLayout = (TextView) dialog.findViewById(R.id.cambiarFotoTexto);
        LinearLayout cerrarImagenLayout = (LinearLayout)dialog.findViewById(R.id.cerrarImagenLayout);
        TextView cerrarImagenIcono = (TextView)dialog.findViewById(R.id.cerrarImagenIcono);

        cerrarImagenIcono.setTypeface(Utils.setFont(context,"fontawesome",true));

        Picasso.with(context).load(foto).resize(fotoImage.getWidth(),250).rotate(90).into(fotoImage);

        cerrarImagenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if(cambiar){
            cambiarFotoLayout.setVisibility(View.VISIBLE);
            cambiarFotoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    GuardarInterface interfazFoto = (GuardarInterface) context;
                    interfazFoto.cambiarFoto();
                    dialog.dismiss();
                }
            });
        }else{
            cambiarFotoLayout.setVisibility(View.GONE);
        }


        dialog.show();

    }
    public static void esperaDialog(Context context){

        final GuardarInterface interfazEspera = (GuardarInterface) context;

        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.espera_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//Para que se vean bien los bordes

        ProgressBar esperaProgress = (ProgressBar)dialog.findViewById(R.id.guardarProgreso);

        esperaProgress.getIndeterminateDrawable().setColorFilter(context.getColor(R.color.blanco), android.graphics.PorterDuff.Mode.MULTIPLY);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                interfazEspera.guardando();
                dialog.dismiss();
            }
        }, 3000);

        dialog.show();

    }

    public static String seleccionar(Context context, LinearLayout[] bordes, TextView[] checks, String color){

        for(int i=0;i<colores.length;i++){
            if(!colores[i].equals(color)){
                bordes[i].setBackground(null);
                checks[i].setVisibility(View.GONE);
            }else{
                bordes[i].setBackground(context.getResources().getDrawable(R.drawable.borde_circulo));
                checks[i].setVisibility(View.VISIBLE);
            }
        }
        return color;
    }
    public static void cerrarDialogoColores(final android.app.Dialog dialogo){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogo.dismiss();
            }
        }, 500);
    }
    public static void dialogoMasDetalles(final Context context, String texto){

        final GuardarInterface interfazDetalles = (GuardarInterface) context;

        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.mas_detalles_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//Para que se vean bien los bordes


        final EditText detalles = (EditText) dialog.findViewById(R.id.detallesTexto);
        TextView acpetar = (TextView)dialog.findViewById(R.id.aceptar_detalles);
        TextView borrar = (TextView)dialog.findViewById(R.id.borrar_detalles);


        detalles.setText(texto);
        detalles.setSelection(detalles.getText().length());

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfazDetalles.ingresarMasDetalles(context.getResources().getString(R.string.MasDetalles));
                dialog.dismiss();
            }
        });

        acpetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!detalles.getText().toString().equals("") && detalles != null) {
                    interfazDetalles.ingresarMasDetalles(detalles.getText().toString());
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void dialogoBase(final Context context, String tipo, final Boolean guardar, final String mensajeDesplegar){

        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.base_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//Para que se vean bien los bordes

        LinearLayout cerrar = (LinearLayout)dialog.findViewById(R.id.cerrarDialogoLayout);
        TextView cerrarIcono = (TextView)dialog.findViewById(R.id.cerrarDialogo);
        TextView titulo = (TextView)dialog.findViewById(R.id.tituloDialogo);
        TextView subtitulo = (TextView)dialog.findViewById(R.id.subtituloDialogo);
        TextView mensaje = (TextView)dialog.findViewById(R.id.mensajeDialogo);
        TextView siBotonTexto = (TextView)dialog.findViewById(R.id.okDialogoTexto);
        LinearLayout siBoton = (LinearLayout)dialog.findViewById(R.id.okDialogo);
        TextView noBotonTexo = (TextView)dialog.findViewById(R.id.cancelarDialogoTexto);
        LinearLayout noBoton = (LinearLayout)dialog.findViewById(R.id.cancelarDialogo);
        ImageView imageFondo = (ImageView)dialog.findViewById(R.id.imageDialogo);

        cerrarIcono.setTypeface(Utils.setFont(context,"fontawesome",true));

        switch (tipo){
            case "detalles":

                titulo.setText(R.string.detallesTitulo);
                subtitulo.setText(R.string.detallesSubtitulo);
                mensaje.setText(mensajeDesplegar);
                siBotonTexto.setText(R.string.ok);
                imageFondo.setImageDrawable(context.getDrawable(R.drawable.details_icon_opaque));
                siBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                break;
            case "encontrado":

                titulo.setText(R.string.encontradoTitulo);
                subtitulo.setText(R.string.encontradoSubtitulo);
                mensaje.setText(R.string.encontradoTexto);
                siBotonTexto.setText(R.string.si);
                noBotonTexo.setText(R.string.todaviaNo);
                imageFondo.setImageDrawable(context.getDrawable(R.drawable.chek_icon_opaque));
                siBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(guardar){
                            MainInterface mainInterface = (MainInterface) context;
                            mainInterface.encontrado(true, dialog);
                        }else{
                            NavigationInterface navigationInterface = (NavigationInterface) context;
                            navigationInterface.encontrado(true, dialog);
                        }
                    }
                });
                noBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(guardar){
                            MainInterface mainInterface = (MainInterface) context;
                            mainInterface.encontrado(false, dialog);
                        }else{
                            NavigationInterface navigationInterface = (NavigationInterface) context;
                            navigationInterface.encontrado(false, dialog);
                        }
                    }
                });
                cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(guardar){
                            MainInterface mainInterface = (MainInterface) context;
                            mainInterface.encontrado(false, dialog);
                        }else{
                            NavigationInterface navigationInterface = (NavigationInterface) context;
                            navigationInterface.encontrado(false, dialog);
                        }
                    }
                });

                break;
            case "gpsMain":
                final MainInterface interfazGpsMain = (MainInterface) context;
                titulo.setText(R.string.alertaMessage);
                subtitulo.setText(R.string.subtituloGps);
                mensaje.setText(R.string.gpsMessage);
                siBotonTexto.setText(R.string.ok);
                noBotonTexo.setText(R.string.ignorar);
                imageFondo.setImageDrawable(context.getDrawable(R.drawable.location_icon_opaque));
                siBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interfazGpsMain.activarGPS();
                        dialog.dismiss();
                    }
                });
                noBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interfazGpsMain.cancelarGPS();
                        dialog.dismiss();
                    }
                });
                cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interfazGpsMain.cancelarGPS();
                        dialog.dismiss();
                    }
                });
                break;
            case "gpsGuardar":
                final GuardarInterface interfazGpsGuardar = (GuardarInterface) context;
                titulo.setText(R.string.alertaMessage);
                subtitulo.setText(R.string.subtituloGps);
                mensaje.setText(R.string.gpsMessage);
                siBotonTexto.setText(R.string.ok);
                imageFondo.setImageDrawable(context.getDrawable(R.drawable.location_icon_opaque));
                siBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interfazGpsGuardar.esconderMapa();
                        dialog.dismiss();
                    }
                });
                cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interfazGpsGuardar.esconderMapa();
                        dialog.dismiss();
                    }
                });
                break;
            case "internet":
                final MainInterface interfazInternet = (MainInterface) context;
                titulo.setText(R.string.alertaMessage);
                subtitulo.setText(R.string.subitituloInternet);
                mensaje.setText(R.string.internetMessage);
                siBotonTexto.setText(R.string.ok);
                noBotonTexo.setText(R.string.ignorar);
                imageFondo.setImageDrawable(context.getDrawable(R.drawable.wifi_icon_opaque));
                siBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interfazInternet.activarInternet();
                        dialog.dismiss();
                    }
                });
                noBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interfazInternet.activarInternet();
                        dialog.dismiss();
                    }
                });
                break;
            case "eliminarCoche":
                final MainInterface interfazEliminar = (MainInterface) context;
                titulo.setText(R.string.alertaMessage);
                subtitulo.setText(R.string.subtituloEliminar);
                if(guardar){
                    mensaje.setText(R.string.eliminarMensajeGuardar);
                }else{
                    mensaje.setText(R.string.eliminarMensajeGuardar);
                }
                siBotonTexto.setText(R.string.si);
                noBotonTexo.setText(R.string.no);
                imageFondo.setImageDrawable(context.getDrawable(R.drawable.delete_icon_opaque));
                siBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interfazEliminar.eliminarAparcamiento(true, dialog,guardar);
                    }
                });
                noBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interfazEliminar.eliminarAparcamiento(false, dialog,guardar);
                    }
                });
                cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interfazEliminar.eliminarAparcamiento(false, dialog,guardar);
                    }
                });
                break;
        }
        dialog.show();

    }
}
