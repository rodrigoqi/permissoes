package com.example.permissoes_camera_microfone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verificarPermissoes();
    }

    public void janelaFoto(View v){
        Intent janela = new Intent(this, FotoActivity.class);
        startActivity(janela);
    }

    public void janelaVideo(View v){
        Intent janela = new Intent(this, VideoActivity.class);
        startActivity(janela);
    }

    public void janelaAudio(View v){
        Intent janela = new Intent(this, AudioActivity.class);
        startActivity(janela);
    }

    //Método auxiliar para gerar um nome de arquivo que contenha o caminho completo do
    //diretório que representa o armazenamento. Passamos por parâmtro u prefixo para o
    //arquivo junto com a extensão do mesmo e para garantir que nunca dois nomes sejam
    //iguais e utiliza no nome a data/hora
    public static String getNomeArquivo(String prefixo, String extensao) throws IOException{
        File diretorio = new File(Environment.getExternalStorageDirectory() +
                File.separator + "DCIM" + File.separator + "QIAPP");
        diretorio.mkdirs();
        String datahora = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String nomeArquivo = prefixo + "_" + datahora + "." + extensao;
        return diretorio + File.separator + nomeArquivo;
    }

    //Este método quando for chamado testa cada uma das permissões que precisamos e se alguma
    //não foi consedida e ele força que o Android pergunte ao usuário
    private void verificarPermissoes(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 1);
        }
    }

}