package client;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.xml.namespace.QName;


class App {
    public static void main(String[] args){
        try {
            /* getAll */
            // String date = "2022/06/04";
            // System.out.println(getAll(date));
            
            /* get */
            // BufferedImage image = base64Decoder(get("quiz3"));
            // File outputfile = new File("image.jpg");
            // ImageIO.write(image, "jpg", outputfile);
            
            /* Set */
            // String uid = "quiz2";            //Quiz name
            // String startAt = "2022/06/04";
            // Integer time = 90;
            // BufferedImage image = ImageIO.read(new File("image.jpg"));
            // set(uid,startAt,time,image);
            
            /* submit */
            // String uid = "Peter";               //Student name
            // String name = "quiz3";              //Quiz name
            // BufferedImage image = ImageIO.read(new File("image.jpg"));
            // submit(uid,name,image);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void set(String name,String startAt,Integer time,BufferedImage image){
        try{
            String encode = base64Encoder(image);
       
            URL dummyUrl = new URL("http://203.121.239.106:8080/quiz/set");

            HashMap<String,String> data = new HashMap<String,String>();
            data.put("name", name);
            data.put("startAt", startAt);
            data.put("time", time.toString());
            data.put("data", encode);
            String dummyData = JSONObject.toJSONString(data);

            // System.out.println(dummyData);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) dummyUrl.openConnection();

            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setDoOutput(true);

            httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            httpUrlConnection.setRequestProperty("charset", "utf-8");
            httpUrlConnection.setRequestProperty("Content-Length", Integer.toString(dummyData.length()));

            DataOutputStream dataOutputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
            dataOutputStream.writeBytes(dummyData);

            InputStream inputStream = httpUrlConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String stringLine;
            while ((stringLine = bufferedReader.readLine()) != null) {
                System.out.println(stringLine);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAll(String date){

        try{
            StringBuilder result = new StringBuilder();
            URL url = new URL("http://203.121.239.106:8080/quiz/getAll?date=" + date);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
            return result.toString();
        }catch(Exception e){}
        return null;
    }
    
    public static String get(String name){
        try{
            StringBuilder result = new StringBuilder();
            URL url = new URL("http://203.121.239.106:8080/quiz/get?name=" + name);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
            JSONParser parser = new JSONParser();
            HashMap<String,String> obj = (JSONObject)parser.parse(result.toString());
            return obj.get("data");

        }catch(Exception e){}
        return null;
    }
    
    public static void submit(String uid,String name,BufferedImage image){
        try {
    
            // DisplayImage(image);
            String encode = base64Encoder(image);
       
            URL dummyUrl = new URL("http://203.121.239.106:8080/quiz/finish");
            // URL dummyUrl = new URL("http://localhost:8080/quiz/finish");

            HashMap<String,String> data = new HashMap<String,String>();
            data.put("uid", uid);
            data.put("name", name);
            data.put("data", encode);
            String dummyData = JSONObject.toJSONString(data);

            // System.out.println(dummyData);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) dummyUrl.openConnection();

            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setDoOutput(true);

            httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            httpUrlConnection.setRequestProperty("charset", "utf-8");
            httpUrlConnection.setRequestProperty("Content-Length", Integer.toString(dummyData.length()));

            DataOutputStream dataOutputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
            dataOutputStream.writeBytes(dummyData);

            InputStream inputStream = httpUrlConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String stringLine;
            while ((stringLine = bufferedReader.readLine()) != null) {
                System.out.println(stringLine);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static public BufferedImage base64Decoder(String base64){
        try{
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] imageBytes = decoder.decode(base64);
            return ImageIO.read(new ByteArrayInputStream(imageBytes));
        }
        catch(Exception e){
            return null;
        }
    }

    static public String base64Encoder(BufferedImage bufferedImage){
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", bos);
            byte[] imageBytes = bos.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(imageBytes);
        }catch(Exception e){
            return null;
        }
    }
    
    static public void displayImage(BufferedImage img)
    {
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame();
        frame.setSize(img.getWidth(),img.getHeight());
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
