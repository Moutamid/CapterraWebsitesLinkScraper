package com.example.andy.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Context context = MainActivity.this;
    private ProgressDialog progressDialog;
    TextView textView;

    ArrayList<String> profileUrlsList = new ArrayList<String>();
    ArrayList<String> websiteUrlsList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.outputTextview);


        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadTask task = new DownloadTask();
                task.execute("https://www.capterra.com/p/170703/Sightcall/");
            }
        });

        readHtmlFile();

    }

    private void readHtmlFile() {
        File fileEvents = new File(getFilePathString() + "/html.txt");

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
                Log.d(TAG, "readFile123: " + line);
            }

//            textView.setText(text.toString());

            br.close();

            Pattern p = Pattern.compile("<a class=\"nb-button nb-button-standard nb-button-primary nb-text-md nb-px-xl nb-leading-lg nb-whitespace-no-wrap\" href=\"(.*?)\"");
            Matcher m = p.matcher(text.toString());

            String finalResult = "";

            while (m.find()) {
//                profileUrlsList.add(m.group(1));
                profileUrlsList.add("https://www.capterra.com" + m.group(1));
//                finalResult = finalResult + m.group(1) + "\n";
            }

            HashSet<String> hashSet = new HashSet<String>();
            hashSet.addAll(profileUrlsList);
            profileUrlsList.clear();
            profileUrlsList.addAll(hashSet);

            for (String url : profileUrlsList) {
                finalResult = finalResult + url + "\n";
            }

            textView.setText(finalResult);
            saveSampleTextFile();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, profileUrlsList.size() + "", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (final IOException e) {
            e.printStackTrace();
            Log.d(TAG, "readFile: " + e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
//        String result = text.toString();
//        return result;
    }

    private void saveTextFile() {

        File file = new File(getFilePathString());
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            File gpxfile = new File(file, "output.txt");
            FileWriter writer = new FileWriter(gpxfile);

            String data = "";

            for (String url_str : websiteUrlsList) {

                writer.append(url_str + "\n");

                data = data + url_str + "\n";
            }

            writer.flush();
            writer.close();
            textView.setText(data);

            Toast.makeText(MainActivity.this, "Saved your text: " + websiteUrlsList.size(), Toast.LENGTH_LONG).show();
        } catch (final Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onClick: " + e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void saveSampleTextFile() {

        File file = new File(getFilePathString());
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            File gpxfile = new File(file, "sample.txt");
            FileWriter writer = new FileWriter(gpxfile);

            String data = "";

            for (String url_str : profileUrlsList) {

                writer.append(url_str + "\n");

                data = data + url_str + "\n";
            }

            writer.flush();
            writer.close();
            textView.setText(data);
//            Toast.makeText(MainActivity.this, "Saved your text", Toast.LENGTH_LONG).show();
        } catch (final Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onClick: " + e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void readFile1() {
        File fileEvents = new File(getFilePathString() + "/sample.txt");

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
                profileUrlsList.add(line);
                Log.d(TAG, "readFile123: " + line);
            }

            textView.setText(text.toString());

            br.close();
        } catch (final IOException e) {
            e.printStackTrace();
            Log.d(TAG, "readFile: " + e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
//        String result = text.toString();
//        return result;
    }

    private String getFilePathString() {
//        String path_save_aud = "";
        String path_save_vid = "";

        if (Build.VERSION.SDK_INT >= 30) {//Build.VERSION_CODES.R
//             Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//            + File.separator
//            path_save_aud =
//                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                            + File.separator +
//                            getResources().getString(R.string.app_name) +
//                            File.separator + "audio";
            path_save_vid =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                            File.separator +
                            getResources().getString(R.string.app_name) +
                            File.separator + "Text";

        } else {
//            path_save_aud =
//                    Environment.getExternalStorageDirectory().getAbsolutePath() +
//                            File.separator +
//                            getResources().getString(R.string.app_name) +
//                            File.separator + "audio";
            path_save_vid =
                    Environment.getExternalStorageDirectory().getAbsolutePath() +
                            File.separator +
                            getResources().getString(R.string.app_name) +
                            File.separator + "Text";

        }


        return path_save_vid;
//        final File newFile2 = new File(path_save_aud);
//        newFile2.mkdir();
//        newFile2.mkdirs();
//
//        final File newFile4 = new File(path_save_vid);
//        newFile4.mkdir();
//        newFile4.mkdirs();

    }

//    private String readFile() {
//        File fileEvents = new File(getFilePathString() + "/sample.txt");
//
//        StringBuilder text = new StringBuilder();
//
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                text.append(line);
//                text.append('\n');
//                Log.d(TAG, "readFile123: " + line);
//            }
//            br.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d(TAG, "readFile: " + e.getMessage());
//        }
//        String result = text.toString();
//        return result;
//    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: ");

//            profileUrlsList.add("https://www.capterra.com/p/170703/Sightcall/");
//            profileUrlsList.add("https://www.capterra.com/p/180838/SimpleACD/");
//            profileUrlsList.add("https://www.capterra.com/p/174615/SIMPSY-Voice/");

            progressDialog.show();

//            readFile1();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: ");
//            String[] splitResult = s.split("<div class=\"listedArticles\">");

//            String data = "";
//
//            for (String url_str : websiteUrlsList) {
//
//                data = data + url_str + "\n";
//
//            }
///
//            textView.setText(data);
//            textView.setText(nameStr + "\n" + urlStr);

            saveTextFile();

            progressDialog.dismiss();

        }

        @Override
        protected String doInBackground(String... urls) {
            Log.d(TAG, "doInBackground: ");
//            String result = "";
//            URL url;
//            HttpURLConnection urlConnection = null;
//
//            try {
//
//                url = new URL(urls[0]);
//
//                urlConnection = (HttpURLConnection) url.openConnection();
//
//                InputStream in = urlConnection.getInputStream();
//
//                InputStreamReader reader = new InputStreamReader(in);
//
//                int data = reader.read();
//                Log.d(TAG, "doInBackground: data: "+ data);
//                while (data != -1) {
//                    char current = (char) data;
//                    Log.d(TAG, "doInBackground: current char: "+current);
//                    result += current;
//                    data = reader.read();
//                }
//
//                return result;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "null";
//            }

            String totalSize = String.valueOf(profileUrlsList.size());

            for (String profileUrl : profileUrlsList) {

                String htmlData = getHtmlString(profileUrl);

                String webUrl = getWebsiteUrl(htmlData);

                websiteUrlsList.add(webUrl);

                String position = String.valueOf(profileUrlsList.indexOf(profileUrl) + 1);

                updateProgressBar(position, totalSize);
            }

            return "null";

        }

        private void updateProgressBar(String position1, String totalSize1) {
            progressDialog.setMessage(position1 + " / " + totalSize1);
        }

        private String getHtmlString(String url) {
            URL google = null;
            try {
                google = new URL(url);
            } catch (final MalformedURLException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            String htmlData = stringBuffer.toString();

            return htmlData;
        }

        private String getWebsiteUrl(String data) {
            Pattern p = Pattern.compile("\"isHighestCpcCategory\":(.*?),\"yearFounded\":");
            Matcher m = p.matcher(data);
//            Matcher m = p.matcher(splitResult[0]);

            String str = "null";

            while (m.find()) {
                str = m.group(1);
            }

//            Pattern nameP = Pattern.compile("\"name\":\"(.*?)\"");
//            Matcher nameM = nameP.matcher(str);
//
//            String nameStr = "null";
//
//            while (nameM.find()) {
//                nameStr = nameM.group(1);
//            }
            Pattern urlP = Pattern.compile("\"url\":\"(.*?)\"");
            Matcher urlM = urlP.matcher(str);

            String urlStr = "null";

            while (urlM.find()) {
                urlStr = urlM.group(1);
            }
            return urlStr;
        }
    }

}
