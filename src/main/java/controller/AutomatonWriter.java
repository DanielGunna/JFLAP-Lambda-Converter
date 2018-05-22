package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.*;
import org.json.JSONObject;
import org.json.XML;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import static controller.Constants.JFLAP_FILE_HEADER;

public class AutomatonWriter {


    public void getJflapFileContentFromAutomaton(Automaton automaton) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        AutomatonStructure automatonStructure = new AutomatonStructure();
        automatonStructure.setType("fa");
        normalizeIds(automaton);
        fillXY(automaton);
        automatonStructure.setAutomaton(automaton);

        String content = convertJsonAutomatonToXml(automaton);
        String xml = JFLAP_FILE_HEADER + "\n" + content;
        try {
            saveJflapFile(xml, "/home/gunna/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveJflapFile(String content, String path) throws IOException {
        File file = new File(path + String.format("/te_%s.jff", String.valueOf(Math.random()).replace(".", "_")));
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }

    private String convertJsonAutomatonToXml(Automaton automaton) {
        String content = "<structure>\n<type>fa</type>\n<automaton>";
        content += "<!--The list of states.-->";
        for (State state : automaton.getStates()) {
            content += String.format("<state name=\"%s\" id=\"%s\">", state.getName(), state.getId());
            if (state.isInitialState())
                content += "<initial/>";
            if (state.isFinalState())
                content += "<final/>";
            content += String.format("<x>%s</x>\n<y>%s</y>", state.getxPosition(), state.getyPosition());
            content += "</state>";
        }
        content += "<!--The list of transitions.-->";
        for (Transition t : automaton.getTransitions()) {
            content += "<transition>";
            content += String.format("<from>%s</from>", t.getFromId());
            content += String.format("<to>%s</to>", t.getToId());
            content += String.format("<read>%s</read>", t.getValue());
            content += "</transition>";
        }
        content += "</automaton>\n</structure>";
        return content;
    }

    private void normalizeIds(Automaton automaton) {
        for (State state : automaton.getStates()) {
            state.setName(state.getName()
                    .replace(",", "")
                    .replace("{", "")
                    .replace("}", ""));
            state.setId(state.getId().replace(",", ""));
        }
        for (Transition t : automaton.getTransitions()) {
            t.setFromId(t.getFromId().replace(",", ""));
            t.setToId(t.getToId().replace(",", ""));
        }
    }

    private String convertJsonToXml(String json) {
        try {
            return XML.toString(new JSONObject(json));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void fillXY(Automaton automaton) {
        for (State state : automaton.getStates()) {
            state.setxPosition((int) (Math.random() * 100));
            state.setyPosition((int) (Math.random() * 100));
        }
    }

}
