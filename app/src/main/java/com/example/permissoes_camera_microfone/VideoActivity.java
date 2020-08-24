package com.example.permissoes_camera_microfone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class VideoActivity extends AppCompatActivity {
    VideoView vdvVideo;
    String caminhoCompleto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        vdvVideo = findViewById(R.id.vdvVideo);
    }

    public void chamarVideo(View v){
        //Cria uma intent vinculada à camera para captura de imagem
        Intent iVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        iVideo.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        File arqTemp = null;

        try {
            //cria um nome de arquivo com caminho completo e um arquivo vazio com o mesmo nome
            //dos quais é gerada uma URI passada para o provider. Isso faz com que quando a
            //câmera gravar o vídeo e devolver, o mesmo seja automaticamente salvo pelo provider
            //no arquivo que criamos
            caminhoCompleto = MainActivity.getNomeArquivo("VID", "mp4");
            arqTemp = new File(caminhoCompleto);
            Uri uriArquivo = FileProvider.getUriForFile(this,
                    this.getApplicationContext().getPackageName() + ".provider", arqTemp);
            iVideo.putExtra(MediaStore.EXTRA_OUTPUT, uriArquivo);
            //Abre a Intent de modo a poder verificar quando a mesma foi fechada
            startActivityForResult(iVideo, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //quanto a intent for fechada (a câmera vinculada à ela no caso) ele trata a foto e mostra
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK){
            try{
                //simplesmente pegamos a URI do vídeo que veio por parâmetro e mandamos
                //carregar e executar no VideoView
                Uri uri = Uri.parse(caminhoCompleto);
                vdvVideo.setVideoURI(uri);
                vdvVideo.start();
            } catch (Exception e) {

            }
        } else {

        }

    }

}