package com.example.andy.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Context context = MainActivity.this;
    private ProgressDialog progressDialog;
    TextView textView;
    private boolean breakk = false;
    ArrayList<String> profileUrlsList = new ArrayList<String>();
    ArrayList<String> websiteUrlsList = new ArrayList<String>();
    EditText loop_execute_count_editText;
    EditText delay_between_requests_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.outputTextview);

        loop_execute_count_editText = findViewById(R.id.loop_execute_count_editText);
        delay_between_requests_editText = findViewById(R.id.delay_between_requests_editText);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");

        findViewById(R.id.resumeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                websiteUrlsList.clear();
                profileUrlsList.clear();
                nullProfileUrlsList.clear();
                readHtmlFile();
                DownloadTask task = new DownloadTask();
                task.execute("https://www.capterra.com/p/170703/Sightcall/");
            }
        });

        findViewById(R.id.start1Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                websiteUrlsList.clear();
                profileUrlsList.clear();
                nullProfileUrlsList.clear();
                readHtmlFile();
                storeInteger(0);
                DownloadTask task = new DownloadTask();
                task.execute("https://www.capterra.com/p/170703/Sightcall/");
            }
        });

        findViewById(R.id.sampleBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeInteger(0);
                progressDialog.show();
                websiteUrlsList.clear();
                profileUrlsList.clear();
                nullProfileUrlsList.clear();
                readFile1();
            }
        });

        findViewById(R.id.browserBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BrowserActivity.class));
            }
        });

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

    private void saveTextFile(boolean FINAL_SAVING) {

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

            if (FINAL_SAVING) {
                textView.setText(data);
                Toast.makeText(MainActivity.this, "Saved your text: " + websiteUrlsList.size(), Toast.LENGTH_LONG).show();
            }

        } catch (final Exception e) {
            e.printStackTrace();
            Log.d(TAG, "FILE SAVING ERROR: " + e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "FILE SAVING ERROR", Toast.LENGTH_SHORT).show();
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
        profileUrlsList.clear();
        websiteUrlsList.clear();

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

            DownloadTask task = new DownloadTask();
            task.execute("https://www.capterra.com/p/170703/Sightcall/");

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
    ArrayList<String> nullProfileUrlsList = new ArrayList<>();

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: ");

//            profileUrlsList.add("https://www.capterra.com/p/170703/Sightcall/");
//            profileUrlsList.add("https://www.capterra.com/p/180838/SimpleACD/");
//            profileUrlsList.add("https://www.capterra.com/p/174615/SIMPSY-Voice/");

            if (!progressDialog.isShowing())
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

            saveTextFile(true);

            progressDialog.dismiss();

        }

//        int position;

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

            // THIS CODE DETERMINES THAT HOW MUCH TIME A LOOP
            // SHOULD RUN LIKE ONLY 5 TIMES
            int i = 0;
            String lll = loop_execute_count_editText.getText().toString();
            int loopCount;
            if (lll.isEmpty())
                loopCount = 0;
            else
                loopCount = Integer.parseInt(lll);

            // THIS CODE DETERMINES THAT WHAT SHOULD BE DELAY
            // BETWEEN EACH NETWORK REQUEST BECAUSE OF CAPPTERRA
            // RESTRICTIONS
            String ppp = delay_between_requests_editText.getText().toString();
            int delayInt;
            if (ppp.isEmpty())
                delayInt = 30000;
            else
                delayInt = Integer.parseInt(ppp);

            int storedInt = getStoredInteger();

            for (String profileUrl : profileUrlsList) {

                int position = profileUrlsList.indexOf(profileUrl) + 1;

                // THIS CODE IS USED TO RESUME THE LOOP WHERE IT WAS BEFORE CRASH
                if (storedInt != 0) {
                    if (position <= storedInt) {
                        continue;
                    }
                }

//                if (profileUrlsList.indexOf(profileUrl) + 1)

                String htmlData = getHtmlString(profileUrl);

                String webUrl = getWebsiteUrl(htmlData);

                if (webUrl.equals("null")) {
                    nullProfileUrlsList.add(profileUrl);
                } else {
                    websiteUrlsList.add(webUrl);
                    saveTextFile(false);
                }

                updateProgressBar(String.valueOf(position), totalSize);

                try {
                    Thread.sleep(delayInt);//60000
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (loopCount != 0) {
                    i++;
                    if (i == loopCount) {
                        break;
                    }
                }

                storeInteger(position);

//                if (breakk) {
//                    break;
//                }

            }

            if (nullProfileUrlsList.isEmpty()) {
                return "null";
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "NOW REQUESTING THE NULL LIST", Toast.LENGTH_LONG).show();
                }
            });

            String nullTotalSize = String.valueOf(nullProfileUrlsList.size());

            // SEPARATE LOOP FOR NULL PROFILES
            // MEANING WHOSE PROFILES GAVE AN ERROR
            // AND PRINTED NULL THEN THOSE ARE LOOPING AGAIN
            for (String nullProfileUrl : nullProfileUrlsList) {

                String htmlData = getHtmlString(nullProfileUrl);

                String webUrl = getWebsiteUrl(htmlData);

                if (webUrl.equals("null")) {
                    websiteUrlsList.add("--NULL: " + nullProfileUrl);
//                    nullProfileUrlsList.add(nullProfileUrl);
                } else
                    websiteUrlsList.add(webUrl);

                saveTextFile(false);

                int position = nullProfileUrlsList.indexOf(nullProfileUrl) + 1;

                updateProgressBar(String.valueOf(position), nullTotalSize + " (NULL LIST)");

                try {
                    Thread.sleep(delayInt);//60000
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                if (loopCount != 0) {
//                    i++;
//                    if (i == loopCount) {
//                        break;
//                    }
//                }

//                if (breakk) {
//                    break;
//                }

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

    boolean ggg = false;
//    int counter = 10;
//    boolean as = true;

    @Override
    public void onBackPressed() {
        if (ggg) {
            super.onBackPressed();
        } else {
//            if (as) {
//                Toast.makeText(MainActivity.this,
//                        "Press back " + counter + " times to stop!", Toast.LENGTH_SHORT).show();
//                as = false;
//            }
//            counter--;
//
//            if (counter == 0){
//                breakk = true;
//            }

        }
    }

    private static final String PACKAGE_NAME = "dev.moutamid.Capterra";

    private SharedPreferences sharedPreferences;

    public void storeInteger(int value) {
        sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("LOOP_VALUE", value).apply();
    }

    public int getStoredInteger() {
        sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("LOOP_VALUE", 0);
    }

}
