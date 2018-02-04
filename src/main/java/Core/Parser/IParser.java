package Core.Parser;

import Core.Parser.Models.File;

// interface to describe a Parser Object.
// relatively empty as we do not want to make assumptions on the object types within a given language.
// let the individual implementations decide how to fill the File Model.
public interface IParser {
    File parse(String[] rawText);
}
