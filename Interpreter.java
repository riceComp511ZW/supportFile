/**
 * Created by zhangzhen on 1/26/17.
 */

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.*;

class EvalException extends RuntimeException {
    EvalException(String msg) { super(msg); }
}


class Interpreter {

    Parser p;

    Interpreter(Parser parser){
        p = parser;
    }

    Interpreter(String fileName) throws IOException {
        this(new Parser(new FileReader(fileName)));
    }
    Interpreter(Reader reader){

    }

    public JamVal callByValue() {

    }

    public JamVal callByName()  {

    }

    public JamVal callByNeed()  {

    }

    private AST error(Token found, String expected) {

        throw new ParseException("Token `" + found + "' appears where " +
                expected + " was expected");
    }
}
