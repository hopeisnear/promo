import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadCSV {

    public static void main(String[] args) {

        ReadCSV obj = new ReadCSV();
        obj.run();

    }

    public void run() {

        String csvFile = "C:\\src\\hiberante\\spring-4-rest-web-service-json-example-tomcat\\Spring4RestWS\\src\\main\\java\\VN_routes.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);


                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("<Route rbeOffer=\"true\" regularOffer=\"true\">");
                stringBuilder.append(System.lineSeparator());

                if(country.length > 3 && country[4] != null) {
                    stringBuilder.append("<Interlines>");
                    stringBuilder.append(System.lineSeparator());
                    stringBuilder.append("<Airline code=\"" + country[4].trim() + "\"/>");
                    stringBuilder.append(System.lineSeparator());
                    stringBuilder.append("</Interlines>");
                    stringBuilder.append(System.lineSeparator());
                }

                stringBuilder.append("<Airport code=\"" + country[0].trim() + "\"/>");
                stringBuilder.append(System.lineSeparator());
                stringBuilder.append("<Airport code=\"" + country[2].trim() + "\"/>");
                stringBuilder.append(System.lineSeparator());
                stringBuilder.append("</Route>");
                stringBuilder.append(System.lineSeparator());

                System.out.print(stringBuilder.toString());

            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}
