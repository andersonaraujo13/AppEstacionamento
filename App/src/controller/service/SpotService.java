package controller.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.Spot;

public class SpotService {

	public List<Spot> getSpots() {

		String url = "http://localhost:8080/SmartParkingWS/getCurrentState";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		HttpResponse response;
		
		try {
			response = client.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			return converJson(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ArrayList<>();

	}

	private List<Spot> converJson(String json) throws UnsupportedEncodingException {
		Gson gson = new Gson();
		JsonObject requestJson = gson.fromJson(json, JsonObject.class);
		JsonArray array = requestJson.getAsJsonArray("Spots");
		
		ArrayList<Spot> spots = new ArrayList<>();
		for(JsonElement element : array) {
			
			Spot spot =  new Spot();
			spot.setId(element.getAsJsonObject().get("identifield").getAsString());			
			spot.setTipo(element.getAsJsonObject().get("type").getAsString());
			boolean ocupado = element.getAsJsonObject().get("isOccupied").getAsBoolean();
			
			if(ocupado) {
				spot.setSituacao("Ocupado");
			}else {
				spot.setSituacao("Livre");
			}
			
			JsonElement motorista = element.getAsJsonObject().get("driver");
			if(motorista == null) {
				spot.setMotorista("N/A");
			}else {
				byte[] ptext = motorista.getAsJsonObject().get("full_name").getAsString().getBytes("ISO_8859_1");
				String name =  new String(ptext, "UTF-8");
				spot.setMotorista(name);
			}			
			spots.add(spot);
		}
		
		
		return spots;
	}
}
