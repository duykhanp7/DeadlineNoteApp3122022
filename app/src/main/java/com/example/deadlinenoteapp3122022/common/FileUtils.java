package com.example.deadlinenoteapp3122022.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

public class FileUtils {


    public static File from(Context context, Uri uri) {

        File var3;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            String fileName = getFileName(context, uri);
            List<String> splitName = splitFileName(fileName);
            File var10000 = File.createTempFile(splitName.get(0).length() >= 3 ? splitName.get(0) : "aaa", splitName.get(1));
            Intrinsics.checkNotNullExpressionValue(var10000, "File.createTempFile(\n   …plitName[1]\n            )");
            File tempFile = var10000;
            tempFile = rename(tempFile, fileName);
            tempFile.deleteOnExit();
            FileOutputStream out = (FileOutputStream)null;

            try {
                out = new FileOutputStream(tempFile);
            } catch (FileNotFoundException var9) {
                var9.printStackTrace();
            }

            if (inputStream != null) {
                if (out != null) {
                    copy(inputStream, (OutputStream)out);
                }

                inputStream.close();
            }

            if (out != null) {
                out.close();
            }

            var3 = tempFile;
        } catch (Exception var10) {
            var10.printStackTrace();
            var3 = null;
        }

        return var3;
    }

    private static final long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0L;
        boolean n = false;
        byte[] buffer = new byte[8192];

        while(true) {
            int var7 = input.read(buffer);
            byte var10 = -1;
            boolean var9 = false;
            Unit var11 = Unit.INSTANCE;
            if (var10 == var7) {
                return count;
            }

            output.write(buffer, 0, var7);
            count += (long)var7;
        }
    }

    public static File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (Intrinsics.areEqual(newFile, file) ^ true) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old " + newName + " file");
            }

            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to " + newName);
            }
        }

        return newFile;
    }

    public static List<String> splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }
        List<String> list = new ArrayList<>();
        list.add(name);
        list.add(extension);
        return list;
    }


    @SuppressLint("Range")
    private static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                cursor.moveToFirst();
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            Integer cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static File getFile(Context context, String fileName, String folderName) {
        File folder = new File(context.getExternalFilesDir(null).getAbsolutePath(), folderName);
        folder.mkdir();
        return new File(folder, fileName);
    }

    public static void copyFile(File src, File des) {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(des)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
