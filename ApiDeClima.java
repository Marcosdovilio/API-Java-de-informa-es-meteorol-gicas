import org.json.JSONObject;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ApiDeClima {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Digite o nome da cidade: ");
		String cidade = scanner.nextLine();

		try{

			String dadosClimaticos = getDadosClimaticos(cidade);

			if(dadosClimaticos.contains("\"code\":1006")) { 
				System.out.println("Localização desconhecida. Por favor, tente novamente");
			} else {
				imprimirDadosClimaticos(dadosClimaticos);
			}

		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public static String getDadosClimaticos(String cidade) throws Exception {
		String apiKey = Files.readString(Paths.get("api-key.txt")).trim();

		String formataNomeCidade = URLEncoder.encode(cidade, StandardCharsets.UTF_8);

		String apiUrl = "https://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + formataNomeCidade;

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		return response.body();
	}

	public static void imprimirDadosClimaticos(String dadosClimaticos) {

		JSONObject dadosJson = new JSONObject(dadosClimaticos);
		JSONObject infoMeteorologica = dadosJson.getJSONObject("current");


		//Cidade, regiao e pais
		String cidade = dadosJson.getJSONObject("location").getString("name");
		String regiao = dadosJson.getJSONObject("location").getString("region");
		String pais = dadosJson.getJSONObject("location").getString("country");

		//Dados climaticos
		String condicaoTempo = infoMeteorologica.getJSONObject("condition").getString("text");
		int umidade = infoMeteorologica.getInt("humidity");
		float velocidadeVento = infoMeteorologica.getFloat("wind_kph");
		float pressaoAtmosferica = infoMeteorologica.getFloat("pressure_mb");
		float sensacaoTermica = infoMeteorologica.getFloat("feelslike_c");
		float temperaturaAtual = infoMeteorologica.getFloat("temp_c");

		//Data e hora
		String dataHoraString = infoMeteorologica.getString("last_updated");

		//print
		System.out.println("Informações Meteorológicas para: " + cidade + " - " + regiao + ", " + pais);
		System.out.println("Data e hora da informação climatica: " + dataHoraString);
		System.out.println("Temperatura Atual: " + temperaturaAtual + "C");
		System.out.println("Sensação Térmica: " + sensacaoTermica + "C");
		System.out.println("Condição do Tempo: " + condicaoTempo);
		System.out.println("Umidade: " + umidade + "%");
		System.out.println("Velocidade do Vento: " + velocidadeVento + " km/h");
		System.out.println("Pressão Atmosférica: " + pressaoAtmosferica + " mb");

	}
}






/*
"\"code\":1006" da linha 20: o "\"palavra\" é tranformado em "palavra",

 \" \" é um metodo java pra poder usar aspas duplas dentro de aspas duplas 
*/