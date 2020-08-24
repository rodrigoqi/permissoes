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
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class FotoActivity extends AppCompatActivity {
    ImageView imgFoto;
    String caminhoCompleto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        imgFoto = findViewById(R.id.imgFoto);
    }

    public void chamarCamera(View v){
        //Cria uma intent vinculada à camera para captura de imagem
        Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        iCamera.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        File arqTemp = null;

        try {
            //cria um nome de arquivo com caminho completo e um arquivo vazio com o mesmo nome
            //dos quais é gerada uma URI passada para o provider. Isso faz com que quando a
            //câmera tirar a foto e devolver, a mesma seja automaticamente salva pelo provider
            //no arquivo que criamos
            caminhoCompleto = MainActivity.getNomeArquivo("IMG", "jpg");
            arqTemp = new File(caminhoCompleto);
            Uri uriArquivo = FileProvider.getUriForFile(this,
                    this.getApplicationContext().getPackageName() + ".provider", arqTemp);
            iCamera.putExtra(MediaStore.EXTRA_OUTPUT, uriArquivo);

            //Abre a Intent de modo a poder verificar quando a mesma foi fechada
            startActivityForResult(iCamera, 1);
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
                //aqui e criado um bitmap com o arquivo recem salvo
                BitmapFactory.Options options  = new BitmapFactory.Options();
                options.inSampleSize = 5;
                Bitmap bitmap = BitmapFactory.decodeFile(caminhoCompleto, options);

                //toda essa mão aqui é para testar a rotação da foto quando foi tirada
                int graus = 0;
                ExifInterface exif = null;
                exif = new ExifInterface(caminhoCompleto);
                if(exif!=null){
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
                    if(orientation != -1){
                        if(orientation == ExifInterface.ORIENTATION_ROTATE_90){
                            graus = 90;
                        }
                        if(orientation == ExifInterface.ORIENTATION_ROTATE_180){
                            graus = 180;
                        }
                        if(orientation == ExifInterface.ORIENTATION_ROTATE_270){
                            graus = 270;
                        }
                    }
                }

                //Sabendo a rotação da foto quando foi tirada criamos outro bitmap aplicando
                //uma rotação para que encaixe certo no ImageView
                Matrix matrix = new Matrix();
                matrix.postRotate(graus);
                Bitmap bitmapRotacionado = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                //aqui carregamos o bitmap já ajustado dentro nosso ImageView
                imgFoto.setImageBitmap(bitmapRotacionado);

            } catch (Exception e) {

            }
        } else {
            //Caso a camera fechada não seja a que chamamos ou o usuário não tenha tirado
            //a foto, carregamos uma foto padrão de imagem vazia
            imgFoto.setImageResource(R.drawable.noimage);
        }

    }

}