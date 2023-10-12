package com.example.lockbox;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Pattern;

public class myMethods {
    public <DT> boolean searchArray(DT[] array, DT value) {
        for (DT element : array) {
            if (element.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public int getCode(JSONObject jsonObject)
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonObject.toString());
            JsonNode codeNode = root.get("code");
            return Integer.parseInt(codeNode.asText());
        }
        catch (Exception e)
        {
            Log.d(Params.loogdTag, "myMethods/getCode: "+ Arrays.toString(e.getStackTrace()));
            return 0;
        }
    }

    public String getMessage(JSONObject jsonObject)
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonObject.toString());
            JsonNode codeNode = root.get("message");
            return codeNode.asText();
        }
        catch (Exception e)
        {
            Log.d(Params.loogdTag, "myMethods/getMessage: "+ Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    public boolean isSccuss(JSONObject jsonObject)
    {
        return searchArray(Params.success,getCode(jsonObject));
    }

    public JSONObject setJsonValidate(String email,String password)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("api_key",Params.API_KEY);
            jsonObject.put("email",email);
            jsonObject.put("password",password);
        }
        catch (Exception e)
        {
            Log.d(Params.loogdTag, "myMethods/setJsonValidate: "+ Arrays.toString(e.getStackTrace()));
        }
        return jsonObject;
    }

    public JSONObject setJsonAdduser(String name,String email,String ph,String password)
    {
        Long phoneNumber = Long.parseLong(ph);
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("api_key",Params.API_KEY);
            jsonObject.put("name",name);
            jsonObject.put("mobile",phoneNumber);
            jsonObject.put("email",email);
            jsonObject.put("password",password);
        }
        catch (Exception e)
        {
            Log.d(Params.loogdTag, "myMethods/setJsonAdduser: "+ Arrays.toString(e.getStackTrace()));
        }
        return jsonObject;
    }
    public JSONObject setJsonAddPass(String user_id, String passName,String pass,String email)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("api_key",Params.API_KEY);
            jsonObject.put("user_id",user_id);
            jsonObject.put("name",passName);
            jsonObject.put("pass",pass);
            jsonObject.put("email",email);
        }
        catch (Exception e)
        {
            Log.d(Params.loogdTag, "myMethods/setJsonAddPass: "+ Arrays.toString(e.getStackTrace()));
        }
        return jsonObject;
    }

    public JSONObject setJsonSendEmailOtp(String email)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("api_key",Params.API_KEY);
            jsonObject.put("email",email);
        }
        catch (Exception e)
        {
            Log.d(Params.loogdTag, "myMethods/setJsonSetFlag: "+ Arrays.toString(e.getStackTrace()));
        }
        return jsonObject;
    }
    public JSONObject setGetPass(String email)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("api_key",Params.API_KEY);
            jsonObject.put("email",email);
        }
        catch (Exception e)
        {
            Log.d(Params.loogdTag, "myMethods/setJsonSetFlag: "+ Arrays.toString(e.getStackTrace()));
        }
        return jsonObject;
    }

    public void setListViewData(ListView listView, String json) {
        try {
            ArrayList<String> dataList = new ArrayList<>(); // Create the ArrayList

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json);

            JsonNode arrayNode = jsonNode.get("data");

            for (JsonNode node : arrayNode) {
                dataList.add(mkList(node.toString()));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(listView.getContext(),
                    android.R.layout.simple_list_item_1, dataList);
            listView.setAdapter(adapter);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public String mkList(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(data);
        JsonNode nameNode = node.get("name");
        JsonNode passNode = node.get("password");
        JsonNode uidNode = node.get("uid");

        return  new String(Base64.getDecoder().decode(nameNode.asText()))
                +" \nUser id = "+new String(Base64.getDecoder().decode(uidNode.asText()))
                +" \npassword = "+ new String(Base64.getDecoder().decode(passNode.asText()));
    }

    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(Params.EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }
}
