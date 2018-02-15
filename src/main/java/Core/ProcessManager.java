package Core;

import Core.Definitions.IDefinition;
import Core.Definitions.SupportedLanguages;
import Core.Exceptions.DefinitionNotFoundException;
import Core.Exceptions.NotSupportedException;
import Core.FileManager.FileManager;
import Core.Parser.Models.File;

import java.util.HashMap;
import java.util.Map;


/*  Responsible for acting as the point of entry for the Core Calculations.
        To use, simply initialize a new ProcessManager(), and then call "process" on a file handle.
            you should get a Report back which contains the results of the run benchmarks.
*/
public class ProcessManager {
    // map which contains all definitions.
    // use map so that we cant accidentally add a definition multiple times.
    private Map<SupportedLanguages, IDefinition> _definitions;

    // Constructor which initializes our objects.
    public ProcessManager(){
        _definitions = new HashMap<>();
        loadDefaultDefinitions();
    }

    // Used to process analyze file handle.
    public Report process(java.io.File file) throws DefinitionNotFoundException, NotSupportedException {
        IDefinition def = findDefinition(FileManager.DetermineLanguage(file));
        File model = def.Parser().parse(FileManager.read(file));
        return def.Analyzer().analyze(model);
    }

    public Report process(String[] rawText, String filename) throws DefinitionNotFoundException, NotSupportedException {
        IDefinition def = findDefinition(SupportedLanguages.Java);
        File model = def.Parser().parse(rawText);
        model.Name(filename);
        return def.Analyzer().analyze(model);
    }

    // Used to add analyze definition at runtime. only use this if we need to dynamically add analyze definition.
    public void addDefintion(IDefinition definition){
        _definitions.putIfAbsent(definition.Language(), definition);
    }

    // Used to load the definitions into the process manager.
    // Should only be called in the constructor.
    private void loadDefaultDefinitions(){
        _definitions.put(SupportedLanguages.Java, new Core.Definitions.Java.Definition());
        _definitions.put(SupportedLanguages.VisualBasic, new Core.Definitions.VisualBasic.Definition());
    }

    // Used to find the definition which matches the file type.
    // Throws NotSupportedException if the Language is not supported.
    // Throws DefinitionNotFoundException if there is no loaded definition which supports the language.
    //      Either you need to create analyze definition which supports the Language, or add it to _definitions.
    private IDefinition findDefinition(SupportedLanguages type) throws NotSupportedException, DefinitionNotFoundException {
        if (type == SupportedLanguages.NOTSUPPORTED) throw new NotSupportedException();
        IDefinition def = _definitions.get(type);
        if (def != null) return def;
        throw new DefinitionNotFoundException();
    }
}
