package com.example.permissoes_camera_microfone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AudioActivity extends AppCompatActivity {
    String caminhoCompletoArquivo;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
    }


    //Método auxiliar que nos retorna um caminho utilizando a URI do arquivo de áudio que é
    //automaticamente salvo em algum lugar pelo Android
    private String getCaminhoPelaUri(Uri uri) {
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        return cursor.getString(index);
    }


    public void chamarMicrofone(View v){
        //Cria uma intent vinculada ao microfone para captura de áudio
        Intent iAudio = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        iAudio.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        try {
            //Abre a Intent de modo a poder verificar quando a mesma foi fechada e passa
            //o nome e caminho completo do arquivo. Diferente dos exemplos com a câmera
            //não podemos usar um provider que salva o arquivo ao final no caminho que
            //definimos. O salvamento de arquivo de áudio no android é automático e não
            //temos controle. Então ao final vamos copiar o arquivo criado automaticamente
            //e criar outro no caminho que desejamos
            caminhoCompletoArquivo = MainActivity.getNomeArquivo("AUD", "mp3");
            startActivityForResult(iAudio, 1);
        } catch (Exception ex) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Pegamos a URI do arquivo de áudio que foi automaticamente sakvo pelo Android
            //em algum lugar que desconhecemos (cada versão usa um diretório distinto)
            Uri uri = data.getData();
            try {
                //Transformamos a URI em um caminho
                String filePath = getCaminhoPelaUri(uri);
                //Criamos um InputStream com o arquivo original que o Android criou e um
                //OutputStream vazio com o caminho que nós criamos. Após, percorremos byte a
                //byte do arquivo original, copiando para o arquivo novo
                InputStream original = new FileInputStream(filePath);
                OutputStream copia = new FileOutputStream(caminhoCompletoArquivo);
                byte[] buf = new byte[1024];
                int len;
                while ((len = original.read(buf)) > 0) {
                    copia.write(buf, 0, len);
                }
                //Terminada a cópia, fechamos os dois arquivos e excluímos o arquivo original
                //para que não fique duplicado
                original.close();
                copia.close();
                getContentResolver().delete(uri, null, null);
                (new File(filePath)).delete();
                //Agora basta usar o caminho completo do arquivo de áudio novo (copiado) e
                //passar de parâmetro para um MediaPlayer para que possamos escutá-lo
                mp = new MediaPlayer();
                mp.setDataSource(caminhoCompletoArquivo);
                mp.prepare();
                mp.start();

            } catch (IOException e) {

            }

        }
    }


}