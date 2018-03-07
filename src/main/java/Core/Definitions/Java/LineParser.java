package Core.Definitions.Java;

import Core.Parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class LineParser extends Core.Parser.LineParser {

    public LineParser(){}

    public LineParser(Parser parent) {
        super(parent);
    }

    protected char[] setDelimiters() {
        return new char[] {'(',')', '{','}', '[',']', '<','>',
                           '=','+','-','*','/',
                           ';',',',
                           '&','|','%'};

    }

    protected List<ConsolidationProfile> setConsolidationProfiles() {
        List<ConsolidationProfile> profiles = new ArrayList<>();
        profiles.add(new ConsolidationProfile(new String[]{"-","+","*","/", "&", "|", "%"},
                                                            new String[]{"=", "/", "*", "&", "|"},
                                                            false));
        profiles.add(new ConsolidationProfile(new String[]{"[", "{", "<"},
                                                            new String[]{"]", "}", ">"},
                                                            true));
        return profiles;
    }
}
