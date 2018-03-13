package Core.FileManager;

import Core.FileManager.Exceptions.OutputDirectoryNotSetException;
import Core.Report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


// class to handle all File based interactions.
// should strive to implement all methods in a static way so that we can reference this object statically.
public class FileManager {

    public static String[] read(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        return lines.toArray(new String[lines.size()]);
    }

    public static Report readCsv(File file) throws Exception {
        return CsvBuilder.read(file);
    }

    public static void write(Report report, File file) throws OutputDirectoryNotSetException, IOException {
        if (!file.isDirectory()) throw new OutputDirectoryNotSetException();
        String[] csv = CsvBuilder.build(report);
        System.out.println(file.getAbsolutePath());
        File out = new File(file.getAbsolutePath() + File.pathSeparator + "Project" +report.Name + longToString(report.Timestamp.getTime()) + ".csv");
        out.createNewFile();
        FileWriter writer = new FileWriter(out);
        for (String line : csv){
            System.out.println(line);
            writer.write(line);
        }
        writer.close();
    }
    private static String longToString(long val){
        return String.format("%020d", val);
    }

}
