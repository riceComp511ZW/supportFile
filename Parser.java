/**
 * Created by zhangzhen on 1/26/17.
 */
/** Parser for Assignment 2 */

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.*;

public class Parser {

    private Lexer in;
    KeyWord ifKey;
    KeyWord thenKey;
    KeyWord elseKey;
    KeyWord letKey;
    KeyWord letrecKey;
    KeyWord inKey;
    KeyWord mapKey;
    KeyWord toKey;
    KeyWord assignKey;

    Parser(Lexer i) {
        in = i;
        initParser();
    }

    Parser(Reader inputStream) {
        this(new Lexer(inputStream));
    }

    Parser(String fileName) throws IOException {
        this(new FileReader(fileName));
    }

    Lexer lexer() { return in; }

    private void initParser() {
        ifKey     = (KeyWord) in.wordTable.get("if");
        thenKey   = (KeyWord) in.wordTable.get("then");
        elseKey   = (KeyWord) in.wordTable.get("else");
        letKey    = (KeyWord) in.wordTable.get("let");
        letrecKey = (KeyWord) in.wordTable.get("letrec");
        inKey     = (KeyWord) in.wordTable.get("in");
        mapKey    = (KeyWord) in.wordTable.get("map");
        toKey     = (KeyWord) in.wordTable.get("to");
        assignKey = (KeyWord) in.wordTable.get(":=");
    }

    public AST parse() throws ParseException {
        AST prog = parseExp();
        Token t = in.readToken();
        if (t == EndOfFile.ONLY) return prog;
        else throw
                new ParseException("Legal program followed by extra token " + t);
    }

    private AST parseExp() {

        Token token = in.readToken();

        // <exp> :: = if <exp> then <exp> else <exp>
        //          | let <prop-def-list> in <exp>
        //          | map <id-list> to <exp>
        //          | <term> { <biop> <term> }*

        if (token == ifKey) return parseIf();
        // if (token == letrecKey) return parseLetRec();
        if (token == letKey) return parseLet();
        if (token == mapKey) return parseMap();

    /*  Supports the addition of blocks to Jam
     if (token == LeftBrace.ONLY) {
     AST[] exps = parseExps(SemiColon.ONLY,RightBrace.ONLY);
     // including closing brace
     if (exps.length == 0) throw new ParseException("Illegal empty block");
     return new Block(exps);
     }
     */

        AST leftHandExp = parseTerm(token);

        while (in.peek() instanceof OpToken) {
            OpToken op = (OpToken) in.readToken();
            if (!op.isBinOp()) error(op, "binary operator");
            AST rightHandTerm = parseTerm(in.readToken());
            leftHandExp = new BinOpApp(op.toBinOp(), leftHandExp, rightHandTerm);
        }

        // return left-associative binop tree
        return leftHandExp;
    }

    private AST parseTerm(Token token) {

        // <term>     ::= { <unop> } <term> |
        //                <constant> |
        //                <factor> {( <exp-list> )}

        // <constant> ::= <null> | <int> | <bool>

        if (token instanceof OpToken) {
            OpToken op = (OpToken) token;
            if (! op.isUnOp()) error(op,"unary operator");
            return new UnOpApp(op.toUnOp(), parseTerm(in.readToken()));
        }

        if (token instanceof Constant) return (Constant) token;

        AST factor = parseFactor(token);

        Token next = in.peek();
        if (next == LeftParen.ONLY) {
            in.readToken();  // remove next from input stream
            AST[] exps = parseArgs();  // including closing paren
            return new App(factor,exps);
        }
        return factor;
    }

    private AST parseFactor(Token token) {

        // <factor>   ::= <prim> | <variable> | ( <exp> )

        if (token == LeftParen.ONLY) {
            AST exp = parseExp();
            token = in.readToken();
            if (token != RightParen.ONLY) error(token,"`)'");
            return exp;
        }

        if (! (token instanceof PrimFun) && ! (token instanceof Variable))
            error(token,"constant, primitive, variable, or `('");

        // Term = Constant | Variable | PrimFun
        return (Term) token;
    }

    private AST parseIf() {
        // parses `if <exp> then <exp> else <exp>'
        // given that `if' has already been read

        AST test = parseExp();
        Token key1 = in.readToken();
        if (key1 != thenKey) error(key1,"`then'");
        AST conseq = parseExp();
        Token key2 = in.readToken();
        if (key2 != elseKey) error(key2,"`else'");
        AST alt = parseExp();
        return new If(test,conseq,alt);
    }

    private AST parseLet() {
        // parses `let <prop-def-list> in <exp>'
        // given that `let' has already been read

        Def[] defs = parseDefs(false);
        // consumes `in'; false means rhs may be non Map
        AST body = parseExp();
        return new Let(defs,body);
    }

  /*
   private AST parseLetRec() {
   // parses `letrec <prop-def-list> in <exp>'
   // given that `letrec' has already been read

   Def[] defs = parseDefs(true);
   // consumes `in'; true means each rhs must be a Map
   AST body = parseExp();
   return new LetRec(defs,body);
   }
   */

    private AST parseMap() {
        // parses `map <id-list> to <exp>'
        // given that `map' has already been read

        Variable[] vars = parseVars(); // consumes the delimiter `to'
        AST body = parseExp();
        return new Map(vars, body);
    }

    private AST[] parseExps(Token separator, Token delim) {
        // parses `<exp-list> <delim>'
        // where
        //   <exp-list>      ::= <empty> | <prop-exp-list>
        //   <empty> ::=
        //   <prop-exp-list> ::= <exp> | <exp> <separator> <prop-exp-list>

        LinkedList<AST> exps = new LinkedList<AST>();
        Token next = in.peek();

        if (next == delim) {
            in.readToken(); // consume RightParen
            return new AST[0];
        }

        // next is still at front of input stream

        do {
            AST exp = parseExp();
            exps.addLast(exp);
            next = in.readToken();
        } while (next == separator);

        if (next != delim) error(next,"`,' or `)'");
        return (AST[]) exps.toArray(new AST[0]);
    }

    private AST[] parseArgs() { return parseExps(Comma.ONLY,RightParen.ONLY); }

    private Variable[] parseVars() {

        // parses <id-list>
        // where
        //   <id-list>       ::= <empty> | <prop-id-list>
        //   <prop-id-list>  ::= <id> | <id> , <id-list>

        // NOTE: consumes `to' following <id-list>

        LinkedList<Variable> vars = new LinkedList<Variable>();
        Token t = in.readToken();
        if (t == toKey) return new Variable[0];

        do {
            if (! (t instanceof Variable)) error(t,"variable");
            vars.addLast((Variable)t);
            t = in.readToken();
            if (t == toKey) break;
            if (t != Comma.ONLY) error(t,"`to' or `,'");
            // Comma found, read next variable
            t = in.readToken();
        } while (true);
        return (Variable[]) vars.toArray(new Variable[0]);
    }

    private Def[] parseDefs(boolean forceMap) {
        // parses  `<prop-def-list> in'
        // where
        //   <prop-def-list> ::= <def> | <def> <def-list>

        // NOTE: consumes `in' following <prop-def-list>

        LinkedList<Def> defs = new LinkedList<Def>();
        Token t = in.readToken();

        do {
            Def d = parseDef(t);
            if (forceMap && (! (d.rhs() instanceof Map)))
                throw new ParseException("right hand side of definition `" + d
                        + "' is not a map expression");
            defs.addLast(d);
            t = in.readToken();
        } while (t != inKey);

        return (Def[]) defs.toArray(new Def[0]);
    }

    private Def parseDef(Token var) {
        // parses <id> := <exp> ;
        // which is <def>
        // given that first token var has been read

        if (! (var instanceof Variable)) error(var,"variable");

        Token key = in.readToken();
        if (key != assignKey) error (key,"`:='");

        AST exp = parseExp();

        Token semi = in.readToken();
        if (semi != SemiColon.ONLY) error(semi,"`;'");
        return new Def((Variable) var, exp);
    }

    private AST error(Token found, String expected) {
        for (int i = 0; i < 10; i++) {
            System.out.println(in.readToken());
        }
        throw new ParseException("Token `" + found + "' appears where " +
                expected + " was expected");
    }
}
