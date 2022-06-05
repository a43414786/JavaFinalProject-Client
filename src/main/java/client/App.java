package client;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.simple.JSONArray;
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



class App {
    public static void main(String[] args){
        

        try {
            Client client = new Client();
            client.setName("Peter");
            /* getAll */
            // String a = client.getAll("2020/04/22");
            // System.out.println(a);
            /* Set */
            // client.set();
            /* get */
            // client.get("quiz3");
            // client.displayImage();
            /* getMyInfo */
            // client.getMyInfo("quiz3");
            // client.printMyInfo();
            /* getMyquiz */
            // client.getMyQuiz("quiz3", 1);
            // client.displayImage();
            /* submit */
            // BufferedImage image = ImageIO.read(new File("image.jpg"));
            // client.submit("quiz3", image);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}


class Client{

    private String name = null;
    private String base64 = null; 
    private BufferedImage bufferedImage = null;
    private JSONArray myinfo = null;
    
    public void setName(String name){
        this.name = name;
    }

    public BufferedImage getImage(){
        return bufferedImage;
    }
    
    public JSONArray getMyInfo(){
        return myinfo;
    }

    public void printMyInfo(){
        for(int i = 0 ; i < myinfo.size() ; i++){
            System.out.printf("%d : %s\n",i,(String)myinfo.get(i));
        }
    }

    public void set(String startAt,Integer time,BufferedImage image){
        try{
            bufferedImage = image;

            base64Encoder();
       
            URL dummyUrl = new URL("http://203.121.239.106:8080/quiz/set");

            HashMap<String,String> data = new HashMap<String,String>();
            data.put("name", name);
            data.put("startAt", startAt);
            data.put("time", time.toString());
            data.put("data", base64);
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

    public String getAll(String date){

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
    
    public void get(String uid){
        try{
            StringBuilder result = new StringBuilder();
            URL url = new URL("http://203.121.239.106:8080/quiz/get?name=" + uid);
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
            base64 = obj.get("data");
            base64Decoder();

        }catch(Exception e){}
        
    }
    
    public void getMyInfo(String uid){
        try{
            StringBuilder result = new StringBuilder();
            URL url = new URL("http://203.121.239.106:8080/quiz/getMyinfo?name=" + name + "&uid=" + uid);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
            JSONParser parser = new JSONParser();
            myinfo = (JSONArray)parser.parse(result.toString());
        }catch(Exception e){}
    }
    
    public String getMyQuiz(String uid,int timeidx){
        try{
            StringBuilder result = new StringBuilder();
            // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
            // time = formatter.format(formatter.parse(time));
            // System.out.println(time);
            URL url = new URL("http://203.121.239.106:8080/quiz/getMyQuiz?name=" + name + "&uid=" + uid + "&time=" + (String)myinfo.get(timeidx));
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
            base64 = obj.get("data");
            base64Decoder();
        }catch(Exception e){}
        return null;
    }
    
    public void submit(String uid,BufferedImage image){
        try {
            bufferedImage = image;
            // DisplayImage(image);
            base64Encoder();
       
            URL dummyUrl = new URL("http://203.121.239.106:8080/quiz/finish");
            // URL dummyUrl = new URL("http://localhost:8080/quiz/finish");

            HashMap<String,String> data = new HashMap<String,String>();
            data.put("uid", uid);
            data.put("name", name);
            data.put("data", base64);
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
    
    public void base64Decoder(){
        try{
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] imageBytes = decoder.decode(base64);
            bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        }
        catch(Exception e){
            
        }
    }

    public void base64Encoder(){
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", bos);
            byte[] imageBytes = bos.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            base64 = encoder.encodeToString(imageBytes);
        }catch(Exception e){
            
        }
    }
    
    public void displayImage()
    {
        ImageIcon icon=new ImageIcon(bufferedImage);
        JFrame frame=new JFrame();
        frame.setSize(bufferedImage.getWidth(),bufferedImage.getHeight());
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}