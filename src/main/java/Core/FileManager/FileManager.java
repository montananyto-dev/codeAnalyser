package Core.FileManager;

import Core.Definitions.SupportedLanguages;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;


// class to handle all File based interactions.
// should strive to implement all methods in a static way so that we can reference this object statically.
public class FileManager {

    public static SupportedLanguages DetermineLanguage(File file){
        throw new NotImplementedException();
    }

    public static String[] read(File file){
        throw new NotImplementedException();
    }
}
