package com.keyou.keyouplayer.tool;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Filestool {

    public void write(Context context,String s) throws IOException {
        FileOutputStream fos = context.openFileOutput("233.xml",Context.MODE_PRIVATE);
        OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
        osw.write(s);
        osw.flush();
        fos.flush();
        osw.close();
        fos.close();
    }

}
