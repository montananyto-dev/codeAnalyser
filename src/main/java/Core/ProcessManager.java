package Core;

import Core.Definitions.IDefinition;
import Core.Definitions.Manifest;
import Core.Definitions.SupportedLanguages;
import Core.Exceptions.DefinitionNotFoundException;
import Core.Exceptions.NotSupportedException;
import Core.FileManager.FileManager;
import Core.Parser.Models.File;
import com.sun.corba.se.spi.ior.IdentifiableContainerBase;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public Report process(java.io.File file) throws NotSupportedException, IOException {
        IDefinition def = findDefinition(file);
        File model = def.Parser().parse(FileManager.read(file));
        model.Name(file.getName());
        return def.Analyzer().analyze(model);
    }

    public Report process(String[] rawText, String filename, SupportedLanguages language) throws DefinitionNotFoundException, NotSupportedException {
        IDefinition def = findDefinition(language);
        File model = def.Parser().parse(rawText);
        model.Name(filename);
        return def.Analyzer().analyze(model);
    }

    // Used to add analyze definition at runtime. only use this if we need to dynamically add analyze definition.
    public void addDefintion(IDefinition definition){
        _definitions.putIfAbsent(definition.Language(), definition);
    }

    public SupportedLanguages determineLanguage(java.io.File file){
        try {
            return findDefinition(file).Language();
        } catch (NotSupportedException e) {
            return SupportedLanguages.NOTSUPPORTED;
        }
    }

    public String[] getSupportedTypeNames(){
        List<String> names = new ArrayList<>();
        for(IDefinition def : _definitions.values()){
           names.add(def.Language().name());
        }
        return names.toArray(new String[names.size()]);
    }

    // Used to load the definitions into the process manager.
    // Should only be called in the constructor.
    private void loadDefaultDefinitions(){
        for(IDefinition def : Manifest.Definitions){
            _definitions.put(def.Language(), def);
        }
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

    private IDefinition findDefinition(java.io.File file) throws NotSupportedException {
        String name = file.getName();
        for (IDefinition def : _definitions.values()){
            if (name.endsWith(def.FileSignature())) return def;
        }
        throw new NotSupportedException();
    }
}
